/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.dbControllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sample.DbTables.Instructors;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sample.DbTables.InstructorPaymentInfo;
import sample.dbControllers.exceptions.IllegalOrphanException;
import sample.dbControllers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class InstructorPaymentInfoJpaController implements Serializable {

    public InstructorPaymentInfoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(InstructorPaymentInfo instructorPaymentInfo) throws IllegalOrphanException {
        List<String> illegalOrphanMessages = null;
        Instructors instructorsOrphanCheck = instructorPaymentInfo.getInstructors();
        if (instructorsOrphanCheck != null) {
            InstructorPaymentInfo oldInstructorPaymentInfoOfInstructors = instructorsOrphanCheck.getInstructorPaymentInfo();
            if (oldInstructorPaymentInfoOfInstructors != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Instructors " + instructorsOrphanCheck + " already has an item of type InstructorPaymentInfo whose instructors column cannot be null. Please make another selection for the instructors field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Instructors instructors = instructorPaymentInfo.getInstructors();
            if (instructors != null) {
                instructors = em.getReference(instructors.getClass(), instructors.getId());
                instructorPaymentInfo.setInstructors(instructors);
            }
            em.persist(instructorPaymentInfo);
            if (instructors != null) {
                instructors.setInstructorPaymentInfo(instructorPaymentInfo);
                instructors = em.merge(instructors);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(InstructorPaymentInfo instructorPaymentInfo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InstructorPaymentInfo persistentInstructorPaymentInfo = em.find(InstructorPaymentInfo.class, instructorPaymentInfo.getId());
            Instructors instructorsOld = persistentInstructorPaymentInfo.getInstructors();
            Instructors instructorsNew = instructorPaymentInfo.getInstructors();
            List<String> illegalOrphanMessages = null;
            if (instructorsNew != null && !instructorsNew.equals(instructorsOld)) {
                InstructorPaymentInfo oldInstructorPaymentInfoOfInstructors = instructorsNew.getInstructorPaymentInfo();
                if (oldInstructorPaymentInfoOfInstructors != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Instructors " + instructorsNew + " already has an item of type InstructorPaymentInfo whose instructors column cannot be null. Please make another selection for the instructors field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (instructorsNew != null) {
                instructorsNew = em.getReference(instructorsNew.getClass(), instructorsNew.getId());
                instructorPaymentInfo.setInstructors(instructorsNew);
            }
            instructorPaymentInfo = em.merge(instructorPaymentInfo);
            if (instructorsOld != null && !instructorsOld.equals(instructorsNew)) {
                instructorsOld.setInstructorPaymentInfo(null);
                instructorsOld = em.merge(instructorsOld);
            }
            if (instructorsNew != null && !instructorsNew.equals(instructorsOld)) {
                instructorsNew.setInstructorPaymentInfo(instructorPaymentInfo);
                instructorsNew = em.merge(instructorsNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = instructorPaymentInfo.getId();
                if (findInstructorPaymentInfo(id) == null) {
                    throw new NonexistentEntityException("The instructorPaymentInfo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InstructorPaymentInfo instructorPaymentInfo;
            try {
                instructorPaymentInfo = em.getReference(InstructorPaymentInfo.class, id);
                instructorPaymentInfo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The instructorPaymentInfo with id " + id + " no longer exists.", enfe);
            }
            Instructors instructors = instructorPaymentInfo.getInstructors();
            if (instructors != null) {
                instructors.setInstructorPaymentInfo(null);
                instructors = em.merge(instructors);
            }
            em.remove(instructorPaymentInfo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<InstructorPaymentInfo> findInstructorPaymentInfoEntities() {
        return findInstructorPaymentInfoEntities(true, -1, -1);
    }

    public List<InstructorPaymentInfo> findInstructorPaymentInfoEntities(int maxResults, int firstResult) {
        return findInstructorPaymentInfoEntities(false, maxResults, firstResult);
    }

    private List<InstructorPaymentInfo> findInstructorPaymentInfoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InstructorPaymentInfo.class));
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

    public InstructorPaymentInfo findInstructorPaymentInfo(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(InstructorPaymentInfo.class, id);
        } finally {
            em.close();
        }
    }

    public int getInstructorPaymentInfoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InstructorPaymentInfo> rt = cq.from(InstructorPaymentInfo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
