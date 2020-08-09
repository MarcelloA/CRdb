package ufpb.minicurso.crdb.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufpb.minicurso.crdb.dto.UsuarioDTO;
import ufpb.minicurso.crdb.entidade.Usuario;
import ufpb.minicurso.crdb.mapper.UsuarioMapper;
import ufpb.minicurso.crdb.mapper.UsuarioMapperImpl;
import ufpb.minicurso.crdb.servico.impl.UsuarioServico;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioServico usuarioServico;

    private UsuarioMapper mapper = new UsuarioMapperImpl();

    @PostMapping
    public ResponseEntity<UsuarioDTO> validarUsuario(@Valid @RequestBody Usuario usuario){
        return salvaUsuario(usuario);
    }

    private ResponseEntity<UsuarioDTO> salvaUsuario(Usuario usuario){
        return new ResponseEntity<>(mapper.map(usuarioServico.save(usuario)), HttpStatus.OK);
    }
}
