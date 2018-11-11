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
import sample.DbTables.Department;
import sample.DbTables.Student;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sample.DbTables.SubDepartment;
import sample.DbTables.Subjects;
import sample.dbControllers.exceptions.IllegalOrphanException;
import sample.dbControllers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class SubDepartmentJpaController implements Serializable {

    public SubDepartmentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SubDepartment subDepartment) {
        if (subDepartment.getStudentList() == null) {
            subDepartment.setStudentList(new ArrayList<Student>());
        }
        if (subDepartment.getSubjectsList() == null) {
            subDepartment.setSubjectsList(new ArrayList<Subjects>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Department majorName = subDepartment.getMajorName();
            if (majorName != null) {
                majorName = em.getReference(majorName.getClass(), majorName.getId());
                subDepartment.setMajorName(majorName);
            }
            List<Student> attachedStudentList = new ArrayList<Student>();
            for (Student studentListStudentToAttach : subDepartment.getStudentList()) {
                studentListStudentToAttach = em.getReference(studentListStudentToAttach.getClass(), studentListStudentToAttach.getStudentId());
                attachedStudentList.add(studentListStudentToAttach);
            }
            subDepartment.setStudentList(attachedStudentList);
            List<Subjects> attachedSubjectsList = new ArrayList<Subjects>();
            for (Subjects subjectsListSubjectsToAttach : subDepartment.getSubjectsList()) {
                subjectsListSubjectsToAttach = em.getReference(subjectsListSubjectsToAttach.getClass(), subjectsListSubjectsToAttach.getId());
                attachedSubjectsList.add(subjectsListSubjectsToAttach);
            }
            subDepartment.setSubjectsList(attachedSubjectsList);
            em.persist(subDepartment);
            if (majorName != null) {
                majorName.getSubDepartmentList().add(subDepartment);
                majorName = em.merge(majorName);
            }
            for (Student studentListStudent : subDepartment.getStudentList()) {
                SubDepartment oldSubMajorOfStudentListStudent = studentListStudent.getSubMajor();
                studentListStudent.setSubMajor(subDepartment);
                studentListStudent = em.merge(studentListStudent);
                if (oldSubMajorOfStudentListStudent != null) {
                    oldSubMajorOfStudentListStudent.getStudentList().remove(studentListStudent);
                    oldSubMajorOfStudentListStudent = em.merge(oldSubMajorOfStudentListStudent);
                }
            }
            for (Subjects subjectsListSubjects : subDepartment.getSubjectsList()) {
                SubDepartment oldMajorOfSubjectsListSubjects = subjectsListSubjects.getMajor();
                subjectsListSubjects.setMajor(subDepartment);
                subjectsListSubjects = em.merge(subjectsListSubjects);
                if (oldMajorOfSubjectsListSubjects != null) {
                    oldMajorOfSubjectsListSubjects.getSubjectsList().remove(subjectsListSubjects);
                    oldMajorOfSubjectsListSubjects = em.merge(oldMajorOfSubjectsListSubjects);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SubDepartment subDepartment) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SubDepartment persistentSubDepartment = em.find(SubDepartment.class, subDepartment.getId());
            Department majorNameOld = persistentSubDepartment.getMajorName();
            Department majorNameNew = subDepartment.getMajorName();
            List<Student> studentListOld = persistentSubDepartment.getStudentList();
            List<Student> studentListNew = subDepartment.getStudentList();
            List<Subjects> subjectsListOld = persistentSubDepartment.getSubjectsList();
            List<Subjects> subjectsListNew = subDepartment.getSubjectsList();
            List<String> illegalOrphanMessages = null;
            for (Student studentListOldStudent : studentListOld) {
                if (!studentListNew.contains(studentListOldStudent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Student " + studentListOldStudent + " since its subMajor field is not nullable.");
                }
            }
            for (Subjects subjectsListOldSubjects : subjectsListOld) {
                if (!subjectsListNew.contains(subjectsListOldSubjects)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Subjects " + subjectsListOldSubjects + " since its major field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (majorNameNew != null) {
                majorNameNew = em.getReference(majorNameNew.getClass(), majorNameNew.getId());
                subDepartment.setMajorName(majorNameNew);
            }
            List<Student> attachedStudentListNew = new ArrayList<Student>();
            for (Student studentListNewStudentToAttach : studentListNew) {
                studentListNewStudentToAttach = em.getReference(studentListNewStudentToAttach.getClass(), studentListNewStudentToAttach.getStudentId());
                attachedStudentListNew.add(studentListNewStudentToAttach);
            }
            studentListNew = attachedStudentListNew;
            subDepartment.setStudentList(studentListNew);
            List<Subjects> attachedSubjectsListNew = new ArrayList<Subjects>();
            for (Subjects subjectsListNewSubjectsToAttach : subjectsListNew) {
                subjectsListNewSubjectsToAttach = em.getReference(subjectsListNewSubjectsToAttach.getClass(), subjectsListNewSubjectsToAttach.getId());
                attachedSubjectsListNew.add(subjectsListNewSubjectsToAttach);
            }
            subjectsListNew = attachedSubjectsListNew;
            subDepartment.setSubjectsList(subjectsListNew);
            subDepartment = em.merge(subDepartment);
            if (majorNameOld != null && !majorNameOld.equals(majorNameNew)) {
                majorNameOld.getSubDepartmentList().remove(subDepartment);
                majorNameOld = em.merge(majorNameOld);
            }
            if (majorNameNew != null && !majorNameNew.equals(majorNameOld)) {
                majorNameNew.getSubDepartmentList().add(subDepartment);
                majorNameNew = em.merge(majorNameNew);
            }
            for (Student studentListNewStudent : studentListNew) {
                if (!studentListOld.contains(studentListNewStudent)) {
                    SubDepartment oldSubMajorOfStudentListNewStudent = studentListNewStudent.getSubMajor();
                    studentListNewStudent.setSubMajor(subDepartment);
                    studentListNewStudent = em.merge(studentListNewStudent);
                    if (oldSubMajorOfStudentListNewStudent != null && !oldSubMajorOfStudentListNewStudent.equals(subDepartment)) {
                        oldSubMajorOfStudentListNewStudent.getStudentList().remove(studentListNewStudent);
                        oldSubMajorOfStudentListNewStudent = em.merge(oldSubMajorOfStudentListNewStudent);
                    }
                }
            }
            for (Subjects subjectsListNewSubjects : subjectsListNew) {
                if (!subjectsListOld.contains(subjectsListNewSubjects)) {
                    SubDepartment oldMajorOfSubjectsListNewSubjects = subjectsListNewSubjects.getMajor();
                    subjectsListNewSubjects.setMajor(subDepartment);
                    subjectsListNewSubjects = em.merge(subjectsListNewSubjects);
                    if (oldMajorOfSubjectsListNewSubjects != null && !oldMajorOfSubjectsListNewSubjects.equals(subDepartment)) {
                        oldMajorOfSubjectsListNewSubjects.getSubjectsList().remove(subjectsListNewSubjects);
                        oldMajorOfSubjectsListNewSubjects = em.merge(oldMajorOfSubjectsListNewSubjects);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = subDepartment.getId();
                if (findSubDepartment(id) == null) {
                    throw new NonexistentEntityException("The subDepartment with id " + id + " no longer exists.");
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
            SubDepartment subDepartment;
            try {
                subDepartment = em.getReference(SubDepartment.class, id);
                subDepartment.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subDepartment with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Student> studentListOrphanCheck = subDepartment.getStudentList();
            for (Student studentListOrphanCheckStudent : studentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SubDepartment (" + subDepartment + ") cannot be destroyed since the Student " + studentListOrphanCheckStudent + " in its studentList field has a non-nullable subMajor field.");
            }
            List<Subjects> subjectsListOrphanCheck = subDepartment.getSubjectsList();
            for (Subjects subjectsListOrphanCheckSubjects : subjectsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SubDepartment (" + subDepartment + ") cannot be destroyed since the Subjects " + subjectsListOrphanCheckSubjects + " in its subjectsList field has a non-nullable major field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Department majorName = subDepartment.getMajorName();
            if (majorName != null) {
                majorName.getSubDepartmentList().remove(subDepartment);
                majorName = em.merge(majorName);
            }
            em.remove(subDepartment);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SubDepartment> findSubDepartmentEntities() {
        return findSubDepartmentEntities(true, -1, -1);
    }

    public List<SubDepartment> findSubDepartmentEntities(int maxResults, int firstResult) {
        return findSubDepartmentEntities(false, maxResults, firstResult);
    }

    private List<SubDepartment> findSubDepartmentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SubDepartment.class));
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

    public SubDepartment findSubDepartment(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SubDepartment.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubDepartmentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SubDepartment> rt = cq.from(SubDepartment.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
