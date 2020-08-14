package ufpb.minicurso.crdb.excecao;

public class DisciplinaNaoEncontrada extends RuntimeException {
    public DisciplinaNaoEncontrada(String mensagem){
        super(mensagem);
    }
}
