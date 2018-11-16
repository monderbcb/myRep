/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university.JPAcontrollers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import university.Entites.Subject;
import university.Entites.Semester;
import university.Entites.Instructor;
import university.Entites.StudentCourse;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import university.Entites.Course;
import university.JPAcontrollers.exceptions.IllegalOrphanException;
import university.JPAcontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class CourseJpaController implements Serializable {

    public CourseJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Course course) {
        if (course.getStudentCourseList() == null) {
            course.setStudentCourseList(new ArrayList<StudentCourse>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subject subjectId = course.getSubjectId();
            if (subjectId != null) {
                subjectId = em.getReference(subjectId.getClass(), subjectId.getId());
                course.setSubjectId(subjectId);
            }
            Semester semesterId = course.getSemesterId();
            if (semesterId != null) {
                semesterId = em.getReference(semesterId.getClass(), semesterId.getId());
                course.setSemesterId(semesterId);
            }
            Instructor instructorId = course.getInstructorId();
            if (instructorId != null) {
                instructorId = em.getReference(instructorId.getClass(), instructorId.getId());
                course.setInstructorId(instructorId);
            }
            List<StudentCourse> attachedStudentCourseList = new ArrayList<StudentCourse>();
            for (StudentCourse studentCourseListStudentCourseToAttach : course.getStudentCourseList()) {
                studentCourseListStudentCourseToAttach = em.getReference(studentCourseListStudentCourseToAttach.getClass(), studentCourseListStudentCourseToAttach.getId());
                attachedStudentCourseList.add(studentCourseListStudentCourseToAttach);
            }
            course.setStudentCourseList(attachedStudentCourseList);
            em.persist(course);
            if (subjectId != null) {
                subjectId.getCourseList().add(course);
                subjectId = em.merge(subjectId);
            }
            if (semesterId != null) {
                semesterId.getCourseList().add(course);
                semesterId = em.merge(semesterId);
            }
            if (instructorId != null) {
                instructorId.getCourseList().add(course);
                instructorId = em.merge(instructorId);
            }
            for (StudentCourse studentCourseListStudentCourse : course.getStudentCourseList()) {
                Course oldCourseIdOfStudentCourseListStudentCourse = studentCourseListStudentCourse.getCourseId();
                studentCourseListStudentCourse.setCourseId(course);
                studentCourseListStudentCourse = em.merge(studentCourseListStudentCourse);
                if (oldCourseIdOfStudentCourseListStudentCourse != null) {
                    oldCourseIdOfStudentCourseListStudentCourse.getStudentCourseList().remove(studentCourseListStudentCourse);
                    oldCourseIdOfStudentCourseListStudentCourse = em.merge(oldCourseIdOfStudentCourseListStudentCourse);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Course course) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Course persistentCourse = em.find(Course.class, course.getId());
            Subject subjectIdOld = persistentCourse.getSubjectId();
            Subject subjectIdNew = course.getSubjectId();
            Semester semesterIdOld = persistentCourse.getSemesterId();
            Semester semesterIdNew = course.getSemesterId();
            Instructor instructorIdOld = persistentCourse.getInstructorId();
            Instructor instructorIdNew = course.getInstructorId();
            List<StudentCourse> studentCourseListOld = persistentCourse.getStudentCourseList();
            List<StudentCourse> studentCourseListNew = course.getStudentCourseList();
            List<String> illegalOrphanMessages = null;
            for (StudentCourse studentCourseListOldStudentCourse : studentCourseListOld) {
                if (!studentCourseListNew.contains(studentCourseListOldStudentCourse)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain StudentCourse " + studentCourseListOldStudentCourse + " since its courseId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (subjectIdNew != null) {
                subjectIdNew = em.getReference(subjectIdNew.getClass(), subjectIdNew.getId());
                course.setSubjectId(subjectIdNew);
            }
            if (semesterIdNew != null) {
                semesterIdNew = em.getReference(semesterIdNew.getClass(), semesterIdNew.getId());
                course.setSemesterId(semesterIdNew);
            }
            if (instructorIdNew != null) {
                instructorIdNew = em.getReference(instructorIdNew.getClass(), instructorIdNew.getId());
                course.setInstructorId(instructorIdNew);
            }
            List<StudentCourse> attachedStudentCourseListNew = new ArrayList<StudentCourse>();
            for (StudentCourse studentCourseListNewStudentCourseToAttach : studentCourseListNew) {
                studentCourseListNewStudentCourseToAttach = em.getReference(studentCourseListNewStudentCourseToAttach.getClass(), studentCourseListNewStudentCourseToAttach.getId());
                attachedStudentCourseListNew.add(studentCourseListNewStudentCourseToAttach);
            }
            studentCourseListNew = attachedStudentCourseListNew;
            course.setStudentCourseList(studentCourseListNew);
            course = em.merge(course);
            if (subjectIdOld != null && !subjectIdOld.equals(subjectIdNew)) {
                subjectIdOld.getCourseList().remove(course);
                subjectIdOld = em.merge(subjectIdOld);
            }
            if (subjectIdNew != null && !subjectIdNew.equals(subjectIdOld)) {
                subjectIdNew.getCourseList().add(course);
                subjectIdNew = em.merge(subjectIdNew);
            }
            if (semesterIdOld != null && !semesterIdOld.equals(semesterIdNew)) {
                semesterIdOld.getCourseList().remove(course);
                semesterIdOld = em.merge(semesterIdOld);
            }
            if (semesterIdNew != null && !semesterIdNew.equals(semesterIdOld)) {
                semesterIdNew.getCourseList().add(course);
                semesterIdNew = em.merge(semesterIdNew);
            }
            if (instructorIdOld != null && !instructorIdOld.equals(instructorIdNew)) {
                instructorIdOld.getCourseList().remove(course);
                instructorIdOld = em.merge(instructorIdOld);
            }
            if (instructorIdNew != null && !instructorIdNew.equals(instructorIdOld)) {
                instructorIdNew.getCourseList().add(course);
                instructorIdNew = em.merge(instructorIdNew);
            }
            for (StudentCourse studentCourseListNewStudentCourse : studentCourseListNew) {
                if (!studentCourseListOld.contains(studentCourseListNewStudentCourse)) {
                    Course oldCourseIdOfStudentCourseListNewStudentCourse = studentCourseListNewStudentCourse.getCourseId();
                    studentCourseListNewStudentCourse.setCourseId(course);
                    studentCourseListNewStudentCourse = em.merge(studentCourseListNewStudentCourse);
                    if (oldCourseIdOfStudentCourseListNewStudentCourse != null && !oldCourseIdOfStudentCourseListNewStudentCourse.equals(course)) {
                        oldCourseIdOfStudentCourseListNewStudentCourse.getStudentCourseList().remove(studentCourseListNewStudentCourse);
                        oldCourseIdOfStudentCourseListNewStudentCourse = em.merge(oldCourseIdOfStudentCourseListNewStudentCourse);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = course.getId();
                if (findCourse(id) == null) {
                    throw new NonexistentEntityException("The course with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Course course;
            try {
                course = em.getReference(Course.class, id);
                course.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The course with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<StudentCourse> studentCourseListOrphanCheck = course.getStudentCourseList();
            for (StudentCourse studentCourseListOrphanCheckStudentCourse : studentCourseListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Course (" + course + ") cannot be destroyed since the StudentCourse " + studentCourseListOrphanCheckStudentCourse + " in its studentCourseList field has a non-nullable courseId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Subject subjectId = course.getSubjectId();
            if (subjectId != null) {
                subjectId.getCourseList().remove(course);
                subjectId = em.merge(subjectId);
            }
            Semester semesterId = course.getSemesterId();
            if (semesterId != null) {
                semesterId.getCourseList().remove(course);
                semesterId = em.merge(semesterId);
            }
            Instructor instructorId = course.getInstructorId();
            if (instructorId != null) {
                instructorId.getCourseList().remove(course);
                instructorId = em.merge(instructorId);
            }
            em.remove(course);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Course> findCourseEntities() {
        return findCourseEntities(true, -1, -1);
    }

    public List<Course> findCourseEntities(int maxResults, int firstResult) {
        return findCourseEntities(false, maxResults, firstResult);
    }

    private List<Course> findCourseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Course.class));
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

    public Course findCourse(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Course.class, id);
        } finally {
            em.close();
        }
    }

    public int getCourseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Course> rt = cq.from(Course.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
