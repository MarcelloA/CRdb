package ufpb.minicurso.crdb.entidade;

import javax.persistence.Column;
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
    private Double nota;

    @Column
    @NotNull
    private Integer favorito;

    @CreatedDate
    private LocalDateTime criadoEm;
}
