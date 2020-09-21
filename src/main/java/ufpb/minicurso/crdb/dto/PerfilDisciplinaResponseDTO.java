package ufpb.minicurso.crdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufpb.minicurso.crdb.entidade.Comentario;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilDisciplinaResponseDTO {

    private String disciplinaNome;
    private List<Comentario> comentarios;
    private Integer numComentarios;
    private List<Double> notas;
    private Double media;
    private Integer numFavoritos;

    public Double getMedia() {
        double not = 0.0;

        for (Double nota : notas) not = not + nota;

        return not/notas.size();
    }

    public Integer getNumComentarios() {
        return comentarios.size();
    }


}
