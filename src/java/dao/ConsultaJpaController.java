/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Consulta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Medico;
import entidades.Paciente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author CPD
 */
public class ConsultaJpaController implements Serializable {

    public ConsultaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Consulta consulta) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Medico idMedico = consulta.getIdMedico();
            if (idMedico != null) {
                idMedico = em.getReference(idMedico.getClass(), idMedico.getIdMedico());
                consulta.setIdMedico(idMedico);
            }
            Paciente idpaciente = consulta.getIdpaciente();
            if (idpaciente != null) {
                idpaciente = em.getReference(idpaciente.getClass(), idpaciente.getIdpaciente());
                consulta.setIdpaciente(idpaciente);
            }
            em.persist(consulta);
            if (idMedico != null) {
                idMedico.getConsultaCollection().add(consulta);
                idMedico = em.merge(idMedico);
            }
            if (idpaciente != null) {
                idpaciente.getConsultaCollection().add(consulta);
                idpaciente = em.merge(idpaciente);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Consulta consulta) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Consulta persistentConsulta = em.find(Consulta.class, consulta.getIdconsulta());
            Medico idMedicoOld = persistentConsulta.getIdMedico();
            Medico idMedicoNew = consulta.getIdMedico();
            Paciente idpacienteOld = persistentConsulta.getIdpaciente();
            Paciente idpacienteNew = consulta.getIdpaciente();
            if (idMedicoNew != null) {
                idMedicoNew = em.getReference(idMedicoNew.getClass(), idMedicoNew.getIdMedico());
                consulta.setIdMedico(idMedicoNew);
            }
            if (idpacienteNew != null) {
                idpacienteNew = em.getReference(idpacienteNew.getClass(), idpacienteNew.getIdpaciente());
                consulta.setIdpaciente(idpacienteNew);
            }
            consulta = em.merge(consulta);
            if (idMedicoOld != null && !idMedicoOld.equals(idMedicoNew)) {
                idMedicoOld.getConsultaCollection().remove(consulta);
                idMedicoOld = em.merge(idMedicoOld);
            }
            if (idMedicoNew != null && !idMedicoNew.equals(idMedicoOld)) {
                idMedicoNew.getConsultaCollection().add(consulta);
                idMedicoNew = em.merge(idMedicoNew);
            }
            if (idpacienteOld != null && !idpacienteOld.equals(idpacienteNew)) {
                idpacienteOld.getConsultaCollection().remove(consulta);
                idpacienteOld = em.merge(idpacienteOld);
            }
            if (idpacienteNew != null && !idpacienteNew.equals(idpacienteOld)) {
                idpacienteNew.getConsultaCollection().add(consulta);
                idpacienteNew = em.merge(idpacienteNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = consulta.getIdconsulta();
                if (findConsulta(id) == null) {
                    throw new NonexistentEntityException("The consulta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Consulta consulta;
            try {
                consulta = em.getReference(Consulta.class, id);
                consulta.getIdconsulta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consulta with id " + id + " no longer exists.", enfe);
            }
            Medico idMedico = consulta.getIdMedico();
            if (idMedico != null) {
                idMedico.getConsultaCollection().remove(consulta);
                idMedico = em.merge(idMedico);
            }
            Paciente idpaciente = consulta.getIdpaciente();
            if (idpaciente != null) {
                idpaciente.getConsultaCollection().remove(consulta);
                idpaciente = em.merge(idpaciente);
            }
            em.remove(consulta);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Consulta> findConsultaEntities() {
        return findConsultaEntities(true, -1, -1);
    }

    public List<Consulta> findConsultaEntities(int maxResults, int firstResult) {
        return findConsultaEntities(false, maxResults, firstResult);
    }

    private List<Consulta> findConsultaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Consulta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Consulta findConsulta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Consulta.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsultaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Consulta> rt = cq.from(Consulta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
