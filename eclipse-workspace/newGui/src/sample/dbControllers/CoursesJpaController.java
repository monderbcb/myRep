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
import sample.DbTables.Courses;
import sample.dbControllers.exceptions.NonexistentEntityException;
import sample.dbControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author monder
 */
public class CoursesJpaController implements Serializable {

    public CoursesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Courses courses) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(courses);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCourses(courses.getCourseId()) != null) {
                throw new PreexistingEntityException("Courses " + courses + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Courses courses) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            courses = em.merge(courses);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = courses.getCourseId();
                if (findCourses(id) == null) {
                    throw new NonexistentEntityException("The courses with id " + id + " no longer exists.");
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
            Courses courses;
            try {
                courses = em.getReference(Courses.class, id);
                courses.getCourseId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The courses with id " + id + " no longer exists.", enfe);
            }
            em.remove(courses);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Courses> findCoursesEntities() {
        return findCoursesEntities(true, -1, -1);
    }

    public List<Courses> findCoursesEntities(int maxResults, int firstResult) {
        return findCoursesEntities(false, maxResults, firstResult);
    }

    private List<Courses> findCoursesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Courses.class));
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

    public Courses findCourses(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Courses.class, id);
        } finally {
            em.close();
        }
    }

    public int getCoursesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Courses> rt = cq.from(Courses.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
