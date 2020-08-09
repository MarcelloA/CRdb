package ufpb.minicurso.crdb.controlador;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufpb.minicurso.crdb.dto.LoginDTO;
import ufpb.minicurso.crdb.dto.UsuarioDTO;
import ufpb.minicurso.crdb.entidade.Usuario;
import ufpb.minicurso.crdb.mapper.UsuarioMapper;
import ufpb.minicurso.crdb.mapper.UsuarioMapperImpl;
import ufpb.minicurso.crdb.servico.impl.JwtServico;
import ufpb.minicurso.crdb.servico.impl.UsuarioServico;

import javax.servlet.ServletException;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class LoginControlador {

    @Autowired
    private UsuarioServico usuarioServico;

    @Autowired
    private JwtServico jwtServico;

    private UsuarioMapper mapper = new UsuarioMapperImpl();

    @PostMapping("/login")
    public ResponseEntity<JSONObject> validarCorpo(@Valid @RequestBody LoginDTO usuario){
        return jwtValidacao(usuario);
    }

    private ResponseEntity<JSONObject> jwtValidacao(LoginDTO usuario){
        return new ResponseEntity<>(jwtServico.validar(usuario), HttpStatus.OK);
    }

    @DeleteMapping("usuario/{email}")
    public ResponseEntity<UsuarioDTO> removerUsuario(@PathVariable("email") String email,
                                                     @RequestHeader("Authorization") String header) throws ServletException{
        return new ResponseEntity<>(mapper.map(usuarioServico.removerUsuario(email,header)), HttpStatus.OK);
    }
}
