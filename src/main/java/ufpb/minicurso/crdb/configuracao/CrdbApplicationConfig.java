package ufpb.minicurso.crdb.configuracao;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "crdb")
@Component
public class CrdbApplicationConfig {

    private String segredo;
    private Long validadeTokenEmSegundos;

    public String getSegredo() {
        return segredo;
    }

    public void setSegredo(String segredo) {
        this.segredo = segredo;
    }

    public Long getValidadeTokenEmSegundos() {
        return validadeTokenEmSegundos;
    }

    public void setValidadeTokenEmSegundos(Long validadeTokenEmSegundos) {
        this.validadeTokenEmSegundos = validadeTokenEmSegundos;
    }
}
