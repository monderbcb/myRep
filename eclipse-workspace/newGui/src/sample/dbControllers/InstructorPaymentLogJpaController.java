/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.dbControllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sample.DbTables.InstructorPaymentLog;
import sample.DbTables.Instructors;
import sample.dbControllers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class InstructorPaymentLogJpaController implements Serializable {

    public InstructorPaymentLogJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(InstructorPaymentLog instructorPaymentLog) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Instructors id2 = instructorPaymentLog.getId2();
            if (id2 != null) {
                id2 = em.getReference(id2.getClass(), id2.getId());
                instructorPaymentLog.setId2(id2);
            }
            em.persist(instructorPaymentLog);
            if (id2 != null) {
                id2.getInstructorPaymentLogList().add(instructorPaymentLog);
                id2 = em.merge(id2);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(InstructorPaymentLog instructorPaymentLog) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InstructorPaymentLog persistentInstructorPaymentLog = em.find(InstructorPaymentLog.class, instructorPaymentLog.getId());
            Instructors id2Old = persistentInstructorPaymentLog.getId2();
            Instructors id2New = instructorPaymentLog.getId2();
            if (id2New != null) {
                id2New = em.getReference(id2New.getClass(), id2New.getId());
                instructorPaymentLog.setId2(id2New);
            }
            instructorPaymentLog = em.merge(instructorPaymentLog);
            if (id2Old != null && !id2Old.equals(id2New)) {
                id2Old.getInstructorPaymentLogList().remove(instructorPaymentLog);
                id2Old = em.merge(id2Old);
            }
            if (id2New != null && !id2New.equals(id2Old)) {
                id2New.getInstructorPaymentLogList().add(instructorPaymentLog);
                id2New = em.merge(id2New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = instructorPaymentLog.getId();
                if (findInstructorPaymentLog(id) == null) {
                    throw new NonexistentEntityException("The instructorPaymentLog with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InstructorPaymentLog instructorPaymentLog;
            try {
                instructorPaymentLog = em.getReference(InstructorPaymentLog.class, id);
                instructorPaymentLog.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The instructorPaymentLog with id " + id + " no longer exists.", enfe);
            }
            Instructors id2 = instructorPaymentLog.getId2();
            if (id2 != null) {
                id2.getInstructorPaymentLogList().remove(instructorPaymentLog);
                id2 = em.merge(id2);
            }
            em.remove(instructorPaymentLog);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<InstructorPaymentLog> findInstructorPaymentLogEntities() {
        return findInstructorPaymentLogEntities(true, -1, -1);
    }

    public List<InstructorPaymentLog> findInstructorPaymentLogEntities(int maxResults, int firstResult) {
        return findInstructorPaymentLogEntities(false, maxResults, firstResult);
    }

    private List<InstructorPaymentLog> findInstructorPaymentLogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InstructorPaymentLog.class));
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

    public InstructorPaymentLog findInstructorPaymentLog(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(InstructorPaymentLog.class, id);
        } finally {
            em.close();
        }
    }

    public int getInstructorPaymentLogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InstructorPaymentLog> rt = cq.from(InstructorPaymentLog.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
