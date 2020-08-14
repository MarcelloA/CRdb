package ufpb.minicurso.crdb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ufpb.minicurso.crdb.dto.AvaliacaoDTO;
import ufpb.minicurso.crdb.entidade.Avaliacao;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AvaliacaoMapper {
    @Mappings({
            @Mapping(source = "avaliacaoId.usuario.email", target = "email"),
            @Mapping(source = "avaliacaoId.disciplina.id", target = "disciplinaId"),
            @Mapping(source = "avaliacaoId.disciplina.nome", target = "disciplinaNome"),
            @Mapping(source = "comentario", target = "comentario"),
            @Mapping(source = "nota", target = "nota"),
            @Mapping(source = "favorito", target = "favorito")
    })
    AvaliacaoDTO map(Avaliacao avaliacao);

    List<AvaliacaoDTO> map(List<Avaliacao> avaliacoes);
}
