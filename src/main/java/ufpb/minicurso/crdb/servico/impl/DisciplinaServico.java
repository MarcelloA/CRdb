package ufpb.minicurso.crdb.servico.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ufpb.minicurso.crdb.dto.PerfilDisciplinaResponseDTO;
import ufpb.minicurso.crdb.entidade.Avaliacao;
import ufpb.minicurso.crdb.entidade.AvaliacaoId;
import ufpb.minicurso.crdb.entidade.Comentario;
import ufpb.minicurso.crdb.entidade.Disciplina;
import ufpb.minicurso.crdb.excecao.DisciplinaNaoEncontradaExcecao;
import ufpb.minicurso.crdb.repositorio.DisciplinaRepositorio;
import ufpb.minicurso.crdb.servico.interfaces.DisciplinaServicoInterface;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DisciplinaServico implements DisciplinaServicoInterface {

    private final DisciplinaRepositorio disciplinaRepositorio;
    private List<Avaliacao> avaliacoes;
    private final AvaliacaoServico avaliacaoServico;
    private PerfilDisciplinaResponseDTO perfil;


    public DisciplinaServico(DisciplinaRepositorio disciplinaRepositorio, AvaliacaoServico avaliacaoServico) {
        this.disciplinaRepositorio = disciplinaRepositorio;
        this.avaliacaoServico = avaliacaoServico;
    }

    @PostConstruct
    public void initCourse() {
        if (disciplinaRepositorio.count() != 0) {
            return;
        } else {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Disciplina>> typeReference = new TypeReference<List<Disciplina>>() {
            };
            InputStream inputStream = ObjectMapper.class.getResourceAsStream("/json/courses.json");
            try {
                List<Disciplina> disciplinas = mapper.readValue(inputStream, typeReference);
                this.disciplinaRepositorio.saveAll(disciplinas);
                System.out.println("Disciplinas salvas no Banco de Dados");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Disciplina> getDisciplinaPorNome(String nome) {
        return this.disciplinaRepositorio.findByNomeContaining(nome);
    }

    @Override
    public List<Disciplina> getDisciplinas() {
        return this.disciplinaRepositorio.findAll();
    }

    @Override
    public Disciplina getDisciplinaPorId(Long id) {
        return this.disciplinaRepositorio.findById(id)
                .orElseThrow(() -> new DisciplinaNaoEncontradaExcecao("Disciplina nao encontrada"));
    }

    private boolean existePerfil(Long disciplinaId) {
        if(this.disciplinaRepositorio.findById(disciplinaId).isPresent()){
            perfil = new PerfilDisciplinaResponseDTO();
            perfil.setDisciplinaNome(getDisciplinaPorId(disciplinaId).getNome());
            perfil.setComentarios(avaliacoes.stream().map(Avaliacao::getComentario).filter(Comentario::getVisibilidade).collect(Collectors.toList()));
            perfil.setNotas(avaliacoes.stream().map(Avaliacao::getNota).filter(Objects::nonNull).collect(Collectors.toList()));
            perfil.setNumFavoritos((int) avaliacoes.stream().filter(Avaliacao::getFavorito).count());

            return true;
        }
        return false;
    }

    private void preencherListaDeAvaliacoes(Long disciplinaId){
        avaliacoes = avaliacaoServico.encontrarAvaliacoesPorDisciplinaId(disciplinaId);
    }

    public PerfilDisciplinaResponseDTO obterPerfil(Long disciplinaId) {
        preencherListaDeAvaliacoes(disciplinaId);
        if(existePerfil(disciplinaId)){
            return perfil;
        }
        return new PerfilDisciplinaResponseDTO("", Collections.emptyList(), 0, Collections.emptyList(), 0.0, 0);
    }

    public List<PerfilDisciplinaResponseDTO> modoDeOrdenacao(String modo) {
        avaliacoes = avaliacaoServico.listarTodasAvaliacoes();
        if(modo.equals("favorito")) {
           return ordenarPerfis(Comparator.comparingInt(PerfilDisciplinaResponseDTO::getNumFavoritos));
        }
        if(modo.equals("comentario")) {
            return ordenarPerfis(Comparator.comparingInt(PerfilDisciplinaResponseDTO::getNumComentarios));
        }
        if(modo.equals("nota")){
            return ordenarPerfis(Comparator.comparingDouble(PerfilDisciplinaResponseDTO::getMedia));
        }
        return Collections.singletonList(new PerfilDisciplinaResponseDTO("", Collections.emptyList(), 0, Collections.emptyList(), 0.0, 0));
    }

    private List<PerfilDisciplinaResponseDTO> ordenarPerfis(Comparator<PerfilDisciplinaResponseDTO> perfilDisciplinaResponseDTOComparator) {
        return avaliacoes.stream()
                .map(Avaliacao::getAvaliacaoId)
                .map(AvaliacaoId::getDisciplina)
                .map(Disciplina::getId)
                .distinct()
                .map(this::obterPerfil)
                .sorted(perfilDisciplinaResponseDTOComparator)
                .collect(Collectors.toList());
    }
}
