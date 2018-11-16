/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university.JPAcontrollers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import university.Entites.Course;
import university.Entites.Student;
import university.Entites.StudentCourse;
import university.JPAcontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class StudentCourseJpaController implements Serializable {

    public StudentCourseJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(StudentCourse studentCourse) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Course courseId = studentCourse.getCourseId();
            if (courseId != null) {
                courseId = em.getReference(courseId.getClass(), courseId.getId());
                studentCourse.setCourseId(courseId);
            }
            Student studentId = studentCourse.getStudentId();
            if (studentId != null) {
                studentId = em.getReference(studentId.getClass(), studentId.getId());
                studentCourse.setStudentId(studentId);
            }
            em.persist(studentCourse);
            if (courseId != null) {
                courseId.getStudentCourseList().add(studentCourse);
                courseId = em.merge(courseId);
            }
            if (studentId != null) {
                studentId.getStudentCourseList().add(studentCourse);
                studentId = em.merge(studentId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(StudentCourse studentCourse) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StudentCourse persistentStudentCourse = em.find(StudentCourse.class, studentCourse.getId());
            Course courseIdOld = persistentStudentCourse.getCourseId();
            Course courseIdNew = studentCourse.getCourseId();
            Student studentIdOld = persistentStudentCourse.getStudentId();
            Student studentIdNew = studentCourse.getStudentId();
            if (courseIdNew != null) {
                courseIdNew = em.getReference(courseIdNew.getClass(), courseIdNew.getId());
                studentCourse.setCourseId(courseIdNew);
            }
            if (studentIdNew != null) {
                studentIdNew = em.getReference(studentIdNew.getClass(), studentIdNew.getId());
                studentCourse.setStudentId(studentIdNew);
            }
            studentCourse = em.merge(studentCourse);
            if (courseIdOld != null && !courseIdOld.equals(courseIdNew)) {
                courseIdOld.getStudentCourseList().remove(studentCourse);
                courseIdOld = em.merge(courseIdOld);
            }
            if (courseIdNew != null && !courseIdNew.equals(courseIdOld)) {
                courseIdNew.getStudentCourseList().add(studentCourse);
                courseIdNew = em.merge(courseIdNew);
            }
            if (studentIdOld != null && !studentIdOld.equals(studentIdNew)) {
                studentIdOld.getStudentCourseList().remove(studentCourse);
                studentIdOld = em.merge(studentIdOld);
            }
            if (studentIdNew != null && !studentIdNew.equals(studentIdOld)) {
                studentIdNew.getStudentCourseList().add(studentCourse);
                studentIdNew = em.merge(studentIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = studentCourse.getId();
                if (findStudentCourse(id) == null) {
                    throw new NonexistentEntityException("The studentCourse with id " + id + " no longer exists.");
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
            StudentCourse studentCourse;
            try {
                studentCourse = em.getReference(StudentCourse.class, id);
                studentCourse.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The studentCourse with id " + id + " no longer exists.", enfe);
            }
            Course courseId = studentCourse.getCourseId();
            if (courseId != null) {
                courseId.getStudentCourseList().remove(studentCourse);
                courseId = em.merge(courseId);
            }
            Student studentId = studentCourse.getStudentId();
            if (studentId != null) {
                studentId.getStudentCourseList().remove(studentCourse);
                studentId = em.merge(studentId);
            }
            em.remove(studentCourse);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<StudentCourse> findStudentCourseEntities() {
        return findStudentCourseEntities(true, -1, -1);
    }

    public List<StudentCourse> findStudentCourseEntities(int maxResults, int firstResult) {
        return findStudentCourseEntities(false, maxResults, firstResult);
    }

    private List<StudentCourse> findStudentCourseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StudentCourse.class));
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

    public StudentCourse findStudentCourse(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StudentCourse.class, id);
        } finally {
            em.close();
        }
    }

    public int getStudentCourseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StudentCourse> rt = cq.from(StudentCourse.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
