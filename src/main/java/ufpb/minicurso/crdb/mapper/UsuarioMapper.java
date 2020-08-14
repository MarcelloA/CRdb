package ufpb.minicurso.crdb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ufpb.minicurso.crdb.dto.UsuarioDTO;
import ufpb.minicurso.crdb.entidade.Usuario;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mappings({
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "primeiroNome", target = "primeiroNome"),
            @Mapping(source = "ultimoNome", target = "ultimoNome"),
    })
    UsuarioDTO map(Usuario usuario);

    List<UsuarioDTO> map(List<Usuario> usuarios);
}
