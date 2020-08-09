package ufpb.minicurso.crdb.servico.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufpb.minicurso.crdb.dto.LoginDTO;
import ufpb.minicurso.crdb.entidade.Usuario;
import ufpb.minicurso.crdb.repositorio.UsuarioRepositorio;
import ufpb.minicurso.crdb.servico.interfaces.UsuarioServicoInterface;

import javax.servlet.ServletException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioServico implements UsuarioServicoInterface{
    @Autowired
    private UsuarioRepositorio<Usuario,String> usuarioRepositorio;

    @Autowired
    private JwtServico jwtServico;

    public Usuario save(Usuario usuario){
        return this.usuarioRepositorio.save(usuario);
    }

    public List<Usuario> findByPrimeiroNome(String primeiroNome){
        return this.usuarioRepositorio.findByPrimeiroNome(primeiroNome);
    }

    public List<Usuario> findByUltimoNome(String ultimoNome){
        return this.usuarioRepositorio.findByUltimoNome(ultimoNome);
    }

    @Override
    public Usuario findById(String email) {
        Optional<Usuario> usuario = usuarioRepositorio.findByEmail(email);
        if(!usuario.isPresent())
            throw new IllegalArgumentException("Usuario nao encontrado");
        return usuario.get();
    }

    @Override
    public boolean validarUsuarioSenha(LoginDTO usuario) {
        Optional<Usuario> optUser = usuarioRepositorio.findByEmail(usuario.getEmail());
        return optUser.isPresent() && optUser.get().getSenha().equals(usuario.getSenha());
    }

    @Override
    public boolean temPermissao(String authorizationHeader, String email) throws ServletException {
        String subject = jwtServico.pegaSujeito(authorizationHeader);
        Optional<Usuario> optUser = usuarioRepositorio.findByEmail(subject);
        return optUser.isPresent() && optUser.get().getEmail().equals(email);
    }

    @Override
    public Usuario removerUsuario(String email, String authHeader) throws ServletException {
        Usuario usuario = findById(email);
        if (temPermissao(authHeader, email)) {
            usuarioRepositorio.delete(usuario);
            return usuario;
        }
        throw new ServletException("Usuario nao tem permissao");
    }
}
