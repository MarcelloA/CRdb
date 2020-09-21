package ufpb.minicurso.crdb.servico.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ufpb.minicurso.crdb.dto.LoginDTO;
import ufpb.minicurso.crdb.entidade.Funcao;
import ufpb.minicurso.crdb.entidade.Usuario;
import ufpb.minicurso.crdb.repositorio.UsuarioRepositorio;
import ufpb.minicurso.crdb.seguranca.JwtTokenProvedor;
import ufpb.minicurso.crdb.servico.interfaces.UsuarioServicoInterface;

import java.util.ArrayList;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UsuarioServico implements UsuarioServicoInterface {

    private final UsuarioRepositorio<Usuario,String> usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvedor jwtTokenProvedor;
    private final AuthenticationManager authenticationManager;

    public void autenticar(LoginDTO usuario) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha()));
    }

    public String cadastrar(Usuario usuario) {
        String nomeUsuario = usuario.getEmail();

        if(usuarioRepositorio.existsById(nomeUsuario))
            return "Usuario já cadatrado";

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setFuncoes(new ArrayList<>(Collections.singletonList(Funcao.ROLE_USUARIO)));

        this.usuarioRepositorio.save(usuario);
        return jwtTokenProvedor.criarToken(nomeUsuario, new ArrayList<>(Collections.singletonList(Funcao.ROLE_USUARIO)));
    }

    public String signIn(LoginDTO usuario) {
        autenticar(usuario);
        return jwtTokenProvedor.criarToken(usuario.getEmail(), usuarioRepositorio.findByEmail(usuario.getEmail()).getFuncoes());
    }

    @Override
    public Usuario encontrarUsuarioPorId(String email) {
        return this.usuarioRepositorio.findByEmail(email);
    }

    @Override
    public String removerUsuario() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioADeletar = encontrarUsuarioPorId(email);
        usuarioRepositorio.delete(usuarioADeletar);

        return "Usuario cujo email é " + usuarioADeletar.getEmail() + " foi deletado";
    }

    public String removerUsuarioPeloAdmin(String email){
        Usuario usuarioADeletar = encontrarUsuarioPorId(email);
        usuarioRepositorio.delete(usuarioADeletar);

        return "Usuario cujo email é " + usuarioADeletar.getEmail() + " foi deletado";
    }

    public JwtTokenProvedor getJwtTokenProvedor() {
        return jwtTokenProvedor;
    }
}
