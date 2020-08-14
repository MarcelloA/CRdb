package ufpb.minicurso.crdb.servico.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufpb.minicurso.crdb.entidade.Avaliacao;
import ufpb.minicurso.crdb.entidade.AvaliacaoId;
import ufpb.minicurso.crdb.entidade.Disciplina;
import ufpb.minicurso.crdb.entidade.Usuario;
import ufpb.minicurso.crdb.repositorio.AvaliacaoRepositorio;
import ufpb.minicurso.crdb.servico.interfaces.AvaliacaoServicoInterface;

import javax.servlet.ServletException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class AvaliacaoServico implements AvaliacaoServicoInterface {

    @Autowired
    private AvaliacaoRepositorio avaliacaoRepositorio;

    @Autowired
    private DisciplinaServico disciplinaServico;

    @Autowired
    private UsuarioServico usuarioServico;

    private Avaliacao avaliacao;
    private AvaliacaoId avaliacaoId;
    private Usuario usuario;
    private Disciplina disciplina;

    private Avaliacao setAvaliacaoComentario(Usuario usuario, Disciplina disciplina, String comentario){
        AvaliacaoId avaId = new AvaliacaoId(usuario, disciplina);
        if(this.avaliacaoRepositorio.findById(avaId).isPresent()){
            avaliacao = this.avaliacaoRepositorio.findById(avaId).get();
            if(avaliacao.getCriadoEm() == null){
                if(avaliacao.getFavorito() == null){
                    return new Avaliacao(avaId,comentario,avaliacao.getNota(), 0,LocalDateTime.now());
                }
                return new Avaliacao(avaId,comentario,avaliacao.getNota(), avaliacao.getFavorito(),LocalDateTime.now());
            } else {
                return new Avaliacao(avaId,comentario,avaliacao.getNota(), avaliacao.getFavorito(),avaliacao.getCriadoEm());
            }
        } else {
            return new Avaliacao(avaId, comentario,0.0, 0, LocalDateTime.now());
        }
    }

    private Avaliacao setAvaliacaoNota(Usuario usuario, Disciplina disciplina, Double nota){
        AvaliacaoId avaId = new AvaliacaoId(usuario, disciplina);

        if(this.avaliacaoRepositorio.findById(avaId).isPresent()){
            avaliacao = this.avaliacaoRepositorio.findById(avaId).get();

            if(avaliacao.getCriadoEm() == null){
                if(avaliacao.getFavorito() == null){
                    return new Avaliacao(avaId,avaliacao.getComentario(),nota,0, LocalDateTime.now());
                }
                return new Avaliacao(avaId,avaliacao.getComentario(),nota,avaliacao.getFavorito(), LocalDateTime.now());
            } else {
                return new Avaliacao(avaId,avaliacao.getComentario(),nota,avaliacao.getFavorito(), avaliacao.getCriadoEm());
            }
        }  else {
            return new Avaliacao(avaId, "",nota, 0, LocalDateTime.now());
        }
    }

    public Avaliacao getAvaliacao(String header,Long disciplinaId, String email) throws ServletException {
        if(usuarioServico.temPermissao(header, email)){
            return avaliacaoRepositorio.findByAvaliacaoIdUsuarioEmailAndAvaliacaoIdDisciplinaId(email, disciplinaId);
        }
        return null;
    }

    @Override
    public Avaliacao comentar(String header, String email, Long disciplinaId, String comentario)
    throws ServletException{
        if(usuarioServico.temPermissao(header,email)){
            avaliacao = setAvaliacaoComentario(usuarioServico.findById(email),
                    disciplinaServico.getDisciplinaPorId(disciplinaId), comentario);

            return avaliacaoRepositorio.save(avaliacao);
        }
        return null;
    }

    @Override
    @Transactional
    public String deleteComentario(String header, Long disciplinaId, String email)
    throws ServletException{
        if(usuarioServico.temPermissao(header,email)){
            if(avaliacaoRepositorio.deleteByAvaliacaoIdUsuarioEmailAndAvaliacaoIdDisciplinaId(email, disciplinaId) == 1) return " ";
            return "Comentario Não deletado";
        }
        return null;
    }

    @Override
    public Avaliacao favoritarDisciplina(String header, Long disciplinaId, String email) throws ServletException {
        if(usuarioServico.temPermissao(header,email)){
            usuario = usuarioServico.findById(email);
            disciplina = disciplinaServico.getDisciplinaPorId(disciplinaId);
            avaliacaoId = new AvaliacaoId(usuario,disciplina);

            if(avaliacaoRepositorio.findById(avaliacaoId).isPresent()){
                if(avaliacaoRepositorio.findById(avaliacaoId).get().getFavorito() == null ||
                        avaliacaoRepositorio.findById(avaliacaoId).get().getFavorito() == 0){
                    avaliacao = avaliacaoRepositorio.findById(avaliacaoId).get();
                    avaliacao.setFavorito(1);
                    avaliacaoRepositorio.save(avaliacao);
                } else {
                    avaliacao = avaliacaoRepositorio.findById(avaliacaoId).get();
                    avaliacao.setFavorito(0);
                    avaliacaoRepositorio.save(avaliacao);
                }

            }
            return avaliacaoRepositorio.findById(avaliacaoId).get();
        }

        return null;
    }

    @Override
    public Avaliacao darNota(String header, Long disciplinaId, String email, Double campoNota) throws ServletException {
        if(usuarioServico.temPermissao(header, email)){

            avaliacao = setAvaliacaoNota(usuarioServico.findById(email),
                    disciplinaServico.getDisciplinaPorId(disciplinaId), campoNota);

            return avaliacaoRepositorio.save(avaliacao);
        }
        return null;
    }

    public List<Avaliacao> getListaAvaliacoes(Long disciplinaId){
        return this.avaliacaoRepositorio.findAllByAvaliacaoIdDisciplinaId(disciplinaId);
    }

    public List<Avaliacao> findAll(){
        return avaliacaoRepositorio.findAll();
    }

}
