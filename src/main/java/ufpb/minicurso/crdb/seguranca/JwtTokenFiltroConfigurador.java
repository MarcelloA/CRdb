package ufpb.minicurso.crdb.seguranca;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtTokenFiltroConfigurador extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private JwtTokenProvedor jwtTokenProvedor;

    public JwtTokenFiltroConfigurador(JwtTokenProvedor jwtTokenProvedor){
        this.jwtTokenProvedor = jwtTokenProvedor;
    }

    @Override
    public void configure(HttpSecurity http) {
        JwtFiltroToken customFilter = new JwtFiltroToken(jwtTokenProvedor);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
