/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Agenda;
import java.util.ArrayList;
import java.util.Collection;
import entidades.Consulta;
import entidades.Paciente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author CPD
 */
public class PacienteJpaController implements Serializable {

    public PacienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Paciente paciente) throws RollbackFailureException, Exception {
        if (paciente.getAgendaCollection() == null) {
            paciente.setAgendaCollection(new ArrayList<Agenda>());
        }
        if (paciente.getConsultaCollection() == null) {
            paciente.setConsultaCollection(new ArrayList<Consulta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Agenda> attachedAgendaCollection = new ArrayList<Agenda>();
            for (Agenda agendaCollectionAgendaToAttach : paciente.getAgendaCollection()) {
                agendaCollectionAgendaToAttach = em.getReference(agendaCollectionAgendaToAttach.getClass(), agendaCollectionAgendaToAttach.getIdagenda());
                attachedAgendaCollection.add(agendaCollectionAgendaToAttach);
            }
            paciente.setAgendaCollection(attachedAgendaCollection);
            Collection<Consulta> attachedConsultaCollection = new ArrayList<Consulta>();
            for (Consulta consultaCollectionConsultaToAttach : paciente.getConsultaCollection()) {
                consultaCollectionConsultaToAttach = em.getReference(consultaCollectionConsultaToAttach.getClass(), consultaCollectionConsultaToAttach.getIdconsulta());
                attachedConsultaCollection.add(consultaCollectionConsultaToAttach);
            }
            paciente.setConsultaCollection(attachedConsultaCollection);
            em.persist(paciente);
            for (Agenda agendaCollectionAgenda : paciente.getAgendaCollection()) {
                Paciente oldIdpacienteOfAgendaCollectionAgenda = agendaCollectionAgenda.getIdpaciente();
                agendaCollectionAgenda.setIdpaciente(paciente);
                agendaCollectionAgenda = em.merge(agendaCollectionAgenda);
                if (oldIdpacienteOfAgendaCollectionAgenda != null) {
                    oldIdpacienteOfAgendaCollectionAgenda.getAgendaCollection().remove(agendaCollectionAgenda);
                    oldIdpacienteOfAgendaCollectionAgenda = em.merge(oldIdpacienteOfAgendaCollectionAgenda);
                }
            }
            for (Consulta consultaCollectionConsulta : paciente.getConsultaCollection()) {
                Paciente oldIdpacienteOfConsultaCollectionConsulta = consultaCollectionConsulta.getIdpaciente();
                consultaCollectionConsulta.setIdpaciente(paciente);
                consultaCollectionConsulta = em.merge(consultaCollectionConsulta);
                if (oldIdpacienteOfConsultaCollectionConsulta != null) {
                    oldIdpacienteOfConsultaCollectionConsulta.getConsultaCollection().remove(consultaCollectionConsulta);
                    oldIdpacienteOfConsultaCollectionConsulta = em.merge(oldIdpacienteOfConsultaCollectionConsulta);
                }
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

    public void edit(Paciente paciente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Paciente persistentPaciente = em.find(Paciente.class, paciente.getIdpaciente());
            Collection<Agenda> agendaCollectionOld = persistentPaciente.getAgendaCollection();
            Collection<Agenda> agendaCollectionNew = paciente.getAgendaCollection();
            Collection<Consulta> consultaCollectionOld = persistentPaciente.getConsultaCollection();
            Collection<Consulta> consultaCollectionNew = paciente.getConsultaCollection();
            List<String> illegalOrphanMessages = null;
            for (Agenda agendaCollectionOldAgenda : agendaCollectionOld) {
                if (!agendaCollectionNew.contains(agendaCollectionOldAgenda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Agenda " + agendaCollectionOldAgenda + " since its idpaciente field is not nullable.");
                }
            }
            for (Consulta consultaCollectionOldConsulta : consultaCollectionOld) {
                if (!consultaCollectionNew.contains(consultaCollectionOldConsulta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Consulta " + consultaCollectionOldConsulta + " since its idpaciente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Agenda> attachedAgendaCollectionNew = new ArrayList<Agenda>();
            for (Agenda agendaCollectionNewAgendaToAttach : agendaCollectionNew) {
                agendaCollectionNewAgendaToAttach = em.getReference(agendaCollectionNewAgendaToAttach.getClass(), agendaCollectionNewAgendaToAttach.getIdagenda());
                attachedAgendaCollectionNew.add(agendaCollectionNewAgendaToAttach);
            }
            agendaCollectionNew = attachedAgendaCollectionNew;
            paciente.setAgendaCollection(agendaCollectionNew);
            Collection<Consulta> attachedConsultaCollectionNew = new ArrayList<Consulta>();
            for (Consulta consultaCollectionNewConsultaToAttach : consultaCollectionNew) {
                consultaCollectionNewConsultaToAttach = em.getReference(consultaCollectionNewConsultaToAttach.getClass(), consultaCollectionNewConsultaToAttach.getIdconsulta());
                attachedConsultaCollectionNew.add(consultaCollectionNewConsultaToAttach);
            }
            consultaCollectionNew = attachedConsultaCollectionNew;
            paciente.setConsultaCollection(consultaCollectionNew);
            paciente = em.merge(paciente);
            for (Agenda agendaCollectionNewAgenda : agendaCollectionNew) {
                if (!agendaCollectionOld.contains(agendaCollectionNewAgenda)) {
                    Paciente oldIdpacienteOfAgendaCollectionNewAgenda = agendaCollectionNewAgenda.getIdpaciente();
                    agendaCollectionNewAgenda.setIdpaciente(paciente);
                    agendaCollectionNewAgenda = em.merge(agendaCollectionNewAgenda);
                    if (oldIdpacienteOfAgendaCollectionNewAgenda != null && !oldIdpacienteOfAgendaCollectionNewAgenda.equals(paciente)) {
                        oldIdpacienteOfAgendaCollectionNewAgenda.getAgendaCollection().remove(agendaCollectionNewAgenda);
                        oldIdpacienteOfAgendaCollectionNewAgenda = em.merge(oldIdpacienteOfAgendaCollectionNewAgenda);
                    }
                }
            }
            for (Consulta consultaCollectionNewConsulta : consultaCollectionNew) {
                if (!consultaCollectionOld.contains(consultaCollectionNewConsulta)) {
                    Paciente oldIdpacienteOfConsultaCollectionNewConsulta = consultaCollectionNewConsulta.getIdpaciente();
                    consultaCollectionNewConsulta.setIdpaciente(paciente);
                    consultaCollectionNewConsulta = em.merge(consultaCollectionNewConsulta);
                    if (oldIdpacienteOfConsultaCollectionNewConsulta != null && !oldIdpacienteOfConsultaCollectionNewConsulta.equals(paciente)) {
                        oldIdpacienteOfConsultaCollectionNewConsulta.getConsultaCollection().remove(consultaCollectionNewConsulta);
                        oldIdpacienteOfConsultaCollectionNewConsulta = em.merge(oldIdpacienteOfConsultaCollectionNewConsulta);
                    }
                }
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
                Integer id = paciente.getIdpaciente();
                if (findPaciente(id) == null) {
                    throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Paciente paciente;
            try {
                paciente = em.getReference(Paciente.class, id);
                paciente.getIdpaciente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Agenda> agendaCollectionOrphanCheck = paciente.getAgendaCollection();
            for (Agenda agendaCollectionOrphanCheckAgenda : agendaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Agenda " + agendaCollectionOrphanCheckAgenda + " in its agendaCollection field has a non-nullable idpaciente field.");
            }
            Collection<Consulta> consultaCollectionOrphanCheck = paciente.getConsultaCollection();
            for (Consulta consultaCollectionOrphanCheckConsulta : consultaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Consulta " + consultaCollectionOrphanCheckConsulta + " in its consultaCollection field has a non-nullable idpaciente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(paciente);
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

    public List<Paciente> findPacienteEntities() {
        return findPacienteEntities(true, -1, -1);
    }

    public List<Paciente> findPacienteEntities(int maxResults, int firstResult) {
        return findPacienteEntities(false, maxResults, firstResult);
    }

    private List<Paciente> findPacienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Paciente.class));
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

    public Paciente findPaciente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Paciente.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Paciente> rt = cq.from(Paciente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
