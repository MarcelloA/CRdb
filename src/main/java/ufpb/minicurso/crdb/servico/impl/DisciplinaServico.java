package ufpb.minicurso.crdb.servico.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufpb.minicurso.crdb.dto.PerfilDisciplinaResponseDTO;
import ufpb.minicurso.crdb.entidade.Avaliacao;
import ufpb.minicurso.crdb.entidade.Disciplina;
import ufpb.minicurso.crdb.excecao.DisciplinaNaoEncontrada;
import ufpb.minicurso.crdb.repositorio.DisciplinaRepositorio;
import ufpb.minicurso.crdb.servico.interfaces.DisciplinaServicoInterface;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class DisciplinaServico implements DisciplinaServicoInterface {

    @Autowired
    private DisciplinaRepositorio disciplinaRepositorio;

    @Autowired
    private UsuarioServico usuarioServico;

    @Autowired
    private AvaliacaoServico avaliacaoServico;

    private List<Disciplina> disciplinas;
    private List<Avaliacao> avaliacoes;
    private PerfilDisciplinaResponseDTO perfil;
    List<PerfilDisciplinaResponseDTO> lista = new ArrayList<>();

    @PostConstruct
    public void initCourse() {
        if (disciplinaRepositorio.count() == 96) {
            return;
        } else {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Disciplina>> typeReference = new TypeReference<List<Disciplina>>() {
            };
            InputStream inputStream = ObjectMapper.class.getResourceAsStream("/json/courses.json");
            try {
                disciplinas = mapper.readValue(inputStream, typeReference);
                this.disciplinaRepositorio.saveAll(disciplinas);
                System.out.println("Courses saved on Db");
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
        return this.disciplinaRepositorio.findById(id).orElseThrow(() -> new DisciplinaNaoEncontrada("Disciplina nao encontrada"));
    }

    public PerfilDisciplinaResponseDTO getPerfilDisciplina(String header, String email, Long disciplinaId) throws ServletException {
        if (usuarioServico.temPermissao(header, email)) {
            this.disciplinaRepositorio.findById(disciplinaId);
            avaliacoes = avaliacaoServico.getListaAvaliacoes(disciplinaId);

            if (criandoPerfil(disciplinaId)) return perfil;
        }
        return null;
    }

    private PerfilDisciplinaResponseDTO getPerfil(Long disciplinaId) {
        if (criandoPerfil(disciplinaId)) return perfil;
        return null;
    }

    private boolean criandoPerfil(Long disciplinaId) {
        if (this.disciplinaRepositorio.findById(disciplinaId).isPresent()) {
            perfil = new PerfilDisciplinaResponseDTO();
            perfil.setDisciplinaNome(this.disciplinaRepositorio.findById(disciplinaId).get().getNome());
            perfil.setComentarios(avaliacoes.stream().map(Avaliacao::getComentario).collect(Collectors.toList()));
            perfil.setNotas(avaliacoes.stream().map(Avaliacao::getNota).collect(Collectors.toList()));
            perfil.setNumFavoritos((int) avaliacoes.stream().filter(a -> a.getFavorito() == 1).count());

            return true;
        }
        return false;
    }


    public List<PerfilDisciplinaResponseDTO> ordenarDisciplinas(String header, String email, String modo) throws ServletException {
        if (usuarioServico.temPermissao(header, email)) {
            avaliacoes = avaliacaoServico.findAll();
            if (modo.equals("favorito")) {
                percorrerAvaliacoes(Comparator.comparingInt(PerfilDisciplinaResponseDTO::getNumFavoritos));
            } else if (modo.equals("comentario")) {
                percorrerAvaliacoes(Comparator.comparingInt(PerfilDisciplinaResponseDTO::getNumComentarios));
            } else if (modo.equals("nota")) {
                for (Avaliacao m : avaliacaoServico.findAll()) {
                    Long dId = m.getAvaliacaoId().getDisciplina().getId();

                    PerfilDisciplinaResponseDTO perfilDisciplina = getPerfil(dId);
                    perfilDisciplina.getNotas().sort(Comparator.naturalOrder());
                    lista.add(perfilDisciplina);
                }

                Collections.sort(lista, (o1, o2) -> {

                    Float f1 = Float.parseFloat(o1.getNotas().toString());
                    Float f2 = Float.parseFloat(o2.getNotas().toString());
                    return f2.compareTo(f1);
                });
                return lista;
            }
        }
        return null;
    }

    private void percorrerAvaliacoes(Comparator<PerfilDisciplinaResponseDTO> perfilDisciplinaResponseDTOComparator) {
        for (Avaliacao m : avaliacaoServico.findAll()) {
            Long dId = m.getAvaliacaoId().getDisciplina().getId();

            PerfilDisciplinaResponseDTO perfilDisciplina = getPerfil(dId);
            lista.add(perfilDisciplina);
        }
        lista.sort(perfilDisciplinaResponseDTOComparator);
    }
}
