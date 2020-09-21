package ufpb.minicurso.crdb.seguranca;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ufpb.minicurso.crdb.entidade.Usuario;
import ufpb.minicurso.crdb.repositorio.UsuarioRepositorio;

@Service
@RequiredArgsConstructor
public class MeuUserDetails implements UserDetailsService {

    private final UsuarioRepositorio<Usuario,String> usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Usuario usuario = usuarioRepositorio.findByEmail(email);

        if(usuario == null) throw new UsernameNotFoundException("Usuario de email '"+ email + "' nao encontrado");

        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(usuario.getSenha())
                .authorities(usuario.getFuncoes())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
