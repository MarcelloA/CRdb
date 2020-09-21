package ufpb.minicurso.crdb.seguranca;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ufpb.minicurso.crdb.excecao.TokenExcecao;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFiltroToken extends OncePerRequestFilter {

    private JwtTokenProvedor jwtTokenProvedor;

    public JwtFiltroToken(JwtTokenProvedor jwtTokenProvedor){
        this.jwtTokenProvedor = jwtTokenProvedor;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                         FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvedor.resolveToken(request);
        try {
            if (token != null && jwtTokenProvedor.validarToken(token)) {
                Authentication auth = jwtTokenProvedor.getAutenticacao(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (TokenExcecao ex) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

}
