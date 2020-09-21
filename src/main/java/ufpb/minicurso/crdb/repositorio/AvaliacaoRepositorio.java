package ufpb.minicurso.crdb.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufpb.minicurso.crdb.entidade.Avaliacao;
import ufpb.minicurso.crdb.entidade.AvaliacaoId;

import java.util.List;

@Repository
public interface AvaliacaoRepositorio extends JpaRepository<Avaliacao, AvaliacaoId> {

    Avaliacao findByAvaliacaoIdUsuarioEmailAndAvaliacaoIdDisciplinaId(String usuarioEmail, Long disciplinaId);

    Integer deleteByAvaliacaoIdUsuarioEmailAndAvaliacaoIdDisciplinaId(String usuarioEmail, Long disciplinaId);

    List<Avaliacao> findAllByAvaliacaoIdDisciplinaId(Long disciplinaId);

}
