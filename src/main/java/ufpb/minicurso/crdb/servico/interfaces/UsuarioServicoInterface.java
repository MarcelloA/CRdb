package ufpb.minicurso.crdb.servico.interfaces;

import ufpb.minicurso.crdb.entidade.Usuario;

public interface UsuarioServicoInterface {

    Usuario encontrarUsuarioPorId(String email);

    String removerUsuario();
}
