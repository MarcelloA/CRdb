package ufpb.minicurso.crdb.servico;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import ufpb.minicurso.crdb.configuracao.CrdbApplicationConfig;
import ufpb.minicurso.crdb.entidade.*;
import ufpb.minicurso.crdb.excecao.DisciplinaNaoEncontradaExcecao;
import ufpb.minicurso.crdb.excecao.NotaInvalidaExcecao;
import ufpb.minicurso.crdb.repositorio.AvaliacaoRepositorio;
import ufpb.minicurso.crdb.repositorio.DisciplinaRepositorio;
import ufpb.minicurso.crdb.repositorio.UsuarioRepositorio;
import ufpb.minicurso.crdb.seguranca.JwtTokenProvedor;
import ufpb.minicurso.crdb.seguranca.MeuUserDetails;
import ufpb.minicurso.crdb.servico.impl.AvaliacaoServico;
import ufpb.minicurso.crdb.servico.impl.UsuarioServico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvaliacaoServicoTeste {

    @Mock
    private AvaliacaoRepositorio avaliacaoRepositorio;
    @Mock
    private DisciplinaRepositorio disciplinaRepositorio;
    @Mock
    private UsuarioRepositorio<Usuario, String> usuarioRepositorio;

    private AvaliacaoServico avaliacaoServico;
    private final Usuario usuario = new Usuario();
    private final Disciplina disciplina = new Disciplina();
    private final CrdbApplicationConfig config = new CrdbApplicationConfig();

    private Avaliacao instanciandoAvaliacaoVazia(Boolean favorito){
        AvaliacaoId avaliacaoId = new AvaliacaoId(usuario, disciplina);

        Comentario comentario = instanciandoComentario(false,"", new Avaliacao());
        Avaliacao avaliacaoEsperada;

        if(favorito){
            avaliacaoEsperada = new Avaliacao(avaliacaoId, comentario, 0.0, true, null);
        } else {
            avaliacaoEsperada = new Avaliacao(avaliacaoId, comentario, 0.0, false, null);
        }

        return avaliacaoEsperada;
    }

    private Comentario instanciandoComentario(boolean visibilidade, String anotacao, Avaliacao avaliacao){
        Comentario comentario = new Comentario();
        comentario.setVisibilidade(visibilidade);
        comentario.setComentario(anotacao);
        comentario.setAvaliacao(avaliacao);

        return comentario;
    }

    @BeforeEach
    void initAvaliacaoTeste(){
        DisciplinaNaoEncontradaExcecao disciplinaNaoEncontradaExcecao = new DisciplinaNaoEncontradaExcecao("Id da disciplina nao encontrado");
        Optional<Disciplina> disciplinaOptional;
        usuario.setEmail("davemustaine@megadeth.com");
        usuario.setPrimeiroNome("Dave");
        usuario.setUltimoNome("Mustaine");
        usuario.setSenha("KimB");
        usuario.setFuncoes(new ArrayList<>(Collections.singletonList(Funcao.ROLE_USUARIO)));
        usuario.setAvaliacoes(Collections.emptyList());

        disciplina.setId(1L);
        disciplina.setNome("BANCO DE DADOS 1");
        disciplina.setAvaliacoes(Collections.emptyList());

        disciplinaOptional = Optional.of(disciplina);

        when(disciplinaRepositorio.count()).thenReturn(99999L);
        lenient().when(disciplinaRepositorio.findById(eq(disciplina.getId()))).thenReturn(disciplinaOptional);
        lenient().when(disciplinaRepositorio.findById(eq(disciplinaRepositorio.count()+1))).thenThrow(disciplinaNaoEncontradaExcecao);

        avaliacaoServico = new AvaliacaoServico(avaliacaoRepositorio, disciplinaRepositorio);
        config.setSegredo("cryptic_writings");
        config.setValidadeTokenEmSegundos(2592000L);
        MeuUserDetails meuUserDetails = new MeuUserDetails(usuarioRepositorio);
        UsuarioServico usuarioServico = new UsuarioServico(usuarioRepositorio,
                NoOpPasswordEncoder.getInstance(),
                new JwtTokenProvedor(config, meuUserDetails),
                null);

        String token = usuarioServico.getJwtTokenProvedor().criarToken(usuario.getEmail(),usuario.getFuncoes());

        when(usuarioRepositorio.findByEmail(usuario.getEmail())).thenReturn(usuario);

        SecurityContextHolder.getContext().setAuthentication(usuarioServico.getJwtTokenProvedor().getAutenticacao(token));
    }

    @Test
    void naoDeveRetornarAvaliacaoIdNulo(){

        assertNotNull(avaliacaoServico.comentar(disciplina.getId(),"").getAvaliacaoId());
        assertNotNull(avaliacaoServico.favoritarDisciplina(disciplina.getId()).getAvaliacaoId());
        assertNotNull(avaliacaoServico.darNota(disciplina.getId(),0.0).getAvaliacaoId());
    }

    @Test
    void naoDeveRetornarComentarioNulo(){

        assertNotNull(avaliacaoServico.comentar(disciplina.getId(),"").getComentario());
        assertNotNull(avaliacaoServico.favoritarDisciplina(disciplina.getId()).getComentario());
        assertNotNull(avaliacaoServico.darNota(disciplina.getId(),0.0).getComentario());
    }

    @Test
    void deveRetornarNotaNulaQuandoComentarEFavoritar(){
        assertNull(avaliacaoServico.comentar(disciplina.getId(),"").getNota());
        assertNull(avaliacaoServico.favoritarDisciplina(disciplina.getId()).getNota());
    }

    @Test
    void naoDeveRetornarNotaNula(){
        assertNotNull(avaliacaoServico.darNota(disciplina.getId(),0.0).getNota());
    }

    @Test
    void naoDeveRetornarFavoritoNulo(){

        assertNotNull(avaliacaoServico.comentar(disciplina.getId(),"").getFavorito());
        assertNotNull(avaliacaoServico.favoritarDisciplina(disciplina.getId()).getFavorito());
        assertNotNull(avaliacaoServico.darNota(disciplina.getId(),0.0).getFavorito());
    }

    @Test
    void naoDeveRetornarCriadoEmNulo(){

        assertNotNull(avaliacaoServico.comentar(disciplina.getId(),"").getCriadoEm());
        assertNotNull(avaliacaoServico.favoritarDisciplina(disciplina.getId()).getCriadoEm());
        assertNotNull(avaliacaoServico.darNota(disciplina.getId(),0.0).getCriadoEm());
    }

    @Test
    void deveLancarDisciplinaNaoEncontradaExcecaoQuandoIdInvalido(){

        DisciplinaNaoEncontradaExcecao excecao = assertThrows(DisciplinaNaoEncontradaExcecao.class, () -> avaliacaoServico.comentar(disciplinaRepositorio.count()+1,"ruim"));
        assertTrue(excecao.getMessage().contains("Id da disciplina nao encontrado"));

        excecao = assertThrows(DisciplinaNaoEncontradaExcecao.class, () -> avaliacaoServico.deletaComentario(disciplinaRepositorio.count()+1));
        assertTrue(excecao.getMessage().contains("Id da disciplina nao encontrado"));

        excecao = assertThrows(DisciplinaNaoEncontradaExcecao.class, () -> avaliacaoServico.favoritarDisciplina(disciplinaRepositorio.count()+1));
        assertTrue(excecao.getMessage().contains("Id da disciplina nao encontrado"));

        excecao = assertThrows(DisciplinaNaoEncontradaExcecao.class, () -> avaliacaoServico.darNota(disciplinaRepositorio.count()+1,0.0));
        assertTrue(excecao.getMessage().contains("Id da disciplina nao encontrado"));
    }

    @Test
    void deveRetornarAvaliacaoModificadaApenasEmComentario(){

        Avaliacao avaliacaoEsperada = instanciandoAvaliacaoVazia(true);
        avaliacaoEsperada.setNota(8.8);
        avaliacaoEsperada.setCriadoEm(LocalDateTime.now());
        Comentario comentarioEsperado = avaliacaoEsperada.getComentario();

        when(avaliacaoRepositorio.findByAvaliacaoIdUsuarioEmailAndAvaliacaoIdDisciplinaId(usuario.getEmail(),disciplina.getId()))
                .thenReturn(avaliacaoEsperada);

        Avaliacao avaliacaoDoServico = avaliacaoServico.comentar(disciplina.getId(),"comentario");

        assertEquals(avaliacaoEsperada.getNota(), avaliacaoDoServico.getNota());
        assertEquals(avaliacaoEsperada.getFavorito(), avaliacaoDoServico.getFavorito());
        assertEquals(avaliacaoEsperada.getCriadoEm(), avaliacaoDoServico.getCriadoEm());
        assertNotEquals(comentarioEsperado, avaliacaoDoServico.getComentario());
    }

    @Test
    void deveRetornarComentarioVisibilidadeIgualAFalseQuandoDeletado(){
        Comentario comentarioDeletado;
        Boolean comentarioEsperadoVisibilidade;
        Avaliacao avaliacao = instanciandoAvaliacaoVazia(false);
        avaliacao.setComentario(instanciandoComentario(true,"comentario", new Avaliacao()));


        comentarioEsperadoVisibilidade = avaliacao.getComentario().getVisibilidade();

        when(avaliacaoRepositorio.findByAvaliacaoIdUsuarioEmailAndAvaliacaoIdDisciplinaId(usuario.getEmail(),disciplina.getId()))
                .thenReturn(avaliacao);

        comentarioDeletado = avaliacaoServico.deletaComentario(disciplina.getId());

        assertNotEquals(comentarioEsperadoVisibilidade, comentarioDeletado.getVisibilidade());
    }

    @Test
    void deveRetornarAvaliacaoModificadaApenasEmFavorito(){
        Avaliacao avaliacaoEsperada = instanciandoAvaliacaoVazia(true);
        avaliacaoEsperada.setNota(8.8);
        avaliacaoEsperada.setComentario(instanciandoComentario(false,"nao deve exibir", new Avaliacao()));

        avaliacaoEsperada.setCriadoEm(LocalDateTime.now());
        Boolean favoritoEsperado = avaliacaoEsperada.getFavorito();

        when(avaliacaoRepositorio.findByAvaliacaoIdUsuarioEmailAndAvaliacaoIdDisciplinaId(usuario.getEmail(),disciplina.getId()))
                .thenReturn(avaliacaoEsperada);

        Avaliacao avaliacaoDoServico = avaliacaoServico.favoritarDisciplina(disciplina.getId());

        assertEquals(avaliacaoEsperada.getNota(), avaliacaoDoServico.getNota());
        assertEquals(avaliacaoEsperada.getComentario(), avaliacaoDoServico.getComentario());
        assertEquals(avaliacaoEsperada.getCriadoEm(), avaliacaoDoServico.getCriadoEm());
        assertNotEquals(favoritoEsperado, avaliacaoDoServico.getFavorito());
    }

    @Test
    void deveRetornarFavoritoIgualAFalseQuandoJaFavoritado(){
        Avaliacao avaliacaoEsperada = instanciandoAvaliacaoVazia(true);
        Boolean favoritoAvaliacaoEsperada = avaliacaoEsperada.getFavorito();

        when(avaliacaoRepositorio.findByAvaliacaoIdUsuarioEmailAndAvaliacaoIdDisciplinaId(usuario.getEmail(),disciplina.getId()))
                .thenReturn(avaliacaoEsperada);

        Avaliacao avaliacaoDoServico = avaliacaoServico.favoritarDisciplina(disciplina.getId());

        assertNotEquals(favoritoAvaliacaoEsperada, avaliacaoDoServico.getFavorito());
    }

    @Test
    void deveRetornarFavoritoIgualATrueQuandoValorAtualFalse(){
        Avaliacao avaliacaoEsperada = instanciandoAvaliacaoVazia(false);
        Boolean favoritoAvaliacaoEsperada = avaliacaoEsperada.getFavorito();

        when(avaliacaoRepositorio.findByAvaliacaoIdUsuarioEmailAndAvaliacaoIdDisciplinaId(usuario.getEmail(),disciplina.getId()))
                .thenReturn(avaliacaoEsperada);

        Avaliacao avaliacaoDoServico = avaliacaoServico.favoritarDisciplina(disciplina.getId());

        assertNotEquals(favoritoAvaliacaoEsperada, avaliacaoDoServico.getFavorito());
    }

    @Test
    void deveRetornarAvaliacaoModificadaApenasEmNota(){
        Avaliacao avaliacaoEsperada = instanciandoAvaliacaoVazia(true);
        avaliacaoEsperada.setComentario(instanciandoComentario(false,"nao deve exibir", new Avaliacao()));

        avaliacaoEsperada.setCriadoEm(LocalDateTime.now());
        Double notaEsperada = avaliacaoEsperada.getNota();

        when(avaliacaoRepositorio.findByAvaliacaoIdUsuarioEmailAndAvaliacaoIdDisciplinaId(usuario.getEmail(),disciplina.getId()))
                .thenReturn(avaliacaoEsperada);

        Avaliacao avaliacaoDoServico = avaliacaoServico.darNota(disciplina.getId(),8.8);

        assertEquals(avaliacaoEsperada.getComentario(), avaliacaoDoServico.getComentario());
        assertEquals(avaliacaoEsperada.getCriadoEm(), avaliacaoDoServico.getCriadoEm());
        assertEquals(avaliacaoEsperada.getFavorito(), avaliacaoDoServico.getFavorito());
        assertNotEquals(notaEsperada, avaliacaoDoServico.getNota());
    }

    @Test
    void deveLancarNotaInvalidaExcecaoQuandoNotaMaiorQueDez(){
        NotaInvalidaExcecao excecao = assertThrows(NotaInvalidaExcecao.class, () -> avaliacaoServico.darNota(disciplina.getId(),11.0));
        assertTrue(excecao.getMessage().contains("Nota invalida"));
    }

    @Test
    void deveLancarNotaInvalidaExcecaoQuandoNotaMenorQueZero(){
        NotaInvalidaExcecao excecao = assertThrows(NotaInvalidaExcecao.class, () -> avaliacaoServico.darNota(disciplina.getId(),-1.0));
        assertTrue(excecao.getMessage().contains("Nota invalida"));
    }

}
