package ufpb.minicurso.crdb.excecao;

public class NotaInvalidaExcecao extends RuntimeException {
    public NotaInvalidaExcecao(String mensagem){ super(mensagem); }
}
