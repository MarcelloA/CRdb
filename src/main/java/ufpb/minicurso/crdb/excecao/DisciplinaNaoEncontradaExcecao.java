package ufpb.minicurso.crdb.excecao;

public class DisciplinaNaoEncontradaExcecao extends RuntimeException {
    public DisciplinaNaoEncontradaExcecao(String mensagem){
        super(mensagem);
    }

}
