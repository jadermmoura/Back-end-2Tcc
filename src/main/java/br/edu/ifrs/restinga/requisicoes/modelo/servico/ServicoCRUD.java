package br.edu.ifrs.restinga.requisicoes.modelo.servico;

import br.edu.ifrs.restinga.requisicoes.modelo.dao.PaginacaoRepository;
import br.edu.ifrs.restinga.requisicoes.modelo.entidade.Entidade;
import br.edu.ifrs.restinga.requisicoes.modelo.exception.NaoEncontradoException;
import br.edu.ifrs.restinga.requisicoes.modelo.rn.RegraNenocio;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class ServicoCRUD<T extends Entidade> {

    public abstract PaginacaoRepository<T, Long> getDAO();

    public abstract RegraNenocio<T> rn();

    @Transactional
    public T cadastrar(T entidade) {
        entidade.setId(Long.valueOf(0));
        rn().validar(entidade);
        return getDAO().save(entidade);
    }

    public Iterable<T> listar() {
        return getDAO().findAll();
    } 
   
    public T atualizar(T entidade) {
        rn().validar(entidade);
        return getDAO().save(entidade);
    }
       
    public T recuperar(Long id) {
        T entidade = getDAO().findById(id).orElseThrow(() -> new NaoEncontradoException());
        return entidade;
    }

    public void excluir(Long id) {
        getDAO().deleteById(id);
    }

    public Page<T> listarPaginacao(Pageable p) {
        return getDAO().findAll(p);
    }

}
