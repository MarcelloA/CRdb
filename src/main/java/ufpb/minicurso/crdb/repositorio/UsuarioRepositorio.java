package ufpb.minicurso.crdb.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufpb.minicurso.crdb.entidade.Usuario;

import java.io.Serializable;

@Repository
public interface UsuarioRepositorio<T, ID extends Serializable> extends JpaRepository<Usuario, String> {

    Usuario findByEmail(String id);
}
