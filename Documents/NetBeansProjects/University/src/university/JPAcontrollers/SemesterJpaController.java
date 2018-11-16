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
import university.Entites.Student;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import university.Entites.Course;
import university.Entites.Semester;
import university.JPAcontrollers.exceptions.IllegalOrphanException;
import university.JPAcontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class SemesterJpaController implements Serializable {

    public SemesterJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Semester semester) {
        if (semester.getStudentList() == null) {
            semester.setStudentList(new ArrayList<Student>());
        }
        if (semester.getCourseList() == null) {
            semester.setCourseList(new ArrayList<Course>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Student> attachedStudentList = new ArrayList<Student>();
            for (Student studentListStudentToAttach : semester.getStudentList()) {
                studentListStudentToAttach = em.getReference(studentListStudentToAttach.getClass(), studentListStudentToAttach.getId());
                attachedStudentList.add(studentListStudentToAttach);
            }
            semester.setStudentList(attachedStudentList);
            List<Course> attachedCourseList = new ArrayList<Course>();
            for (Course courseListCourseToAttach : semester.getCourseList()) {
                courseListCourseToAttach = em.getReference(courseListCourseToAttach.getClass(), courseListCourseToAttach.getId());
                attachedCourseList.add(courseListCourseToAttach);
            }
            semester.setCourseList(attachedCourseList);
            em.persist(semester);
            for (Student studentListStudent : semester.getStudentList()) {
                Semester oldSemesterIdOfStudentListStudent = studentListStudent.getSemesterId();
                studentListStudent.setSemesterId(semester);
                studentListStudent = em.merge(studentListStudent);
                if (oldSemesterIdOfStudentListStudent != null) {
                    oldSemesterIdOfStudentListStudent.getStudentList().remove(studentListStudent);
                    oldSemesterIdOfStudentListStudent = em.merge(oldSemesterIdOfStudentListStudent);
                }
            }
            for (Course courseListCourse : semester.getCourseList()) {
                Semester oldSemesterIdOfCourseListCourse = courseListCourse.getSemesterId();
                courseListCourse.setSemesterId(semester);
                courseListCourse = em.merge(courseListCourse);
                if (oldSemesterIdOfCourseListCourse != null) {
                    oldSemesterIdOfCourseListCourse.getCourseList().remove(courseListCourse);
                    oldSemesterIdOfCourseListCourse = em.merge(oldSemesterIdOfCourseListCourse);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Semester semester) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Semester persistentSemester = em.find(Semester.class, semester.getId());
            List<Student> studentListOld = persistentSemester.getStudentList();
            List<Student> studentListNew = semester.getStudentList();
            List<Course> courseListOld = persistentSemester.getCourseList();
            List<Course> courseListNew = semester.getCourseList();
            List<String> illegalOrphanMessages = null;
            for (Student studentListOldStudent : studentListOld) {
                if (!studentListNew.contains(studentListOldStudent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Student " + studentListOldStudent + " since its semesterId field is not nullable.");
                }
            }
            for (Course courseListOldCourse : courseListOld) {
                if (!courseListNew.contains(courseListOldCourse)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Course " + courseListOldCourse + " since its semesterId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Student> attachedStudentListNew = new ArrayList<Student>();
            for (Student studentListNewStudentToAttach : studentListNew) {
                studentListNewStudentToAttach = em.getReference(studentListNewStudentToAttach.getClass(), studentListNewStudentToAttach.getId());
                attachedStudentListNew.add(studentListNewStudentToAttach);
            }
            studentListNew = attachedStudentListNew;
            semester.setStudentList(studentListNew);
            List<Course> attachedCourseListNew = new ArrayList<Course>();
            for (Course courseListNewCourseToAttach : courseListNew) {
                courseListNewCourseToAttach = em.getReference(courseListNewCourseToAttach.getClass(), courseListNewCourseToAttach.getId());
                attachedCourseListNew.add(courseListNewCourseToAttach);
            }
            courseListNew = attachedCourseListNew;
            semester.setCourseList(courseListNew);
            semester = em.merge(semester);
            for (Student studentListNewStudent : studentListNew) {
                if (!studentListOld.contains(studentListNewStudent)) {
                    Semester oldSemesterIdOfStudentListNewStudent = studentListNewStudent.getSemesterId();
                    studentListNewStudent.setSemesterId(semester);
                    studentListNewStudent = em.merge(studentListNewStudent);
                    if (oldSemesterIdOfStudentListNewStudent != null && !oldSemesterIdOfStudentListNewStudent.equals(semester)) {
                        oldSemesterIdOfStudentListNewStudent.getStudentList().remove(studentListNewStudent);
                        oldSemesterIdOfStudentListNewStudent = em.merge(oldSemesterIdOfStudentListNewStudent);
                    }
                }
            }
            for (Course courseListNewCourse : courseListNew) {
                if (!courseListOld.contains(courseListNewCourse)) {
                    Semester oldSemesterIdOfCourseListNewCourse = courseListNewCourse.getSemesterId();
                    courseListNewCourse.setSemesterId(semester);
                    courseListNewCourse = em.merge(courseListNewCourse);
                    if (oldSemesterIdOfCourseListNewCourse != null && !oldSemesterIdOfCourseListNewCourse.equals(semester)) {
                        oldSemesterIdOfCourseListNewCourse.getCourseList().remove(courseListNewCourse);
                        oldSemesterIdOfCourseListNewCourse = em.merge(oldSemesterIdOfCourseListNewCourse);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = semester.getId();
                if (findSemester(id) == null) {
                    throw new NonexistentEntityException("The semester with id " + id + " no longer exists.");
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
            Semester semester;
            try {
                semester = em.getReference(Semester.class, id);
                semester.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The semester with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Student> studentListOrphanCheck = semester.getStudentList();
            for (Student studentListOrphanCheckStudent : studentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Semester (" + semester + ") cannot be destroyed since the Student " + studentListOrphanCheckStudent + " in its studentList field has a non-nullable semesterId field.");
            }
            List<Course> courseListOrphanCheck = semester.getCourseList();
            for (Course courseListOrphanCheckCourse : courseListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Semester (" + semester + ") cannot be destroyed since the Course " + courseListOrphanCheckCourse + " in its courseList field has a non-nullable semesterId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(semester);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Semester> findSemesterEntities() {
        return findSemesterEntities(true, -1, -1);
    }

    public List<Semester> findSemesterEntities(int maxResults, int firstResult) {
        return findSemesterEntities(false, maxResults, firstResult);
    }

    private List<Semester> findSemesterEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Semester.class));
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

    public Semester findSemester(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Semester.class, id);
        } finally {
            em.close();
        }
    }

    public int getSemesterCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Semester> rt = cq.from(Semester.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
