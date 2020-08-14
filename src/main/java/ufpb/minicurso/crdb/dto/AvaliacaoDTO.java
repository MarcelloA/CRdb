package ufpb.minicurso.crdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvaliacaoDTO {

    @NotNull
    private String email;
    @NotNull
    private Long disciplinaId;

    private String disciplinaNome;

    private String comentario;

    @Max(10)
    @Min(0)
    private Double nota;

    @Max(1)
    @Min(0)
    private Integer favorito;
}
