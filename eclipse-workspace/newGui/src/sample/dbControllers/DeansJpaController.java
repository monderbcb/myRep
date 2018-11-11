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
import sample.DbTables.Deans;
import sample.DbTables.Instructors;
import sample.DbTables.Department;
import sample.dbControllers.exceptions.NonexistentEntityException;
import sample.dbControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author monder
 */
public class DeansJpaController implements Serializable {

    public DeansJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Deans deans) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Instructors instructorId = deans.getInstructorId();
            if (instructorId != null) {
                instructorId = em.getReference(instructorId.getClass(), instructorId.getId());
                deans.setInstructorId(instructorId);
            }
            Department major = deans.getMajor();
            if (major != null) {
                major = em.getReference(major.getClass(), major.getId());
                deans.setMajor(major);
            }
            em.persist(deans);
            if (instructorId != null) {
                instructorId.getDeansList().add(deans);
                instructorId = em.merge(instructorId);
            }
            if (major != null) {
                major.getDeansList().add(deans);
                major = em.merge(major);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDeans(deans.getId()) != null) {
                throw new PreexistingEntityException("Deans " + deans + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Deans deans) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Deans persistentDeans = em.find(Deans.class, deans.getId());
            Instructors instructorIdOld = persistentDeans.getInstructorId();
            Instructors instructorIdNew = deans.getInstructorId();
            Department majorOld = persistentDeans.getMajor();
            Department majorNew = deans.getMajor();
            if (instructorIdNew != null) {
                instructorIdNew = em.getReference(instructorIdNew.getClass(), instructorIdNew.getId());
                deans.setInstructorId(instructorIdNew);
            }
            if (majorNew != null) {
                majorNew = em.getReference(majorNew.getClass(), majorNew.getId());
                deans.setMajor(majorNew);
            }
            deans = em.merge(deans);
            if (instructorIdOld != null && !instructorIdOld.equals(instructorIdNew)) {
                instructorIdOld.getDeansList().remove(deans);
                instructorIdOld = em.merge(instructorIdOld);
            }
            if (instructorIdNew != null && !instructorIdNew.equals(instructorIdOld)) {
                instructorIdNew.getDeansList().add(deans);
                instructorIdNew = em.merge(instructorIdNew);
            }
            if (majorOld != null && !majorOld.equals(majorNew)) {
                majorOld.getDeansList().remove(deans);
                majorOld = em.merge(majorOld);
            }
            if (majorNew != null && !majorNew.equals(majorOld)) {
                majorNew.getDeansList().add(deans);
                majorNew = em.merge(majorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = deans.getId();
                if (findDeans(id) == null) {
                    throw new NonexistentEntityException("The deans with id " + id + " no longer exists.");
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
            Deans deans;
            try {
                deans = em.getReference(Deans.class, id);
                deans.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The deans with id " + id + " no longer exists.", enfe);
            }
            Instructors instructorId = deans.getInstructorId();
            if (instructorId != null) {
                instructorId.getDeansList().remove(deans);
                instructorId = em.merge(instructorId);
            }
            Department major = deans.getMajor();
            if (major != null) {
                major.getDeansList().remove(deans);
                major = em.merge(major);
            }
            em.remove(deans);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Deans> findDeansEntities() {
        return findDeansEntities(true, -1, -1);
    }

    public List<Deans> findDeansEntities(int maxResults, int firstResult) {
        return findDeansEntities(false, maxResults, firstResult);
    }

    private List<Deans> findDeansEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Deans.class));
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

    public Deans findDeans(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Deans.class, id);
        } finally {
            em.close();
        }
    }

    public int getDeansCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Deans> rt = cq.from(Deans.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
