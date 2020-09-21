package ufpb.minicurso.crdb.excecao;

public class UsuarioSenhaNaoValidosExcecao extends RuntimeException {
    public UsuarioSenhaNaoValidosExcecao(String mensagem){
        super(mensagem);
    }
}
