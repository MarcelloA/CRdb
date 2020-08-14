package ufpb.minicurso.crdb.excecao;

public class UsuarioSenhaNaoValidos extends RuntimeException {
    public UsuarioSenhaNaoValidos(String mensagem){
        super(mensagem);
    }
}
