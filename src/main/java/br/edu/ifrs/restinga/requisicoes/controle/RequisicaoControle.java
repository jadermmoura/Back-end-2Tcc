package br.edu.ifrs.restinga.requisicoes.controle;

import br.edu.ifrs.restinga.requisicoes.modelo.entidade.Requisicao;
import br.edu.ifrs.restinga.requisicoes.modelo.entidade.RequisicaoAproveitamento;
import br.edu.ifrs.restinga.requisicoes.modelo.entidade.RequisicaoCertificacao;
import br.edu.ifrs.restinga.requisicoes.modelo.servico.RequisicaoServico;
import br.edu.ifrs.restinga.requisicoes.modelo.servico.ServicoCRUD;
import io.swagger.annotations.Api;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Api("Api : Requisições")
@RequestMapping("/api/requisicoes/")
public class RequisicaoControle extends CRUDControle<Requisicao> {

    @Autowired
    RequisicaoServico requisicaoServico;

    @Override
    public ServicoCRUD<Requisicao> getService() {
        return requisicaoServico;
    }

    @GetMapping("aproveitamentos/")
    public ResponseEntity<Iterable<RequisicaoAproveitamento>> listarAproveitamento() {
        return ResponseEntity.ok().body(requisicaoServico.listarAproveitamento());
    }

    @GetMapping("certificacoes/")
    public ResponseEntity<Iterable<RequisicaoCertificacao>> listarCertificao() {
        return ResponseEntity.ok().body(requisicaoServico.listarCertificacao());
    }

    @GetMapping("solicitante/{id}")
    public ResponseEntity<List<Requisicao>> listarCertificaoUsuario(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(requisicaoServico.listarCertificacaoSolicitante(id));
    }

    @GetMapping("professor/{id}")
    public ResponseEntity<List<Requisicao>> listarRequisicoesProfessor(@PathVariable("id") Long id, @RequestParam(value = "tipo") String tipo) {
        return ResponseEntity.ok().body(requisicaoServico.listarPorProfessor(id, tipo));
    }

    @GetMapping("data/{data}")
    public ResponseEntity<List<Requisicao>> listarData(@PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy")  Date data ) {
        return ResponseEntity.ok(requisicaoServico.listarData(data));
    }
    
    @GetMapping("alunos/{id}")
    public ResponseEntity<List<Requisicao>> listarTodas(@PathVariable Long id) {
        return ResponseEntity.ok(requisicaoServico.listarRequisicaoAluno(id));
    }
}
