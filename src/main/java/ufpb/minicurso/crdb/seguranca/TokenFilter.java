package ufpb.minicurso.crdb.seguranca;

import io.jsonwebtoken.*;
import org.springframework.web.filter.GenericFilterBean;
import ufpb.minicurso.crdb.servico.impl.JwtServico;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class TokenFilter extends GenericFilterBean {

    public final static int TOKEN_INDEX = 7;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        Optional<String> header = Optional.ofNullable(httpRequest.getHeader("Authorization"));

        if(!header.isPresent() || !header.get().startsWith("Bearer ")){
            throw new ServletException("Token nao existe ou mal formado");
        }

        String token = header.get().substring(TOKEN_INDEX);

        try {
            Jwts.parser().setSigningKey(JwtServico.TOKEN_KEY).parseClaimsJws(token).getBody();
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException | PrematureJwtException | UnsupportedJwtException | IllegalArgumentException e){
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());

            return;
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
