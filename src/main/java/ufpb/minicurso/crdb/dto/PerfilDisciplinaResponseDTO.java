package ufpb.minicurso.crdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilDisciplinaResponseDTO {

    private String disciplinaNome;
    private List<String> comentarios;
    private Integer numComentarios;
    private List<Double> notas;
    private Double media;
    private Integer numFavoritos;

    public Double getMedia() {
        double not = 0.0;

        for(int i=0; i < notas.size(); i++) not = not + notas.get(i);

        return not/notas.size();
    }

    public Integer getNumComentarios() {
        return comentarios.size();
    }



}
