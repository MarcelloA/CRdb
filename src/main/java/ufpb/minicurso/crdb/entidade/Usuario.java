package ufpb.minicurso.crdb.entidade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Usuario {
    @Id
    @Email
    private String email;

    @Column
    @NotNull
    private String primeiroNome;

    @Column
    private String ultimoNome;

    @Column
    @NotNull
    private String senha;

    @JsonIgnore
    @OneToMany(mappedBy = "avaliacaoId.usuario")
    private List<Avaliacao> avaliacoes;

}
