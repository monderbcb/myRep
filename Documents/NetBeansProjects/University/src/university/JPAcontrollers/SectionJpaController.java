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
import university.Entites.SubDepartment;
import university.Entites.Subject;
import university.Entites.Student;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import university.Entites.Section;
import university.JPAcontrollers.exceptions.IllegalOrphanException;
import university.JPAcontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class SectionJpaController implements Serializable {

    public SectionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Section section) {
        if (section.getStudentList() == null) {
            section.setStudentList(new ArrayList<Student>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SubDepartment subDepartmentId = section.getSubDepartmentId();
            if (subDepartmentId != null) {
                subDepartmentId = em.getReference(subDepartmentId.getClass(), subDepartmentId.getId());
                section.setSubDepartmentId(subDepartmentId);
            }
            Subject subjectId = section.getSubjectId();
            if (subjectId != null) {
                subjectId = em.getReference(subjectId.getClass(), subjectId.getId());
                section.setSubjectId(subjectId);
            }
            List<Student> attachedStudentList = new ArrayList<Student>();
            for (Student studentListStudentToAttach : section.getStudentList()) {
                studentListStudentToAttach = em.getReference(studentListStudentToAttach.getClass(), studentListStudentToAttach.getId());
                attachedStudentList.add(studentListStudentToAttach);
            }
            section.setStudentList(attachedStudentList);
            em.persist(section);
            if (subDepartmentId != null) {
                subDepartmentId.getSectionList().add(section);
                subDepartmentId = em.merge(subDepartmentId);
            }
            if (subjectId != null) {
                subjectId.getSectionList().add(section);
                subjectId = em.merge(subjectId);
            }
            for (Student studentListStudent : section.getStudentList()) {
                Section oldSectionOfStudentListStudent = studentListStudent.getSection();
                studentListStudent.setSection(section);
                studentListStudent = em.merge(studentListStudent);
                if (oldSectionOfStudentListStudent != null) {
                    oldSectionOfStudentListStudent.getStudentList().remove(studentListStudent);
                    oldSectionOfStudentListStudent = em.merge(oldSectionOfStudentListStudent);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Section section) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Section persistentSection = em.find(Section.class, section.getId());
            SubDepartment subDepartmentIdOld = persistentSection.getSubDepartmentId();
            SubDepartment subDepartmentIdNew = section.getSubDepartmentId();
            Subject subjectIdOld = persistentSection.getSubjectId();
            Subject subjectIdNew = section.getSubjectId();
            List<Student> studentListOld = persistentSection.getStudentList();
            List<Student> studentListNew = section.getStudentList();
            List<String> illegalOrphanMessages = null;
            for (Student studentListOldStudent : studentListOld) {
                if (!studentListNew.contains(studentListOldStudent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Student " + studentListOldStudent + " since its section field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (subDepartmentIdNew != null) {
                subDepartmentIdNew = em.getReference(subDepartmentIdNew.getClass(), subDepartmentIdNew.getId());
                section.setSubDepartmentId(subDepartmentIdNew);
            }
            if (subjectIdNew != null) {
                subjectIdNew = em.getReference(subjectIdNew.getClass(), subjectIdNew.getId());
                section.setSubjectId(subjectIdNew);
            }
            List<Student> attachedStudentListNew = new ArrayList<Student>();
            for (Student studentListNewStudentToAttach : studentListNew) {
                studentListNewStudentToAttach = em.getReference(studentListNewStudentToAttach.getClass(), studentListNewStudentToAttach.getId());
                attachedStudentListNew.add(studentListNewStudentToAttach);
            }
            studentListNew = attachedStudentListNew;
            section.setStudentList(studentListNew);
            section = em.merge(section);
            if (subDepartmentIdOld != null && !subDepartmentIdOld.equals(subDepartmentIdNew)) {
                subDepartmentIdOld.getSectionList().remove(section);
                subDepartmentIdOld = em.merge(subDepartmentIdOld);
            }
            if (subDepartmentIdNew != null && !subDepartmentIdNew.equals(subDepartmentIdOld)) {
                subDepartmentIdNew.getSectionList().add(section);
                subDepartmentIdNew = em.merge(subDepartmentIdNew);
            }
            if (subjectIdOld != null && !subjectIdOld.equals(subjectIdNew)) {
                subjectIdOld.getSectionList().remove(section);
                subjectIdOld = em.merge(subjectIdOld);
            }
            if (subjectIdNew != null && !subjectIdNew.equals(subjectIdOld)) {
                subjectIdNew.getSectionList().add(section);
                subjectIdNew = em.merge(subjectIdNew);
            }
            for (Student studentListNewStudent : studentListNew) {
                if (!studentListOld.contains(studentListNewStudent)) {
                    Section oldSectionOfStudentListNewStudent = studentListNewStudent.getSection();
                    studentListNewStudent.setSection(section);
                    studentListNewStudent = em.merge(studentListNewStudent);
                    if (oldSectionOfStudentListNewStudent != null && !oldSectionOfStudentListNewStudent.equals(section)) {
                        oldSectionOfStudentListNewStudent.getStudentList().remove(studentListNewStudent);
                        oldSectionOfStudentListNewStudent = em.merge(oldSectionOfStudentListNewStudent);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = section.getId();
                if (findSection(id) == null) {
                    throw new NonexistentEntityException("The section with id " + id + " no longer exists.");
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
            Section section;
            try {
                section = em.getReference(Section.class, id);
                section.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The section with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Student> studentListOrphanCheck = section.getStudentList();
            for (Student studentListOrphanCheckStudent : studentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Section (" + section + ") cannot be destroyed since the Student " + studentListOrphanCheckStudent + " in its studentList field has a non-nullable section field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            SubDepartment subDepartmentId = section.getSubDepartmentId();
            if (subDepartmentId != null) {
                subDepartmentId.getSectionList().remove(section);
                subDepartmentId = em.merge(subDepartmentId);
            }
            Subject subjectId = section.getSubjectId();
            if (subjectId != null) {
                subjectId.getSectionList().remove(section);
                subjectId = em.merge(subjectId);
            }
            em.remove(section);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Section> findSectionEntities() {
        return findSectionEntities(true, -1, -1);
    }

    public List<Section> findSectionEntities(int maxResults, int firstResult) {
        return findSectionEntities(false, maxResults, firstResult);
    }

    private List<Section> findSectionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Section.class));
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

    public Section findSection(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Section.class, id);
        } finally {
            em.close();
        }
    }

    public int getSectionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Section> rt = cq.from(Section.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
