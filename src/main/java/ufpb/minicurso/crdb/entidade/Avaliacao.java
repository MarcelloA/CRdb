package ufpb.minicurso.crdb.entidade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "avaliacao")
public class Avaliacao {

    @EmbeddedId
    private AvaliacaoId avaliacaoId;

    @OneToOne(cascade = CascadeType.ALL)
    private Comentario comentario;

    @Column
    @NotNull
    @Max(10)
    @Min(0)
    private Double nota;

    @Column
    private Boolean favorito;

    @CreatedDate
    private LocalDateTime criadoEm;
}
