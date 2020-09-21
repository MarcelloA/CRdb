package ufpb.minicurso.crdb.entidade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collections;
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

    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    List<Funcao> funcoes;

    @JsonIgnore
    @OneToMany(mappedBy = "avaliacaoId.usuario")
    private List<Avaliacao> avaliacoes;

    public Usuario(String email, String primeiroNome, String ultimoNome, String senha){
        this.email = email;
        this.primeiroNome = primeiroNome;
        this.ultimoNome = ultimoNome;
        this.senha = senha;
        this.funcoes = Collections.singletonList(Funcao.ROLE_USUARIO);
        this.avaliacoes = Collections.emptyList();
    }

    public Usuario(String email, String primeiroNome, String ultimoNome, String senha, List<Funcao> funcoes) {
    this.email = email;
    this.primeiroNome = primeiroNome;
    this.ultimoNome = ultimoNome;
    this.senha = senha;
    this.funcoes = funcoes;
    this.avaliacoes = Collections.emptyList();
    }
}
