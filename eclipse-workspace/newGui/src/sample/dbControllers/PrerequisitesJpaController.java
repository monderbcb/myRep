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
import sample.DbTables.Prerequisites;
import sample.DbTables.Subjects;
import sample.dbControllers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class PrerequisitesJpaController implements Serializable {

    public PrerequisitesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Prerequisites prerequisites) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subjects subjectId = prerequisites.getSubjectId();
            if (subjectId != null) {
                subjectId = em.getReference(subjectId.getClass(), subjectId.getId());
                prerequisites.setSubjectId(subjectId);
            }
            Subjects prerequisite = prerequisites.getPrerequisite();
            if (prerequisite != null) {
                prerequisite = em.getReference(prerequisite.getClass(), prerequisite.getId());
                prerequisites.setPrerequisite(prerequisite);
            }
            em.persist(prerequisites);
            if (subjectId != null) {
                subjectId.getPrerequisitesList().add(prerequisites);
                subjectId = em.merge(subjectId);
            }
            if (prerequisite != null) {
                prerequisite.getPrerequisitesList().add(prerequisites);
                prerequisite = em.merge(prerequisite);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Prerequisites prerequisites) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Prerequisites persistentPrerequisites = em.find(Prerequisites.class, prerequisites.getId());
            Subjects subjectIdOld = persistentPrerequisites.getSubjectId();
            Subjects subjectIdNew = prerequisites.getSubjectId();
            Subjects prerequisiteOld = persistentPrerequisites.getPrerequisite();
            Subjects prerequisiteNew = prerequisites.getPrerequisite();
            if (subjectIdNew != null) {
                subjectIdNew = em.getReference(subjectIdNew.getClass(), subjectIdNew.getId());
                prerequisites.setSubjectId(subjectIdNew);
            }
            if (prerequisiteNew != null) {
                prerequisiteNew = em.getReference(prerequisiteNew.getClass(), prerequisiteNew.getId());
                prerequisites.setPrerequisite(prerequisiteNew);
            }
            prerequisites = em.merge(prerequisites);
            if (subjectIdOld != null && !subjectIdOld.equals(subjectIdNew)) {
                subjectIdOld.getPrerequisitesList().remove(prerequisites);
                subjectIdOld = em.merge(subjectIdOld);
            }
            if (subjectIdNew != null && !subjectIdNew.equals(subjectIdOld)) {
                subjectIdNew.getPrerequisitesList().add(prerequisites);
                subjectIdNew = em.merge(subjectIdNew);
            }
            if (prerequisiteOld != null && !prerequisiteOld.equals(prerequisiteNew)) {
                prerequisiteOld.getPrerequisitesList().remove(prerequisites);
                prerequisiteOld = em.merge(prerequisiteOld);
            }
            if (prerequisiteNew != null && !prerequisiteNew.equals(prerequisiteOld)) {
                prerequisiteNew.getPrerequisitesList().add(prerequisites);
                prerequisiteNew = em.merge(prerequisiteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = prerequisites.getId();
                if (findPrerequisites(id) == null) {
                    throw new NonexistentEntityException("The prerequisites with id " + id + " no longer exists.");
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
            Prerequisites prerequisites;
            try {
                prerequisites = em.getReference(Prerequisites.class, id);
                prerequisites.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prerequisites with id " + id + " no longer exists.", enfe);
            }
            Subjects subjectId = prerequisites.getSubjectId();
            if (subjectId != null) {
                subjectId.getPrerequisitesList().remove(prerequisites);
                subjectId = em.merge(subjectId);
            }
            Subjects prerequisite = prerequisites.getPrerequisite();
            if (prerequisite != null) {
                prerequisite.getPrerequisitesList().remove(prerequisites);
                prerequisite = em.merge(prerequisite);
            }
            em.remove(prerequisites);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Prerequisites> findPrerequisitesEntities() {
        return findPrerequisitesEntities(true, -1, -1);
    }

    public List<Prerequisites> findPrerequisitesEntities(int maxResults, int firstResult) {
        return findPrerequisitesEntities(false, maxResults, firstResult);
    }

    private List<Prerequisites> findPrerequisitesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prerequisites.class));
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

    public Prerequisites findPrerequisites(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Prerequisites.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrerequisitesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prerequisites> rt = cq.from(Prerequisites.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
