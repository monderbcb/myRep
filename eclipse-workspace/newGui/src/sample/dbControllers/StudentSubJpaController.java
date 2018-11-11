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
import sample.DbTables.Student;
import sample.DbTables.Subjects;
import sample.DbTables.Semester;
import sample.DbTables.Grade;
import sample.DbTables.StudentPayment;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sample.DbTables.StudentSub;
import sample.dbControllers.exceptions.IllegalOrphanException;
import sample.dbControllers.exceptions.NonexistentEntityException;
import sample.dbControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author monder
 */
public class StudentSubJpaController implements Serializable {

    public StudentSubJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(StudentSub studentSub) throws PreexistingEntityException, Exception {
        if (studentSub.getStudentPaymentList() == null) {
            studentSub.setStudentPaymentList(new ArrayList<StudentPayment>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Student studentId = studentSub.getStudentId();
            if (studentId != null) {
                studentId = em.getReference(studentId.getClass(), studentId.getStudentId());
                studentSub.setStudentId(studentId);
            }
            Subjects subjectId = studentSub.getSubjectId();
            if (subjectId != null) {
                subjectId = em.getReference(subjectId.getClass(), subjectId.getId());
                studentSub.setSubjectId(subjectId);
            }
            Semester semesterId = studentSub.getSemesterId();
            if (semesterId != null) {
                semesterId = em.getReference(semesterId.getClass(), semesterId.getId());
                studentSub.setSemesterId(semesterId);
            }
            Grade grade1 = studentSub.getGrade1();
            if (grade1 != null) {
                grade1 = em.getReference(grade1.getClass(), grade1.getId());
                studentSub.setGrade1(grade1);
            }
            List<StudentPayment> attachedStudentPaymentList = new ArrayList<StudentPayment>();
            for (StudentPayment studentPaymentListStudentPaymentToAttach : studentSub.getStudentPaymentList()) {
                studentPaymentListStudentPaymentToAttach = em.getReference(studentPaymentListStudentPaymentToAttach.getClass(), studentPaymentListStudentPaymentToAttach.getId());
                attachedStudentPaymentList.add(studentPaymentListStudentPaymentToAttach);
            }
            studentSub.setStudentPaymentList(attachedStudentPaymentList);
            em.persist(studentSub);
            if (studentId != null) {
                studentId.getStudentSubList().add(studentSub);
                studentId = em.merge(studentId);
            }
            if (subjectId != null) {
                subjectId.getStudentSubList().add(studentSub);
                subjectId = em.merge(subjectId);
            }
            if (semesterId != null) {
                semesterId.getStudentSubList().add(studentSub);
                semesterId = em.merge(semesterId);
            }
            if (grade1 != null) {
                StudentSub oldStudentSubOfGrade1 = grade1.getStudentSub();
                if (oldStudentSubOfGrade1 != null) {
                    oldStudentSubOfGrade1.setGrade1(null);
                    oldStudentSubOfGrade1 = em.merge(oldStudentSubOfGrade1);
                }
                grade1.setStudentSub(studentSub);
                grade1 = em.merge(grade1);
            }
            for (StudentPayment studentPaymentListStudentPayment : studentSub.getStudentPaymentList()) {
                StudentSub oldStudentSubjectRowIdOfStudentPaymentListStudentPayment = studentPaymentListStudentPayment.getStudentSubjectRowId();
                studentPaymentListStudentPayment.setStudentSubjectRowId(studentSub);
                studentPaymentListStudentPayment = em.merge(studentPaymentListStudentPayment);
                if (oldStudentSubjectRowIdOfStudentPaymentListStudentPayment != null) {
                    oldStudentSubjectRowIdOfStudentPaymentListStudentPayment.getStudentPaymentList().remove(studentPaymentListStudentPayment);
                    oldStudentSubjectRowIdOfStudentPaymentListStudentPayment = em.merge(oldStudentSubjectRowIdOfStudentPaymentListStudentPayment);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStudentSub(studentSub.getId()) != null) {
                throw new PreexistingEntityException("StudentSub " + studentSub + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(StudentSub studentSub) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StudentSub persistentStudentSub = em.find(StudentSub.class, studentSub.getId());
            Student studentIdOld = persistentStudentSub.getStudentId();
            Student studentIdNew = studentSub.getStudentId();
            Subjects subjectIdOld = persistentStudentSub.getSubjectId();
            Subjects subjectIdNew = studentSub.getSubjectId();
            Semester semesterIdOld = persistentStudentSub.getSemesterId();
            Semester semesterIdNew = studentSub.getSemesterId();
            Grade grade1Old = persistentStudentSub.getGrade1();
            Grade grade1New = studentSub.getGrade1();
            List<StudentPayment> studentPaymentListOld = persistentStudentSub.getStudentPaymentList();
            List<StudentPayment> studentPaymentListNew = studentSub.getStudentPaymentList();
            List<String> illegalOrphanMessages = null;
            if (grade1Old != null && !grade1Old.equals(grade1New)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Grade " + grade1Old + " since its studentSub field is not nullable.");
            }
            for (StudentPayment studentPaymentListOldStudentPayment : studentPaymentListOld) {
                if (!studentPaymentListNew.contains(studentPaymentListOldStudentPayment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain StudentPayment " + studentPaymentListOldStudentPayment + " since its studentSubjectRowId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (studentIdNew != null) {
                studentIdNew = em.getReference(studentIdNew.getClass(), studentIdNew.getStudentId());
                studentSub.setStudentId(studentIdNew);
            }
            if (subjectIdNew != null) {
                subjectIdNew = em.getReference(subjectIdNew.getClass(), subjectIdNew.getId());
                studentSub.setSubjectId(subjectIdNew);
            }
            if (semesterIdNew != null) {
                semesterIdNew = em.getReference(semesterIdNew.getClass(), semesterIdNew.getId());
                studentSub.setSemesterId(semesterIdNew);
            }
            if (grade1New != null) {
                grade1New = em.getReference(grade1New.getClass(), grade1New.getId());
                studentSub.setGrade1(grade1New);
            }
            List<StudentPayment> attachedStudentPaymentListNew = new ArrayList<StudentPayment>();
            for (StudentPayment studentPaymentListNewStudentPaymentToAttach : studentPaymentListNew) {
                studentPaymentListNewStudentPaymentToAttach = em.getReference(studentPaymentListNewStudentPaymentToAttach.getClass(), studentPaymentListNewStudentPaymentToAttach.getId());
                attachedStudentPaymentListNew.add(studentPaymentListNewStudentPaymentToAttach);
            }
            studentPaymentListNew = attachedStudentPaymentListNew;
            studentSub.setStudentPaymentList(studentPaymentListNew);
            studentSub = em.merge(studentSub);
            if (studentIdOld != null && !studentIdOld.equals(studentIdNew)) {
                studentIdOld.getStudentSubList().remove(studentSub);
                studentIdOld = em.merge(studentIdOld);
            }
            if (studentIdNew != null && !studentIdNew.equals(studentIdOld)) {
                studentIdNew.getStudentSubList().add(studentSub);
                studentIdNew = em.merge(studentIdNew);
            }
            if (subjectIdOld != null && !subjectIdOld.equals(subjectIdNew)) {
                subjectIdOld.getStudentSubList().remove(studentSub);
                subjectIdOld = em.merge(subjectIdOld);
            }
            if (subjectIdNew != null && !subjectIdNew.equals(subjectIdOld)) {
                subjectIdNew.getStudentSubList().add(studentSub);
                subjectIdNew = em.merge(subjectIdNew);
            }
            if (semesterIdOld != null && !semesterIdOld.equals(semesterIdNew)) {
                semesterIdOld.getStudentSubList().remove(studentSub);
                semesterIdOld = em.merge(semesterIdOld);
            }
            if (semesterIdNew != null && !semesterIdNew.equals(semesterIdOld)) {
                semesterIdNew.getStudentSubList().add(studentSub);
                semesterIdNew = em.merge(semesterIdNew);
            }
            if (grade1New != null && !grade1New.equals(grade1Old)) {
                StudentSub oldStudentSubOfGrade1 = grade1New.getStudentSub();
                if (oldStudentSubOfGrade1 != null) {
                    oldStudentSubOfGrade1.setGrade1(null);
                    oldStudentSubOfGrade1 = em.merge(oldStudentSubOfGrade1);
                }
                grade1New.setStudentSub(studentSub);
                grade1New = em.merge(grade1New);
            }
            for (StudentPayment studentPaymentListNewStudentPayment : studentPaymentListNew) {
                if (!studentPaymentListOld.contains(studentPaymentListNewStudentPayment)) {
                    StudentSub oldStudentSubjectRowIdOfStudentPaymentListNewStudentPayment = studentPaymentListNewStudentPayment.getStudentSubjectRowId();
                    studentPaymentListNewStudentPayment.setStudentSubjectRowId(studentSub);
                    studentPaymentListNewStudentPayment = em.merge(studentPaymentListNewStudentPayment);
                    if (oldStudentSubjectRowIdOfStudentPaymentListNewStudentPayment != null && !oldStudentSubjectRowIdOfStudentPaymentListNewStudentPayment.equals(studentSub)) {
                        oldStudentSubjectRowIdOfStudentPaymentListNewStudentPayment.getStudentPaymentList().remove(studentPaymentListNewStudentPayment);
                        oldStudentSubjectRowIdOfStudentPaymentListNewStudentPayment = em.merge(oldStudentSubjectRowIdOfStudentPaymentListNewStudentPayment);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = studentSub.getId();
                if (findStudentSub(id) == null) {
                    throw new NonexistentEntityException("The studentSub with id " + id + " no longer exists.");
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
            StudentSub studentSub;
            try {
                studentSub = em.getReference(StudentSub.class, id);
                studentSub.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The studentSub with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Grade grade1OrphanCheck = studentSub.getGrade1();
            if (grade1OrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This StudentSub (" + studentSub + ") cannot be destroyed since the Grade " + grade1OrphanCheck + " in its grade1 field has a non-nullable studentSub field.");
            }
            List<StudentPayment> studentPaymentListOrphanCheck = studentSub.getStudentPaymentList();
            for (StudentPayment studentPaymentListOrphanCheckStudentPayment : studentPaymentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This StudentSub (" + studentSub + ") cannot be destroyed since the StudentPayment " + studentPaymentListOrphanCheckStudentPayment + " in its studentPaymentList field has a non-nullable studentSubjectRowId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Student studentId = studentSub.getStudentId();
            if (studentId != null) {
                studentId.getStudentSubList().remove(studentSub);
                studentId = em.merge(studentId);
            }
            Subjects subjectId = studentSub.getSubjectId();
            if (subjectId != null) {
                subjectId.getStudentSubList().remove(studentSub);
                subjectId = em.merge(subjectId);
            }
            Semester semesterId = studentSub.getSemesterId();
            if (semesterId != null) {
                semesterId.getStudentSubList().remove(studentSub);
                semesterId = em.merge(semesterId);
            }
            em.remove(studentSub);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<StudentSub> findStudentSubEntities() {
        return findStudentSubEntities(true, -1, -1);
    }

    public List<StudentSub> findStudentSubEntities(int maxResults, int firstResult) {
        return findStudentSubEntities(false, maxResults, firstResult);
    }

    private List<StudentSub> findStudentSubEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StudentSub.class));
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

    public StudentSub findStudentSub(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StudentSub.class, id);
        } finally {
            em.close();
        }
    }

    public int getStudentSubCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StudentSub> rt = cq.from(StudentSub.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
