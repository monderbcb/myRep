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
import sample.DbTables.Semester;
import sample.DbTables.StudentSub;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sample.DbTables.Grade;
import sample.dbControllers.exceptions.IllegalOrphanException;
import sample.dbControllers.exceptions.NonexistentEntityException;
import sample.dbControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author monder
 */
public class GradeJpaController implements Serializable {

    public GradeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grade grade) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        StudentSub studentSubOrphanCheck = grade.getStudentSub();
        if (studentSubOrphanCheck != null) {
            Grade oldGrade1OfStudentSub = studentSubOrphanCheck.getGrade1();
            if (oldGrade1OfStudentSub != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The StudentSub " + studentSubOrphanCheck + " already has an item of type Grade whose studentSub column cannot be null. Please make another selection for the studentSub field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Semester semesterId = grade.getSemesterId();
            if (semesterId != null) {
                semesterId = em.getReference(semesterId.getClass(), semesterId.getId());
                grade.setSemesterId(semesterId);
            }
            StudentSub studentSub = grade.getStudentSub();
            if (studentSub != null) {
                studentSub = em.getReference(studentSub.getClass(), studentSub.getId());
                grade.setStudentSub(studentSub);
            }
            em.persist(grade);
            if (semesterId != null) {
                semesterId.getGradeList().add(grade);
                semesterId = em.merge(semesterId);
            }
            if (studentSub != null) {
                studentSub.setGrade1(grade);
                studentSub = em.merge(studentSub);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGrade(grade.getId()) != null) {
                throw new PreexistingEntityException("Grade " + grade + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grade grade) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grade persistentGrade = em.find(Grade.class, grade.getId());
            Semester semesterIdOld = persistentGrade.getSemesterId();
            Semester semesterIdNew = grade.getSemesterId();
            StudentSub studentSubOld = persistentGrade.getStudentSub();
            StudentSub studentSubNew = grade.getStudentSub();
            List<String> illegalOrphanMessages = null;
            if (studentSubNew != null && !studentSubNew.equals(studentSubOld)) {
                Grade oldGrade1OfStudentSub = studentSubNew.getGrade1();
                if (oldGrade1OfStudentSub != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The StudentSub " + studentSubNew + " already has an item of type Grade whose studentSub column cannot be null. Please make another selection for the studentSub field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (semesterIdNew != null) {
                semesterIdNew = em.getReference(semesterIdNew.getClass(), semesterIdNew.getId());
                grade.setSemesterId(semesterIdNew);
            }
            if (studentSubNew != null) {
                studentSubNew = em.getReference(studentSubNew.getClass(), studentSubNew.getId());
                grade.setStudentSub(studentSubNew);
            }
            grade = em.merge(grade);
            if (semesterIdOld != null && !semesterIdOld.equals(semesterIdNew)) {
                semesterIdOld.getGradeList().remove(grade);
                semesterIdOld = em.merge(semesterIdOld);
            }
            if (semesterIdNew != null && !semesterIdNew.equals(semesterIdOld)) {
                semesterIdNew.getGradeList().add(grade);
                semesterIdNew = em.merge(semesterIdNew);
            }
            if (studentSubOld != null && !studentSubOld.equals(studentSubNew)) {
                studentSubOld.setGrade1(null);
                studentSubOld = em.merge(studentSubOld);
            }
            if (studentSubNew != null && !studentSubNew.equals(studentSubOld)) {
                studentSubNew.setGrade1(grade);
                studentSubNew = em.merge(studentSubNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = grade.getId();
                if (findGrade(id) == null) {
                    throw new NonexistentEntityException("The grade with id " + id + " no longer exists.");
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
            Grade grade;
            try {
                grade = em.getReference(Grade.class, id);
                grade.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grade with id " + id + " no longer exists.", enfe);
            }
            Semester semesterId = grade.getSemesterId();
            if (semesterId != null) {
                semesterId.getGradeList().remove(grade);
                semesterId = em.merge(semesterId);
            }
            StudentSub studentSub = grade.getStudentSub();
            if (studentSub != null) {
                studentSub.setGrade1(null);
                studentSub = em.merge(studentSub);
            }
            em.remove(grade);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grade> findGradeEntities() {
        return findGradeEntities(true, -1, -1);
    }

    public List<Grade> findGradeEntities(int maxResults, int firstResult) {
        return findGradeEntities(false, maxResults, firstResult);
    }

    private List<Grade> findGradeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grade.class));
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

    public Grade findGrade(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grade.class, id);
        } finally {
            em.close();
        }
    }

    public int getGradeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grade> rt = cq.from(Grade.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
