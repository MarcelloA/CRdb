package ufpb.minicurso.crdb.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ufpb.minicurso.crdb.dto.LoginDTO;
import ufpb.minicurso.crdb.entidade.Usuario;
import ufpb.minicurso.crdb.servico.impl.UsuarioServico;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioControlador {

    private final UsuarioServico usuarioServico;

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarUsuario(@Valid @RequestBody Usuario usuario){
        return new ResponseEntity<>(usuarioServico.cadastrar(usuario), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> signIn(@Valid @RequestBody LoginDTO usuario) {
        return new ResponseEntity<>(usuarioServico.signIn(usuario), HttpStatus.OK);
    }

    @DeleteMapping("/deletar")
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<String> removerUsuario(){
        return new ResponseEntity<>(usuarioServico.removerUsuario(), HttpStatus.OK);
    }

    @DeleteMapping("/admin/deletar/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> removerUsuarioPeloAdmin(@PathVariable("email") String email){
        return new ResponseEntity<>(usuarioServico.removerUsuarioPeloAdmin(email), HttpStatus.OK);
    }
}
