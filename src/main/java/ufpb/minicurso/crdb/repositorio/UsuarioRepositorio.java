package ufpb.minicurso.crdb.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufpb.minicurso.crdb.entidade.Usuario;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio<T, ID extends Serializable> extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByEmail(String id);

    List<Usuario> findByPrimeiroNome(String primeiroNome);

    List<Usuario> findByUltimoNome(String ultimoNome);



}
