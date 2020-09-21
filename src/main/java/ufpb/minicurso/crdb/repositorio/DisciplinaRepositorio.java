package ufpb.minicurso.crdb.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufpb.minicurso.crdb.entidade.Disciplina;

import java.util.List;
@Repository
public interface DisciplinaRepositorio extends JpaRepository<Disciplina, Long> {

    List<Disciplina> findByNomeContaining(String nome);

}
