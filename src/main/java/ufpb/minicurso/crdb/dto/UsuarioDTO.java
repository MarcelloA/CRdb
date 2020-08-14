package ufpb.minicurso.crdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private String email;
    private String primeiroNome;
    private String ultimoNome;
}
