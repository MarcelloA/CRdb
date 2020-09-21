package ufpb.minicurso.crdb.entidade;

import org.springframework.security.core.GrantedAuthority;

public enum Funcao implements GrantedAuthority {
    ROLE_ADMIN, ROLE_USUARIO;

    @Override
    public String getAuthority() {
        return name();
    }
}
