package ufpb.minicurso.crdb.controlador;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ufpb.minicurso.crdb.dto.AvaliacaoDTO;
import ufpb.minicurso.crdb.entidade.Comentario;
import ufpb.minicurso.crdb.mapper.AvaliacaoMapper;
import ufpb.minicurso.crdb.mapper.AvaliacaoMapperImpl;
import ufpb.minicurso.crdb.servico.impl.AvaliacaoServico;

@RestController
@RequestMapping("/avaliacao")
@RequiredArgsConstructor
public class AvaliacaoControlador {

    private final AvaliacaoServico avaliacaoServico;

    private final AvaliacaoMapper mapper = new AvaliacaoMapperImpl();

    @PostMapping("/comentario/{disciplinaId}")
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<AvaliacaoDTO> comentarAvaliacao(@PathVariable("disciplinaId") Long disciplinaId, @RequestBody JSONObject comentarioJson){

        String comentario = (String) comentarioJson.get("comentario");
        return new ResponseEntity<>(mapper.map(avaliacaoServico.comentar(disciplinaId, comentario)), HttpStatus.OK);
    }

    @PostMapping("/nota/{disciplinaId}")
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<AvaliacaoDTO> darnotaAvaliacao(@PathVariable("disciplinaId") Long disciplinaId, @RequestBody JSONObject notaJson) {
        Double nota = (Double) notaJson.get("nota");
        return new ResponseEntity<>(mapper.map(avaliacaoServico.darNota(disciplinaId,nota)), HttpStatus.OK);
    }

    @PostMapping("/favorito/{disciplinaId}")
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<AvaliacaoDTO> favoritarAvaliacao(@PathVariable("disciplinaId") Long disciplinaId) {
        return new ResponseEntity<>(mapper.map(avaliacaoServico.favoritarDisciplina(disciplinaId)), HttpStatus.OK);
    }

    @DeleteMapping("/comentario/deletar/{disciplinaId}")
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<Comentario> deletarComentario(@PathVariable("disciplinaId") Long disciplinaId){
        return new ResponseEntity<>(avaliacaoServico.deletaComentario(disciplinaId), HttpStatus.OK);
    }

}
