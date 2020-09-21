package ufpb.minicurso.crdb.seguranca;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ufpb.minicurso.crdb.configuracao.CrdbApplicationConfig;
import ufpb.minicurso.crdb.entidade.Funcao;
import ufpb.minicurso.crdb.excecao.TokenExcecao;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvedor {

    private final String chaveSegredo;
    private final Long validadeTokenEmMilisegundos;

    private final MeuUserDetails meuUserDetail;

    public JwtTokenProvedor(CrdbApplicationConfig config, MeuUserDetails meuUserDetail) {
        this.chaveSegredo = Base64.getEncoder().encodeToString(config.getSegredo().getBytes());
        this.validadeTokenEmMilisegundos = 1000 * config.getValidadeTokenEmSegundos();
        this.meuUserDetail = meuUserDetail;
    }

    public String criarToken(String nomeUsuario, List<Funcao> funcoes) {
        Claims claims = Jwts.claims().setSubject(nomeUsuario);
        claims.put("auth", funcoes.stream()
                .map(s -> new SimpleGrantedAuthority(s.getAuthority()))
                .collect(Collectors.toList()));

        Date agora = new Date();
        Date validade = new Date(agora.getTime() + validadeTokenEmMilisegundos);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(agora)
                .setExpiration(validade)
                .signWith(SignatureAlgorithm.HS256, chaveSegredo)
                .compact();
    }

    public Authentication getAutenticacao(String token) {
        UserDetails detalhesUsuario = meuUserDetail.loadUserByUsername(getUsuarioNome(token));
        return new UsernamePasswordAuthenticationToken(detalhesUsuario,"",detalhesUsuario.getAuthorities());
    }

    public String getUsuarioNome(String token) {
        return Jwts.parser().setSigningKey(chaveSegredo).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return "Nao achado Header Authorization";
    }


    public boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(chaveSegredo).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenExcecao("token JWT expirado ou invalido");
        }
    }

}
