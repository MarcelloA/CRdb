package ufpb.minicurso.crdb.excecao;

public class TokenExcecao extends RuntimeException{
    public TokenExcecao(String mensagem){
        super(mensagem);
    }
}
