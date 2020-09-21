package ufpb.minicurso.crdb.servico.interfaces;

import ufpb.minicurso.crdb.entidade.Avaliacao;
import ufpb.minicurso.crdb.entidade.Comentario;

public interface AvaliacaoServicoInterface {

    Avaliacao comentar(Long disciplinaId, String comentario);

    Comentario deletaComentario(Long disciplinaId);

    Avaliacao favoritarDisciplina(Long disciplinaId);

    Avaliacao darNota(Long disciplinaId, Double campoNota);

}
