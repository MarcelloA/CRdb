package ufpb.minicurso.crdb.entidade;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Avaliacao {

    @EmbeddedId
    private AvaliacaoId avaliacaoId;

    @Column
    private String comentario;

    @Column
    @NotNull
    @Max(10)
    @Min(0)
    private Double nota;

    @Column
    @Max(1)
    @Min(0)
    private Integer favorito;

    @CreatedDate
    private LocalDateTime criadoEm;

    public Avaliacao(AvaliacaoId avaliacaoId, String comentario, LocalDateTime criadoEm) {
        this.avaliacaoId = avaliacaoId;
        this.comentario = comentario;
        this.criadoEm = criadoEm;
    }

    public Avaliacao(AvaliacaoId avaliacaoId, Double nota, LocalDateTime criadoEm) {
        this.avaliacaoId = avaliacaoId;
        this.nota = nota;
        this.criadoEm = criadoEm;
    }

}
