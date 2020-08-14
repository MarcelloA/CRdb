package ufpb.minicurso.crdb.servico.interfaces;

import ufpb.minicurso.crdb.entidade.Avaliacao;

import javax.servlet.ServletException;

public interface AvaliacaoServicoInterface {

    Avaliacao comentar(String header, String email, Long disciplinaId, String comentario) throws ServletException;

    String deleteComentario(String header, Long disciplinaId, String email) throws ServletException;

    Avaliacao favoritarDisciplina(String header, Long disciplinaId, String email) throws ServletException;

    Avaliacao darNota(String header, Long disciplinaId, String email, Double campoNota) throws ServletException;

}
