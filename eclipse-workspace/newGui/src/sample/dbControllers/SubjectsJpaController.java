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
import sample.DbTables.StudentSub;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sample.DbTables.InstructorSub;
import sample.DbTables.Prerequisites;
import sample.DbTables.Subjects;
import sample.dbControllers.exceptions.IllegalOrphanException;
import sample.dbControllers.exceptions.NonexistentEntityException;
import sample.dbControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author monder
 */
public class SubjectsJpaController implements Serializable {

    public SubjectsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Subjects subjects) throws PreexistingEntityException, Exception {
        if (subjects.getStudentSubList() == null) {
            subjects.setStudentSubList(new ArrayList<StudentSub>());
        }
        if (subjects.getInstructorSubList() == null) {
            subjects.setInstructorSubList(new ArrayList<InstructorSub>());
        }
        if (subjects.getPrerequisitesList() == null) {
            subjects.setPrerequisitesList(new ArrayList<Prerequisites>());
        }
        if (subjects.getPrerequisitesList1() == null) {
            subjects.setPrerequisitesList1(new ArrayList<Prerequisites>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SubDepartment major = subjects.getMajor();
            if (major != null) {
                major = em.getReference(major.getClass(), major.getId());
                subjects.setMajor(major);
            }
            List<StudentSub> attachedStudentSubList = new ArrayList<StudentSub>();
            for (StudentSub studentSubListStudentSubToAttach : subjects.getStudentSubList()) {
                studentSubListStudentSubToAttach = em.getReference(studentSubListStudentSubToAttach.getClass(), studentSubListStudentSubToAttach.getId());
                attachedStudentSubList.add(studentSubListStudentSubToAttach);
            }
            subjects.setStudentSubList(attachedStudentSubList);
            List<InstructorSub> attachedInstructorSubList = new ArrayList<InstructorSub>();
            for (InstructorSub instructorSubListInstructorSubToAttach : subjects.getInstructorSubList()) {
                instructorSubListInstructorSubToAttach = em.getReference(instructorSubListInstructorSubToAttach.getClass(), instructorSubListInstructorSubToAttach.getId());
                attachedInstructorSubList.add(instructorSubListInstructorSubToAttach);
            }
            subjects.setInstructorSubList(attachedInstructorSubList);
            List<Prerequisites> attachedPrerequisitesList = new ArrayList<Prerequisites>();
            for (Prerequisites prerequisitesListPrerequisitesToAttach : subjects.getPrerequisitesList()) {
                prerequisitesListPrerequisitesToAttach = em.getReference(prerequisitesListPrerequisitesToAttach.getClass(), prerequisitesListPrerequisitesToAttach.getId());
                attachedPrerequisitesList.add(prerequisitesListPrerequisitesToAttach);
            }
            subjects.setPrerequisitesList(attachedPrerequisitesList);
            List<Prerequisites> attachedPrerequisitesList1 = new ArrayList<Prerequisites>();
            for (Prerequisites prerequisitesList1PrerequisitesToAttach : subjects.getPrerequisitesList1()) {
                prerequisitesList1PrerequisitesToAttach = em.getReference(prerequisitesList1PrerequisitesToAttach.getClass(), prerequisitesList1PrerequisitesToAttach.getId());
                attachedPrerequisitesList1.add(prerequisitesList1PrerequisitesToAttach);
            }
            subjects.setPrerequisitesList1(attachedPrerequisitesList1);
            em.persist(subjects);
            if (major != null) {
                major.getSubjectsList().add(subjects);
                major = em.merge(major);
            }
            for (StudentSub studentSubListStudentSub : subjects.getStudentSubList()) {
                Subjects oldSubjectIdOfStudentSubListStudentSub = studentSubListStudentSub.getSubjectId();
                studentSubListStudentSub.setSubjectId(subjects);
                studentSubListStudentSub = em.merge(studentSubListStudentSub);
                if (oldSubjectIdOfStudentSubListStudentSub != null) {
                    oldSubjectIdOfStudentSubListStudentSub.getStudentSubList().remove(studentSubListStudentSub);
                    oldSubjectIdOfStudentSubListStudentSub = em.merge(oldSubjectIdOfStudentSubListStudentSub);
                }
            }
            for (InstructorSub instructorSubListInstructorSub : subjects.getInstructorSubList()) {
                Subjects oldSubjectIdOfInstructorSubListInstructorSub = instructorSubListInstructorSub.getSubjectId();
                instructorSubListInstructorSub.setSubjectId(subjects);
                instructorSubListInstructorSub = em.merge(instructorSubListInstructorSub);
                if (oldSubjectIdOfInstructorSubListInstructorSub != null) {
                    oldSubjectIdOfInstructorSubListInstructorSub.getInstructorSubList().remove(instructorSubListInstructorSub);
                    oldSubjectIdOfInstructorSubListInstructorSub = em.merge(oldSubjectIdOfInstructorSubListInstructorSub);
                }
            }
            for (Prerequisites prerequisitesListPrerequisites : subjects.getPrerequisitesList()) {
                Subjects oldSubjectIdOfPrerequisitesListPrerequisites = prerequisitesListPrerequisites.getSubjectId();
                prerequisitesListPrerequisites.setSubjectId(subjects);
                prerequisitesListPrerequisites = em.merge(prerequisitesListPrerequisites);
                if (oldSubjectIdOfPrerequisitesListPrerequisites != null) {
                    oldSubjectIdOfPrerequisitesListPrerequisites.getPrerequisitesList().remove(prerequisitesListPrerequisites);
                    oldSubjectIdOfPrerequisitesListPrerequisites = em.merge(oldSubjectIdOfPrerequisitesListPrerequisites);
                }
            }
            for (Prerequisites prerequisitesList1Prerequisites : subjects.getPrerequisitesList1()) {
                Subjects oldPrerequisiteOfPrerequisitesList1Prerequisites = prerequisitesList1Prerequisites.getPrerequisite();
                prerequisitesList1Prerequisites.setPrerequisite(subjects);
                prerequisitesList1Prerequisites = em.merge(prerequisitesList1Prerequisites);
                if (oldPrerequisiteOfPrerequisitesList1Prerequisites != null) {
                    oldPrerequisiteOfPrerequisitesList1Prerequisites.getPrerequisitesList1().remove(prerequisitesList1Prerequisites);
                    oldPrerequisiteOfPrerequisitesList1Prerequisites = em.merge(oldPrerequisiteOfPrerequisitesList1Prerequisites);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSubjects(subjects.getId()) != null) {
                throw new PreexistingEntityException("Subjects " + subjects + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Subjects subjects) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subjects persistentSubjects = em.find(Subjects.class, subjects.getId());
            SubDepartment majorOld = persistentSubjects.getMajor();
            SubDepartment majorNew = subjects.getMajor();
            List<StudentSub> studentSubListOld = persistentSubjects.getStudentSubList();
            List<StudentSub> studentSubListNew = subjects.getStudentSubList();
            List<InstructorSub> instructorSubListOld = persistentSubjects.getInstructorSubList();
            List<InstructorSub> instructorSubListNew = subjects.getInstructorSubList();
            List<Prerequisites> prerequisitesListOld = persistentSubjects.getPrerequisitesList();
            List<Prerequisites> prerequisitesListNew = subjects.getPrerequisitesList();
            List<Prerequisites> prerequisitesList1Old = persistentSubjects.getPrerequisitesList1();
            List<Prerequisites> prerequisitesList1New = subjects.getPrerequisitesList1();
            List<String> illegalOrphanMessages = null;
            for (StudentSub studentSubListOldStudentSub : studentSubListOld) {
                if (!studentSubListNew.contains(studentSubListOldStudentSub)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain StudentSub " + studentSubListOldStudentSub + " since its subjectId field is not nullable.");
                }
            }
            for (InstructorSub instructorSubListOldInstructorSub : instructorSubListOld) {
                if (!instructorSubListNew.contains(instructorSubListOldInstructorSub)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain InstructorSub " + instructorSubListOldInstructorSub + " since its subjectId field is not nullable.");
                }
            }
            for (Prerequisites prerequisitesListOldPrerequisites : prerequisitesListOld) {
                if (!prerequisitesListNew.contains(prerequisitesListOldPrerequisites)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Prerequisites " + prerequisitesListOldPrerequisites + " since its subjectId field is not nullable.");
                }
            }
            for (Prerequisites prerequisitesList1OldPrerequisites : prerequisitesList1Old) {
                if (!prerequisitesList1New.contains(prerequisitesList1OldPrerequisites)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Prerequisites " + prerequisitesList1OldPrerequisites + " since its prerequisite field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (majorNew != null) {
                majorNew = em.getReference(majorNew.getClass(), majorNew.getId());
                subjects.setMajor(majorNew);
            }
            List<StudentSub> attachedStudentSubListNew = new ArrayList<StudentSub>();
            for (StudentSub studentSubListNewStudentSubToAttach : studentSubListNew) {
                studentSubListNewStudentSubToAttach = em.getReference(studentSubListNewStudentSubToAttach.getClass(), studentSubListNewStudentSubToAttach.getId());
                attachedStudentSubListNew.add(studentSubListNewStudentSubToAttach);
            }
            studentSubListNew = attachedStudentSubListNew;
            subjects.setStudentSubList(studentSubListNew);
            List<InstructorSub> attachedInstructorSubListNew = new ArrayList<InstructorSub>();
            for (InstructorSub instructorSubListNewInstructorSubToAttach : instructorSubListNew) {
                instructorSubListNewInstructorSubToAttach = em.getReference(instructorSubListNewInstructorSubToAttach.getClass(), instructorSubListNewInstructorSubToAttach.getId());
                attachedInstructorSubListNew.add(instructorSubListNewInstructorSubToAttach);
            }
            instructorSubListNew = attachedInstructorSubListNew;
            subjects.setInstructorSubList(instructorSubListNew);
            List<Prerequisites> attachedPrerequisitesListNew = new ArrayList<Prerequisites>();
            for (Prerequisites prerequisitesListNewPrerequisitesToAttach : prerequisitesListNew) {
                prerequisitesListNewPrerequisitesToAttach = em.getReference(prerequisitesListNewPrerequisitesToAttach.getClass(), prerequisitesListNewPrerequisitesToAttach.getId());
                attachedPrerequisitesListNew.add(prerequisitesListNewPrerequisitesToAttach);
            }
            prerequisitesListNew = attachedPrerequisitesListNew;
            subjects.setPrerequisitesList(prerequisitesListNew);
            List<Prerequisites> attachedPrerequisitesList1New = new ArrayList<Prerequisites>();
            for (Prerequisites prerequisitesList1NewPrerequisitesToAttach : prerequisitesList1New) {
                prerequisitesList1NewPrerequisitesToAttach = em.getReference(prerequisitesList1NewPrerequisitesToAttach.getClass(), prerequisitesList1NewPrerequisitesToAttach.getId());
                attachedPrerequisitesList1New.add(prerequisitesList1NewPrerequisitesToAttach);
            }
            prerequisitesList1New = attachedPrerequisitesList1New;
            subjects.setPrerequisitesList1(prerequisitesList1New);
            subjects = em.merge(subjects);
            if (majorOld != null && !majorOld.equals(majorNew)) {
                majorOld.getSubjectsList().remove(subjects);
                majorOld = em.merge(majorOld);
            }
            if (majorNew != null && !majorNew.equals(majorOld)) {
                majorNew.getSubjectsList().add(subjects);
                majorNew = em.merge(majorNew);
            }
            for (StudentSub studentSubListNewStudentSub : studentSubListNew) {
                if (!studentSubListOld.contains(studentSubListNewStudentSub)) {
                    Subjects oldSubjectIdOfStudentSubListNewStudentSub = studentSubListNewStudentSub.getSubjectId();
                    studentSubListNewStudentSub.setSubjectId(subjects);
                    studentSubListNewStudentSub = em.merge(studentSubListNewStudentSub);
                    if (oldSubjectIdOfStudentSubListNewStudentSub != null && !oldSubjectIdOfStudentSubListNewStudentSub.equals(subjects)) {
                        oldSubjectIdOfStudentSubListNewStudentSub.getStudentSubList().remove(studentSubListNewStudentSub);
                        oldSubjectIdOfStudentSubListNewStudentSub = em.merge(oldSubjectIdOfStudentSubListNewStudentSub);
                    }
                }
            }
            for (InstructorSub instructorSubListNewInstructorSub : instructorSubListNew) {
                if (!instructorSubListOld.contains(instructorSubListNewInstructorSub)) {
                    Subjects oldSubjectIdOfInstructorSubListNewInstructorSub = instructorSubListNewInstructorSub.getSubjectId();
                    instructorSubListNewInstructorSub.setSubjectId(subjects);
                    instructorSubListNewInstructorSub = em.merge(instructorSubListNewInstructorSub);
                    if (oldSubjectIdOfInstructorSubListNewInstructorSub != null && !oldSubjectIdOfInstructorSubListNewInstructorSub.equals(subjects)) {
                        oldSubjectIdOfInstructorSubListNewInstructorSub.getInstructorSubList().remove(instructorSubListNewInstructorSub);
                        oldSubjectIdOfInstructorSubListNewInstructorSub = em.merge(oldSubjectIdOfInstructorSubListNewInstructorSub);
                    }
                }
            }
            for (Prerequisites prerequisitesListNewPrerequisites : prerequisitesListNew) {
                if (!prerequisitesListOld.contains(prerequisitesListNewPrerequisites)) {
                    Subjects oldSubjectIdOfPrerequisitesListNewPrerequisites = prerequisitesListNewPrerequisites.getSubjectId();
                    prerequisitesListNewPrerequisites.setSubjectId(subjects);
                    prerequisitesListNewPrerequisites = em.merge(prerequisitesListNewPrerequisites);
                    if (oldSubjectIdOfPrerequisitesListNewPrerequisites != null && !oldSubjectIdOfPrerequisitesListNewPrerequisites.equals(subjects)) {
                        oldSubjectIdOfPrerequisitesListNewPrerequisites.getPrerequisitesList().remove(prerequisitesListNewPrerequisites);
                        oldSubjectIdOfPrerequisitesListNewPrerequisites = em.merge(oldSubjectIdOfPrerequisitesListNewPrerequisites);
                    }
                }
            }
            for (Prerequisites prerequisitesList1NewPrerequisites : prerequisitesList1New) {
                if (!prerequisitesList1Old.contains(prerequisitesList1NewPrerequisites)) {
                    Subjects oldPrerequisiteOfPrerequisitesList1NewPrerequisites = prerequisitesList1NewPrerequisites.getPrerequisite();
                    prerequisitesList1NewPrerequisites.setPrerequisite(subjects);
                    prerequisitesList1NewPrerequisites = em.merge(prerequisitesList1NewPrerequisites);
                    if (oldPrerequisiteOfPrerequisitesList1NewPrerequisites != null && !oldPrerequisiteOfPrerequisitesList1NewPrerequisites.equals(subjects)) {
                        oldPrerequisiteOfPrerequisitesList1NewPrerequisites.getPrerequisitesList1().remove(prerequisitesList1NewPrerequisites);
                        oldPrerequisiteOfPrerequisitesList1NewPrerequisites = em.merge(oldPrerequisiteOfPrerequisitesList1NewPrerequisites);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = subjects.getId();
                if (findSubjects(id) == null) {
                    throw new NonexistentEntityException("The subjects with id " + id + " no longer exists.");
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
            Subjects subjects;
            try {
                subjects = em.getReference(Subjects.class, id);
                subjects.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subjects with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<StudentSub> studentSubListOrphanCheck = subjects.getStudentSubList();
            for (StudentSub studentSubListOrphanCheckStudentSub : studentSubListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Subjects (" + subjects + ") cannot be destroyed since the StudentSub " + studentSubListOrphanCheckStudentSub + " in its studentSubList field has a non-nullable subjectId field.");
            }
            List<InstructorSub> instructorSubListOrphanCheck = subjects.getInstructorSubList();
            for (InstructorSub instructorSubListOrphanCheckInstructorSub : instructorSubListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Subjects (" + subjects + ") cannot be destroyed since the InstructorSub " + instructorSubListOrphanCheckInstructorSub + " in its instructorSubList field has a non-nullable subjectId field.");
            }
            List<Prerequisites> prerequisitesListOrphanCheck = subjects.getPrerequisitesList();
            for (Prerequisites prerequisitesListOrphanCheckPrerequisites : prerequisitesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Subjects (" + subjects + ") cannot be destroyed since the Prerequisites " + prerequisitesListOrphanCheckPrerequisites + " in its prerequisitesList field has a non-nullable subjectId field.");
            }
            List<Prerequisites> prerequisitesList1OrphanCheck = subjects.getPrerequisitesList1();
            for (Prerequisites prerequisitesList1OrphanCheckPrerequisites : prerequisitesList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Subjects (" + subjects + ") cannot be destroyed since the Prerequisites " + prerequisitesList1OrphanCheckPrerequisites + " in its prerequisitesList1 field has a non-nullable prerequisite field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            SubDepartment major = subjects.getMajor();
            if (major != null) {
                major.getSubjectsList().remove(subjects);
                major = em.merge(major);
            }
            em.remove(subjects);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Subjects> findSubjectsEntities() {
        return findSubjectsEntities(true, -1, -1);
    }

    public List<Subjects> findSubjectsEntities(int maxResults, int firstResult) {
        return findSubjectsEntities(false, maxResults, firstResult);
    }

    private List<Subjects> findSubjectsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Subjects.class));
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

    public Subjects findSubjects(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Subjects.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubjectsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Subjects> rt = cq.from(Subjects.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
