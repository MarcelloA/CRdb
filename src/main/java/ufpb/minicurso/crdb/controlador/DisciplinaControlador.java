package ufpb.minicurso.crdb.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ufpb.minicurso.crdb.dto.PerfilDisciplinaResponseDTO;
import ufpb.minicurso.crdb.entidade.Disciplina;
import ufpb.minicurso.crdb.servico.impl.DisciplinaServico;

import java.util.List;

@RestController
@RequestMapping("/disciplina")
@RequiredArgsConstructor
public class DisciplinaControlador {
    private final DisciplinaServico disciplinaServico;

    @GetMapping("/buscar/todas")
    public ResponseEntity<List<Disciplina>> listarTodasDisciplinas(){
        return new ResponseEntity<>(disciplinaServico.getDisciplinas(), HttpStatus.OK);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Disciplina>> acharDisciplinasPorNome(@RequestParam String nome) {
        return new ResponseEntity<>(disciplinaServico.getDisciplinaPorNome(nome), HttpStatus.OK);
    }

    @GetMapping("/buscar/identificador")
    public ResponseEntity<Disciplina> acharDisciplinaPorId(@RequestParam Long id) {
        return new ResponseEntity<>(disciplinaServico.getDisciplinaPorId(id), HttpStatus.OK);
    }

    @GetMapping("/perfil/{disciplinaId}")
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<PerfilDisciplinaResponseDTO> acharPerfildeDisciplina(@PathVariable("disciplinaId") Long disciplinaId){
        return new ResponseEntity<>(disciplinaServico.obterPerfil(disciplinaId), HttpStatus.OK);
    }

    @GetMapping("/perfil/rank")
    public ResponseEntity<List<PerfilDisciplinaResponseDTO>> rankearPerfis(@RequestParam String modo){
        return new ResponseEntity<>(disciplinaServico.modoDeOrdenacao(modo), HttpStatus.OK);
    }

}
