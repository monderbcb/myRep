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
import sample.DbTables.StudentPayment;
import sample.DbTables.StudentSub;
import sample.dbControllers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class StudentPaymentJpaController implements Serializable {

    public StudentPaymentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(StudentPayment studentPayment) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StudentSub studentSubjectRowId = studentPayment.getStudentSubjectRowId();
            if (studentSubjectRowId != null) {
                studentSubjectRowId = em.getReference(studentSubjectRowId.getClass(), studentSubjectRowId.getId());
                studentPayment.setStudentSubjectRowId(studentSubjectRowId);
            }
            em.persist(studentPayment);
            if (studentSubjectRowId != null) {
                studentSubjectRowId.getStudentPaymentList().add(studentPayment);
                studentSubjectRowId = em.merge(studentSubjectRowId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(StudentPayment studentPayment) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StudentPayment persistentStudentPayment = em.find(StudentPayment.class, studentPayment.getId());
            StudentSub studentSubjectRowIdOld = persistentStudentPayment.getStudentSubjectRowId();
            StudentSub studentSubjectRowIdNew = studentPayment.getStudentSubjectRowId();
            if (studentSubjectRowIdNew != null) {
                studentSubjectRowIdNew = em.getReference(studentSubjectRowIdNew.getClass(), studentSubjectRowIdNew.getId());
                studentPayment.setStudentSubjectRowId(studentSubjectRowIdNew);
            }
            studentPayment = em.merge(studentPayment);
            if (studentSubjectRowIdOld != null && !studentSubjectRowIdOld.equals(studentSubjectRowIdNew)) {
                studentSubjectRowIdOld.getStudentPaymentList().remove(studentPayment);
                studentSubjectRowIdOld = em.merge(studentSubjectRowIdOld);
            }
            if (studentSubjectRowIdNew != null && !studentSubjectRowIdNew.equals(studentSubjectRowIdOld)) {
                studentSubjectRowIdNew.getStudentPaymentList().add(studentPayment);
                studentSubjectRowIdNew = em.merge(studentSubjectRowIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = studentPayment.getId();
                if (findStudentPayment(id) == null) {
                    throw new NonexistentEntityException("The studentPayment with id " + id + " no longer exists.");
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
            StudentPayment studentPayment;
            try {
                studentPayment = em.getReference(StudentPayment.class, id);
                studentPayment.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The studentPayment with id " + id + " no longer exists.", enfe);
            }
            StudentSub studentSubjectRowId = studentPayment.getStudentSubjectRowId();
            if (studentSubjectRowId != null) {
                studentSubjectRowId.getStudentPaymentList().remove(studentPayment);
                studentSubjectRowId = em.merge(studentSubjectRowId);
            }
            em.remove(studentPayment);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<StudentPayment> findStudentPaymentEntities() {
        return findStudentPaymentEntities(true, -1, -1);
    }

    public List<StudentPayment> findStudentPaymentEntities(int maxResults, int firstResult) {
        return findStudentPaymentEntities(false, maxResults, firstResult);
    }

    private List<StudentPayment> findStudentPaymentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StudentPayment.class));
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

    public StudentPayment findStudentPayment(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StudentPayment.class, id);
        } finally {
            em.close();
        }
    }

    public int getStudentPaymentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StudentPayment> rt = cq.from(StudentPayment.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
