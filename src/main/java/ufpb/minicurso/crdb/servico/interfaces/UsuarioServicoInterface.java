package ufpb.minicurso.crdb.servico.interfaces;

import ufpb.minicurso.crdb.dto.LoginDTO;
import ufpb.minicurso.crdb.entidade.Usuario;

import javax.servlet.ServletException;

public interface UsuarioServicoInterface {

    Usuario findById(String email);

    boolean validarUsuarioSenha(LoginDTO usuario);

    boolean temPermissao(String authorizationHeader, String email) throws ServletException;

    Usuario removerUsuario(String email, String authHeader) throws ServletException;
}
