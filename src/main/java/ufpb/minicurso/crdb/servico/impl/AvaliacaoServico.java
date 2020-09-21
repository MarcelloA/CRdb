package ufpb.minicurso.crdb.servico.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ufpb.minicurso.crdb.entidade.*;
import ufpb.minicurso.crdb.excecao.DisciplinaNaoEncontradaExcecao;
import ufpb.minicurso.crdb.excecao.NotaInvalidaExcecao;
import ufpb.minicurso.crdb.repositorio.AvaliacaoRepositorio;
import ufpb.minicurso.crdb.repositorio.DisciplinaRepositorio;
import ufpb.minicurso.crdb.servico.interfaces.AvaliacaoServicoInterface;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AvaliacaoServico implements AvaliacaoServicoInterface {
    private final AvaliacaoRepositorio avaliacaoRepositorio;
    private final DisciplinaRepositorio disciplinaRepositorio;

    private Avaliacao instanciandoAvaliacaoVazia(Long disciplinaId)  {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = new Usuario(email,"","","",
                new ArrayList<>(Collections.singletonList(Funcao.ROLE_USUARIO)),
                Collections.emptyList());
        Disciplina disciplina = new Disciplina(disciplinaId,"",Collections.emptyList());
        AvaliacaoId avaliacaoId = new AvaliacaoId(usuario,disciplina);
        Comentario comentario = new Comentario(-1L,false,"", new Avaliacao());

        Avaliacao avaliacao = new Avaliacao(avaliacaoId, comentario,null,false, LocalDateTime.now());

        avaliacao.getComentario().setAvaliacao(avaliacao);

        return avaliacao;
    }

    private Boolean disciplinaEncontrada(Long disciplinaId){
        disciplinaRepositorio.findById(disciplinaId).orElseThrow(() -> new DisciplinaNaoEncontradaExcecao("Id da disciplina nao encontrado"));
        return true;
    }

    @Override
    public Avaliacao comentar(Long disciplinaId, String comentario) {
        Avaliacao avaliacao = instanciandoAvaliacaoVazia(disciplinaId);

        if(comentario.equals("")) return avaliacao;

        if(disciplinaEncontrada(disciplinaId)) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            avaliacao = avaliacaoRepositorio.findByAvaliacaoIdUsuarioEmailAndAvaliacaoIdDisciplinaId(email, disciplinaId);

            if(avaliacao == null) {
                avaliacao = instanciandoAvaliacaoVazia(disciplinaId);

                avaliacao.setComentario(instanciandoComentario(true, comentario, new Avaliacao()));

                avaliacao.getComentario().setAvaliacao(avaliacao);

                avaliacaoRepositorio.save(avaliacao);
                return avaliacao;
            }

            avaliacao.setComentario(instanciandoComentario(true,comentario, avaliacao));
            avaliacaoRepositorio.save(avaliacao);
        }
        return avaliacao;
    }

    private Comentario instanciandoComentario(boolean visibilidade, String anotacao, Avaliacao avaliacao){
        Comentario comentario = new Comentario();
        comentario.setVisibilidade(visibilidade);
        comentario.setComentario(anotacao);
        comentario.setAvaliacao(avaliacao);

        return comentario;
    }

    @Override
    public Comentario deletaComentario(Long disciplinaId) {
        Avaliacao avaliacao = instanciandoAvaliacaoVazia(disciplinaId);

        if(disciplinaEncontrada(disciplinaId)){
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            avaliacao = avaliacaoRepositorio.findByAvaliacaoIdUsuarioEmailAndAvaliacaoIdDisciplinaId(email, disciplinaId);

            if(avaliacao == null) return instanciandoComentario(true,"Comentario nao foi deletado. Avaliacao nao encontrada", new Avaliacao());

            avaliacao.getComentario().setVisibilidade(false);
            avaliacaoRepositorio.save(avaliacao);
        }
        return avaliacao.getComentario();
    }

    @Override
    public Avaliacao favoritarDisciplina(Long disciplinaId) {
        Avaliacao avaliacao = instanciandoAvaliacaoVazia(disciplinaId);

        if(disciplinaEncontrada(disciplinaId)){
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            avaliacao = avaliacaoRepositorio.findByAvaliacaoIdUsuarioEmailAndAvaliacaoIdDisciplinaId(email, disciplinaId);

            if(avaliacao == null) return instanciandoAvaliacaoVazia(disciplinaId);

            if(!avaliacao.getFavorito()) {
                avaliacao.setFavorito(true);

                avaliacaoRepositorio.save(avaliacao);
                return avaliacao;
            }

            avaliacao.setFavorito(false);
            avaliacaoRepositorio.save(avaliacao);
        }
        return avaliacao;
    }

    @Override
    public Avaliacao darNota(Long disciplinaId, Double campoNota) {
        Avaliacao avaliacao = instanciandoAvaliacaoVazia(disciplinaId);

        if(campoNota > 10 || campoNota < 0) throw new NotaInvalidaExcecao("Nota invalida");

        if(disciplinaEncontrada(disciplinaId)) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            avaliacao = avaliacaoRepositorio.findByAvaliacaoIdUsuarioEmailAndAvaliacaoIdDisciplinaId(email, disciplinaId);

            if(avaliacao == null){
                avaliacao = instanciandoAvaliacaoVazia(disciplinaId);
                avaliacao.setNota(campoNota);

                avaliacaoRepositorio.save(avaliacao);
                return avaliacao;
            }

            avaliacao.setNota(campoNota);
            avaliacaoRepositorio.save(avaliacao);
        }
        return avaliacao;
    }

    public List<Avaliacao> encontrarAvaliacoesPorDisciplinaId(Long disciplinaId) {
        return avaliacaoRepositorio.findAllByAvaliacaoIdDisciplinaId(disciplinaId);
    }

    public List<Avaliacao> listarTodasAvaliacoes(){
        return avaliacaoRepositorio.findAll();
    }
}
