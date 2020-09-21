package ufpb.minicurso.crdb.excecao;

public class UsuarioNaoEncontradoExcecao extends RuntimeException {
    public UsuarioNaoEncontradoExcecao(String mensagem){ super(mensagem); }
}
