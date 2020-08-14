package ufpb.minicurso.crdb.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufpb.minicurso.crdb.dto.AvaliacaoDTO;
import ufpb.minicurso.crdb.dto.PerfilDisciplinaResponseDTO;
import ufpb.minicurso.crdb.entidade.Disciplina;
import ufpb.minicurso.crdb.mapper.AvaliacaoMapper;
import ufpb.minicurso.crdb.mapper.AvaliacaoMapperImpl;
import ufpb.minicurso.crdb.servico.impl.AvaliacaoServico;
import ufpb.minicurso.crdb.servico.impl.DisciplinaServico;
import ufpb.minicurso.crdb.servico.impl.UsuarioServico;

import javax.servlet.ServletException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/disciplina")
public class DisciplinaControlador {

    @Autowired
    private DisciplinaServico disciplinaServico;

    @Autowired
    private AvaliacaoServico avaliacaoServico;

    private final AvaliacaoMapper mapper = new AvaliacaoMapperImpl();

    @GetMapping("/buscar")
    public ResponseEntity<List<Disciplina>> getTodasDisciplinas(){
        return new ResponseEntity<>(disciplinaServico.getDisciplinas(), HttpStatus.OK);
    }

    @GetMapping("/buscar/{nome}")
    public ResponseEntity<List<Disciplina>> getDisciplinaPorNome(@PathVariable("nome") String nome){
        return new ResponseEntity<>(disciplinaServico.getDisciplinaPorNome(nome), HttpStatus.OK);
    }

    @GetMapping("/avaliacao")
    public ResponseEntity<AvaliacaoDTO> getAvaliacao(@RequestHeader("Authorization") String header,
                                                  @Valid @RequestBody AvaliacaoDTO avaliacaoDTO) throws ServletException {
        return new ResponseEntity<>(mapper.map(avaliacaoServico.getAvaliacao(header, avaliacaoDTO.getDisciplinaId(), avaliacaoDTO.getEmail())),
                HttpStatus.OK);
    }

    @PostMapping("/avaliacao/comentario")
    public ResponseEntity<AvaliacaoDTO> setDisciplinaAvaliacaoComentario(@RequestHeader("Authorization") String header,
                                                                      @Valid
                                                                      @RequestBody AvaliacaoDTO avaliacaoDTO) throws ServletException {
        return new ResponseEntity<>(mapper.map(avaliacaoServico.comentar(header,
                avaliacaoDTO.getEmail(),avaliacaoDTO.getDisciplinaId(),avaliacaoDTO.getComentario())), HttpStatus.OK);
    }

  @DeleteMapping("/avaliacao")
    public ResponseEntity<String> deleteDisciplinaAvaliacaoComentario(@RequestHeader("Authorization") String header,
                                                                      @Valid
                                                                      @RequestBody AvaliacaoDTO avaliacaoDTO) throws ServletException {
        return new ResponseEntity<>(avaliacaoServico.deleteComentario(header,
                avaliacaoDTO.getDisciplinaId(), avaliacaoDTO.getEmail()) , HttpStatus.OK);
    }

    @PostMapping("/avaliacao/favoritar")
    public ResponseEntity<AvaliacaoDTO> setDisciplinaAvaliacaoFavorito(@RequestHeader("Authorization") String header,
                                                                       @Valid
                                                                       @RequestBody AvaliacaoDTO avaliacaoDTO) throws ServletException {
        return new ResponseEntity<>(mapper.map(avaliacaoServico.favoritarDisciplina(header,
                avaliacaoDTO.getDisciplinaId(),avaliacaoDTO.getEmail())),HttpStatus.OK);
    }

    @PostMapping("/avaliacao/nota")
    public ResponseEntity<AvaliacaoDTO> setDisciplinaAvaliacaoNota(@RequestHeader("Authorization") String header,
                                                                         @Valid
                                                                         @RequestBody AvaliacaoDTO avaliacaoDTO) throws ServletException {
        return new ResponseEntity<>(mapper.map(avaliacaoServico.darNota(header,
                avaliacaoDTO.getDisciplinaId(), avaliacaoDTO.getEmail(), avaliacaoDTO.getNota())), HttpStatus.OK);
    }

    @GetMapping("/perfil/{email}/{id}")
    public ResponseEntity<PerfilDisciplinaResponseDTO> getPerfil(@RequestHeader("Authorization") String header,
                                                                 @PathVariable("id") String id,
                                                                 @PathVariable("email") String email) throws ServletException {
        return new ResponseEntity<>(disciplinaServico.getPerfilDisciplina(header,
                email, Long.parseLong(id)), HttpStatus.OK);
    }

    @GetMapping("/ranking/{email}/{modo}")
    public ResponseEntity<List<PerfilDisciplinaResponseDTO>> getRanking(@RequestHeader("Authorization") String header,
                                                                        @PathVariable("modo") String modo,
                                                                        @PathVariable("email") String email) throws ServletException {
        return new ResponseEntity<>(disciplinaServico.ordenarDisciplinas(header,email,modo), HttpStatus.OK);
    }

}
