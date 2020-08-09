package ufpb.minicurso.crdb.servico.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufpb.minicurso.crdb.dto.LoginDTO;
import ufpb.minicurso.crdb.entidade.Usuario;
import ufpb.minicurso.crdb.excecao.UsuarioSenhaNaoValidos;
import ufpb.minicurso.crdb.seguranca.TokenFilter;

import javax.servlet.ServletException;
import java.util.Date;

@Service
public class JwtServico {
    @Autowired
    UsuarioServico usuarioServico;
    public static final String TOKEN_KEY = "cryptic writings";

    public JSONObject validar(LoginDTO usuario){
        JSONObject tokenJSON = new JSONObject();

        if (!usuarioServico.validarUsuarioSenha(usuario)) {
            throw new UsuarioSenhaNaoValidos("Usuario ou senha nao validos. Nao e possivel loggar.");
        }

        String token = GeradorToken(usuario.getEmail());

        tokenJSON.put("token",token);
        return tokenJSON;
    }

    private String GeradorToken(String email) {
        return Jwts.builder().setHeaderParam("typ", "JWT").setSubject(email)
                .signWith(SignatureAlgorithm.HS512, TOKEN_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000)).compact();
    }


    public String pegaSujeito(String authorizationHeader) throws ServletException {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ServletException("Token nao achado ou mal formado!");
        }

        String token = authorizationHeader.substring(TokenFilter.TOKEN_INDEX);

        return Jwts.parser().setSigningKey(TOKEN_KEY).parseClaimsJws(token).getBody().getSubject();
    }

}
