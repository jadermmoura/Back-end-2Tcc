package br.edu.ifrs.restinga.requisicoes.modelo.servico;

import br.edu.ifrs.restinga.requisicoes.modelo.dao.CursoDao;
import br.edu.ifrs.restinga.requisicoes.modelo.dao.DisciplinaDao;
import br.edu.ifrs.restinga.requisicoes.modelo.dao.PaginacaoRepository;
import br.edu.ifrs.restinga.requisicoes.modelo.dao.RequisicaoDao;
import br.edu.ifrs.restinga.requisicoes.modelo.dao.UsuarioDao;
import br.edu.ifrs.restinga.requisicoes.modelo.entidade.Curso;
import br.edu.ifrs.restinga.requisicoes.modelo.entidade.Disciplina;
import br.edu.ifrs.restinga.requisicoes.modelo.entidade.Requisicao;
import br.edu.ifrs.restinga.requisicoes.modelo.exception.MensagemErroGenericaException;
import br.edu.ifrs.restinga.requisicoes.modelo.rn.CursoRN;
import br.edu.ifrs.restinga.requisicoes.modelo.rn.DisciplinaRN;
import br.edu.ifrs.restinga.requisicoes.modelo.rn.RegraNenocio;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CursoServico extends ServicoCRUD<Curso> {

    @Autowired
    private CursoDao dao;
    @Autowired
    private CursoRN rn;
    @Autowired
    private DisciplinaRN rnDisciplina;
    @Autowired
    private DisciplinaServico disciplinaServico;

    @Autowired
    private DisciplinaDao disciplinaDao;

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private RequisicaoDao requisicaoDao;

    @Override
    public PaginacaoRepository<Curso, Long> getDAO() {
        return dao;
    }

    @Override
    public RegraNenocio<Curso> rn() {
        return rn;
    }

    public List<Disciplina> cadastrarDisciplinaNoCurso(Long id, Disciplina d) {
        rnDisciplina.validar(d);
        Curso curso = super.recuperar(id);
        curso.getDisciplinas().add(d);
        Curso cursoRetorno = dao.save(curso);
        return cursoRetorno.getDisciplinas();
    }

    @Override
    public Curso atualizar(Curso entidade) {
        List<Disciplina> listarDisciplinas = this.listarDisciplinas(entidade.getId());
        entidade.setDisciplinas(listarDisciplinas);
        return super.atualizar(entidade);
    }

    public Curso atualizarDisciplina(Long id, Disciplina entidade) {
        rnDisciplina.validar(entidade);
        Curso curso = super.recuperar(id);
        List<Disciplina> disciplinas = curso.getDisciplinas();
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getId() == entidade.getId()) {
                disciplina.setId(disciplina.getId());
                disciplina.setNome(entidade.getNome());
                disciplina.setCargaHoraria(entidade.getCargaHoraria());
            }
        }
        return dao.save(curso);

    }

    public List<Disciplina> listarDisciplinas(Long id) {
        Curso curso = super.recuperar(id);
        return curso.getDisciplinas();
    }

    public Disciplina listarDisciplinasPeloID(Long id, Long idDisciplina) {
        Curso curso = super.recuperar(id);
        List<Disciplina> disciplinas = curso.getDisciplinas();
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getId() == idDisciplina) {

                return disciplina;
            }
        }
        return null;
    }

    public void deletarDisciplina(Long id, Long idDisciplina) {
        Curso curso = super.recuperar(id);
        List<Disciplina> disciplinas = (List<Disciplina>) disciplinaDao.findAll();
        List<Requisicao> requisicao = (List<Requisicao>) requisicaoDao.findAll();
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getId() == idDisciplina) {
                curso.getDisciplinas().remove(disciplina);
            }
        }
        dao.save(curso);
    }

    public List<Disciplina> pesquisarDisciplinaNomeCurso(String nome) {
        List<Disciplina> cursoDisciplina = dao.findByNome(nome).getDisciplinas();
        return cursoDisciplina;
    }

    public Curso listaCursoNome(String nome) {
        return dao.findByNome(nome);
    }

    public Page<Disciplina> listarPaginacao(Long id, Pageable p) {
        return dao.findAll(id, p);
    }

    public String listarCuroPeloIdDisciplina(Long id) {
        List<Curso> cursos = dao.findAllCurso();
        
        for (Curso curso : cursos) {
            if (curso.getDisciplinas() != null) {
                List<Disciplina> disciplinas = curso.getDisciplinas();
                for (Disciplina disciplina : disciplinas) {
                    if(disciplina.getId() == id){
                        return curso.getNome();
                    }
                }
            }
        }
        return "Curso não encontrado";
    }

    public ArrayList<Curso> listarPeloCoordenador(Long id) {
        Iterable<Curso> cursos = dao.findAllCurso();
        ArrayList<Curso> mostraNomeCurso = new ArrayList<>();

        for (Curso curso : cursos) {
            if (curso.getUsuario() != null) {
                if (curso.getUsuario().getId() == id) {
                    mostraNomeCurso.add(curso);
                }
            }
        }
        return mostraNomeCurso;
    }

    public void deletarCoordenador(Long id) {
        System.out.println(id);
        Iterable<Curso> cursos = dao.findAllCurso();
        for (Curso curso : cursos) {
            if (curso.getUsuario().getId() == id) {
                dao.deleteById(curso.getUsuario().getId());
            }
        }
    }

    @Override
    public List<Curso> listar() {
        return dao.findAllCurso();
    }

}
