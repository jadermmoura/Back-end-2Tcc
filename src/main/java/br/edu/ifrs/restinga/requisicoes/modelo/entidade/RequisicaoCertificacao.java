package br.edu.ifrs.restinga.requisicoes.modelo.entidade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="requisicoes_certificacao")
public class RequisicaoCertificacao extends Requisicao implements Serializable,Entidade{
    @Transient
    @JsonProperty("tipo")
    private final String tipo ="certificacao";
    private String formacaoAtividadeAnterior;
    @OneToOne
    private Anexo prova;
    
}
