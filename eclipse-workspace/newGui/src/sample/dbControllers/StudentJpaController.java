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
import sample.DbTables.SubDepartment;
import sample.DbTables.Countries;
import sample.DbTables.Semester;
import sample.DbTables.StudentSub;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sample.DbTables.Student;
import sample.dbControllers.exceptions.IllegalOrphanException;
import sample.dbControllers.exceptions.NonexistentEntityException;
import sample.dbControllers.exceptions.PreexistingEntityException;

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

    public void create(Student student) throws PreexistingEntityException, Exception {
        if (student.getStudentSubList() == null) {
            student.setStudentSubList(new ArrayList<StudentSub>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SubDepartment subMajor = student.getSubMajor();
            if (subMajor != null) {
                subMajor = em.getReference(subMajor.getClass(), subMajor.getId());
                student.setSubMajor(subMajor);
            }
            Countries nationality = student.getNationality();
            if (nationality != null) {
                nationality = em.getReference(nationality.getClass(), nationality.getNumCode());
                student.setNationality(nationality);
            }
            Semester semsterId = student.getSemsterId();
            if (semsterId != null) {
                semsterId = em.getReference(semsterId.getClass(), semsterId.getId());
                student.setSemsterId(semsterId);
            }
            List<StudentSub> attachedStudentSubList = new ArrayList<StudentSub>();
            for (StudentSub studentSubListStudentSubToAttach : student.getStudentSubList()) {
                studentSubListStudentSubToAttach = em.getReference(studentSubListStudentSubToAttach.getClass(), studentSubListStudentSubToAttach.getId());
                attachedStudentSubList.add(studentSubListStudentSubToAttach);
            }
            student.setStudentSubList(attachedStudentSubList);
            em.persist(student);
            if (subMajor != null) {
                subMajor.getStudentList().add(student);
                subMajor = em.merge(subMajor);
            }
            if (nationality != null) {
                nationality.getStudentList().add(student);
                nationality = em.merge(nationality);
            }
            if (semsterId != null) {
                semsterId.getStudentList().add(student);
                semsterId = em.merge(semsterId);
            }
            for (StudentSub studentSubListStudentSub : student.getStudentSubList()) {
                Student oldStudentIdOfStudentSubListStudentSub = studentSubListStudentSub.getStudentId();
                studentSubListStudentSub.setStudentId(student);
                studentSubListStudentSub = em.merge(studentSubListStudentSub);
                if (oldStudentIdOfStudentSubListStudentSub != null) {
                    oldStudentIdOfStudentSubListStudentSub.getStudentSubList().remove(studentSubListStudentSub);
                    oldStudentIdOfStudentSubListStudentSub = em.merge(oldStudentIdOfStudentSubListStudentSub);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStudent(student.getStudentId()) != null) {
                throw new PreexistingEntityException("Student " + student + " already exists.", ex);
            }
            throw ex;
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
            Student persistentStudent = em.find(Student.class, student.getStudentId());
            SubDepartment subMajorOld = persistentStudent.getSubMajor();
            SubDepartment subMajorNew = student.getSubMajor();
            Countries nationalityOld = persistentStudent.getNationality();
            Countries nationalityNew = student.getNationality();
            Semester semsterIdOld = persistentStudent.getSemsterId();
            Semester semsterIdNew = student.getSemsterId();
            List<StudentSub> studentSubListOld = persistentStudent.getStudentSubList();
            List<StudentSub> studentSubListNew = student.getStudentSubList();
            List<String> illegalOrphanMessages = null;
            for (StudentSub studentSubListOldStudentSub : studentSubListOld) {
                if (!studentSubListNew.contains(studentSubListOldStudentSub)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain StudentSub " + studentSubListOldStudentSub + " since its studentId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (subMajorNew != null) {
                subMajorNew = em.getReference(subMajorNew.getClass(), subMajorNew.getId());
                student.setSubMajor(subMajorNew);
            }
            if (nationalityNew != null) {
                nationalityNew = em.getReference(nationalityNew.getClass(), nationalityNew.getNumCode());
                student.setNationality(nationalityNew);
            }
            if (semsterIdNew != null) {
                semsterIdNew = em.getReference(semsterIdNew.getClass(), semsterIdNew.getId());
                student.setSemsterId(semsterIdNew);
            }
            List<StudentSub> attachedStudentSubListNew = new ArrayList<StudentSub>();
            for (StudentSub studentSubListNewStudentSubToAttach : studentSubListNew) {
                studentSubListNewStudentSubToAttach = em.getReference(studentSubListNewStudentSubToAttach.getClass(), studentSubListNewStudentSubToAttach.getId());
                attachedStudentSubListNew.add(studentSubListNewStudentSubToAttach);
            }
            studentSubListNew = attachedStudentSubListNew;
            student.setStudentSubList(studentSubListNew);
            student = em.merge(student);
            if (subMajorOld != null && !subMajorOld.equals(subMajorNew)) {
                subMajorOld.getStudentList().remove(student);
                subMajorOld = em.merge(subMajorOld);
            }
            if (subMajorNew != null && !subMajorNew.equals(subMajorOld)) {
                subMajorNew.getStudentList().add(student);
                subMajorNew = em.merge(subMajorNew);
            }
            if (nationalityOld != null && !nationalityOld.equals(nationalityNew)) {
                nationalityOld.getStudentList().remove(student);
                nationalityOld = em.merge(nationalityOld);
            }
            if (nationalityNew != null && !nationalityNew.equals(nationalityOld)) {
                nationalityNew.getStudentList().add(student);
                nationalityNew = em.merge(nationalityNew);
            }
            if (semsterIdOld != null && !semsterIdOld.equals(semsterIdNew)) {
                semsterIdOld.getStudentList().remove(student);
                semsterIdOld = em.merge(semsterIdOld);
            }
            if (semsterIdNew != null && !semsterIdNew.equals(semsterIdOld)) {
                semsterIdNew.getStudentList().add(student);
                semsterIdNew = em.merge(semsterIdNew);
            }
            for (StudentSub studentSubListNewStudentSub : studentSubListNew) {
                if (!studentSubListOld.contains(studentSubListNewStudentSub)) {
                    Student oldStudentIdOfStudentSubListNewStudentSub = studentSubListNewStudentSub.getStudentId();
                    studentSubListNewStudentSub.setStudentId(student);
                    studentSubListNewStudentSub = em.merge(studentSubListNewStudentSub);
                    if (oldStudentIdOfStudentSubListNewStudentSub != null && !oldStudentIdOfStudentSubListNewStudentSub.equals(student)) {
                        oldStudentIdOfStudentSubListNewStudentSub.getStudentSubList().remove(studentSubListNewStudentSub);
                        oldStudentIdOfStudentSubListNewStudentSub = em.merge(oldStudentIdOfStudentSubListNewStudentSub);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = student.getStudentId();
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
                student.getStudentId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The student with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<StudentSub> studentSubListOrphanCheck = student.getStudentSubList();
            for (StudentSub studentSubListOrphanCheckStudentSub : studentSubListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Student (" + student + ") cannot be destroyed since the StudentSub " + studentSubListOrphanCheckStudentSub + " in its studentSubList field has a non-nullable studentId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            SubDepartment subMajor = student.getSubMajor();
            if (subMajor != null) {
                subMajor.getStudentList().remove(student);
                subMajor = em.merge(subMajor);
            }
            Countries nationality = student.getNationality();
            if (nationality != null) {
                nationality.getStudentList().remove(student);
                nationality = em.merge(nationality);
            }
            Semester semsterId = student.getSemsterId();
            if (semsterId != null) {
                semsterId.getStudentList().remove(student);
                semsterId = em.merge(semsterId);
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
