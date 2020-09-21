package ufpb.minicurso.crdb.servico;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import ufpb.minicurso.crdb.configuracao.CrdbApplicationConfig;
import ufpb.minicurso.crdb.entidade.Funcao;
import ufpb.minicurso.crdb.entidade.Usuario;
import ufpb.minicurso.crdb.repositorio.UsuarioRepositorio;
import ufpb.minicurso.crdb.seguranca.JwtTokenProvedor;
import ufpb.minicurso.crdb.seguranca.MeuUserDetails;
import ufpb.minicurso.crdb.servico.impl.UsuarioServico;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServicoTeste {

    @Mock
    private UsuarioRepositorio<Usuario, String> usuarioRepositorio;

    private UsuarioServico usuarioServico;
    private MeuUserDetails meuUserDetails;
    private final Usuario usuario = new Usuario();
    private final CrdbApplicationConfig config = new CrdbApplicationConfig();

    @BeforeEach
    void initUsuarioTeste(){
        config.setSegredo("cryptic_writings");
        config.setValidadeTokenEmSegundos(2592000L);
        usuarioServico = new UsuarioServico(usuarioRepositorio,
                NoOpPasswordEncoder.getInstance(),
                new JwtTokenProvedor(config,this.meuUserDetails),
                null);

    }

    private void simulaSalvandoNoRepositorio(){
        when(usuarioRepositorio.save(any(Usuario.class))).then(returnsFirstArg());
    }

    private void instanciandoUsuario(){

        usuario.setEmail("davemustaine@megadeth.com");
        usuario.setPrimeiroNome("Dave");
        usuario.setUltimoNome("Mustaine");
        usuario.setSenha("Killing is my Bussiness");
        usuario.setFuncoes(new ArrayList<>(Collections.singletonList(Funcao.ROLE_USUARIO)));
        usuario.setAvaliacoes(Collections.emptyList());
    }

    @Test
    void deveSalvarNovoUsuario(){
        simulaSalvandoNoRepositorio();

        instanciandoUsuario();

        String tokenAtual = usuarioServico.cadastrar(usuario);
        String tokenEsperado = usuarioServico
                .getJwtTokenProvedor()
                .criarToken(usuario.getEmail(), usuario.getFuncoes());

        assertEquals(tokenEsperado, tokenAtual);
    }

    @Test
    void naoDeveSalvarUsuarioJaCadastrado(){
        when(usuarioRepositorio.existsById(usuario.getEmail())).thenReturn(true);

        assertEquals(usuarioServico.cadastrar(usuario), "Usuario já cadatrado");
    }
}
