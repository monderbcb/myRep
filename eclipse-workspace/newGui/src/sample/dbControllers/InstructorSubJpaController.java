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
import sample.DbTables.InstructorSub;
import sample.DbTables.Subjects;
import sample.DbTables.Instructors;
import sample.DbTables.Semester;
import sample.dbControllers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class InstructorSubJpaController implements Serializable {

    public InstructorSubJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(InstructorSub instructorSub) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subjects subjectId = instructorSub.getSubjectId();
            if (subjectId != null) {
                subjectId = em.getReference(subjectId.getClass(), subjectId.getId());
                instructorSub.setSubjectId(subjectId);
            }
            Instructors instructorId = instructorSub.getInstructorId();
            if (instructorId != null) {
                instructorId = em.getReference(instructorId.getClass(), instructorId.getId());
                instructorSub.setInstructorId(instructorId);
            }
            Semester semesterId = instructorSub.getSemesterId();
            if (semesterId != null) {
                semesterId = em.getReference(semesterId.getClass(), semesterId.getId());
                instructorSub.setSemesterId(semesterId);
            }
            em.persist(instructorSub);
            if (subjectId != null) {
                subjectId.getInstructorSubList().add(instructorSub);
                subjectId = em.merge(subjectId);
            }
            if (instructorId != null) {
                instructorId.getInstructorSubList().add(instructorSub);
                instructorId = em.merge(instructorId);
            }
            if (semesterId != null) {
                semesterId.getInstructorSubList().add(instructorSub);
                semesterId = em.merge(semesterId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(InstructorSub instructorSub) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InstructorSub persistentInstructorSub = em.find(InstructorSub.class, instructorSub.getId());
            Subjects subjectIdOld = persistentInstructorSub.getSubjectId();
            Subjects subjectIdNew = instructorSub.getSubjectId();
            Instructors instructorIdOld = persistentInstructorSub.getInstructorId();
            Instructors instructorIdNew = instructorSub.getInstructorId();
            Semester semesterIdOld = persistentInstructorSub.getSemesterId();
            Semester semesterIdNew = instructorSub.getSemesterId();
            if (subjectIdNew != null) {
                subjectIdNew = em.getReference(subjectIdNew.getClass(), subjectIdNew.getId());
                instructorSub.setSubjectId(subjectIdNew);
            }
            if (instructorIdNew != null) {
                instructorIdNew = em.getReference(instructorIdNew.getClass(), instructorIdNew.getId());
                instructorSub.setInstructorId(instructorIdNew);
            }
            if (semesterIdNew != null) {
                semesterIdNew = em.getReference(semesterIdNew.getClass(), semesterIdNew.getId());
                instructorSub.setSemesterId(semesterIdNew);
            }
            instructorSub = em.merge(instructorSub);
            if (subjectIdOld != null && !subjectIdOld.equals(subjectIdNew)) {
                subjectIdOld.getInstructorSubList().remove(instructorSub);
                subjectIdOld = em.merge(subjectIdOld);
            }
            if (subjectIdNew != null && !subjectIdNew.equals(subjectIdOld)) {
                subjectIdNew.getInstructorSubList().add(instructorSub);
                subjectIdNew = em.merge(subjectIdNew);
            }
            if (instructorIdOld != null && !instructorIdOld.equals(instructorIdNew)) {
                instructorIdOld.getInstructorSubList().remove(instructorSub);
                instructorIdOld = em.merge(instructorIdOld);
            }
            if (instructorIdNew != null && !instructorIdNew.equals(instructorIdOld)) {
                instructorIdNew.getInstructorSubList().add(instructorSub);
                instructorIdNew = em.merge(instructorIdNew);
            }
            if (semesterIdOld != null && !semesterIdOld.equals(semesterIdNew)) {
                semesterIdOld.getInstructorSubList().remove(instructorSub);
                semesterIdOld = em.merge(semesterIdOld);
            }
            if (semesterIdNew != null && !semesterIdNew.equals(semesterIdOld)) {
                semesterIdNew.getInstructorSubList().add(instructorSub);
                semesterIdNew = em.merge(semesterIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = instructorSub.getId();
                if (findInstructorSub(id) == null) {
                    throw new NonexistentEntityException("The instructorSub with id " + id + " no longer exists.");
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
            InstructorSub instructorSub;
            try {
                instructorSub = em.getReference(InstructorSub.class, id);
                instructorSub.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The instructorSub with id " + id + " no longer exists.", enfe);
            }
            Subjects subjectId = instructorSub.getSubjectId();
            if (subjectId != null) {
                subjectId.getInstructorSubList().remove(instructorSub);
                subjectId = em.merge(subjectId);
            }
            Instructors instructorId = instructorSub.getInstructorId();
            if (instructorId != null) {
                instructorId.getInstructorSubList().remove(instructorSub);
                instructorId = em.merge(instructorId);
            }
            Semester semesterId = instructorSub.getSemesterId();
            if (semesterId != null) {
                semesterId.getInstructorSubList().remove(instructorSub);
                semesterId = em.merge(semesterId);
            }
            em.remove(instructorSub);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<InstructorSub> findInstructorSubEntities() {
        return findInstructorSubEntities(true, -1, -1);
    }

    public List<InstructorSub> findInstructorSubEntities(int maxResults, int firstResult) {
        return findInstructorSubEntities(false, maxResults, firstResult);
    }

    private List<InstructorSub> findInstructorSubEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InstructorSub.class));
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

    public InstructorSub findInstructorSub(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(InstructorSub.class, id);
        } finally {
            em.close();
        }
    }

    public int getInstructorSubCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InstructorSub> rt = cq.from(InstructorSub.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
