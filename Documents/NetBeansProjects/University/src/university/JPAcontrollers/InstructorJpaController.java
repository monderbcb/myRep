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
import university.Entites.Department;
import university.Entites.Course;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import university.Entites.Instructor;
import university.JPAcontrollers.exceptions.IllegalOrphanException;
import university.JPAcontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class InstructorJpaController implements Serializable {

    public InstructorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Instructor instructor) {
        if (instructor.getCourseList() == null) {
            instructor.setCourseList(new ArrayList<Course>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Department departmentId = instructor.getDepartmentId();
            if (departmentId != null) {
                departmentId = em.getReference(departmentId.getClass(), departmentId.getId());
                instructor.setDepartmentId(departmentId);
            }
            Department department = instructor.getDepartment();
            if (department != null) {
                department = em.getReference(department.getClass(), department.getId());
                instructor.setDepartment(department);
            }
            List<Course> attachedCourseList = new ArrayList<Course>();
            for (Course courseListCourseToAttach : instructor.getCourseList()) {
                courseListCourseToAttach = em.getReference(courseListCourseToAttach.getClass(), courseListCourseToAttach.getId());
                attachedCourseList.add(courseListCourseToAttach);
            }
            instructor.setCourseList(attachedCourseList);
            em.persist(instructor);
            if (departmentId != null) {
                departmentId.getInstructorList().add(instructor);
                departmentId = em.merge(departmentId);
            }
            if (department != null) {
                Instructor oldInstructorIdOfDepartment = department.getInstructorId();
                if (oldInstructorIdOfDepartment != null) {
                    oldInstructorIdOfDepartment.setDepartment(null);
                    oldInstructorIdOfDepartment = em.merge(oldInstructorIdOfDepartment);
                }
                department.setInstructorId(instructor);
                department = em.merge(department);
            }
            for (Course courseListCourse : instructor.getCourseList()) {
                Instructor oldInstructorIdOfCourseListCourse = courseListCourse.getInstructorId();
                courseListCourse.setInstructorId(instructor);
                courseListCourse = em.merge(courseListCourse);
                if (oldInstructorIdOfCourseListCourse != null) {
                    oldInstructorIdOfCourseListCourse.getCourseList().remove(courseListCourse);
                    oldInstructorIdOfCourseListCourse = em.merge(oldInstructorIdOfCourseListCourse);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Instructor instructor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Instructor persistentInstructor = em.find(Instructor.class, instructor.getId());
            Department departmentIdOld = persistentInstructor.getDepartmentId();
            Department departmentIdNew = instructor.getDepartmentId();
            Department departmentOld = persistentInstructor.getDepartment();
            Department departmentNew = instructor.getDepartment();
            List<Course> courseListOld = persistentInstructor.getCourseList();
            List<Course> courseListNew = instructor.getCourseList();
            List<String> illegalOrphanMessages = null;
            for (Course courseListOldCourse : courseListOld) {
                if (!courseListNew.contains(courseListOldCourse)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Course " + courseListOldCourse + " since its instructorId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (departmentIdNew != null) {
                departmentIdNew = em.getReference(departmentIdNew.getClass(), departmentIdNew.getId());
                instructor.setDepartmentId(departmentIdNew);
            }
            if (departmentNew != null) {
                departmentNew = em.getReference(departmentNew.getClass(), departmentNew.getId());
                instructor.setDepartment(departmentNew);
            }
            List<Course> attachedCourseListNew = new ArrayList<Course>();
            for (Course courseListNewCourseToAttach : courseListNew) {
                courseListNewCourseToAttach = em.getReference(courseListNewCourseToAttach.getClass(), courseListNewCourseToAttach.getId());
                attachedCourseListNew.add(courseListNewCourseToAttach);
            }
            courseListNew = attachedCourseListNew;
            instructor.setCourseList(courseListNew);
            instructor = em.merge(instructor);
            if (departmentIdOld != null && !departmentIdOld.equals(departmentIdNew)) {
                departmentIdOld.getInstructorList().remove(instructor);
                departmentIdOld = em.merge(departmentIdOld);
            }
            if (departmentIdNew != null && !departmentIdNew.equals(departmentIdOld)) {
                departmentIdNew.getInstructorList().add(instructor);
                departmentIdNew = em.merge(departmentIdNew);
            }
            if (departmentOld != null && !departmentOld.equals(departmentNew)) {
                departmentOld.setInstructorId(null);
                departmentOld = em.merge(departmentOld);
            }
            if (departmentNew != null && !departmentNew.equals(departmentOld)) {
                Instructor oldInstructorIdOfDepartment = departmentNew.getInstructorId();
                if (oldInstructorIdOfDepartment != null) {
                    oldInstructorIdOfDepartment.setDepartment(null);
                    oldInstructorIdOfDepartment = em.merge(oldInstructorIdOfDepartment);
                }
                departmentNew.setInstructorId(instructor);
                departmentNew = em.merge(departmentNew);
            }
            for (Course courseListNewCourse : courseListNew) {
                if (!courseListOld.contains(courseListNewCourse)) {
                    Instructor oldInstructorIdOfCourseListNewCourse = courseListNewCourse.getInstructorId();
                    courseListNewCourse.setInstructorId(instructor);
                    courseListNewCourse = em.merge(courseListNewCourse);
                    if (oldInstructorIdOfCourseListNewCourse != null && !oldInstructorIdOfCourseListNewCourse.equals(instructor)) {
                        oldInstructorIdOfCourseListNewCourse.getCourseList().remove(courseListNewCourse);
                        oldInstructorIdOfCourseListNewCourse = em.merge(oldInstructorIdOfCourseListNewCourse);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = instructor.getId();
                if (findInstructor(id) == null) {
                    throw new NonexistentEntityException("The instructor with id " + id + " no longer exists.");
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
            Instructor instructor;
            try {
                instructor = em.getReference(Instructor.class, id);
                instructor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The instructor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Course> courseListOrphanCheck = instructor.getCourseList();
            for (Course courseListOrphanCheckCourse : courseListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Instructor (" + instructor + ") cannot be destroyed since the Course " + courseListOrphanCheckCourse + " in its courseList field has a non-nullable instructorId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Department departmentId = instructor.getDepartmentId();
            if (departmentId != null) {
                departmentId.getInstructorList().remove(instructor);
                departmentId = em.merge(departmentId);
            }
            Department department = instructor.getDepartment();
            if (department != null) {
                department.setInstructorId(null);
                department = em.merge(department);
            }
            em.remove(instructor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Instructor> findInstructorEntities() {
        return findInstructorEntities(true, -1, -1);
    }

    public List<Instructor> findInstructorEntities(int maxResults, int firstResult) {
        return findInstructorEntities(false, maxResults, firstResult);
    }

    private List<Instructor> findInstructorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Instructor.class));
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

    public Instructor findInstructor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Instructor.class, id);
        } finally {
            em.close();
        }
    }

    public int getInstructorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Instructor> rt = cq.from(Instructor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
