package ufpb.minicurso.crdb.servico.interfaces;

import ufpb.minicurso.crdb.entidade.Disciplina;

import java.util.List;

public interface DisciplinaServicoInterface {

    List<Disciplina> getDisciplinaPorNome(String nome);

    List<Disciplina> getDisciplinas();

    Disciplina getDisciplinaPorId(Long id);

}
