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
import university.Entites.Section;
import university.Entites.Semester;
import university.Entites.StudentCourse;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import university.Entites.Student;
import university.JPAcontrollers.exceptions.IllegalOrphanException;
import university.JPAcontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class StudentJpaController implements Serializable {

    public StudentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Student student) {
        if (student.getStudentCourseList() == null) {
            student.setStudentCourseList(new ArrayList<StudentCourse>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Section section = student.getSection();
            if (section != null) {
                section = em.getReference(section.getClass(), section.getId());
                student.setSection(section);
            }
            Semester semesterId = student.getSemesterId();
            if (semesterId != null) {
                semesterId = em.getReference(semesterId.getClass(), semesterId.getId());
                student.setSemesterId(semesterId);
            }
            List<StudentCourse> attachedStudentCourseList = new ArrayList<StudentCourse>();
            for (StudentCourse studentCourseListStudentCourseToAttach : student.getStudentCourseList()) {
                studentCourseListStudentCourseToAttach = em.getReference(studentCourseListStudentCourseToAttach.getClass(), studentCourseListStudentCourseToAttach.getId());
                attachedStudentCourseList.add(studentCourseListStudentCourseToAttach);
            }
            student.setStudentCourseList(attachedStudentCourseList);
            em.persist(student);
            if (section != null) {
                section.getStudentList().add(student);
                section = em.merge(section);
            }
            if (semesterId != null) {
                semesterId.getStudentList().add(student);
                semesterId = em.merge(semesterId);
            }
            for (StudentCourse studentCourseListStudentCourse : student.getStudentCourseList()) {
                Student oldStudentIdOfStudentCourseListStudentCourse = studentCourseListStudentCourse.getStudentId();
                studentCourseListStudentCourse.setStudentId(student);
                studentCourseListStudentCourse = em.merge(studentCourseListStudentCourse);
                if (oldStudentIdOfStudentCourseListStudentCourse != null) {
                    oldStudentIdOfStudentCourseListStudentCourse.getStudentCourseList().remove(studentCourseListStudentCourse);
                    oldStudentIdOfStudentCourseListStudentCourse = em.merge(oldStudentIdOfStudentCourseListStudentCourse);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Student student) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Student persistentStudent = em.find(Student.class, student.getId());
            Section sectionOld = persistentStudent.getSection();
            Section sectionNew = student.getSection();
            Semester semesterIdOld = persistentStudent.getSemesterId();
            Semester semesterIdNew = student.getSemesterId();
            List<StudentCourse> studentCourseListOld = persistentStudent.getStudentCourseList();
            List<StudentCourse> studentCourseListNew = student.getStudentCourseList();
            List<String> illegalOrphanMessages = null;
            for (StudentCourse studentCourseListOldStudentCourse : studentCourseListOld) {
                if (!studentCourseListNew.contains(studentCourseListOldStudentCourse)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain StudentCourse " + studentCourseListOldStudentCourse + " since its studentId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (sectionNew != null) {
                sectionNew = em.getReference(sectionNew.getClass(), sectionNew.getId());
                student.setSection(sectionNew);
            }
            if (semesterIdNew != null) {
                semesterIdNew = em.getReference(semesterIdNew.getClass(), semesterIdNew.getId());
                student.setSemesterId(semesterIdNew);
            }
            List<StudentCourse> attachedStudentCourseListNew = new ArrayList<StudentCourse>();
            for (StudentCourse studentCourseListNewStudentCourseToAttach : studentCourseListNew) {
                studentCourseListNewStudentCourseToAttach = em.getReference(studentCourseListNewStudentCourseToAttach.getClass(), studentCourseListNewStudentCourseToAttach.getId());
                attachedStudentCourseListNew.add(studentCourseListNewStudentCourseToAttach);
            }
            studentCourseListNew = attachedStudentCourseListNew;
            student.setStudentCourseList(studentCourseListNew);
            student = em.merge(student);
            if (sectionOld != null && !sectionOld.equals(sectionNew)) {
                sectionOld.getStudentList().remove(student);
                sectionOld = em.merge(sectionOld);
            }
            if (sectionNew != null && !sectionNew.equals(sectionOld)) {
                sectionNew.getStudentList().add(student);
                sectionNew = em.merge(sectionNew);
            }
            if (semesterIdOld != null && !semesterIdOld.equals(semesterIdNew)) {
                semesterIdOld.getStudentList().remove(student);
                semesterIdOld = em.merge(semesterIdOld);
            }
            if (semesterIdNew != null && !semesterIdNew.equals(semesterIdOld)) {
                semesterIdNew.getStudentList().add(student);
                semesterIdNew = em.merge(semesterIdNew);
            }
            for (StudentCourse studentCourseListNewStudentCourse : studentCourseListNew) {
                if (!studentCourseListOld.contains(studentCourseListNewStudentCourse)) {
                    Student oldStudentIdOfStudentCourseListNewStudentCourse = studentCourseListNewStudentCourse.getStudentId();
                    studentCourseListNewStudentCourse.setStudentId(student);
                    studentCourseListNewStudentCourse = em.merge(studentCourseListNewStudentCourse);
                    if (oldStudentIdOfStudentCourseListNewStudentCourse != null && !oldStudentIdOfStudentCourseListNewStudentCourse.equals(student)) {
                        oldStudentIdOfStudentCourseListNewStudentCourse.getStudentCourseList().remove(studentCourseListNewStudentCourse);
                        oldStudentIdOfStudentCourseListNewStudentCourse = em.merge(oldStudentIdOfStudentCourseListNewStudentCourse);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = student.getId();
                if (findStudent(id) == null) {
                    throw new NonexistentEntityException("The student with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Student student;
            try {
                student = em.getReference(Student.class, id);
                student.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The student with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<StudentCourse> studentCourseListOrphanCheck = student.getStudentCourseList();
            for (StudentCourse studentCourseListOrphanCheckStudentCourse : studentCourseListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Student (" + student + ") cannot be destroyed since the StudentCourse " + studentCourseListOrphanCheckStudentCourse + " in its studentCourseList field has a non-nullable studentId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Section section = student.getSection();
            if (section != null) {
                section.getStudentList().remove(student);
                section = em.merge(section);
            }
            Semester semesterId = student.getSemesterId();
            if (semesterId != null) {
                semesterId.getStudentList().remove(student);
                semesterId = em.merge(semesterId);
            }
            em.remove(student);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Student> findStudentEntities() {
        return findStudentEntities(true, -1, -1);
    }

    public List<Student> findStudentEntities(int maxResults, int firstResult) {
        return findStudentEntities(false, maxResults, firstResult);
    }

    private List<Student> findStudentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Student.class));
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

    public Student findStudent(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Student.class, id);
        } finally {
            em.close();
        }
    }

    public int getStudentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Student> rt = cq.from(Student.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
