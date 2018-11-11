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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sample.DbTables.StudentSub;
import sample.DbTables.InstructorSub;
import sample.DbTables.Instructors;
import sample.DbTables.Grade;
import sample.DbTables.Semester;
import sample.dbControllers.exceptions.IllegalOrphanException;
import sample.dbControllers.exceptions.NonexistentEntityException;

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
        if (semester.getStudentSubList() == null) {
            semester.setStudentSubList(new ArrayList<StudentSub>());
        }
        if (semester.getInstructorSubList() == null) {
            semester.setInstructorSubList(new ArrayList<InstructorSub>());
        }
        if (semester.getInstructorsList() == null) {
            semester.setInstructorsList(new ArrayList<Instructors>());
        }
        if (semester.getGradeList() == null) {
            semester.setGradeList(new ArrayList<Grade>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Student> attachedStudentList = new ArrayList<Student>();
            for (Student studentListStudentToAttach : semester.getStudentList()) {
                studentListStudentToAttach = em.getReference(studentListStudentToAttach.getClass(), studentListStudentToAttach.getStudentId());
                attachedStudentList.add(studentListStudentToAttach);
            }
            semester.setStudentList(attachedStudentList);
            List<StudentSub> attachedStudentSubList = new ArrayList<StudentSub>();
            for (StudentSub studentSubListStudentSubToAttach : semester.getStudentSubList()) {
                studentSubListStudentSubToAttach = em.getReference(studentSubListStudentSubToAttach.getClass(), studentSubListStudentSubToAttach.getId());
                attachedStudentSubList.add(studentSubListStudentSubToAttach);
            }
            semester.setStudentSubList(attachedStudentSubList);
            List<InstructorSub> attachedInstructorSubList = new ArrayList<InstructorSub>();
            for (InstructorSub instructorSubListInstructorSubToAttach : semester.getInstructorSubList()) {
                instructorSubListInstructorSubToAttach = em.getReference(instructorSubListInstructorSubToAttach.getClass(), instructorSubListInstructorSubToAttach.getId());
                attachedInstructorSubList.add(instructorSubListInstructorSubToAttach);
            }
            semester.setInstructorSubList(attachedInstructorSubList);
            List<Instructors> attachedInstructorsList = new ArrayList<Instructors>();
            for (Instructors instructorsListInstructorsToAttach : semester.getInstructorsList()) {
                instructorsListInstructorsToAttach = em.getReference(instructorsListInstructorsToAttach.getClass(), instructorsListInstructorsToAttach.getId());
                attachedInstructorsList.add(instructorsListInstructorsToAttach);
            }
            semester.setInstructorsList(attachedInstructorsList);
            List<Grade> attachedGradeList = new ArrayList<Grade>();
            for (Grade gradeListGradeToAttach : semester.getGradeList()) {
                gradeListGradeToAttach = em.getReference(gradeListGradeToAttach.getClass(), gradeListGradeToAttach.getId());
                attachedGradeList.add(gradeListGradeToAttach);
            }
            semester.setGradeList(attachedGradeList);
            em.persist(semester);
            for (Student studentListStudent : semester.getStudentList()) {
                Semester oldSemsterIdOfStudentListStudent = studentListStudent.getSemsterId();
                studentListStudent.setSemsterId(semester);
                studentListStudent = em.merge(studentListStudent);
                if (oldSemsterIdOfStudentListStudent != null) {
                    oldSemsterIdOfStudentListStudent.getStudentList().remove(studentListStudent);
                    oldSemsterIdOfStudentListStudent = em.merge(oldSemsterIdOfStudentListStudent);
                }
            }
            for (StudentSub studentSubListStudentSub : semester.getStudentSubList()) {
                Semester oldSemesterIdOfStudentSubListStudentSub = studentSubListStudentSub.getSemesterId();
                studentSubListStudentSub.setSemesterId(semester);
                studentSubListStudentSub = em.merge(studentSubListStudentSub);
                if (oldSemesterIdOfStudentSubListStudentSub != null) {
                    oldSemesterIdOfStudentSubListStudentSub.getStudentSubList().remove(studentSubListStudentSub);
                    oldSemesterIdOfStudentSubListStudentSub = em.merge(oldSemesterIdOfStudentSubListStudentSub);
                }
            }
            for (InstructorSub instructorSubListInstructorSub : semester.getInstructorSubList()) {
                Semester oldSemesterIdOfInstructorSubListInstructorSub = instructorSubListInstructorSub.getSemesterId();
                instructorSubListInstructorSub.setSemesterId(semester);
                instructorSubListInstructorSub = em.merge(instructorSubListInstructorSub);
                if (oldSemesterIdOfInstructorSubListInstructorSub != null) {
                    oldSemesterIdOfInstructorSubListInstructorSub.getInstructorSubList().remove(instructorSubListInstructorSub);
                    oldSemesterIdOfInstructorSubListInstructorSub = em.merge(oldSemesterIdOfInstructorSubListInstructorSub);
                }
            }
            for (Instructors instructorsListInstructors : semester.getInstructorsList()) {
                Semester oldSemesterIdOfInstructorsListInstructors = instructorsListInstructors.getSemesterId();
                instructorsListInstructors.setSemesterId(semester);
                instructorsListInstructors = em.merge(instructorsListInstructors);
                if (oldSemesterIdOfInstructorsListInstructors != null) {
                    oldSemesterIdOfInstructorsListInstructors.getInstructorsList().remove(instructorsListInstructors);
                    oldSemesterIdOfInstructorsListInstructors = em.merge(oldSemesterIdOfInstructorsListInstructors);
                }
            }
            for (Grade gradeListGrade : semester.getGradeList()) {
                Semester oldSemesterIdOfGradeListGrade = gradeListGrade.getSemesterId();
                gradeListGrade.setSemesterId(semester);
                gradeListGrade = em.merge(gradeListGrade);
                if (oldSemesterIdOfGradeListGrade != null) {
                    oldSemesterIdOfGradeListGrade.getGradeList().remove(gradeListGrade);
                    oldSemesterIdOfGradeListGrade = em.merge(oldSemesterIdOfGradeListGrade);
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
            List<StudentSub> studentSubListOld = persistentSemester.getStudentSubList();
            List<StudentSub> studentSubListNew = semester.getStudentSubList();
            List<InstructorSub> instructorSubListOld = persistentSemester.getInstructorSubList();
            List<InstructorSub> instructorSubListNew = semester.getInstructorSubList();
            List<Instructors> instructorsListOld = persistentSemester.getInstructorsList();
            List<Instructors> instructorsListNew = semester.getInstructorsList();
            List<Grade> gradeListOld = persistentSemester.getGradeList();
            List<Grade> gradeListNew = semester.getGradeList();
            List<String> illegalOrphanMessages = null;
            for (Student studentListOldStudent : studentListOld) {
                if (!studentListNew.contains(studentListOldStudent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Student " + studentListOldStudent + " since its semsterId field is not nullable.");
                }
            }
            for (StudentSub studentSubListOldStudentSub : studentSubListOld) {
                if (!studentSubListNew.contains(studentSubListOldStudentSub)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain StudentSub " + studentSubListOldStudentSub + " since its semesterId field is not nullable.");
                }
            }
            for (InstructorSub instructorSubListOldInstructorSub : instructorSubListOld) {
                if (!instructorSubListNew.contains(instructorSubListOldInstructorSub)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain InstructorSub " + instructorSubListOldInstructorSub + " since its semesterId field is not nullable.");
                }
            }
            for (Instructors instructorsListOldInstructors : instructorsListOld) {
                if (!instructorsListNew.contains(instructorsListOldInstructors)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Instructors " + instructorsListOldInstructors + " since its semesterId field is not nullable.");
                }
            }
            for (Grade gradeListOldGrade : gradeListOld) {
                if (!gradeListNew.contains(gradeListOldGrade)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Grade " + gradeListOldGrade + " since its semesterId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Student> attachedStudentListNew = new ArrayList<Student>();
            for (Student studentListNewStudentToAttach : studentListNew) {
                studentListNewStudentToAttach = em.getReference(studentListNewStudentToAttach.getClass(), studentListNewStudentToAttach.getStudentId());
                attachedStudentListNew.add(studentListNewStudentToAttach);
            }
            studentListNew = attachedStudentListNew;
            semester.setStudentList(studentListNew);
            List<StudentSub> attachedStudentSubListNew = new ArrayList<StudentSub>();
            for (StudentSub studentSubListNewStudentSubToAttach : studentSubListNew) {
                studentSubListNewStudentSubToAttach = em.getReference(studentSubListNewStudentSubToAttach.getClass(), studentSubListNewStudentSubToAttach.getId());
                attachedStudentSubListNew.add(studentSubListNewStudentSubToAttach);
            }
            studentSubListNew = attachedStudentSubListNew;
            semester.setStudentSubList(studentSubListNew);
            List<InstructorSub> attachedInstructorSubListNew = new ArrayList<InstructorSub>();
            for (InstructorSub instructorSubListNewInstructorSubToAttach : instructorSubListNew) {
                instructorSubListNewInstructorSubToAttach = em.getReference(instructorSubListNewInstructorSubToAttach.getClass(), instructorSubListNewInstructorSubToAttach.getId());
                attachedInstructorSubListNew.add(instructorSubListNewInstructorSubToAttach);
            }
            instructorSubListNew = attachedInstructorSubListNew;
            semester.setInstructorSubList(instructorSubListNew);
            List<Instructors> attachedInstructorsListNew = new ArrayList<Instructors>();
            for (Instructors instructorsListNewInstructorsToAttach : instructorsListNew) {
                instructorsListNewInstructorsToAttach = em.getReference(instructorsListNewInstructorsToAttach.getClass(), instructorsListNewInstructorsToAttach.getId());
                attachedInstructorsListNew.add(instructorsListNewInstructorsToAttach);
            }
            instructorsListNew = attachedInstructorsListNew;
            semester.setInstructorsList(instructorsListNew);
            List<Grade> attachedGradeListNew = new ArrayList<Grade>();
            for (Grade gradeListNewGradeToAttach : gradeListNew) {
                gradeListNewGradeToAttach = em.getReference(gradeListNewGradeToAttach.getClass(), gradeListNewGradeToAttach.getId());
                attachedGradeListNew.add(gradeListNewGradeToAttach);
            }
            gradeListNew = attachedGradeListNew;
            semester.setGradeList(gradeListNew);
            semester = em.merge(semester);
            for (Student studentListNewStudent : studentListNew) {
                if (!studentListOld.contains(studentListNewStudent)) {
                    Semester oldSemsterIdOfStudentListNewStudent = studentListNewStudent.getSemsterId();
                    studentListNewStudent.setSemsterId(semester);
                    studentListNewStudent = em.merge(studentListNewStudent);
                    if (oldSemsterIdOfStudentListNewStudent != null && !oldSemsterIdOfStudentListNewStudent.equals(semester)) {
                        oldSemsterIdOfStudentListNewStudent.getStudentList().remove(studentListNewStudent);
                        oldSemsterIdOfStudentListNewStudent = em.merge(oldSemsterIdOfStudentListNewStudent);
                    }
                }
            }
            for (StudentSub studentSubListNewStudentSub : studentSubListNew) {
                if (!studentSubListOld.contains(studentSubListNewStudentSub)) {
                    Semester oldSemesterIdOfStudentSubListNewStudentSub = studentSubListNewStudentSub.getSemesterId();
                    studentSubListNewStudentSub.setSemesterId(semester);
                    studentSubListNewStudentSub = em.merge(studentSubListNewStudentSub);
                    if (oldSemesterIdOfStudentSubListNewStudentSub != null && !oldSemesterIdOfStudentSubListNewStudentSub.equals(semester)) {
                        oldSemesterIdOfStudentSubListNewStudentSub.getStudentSubList().remove(studentSubListNewStudentSub);
                        oldSemesterIdOfStudentSubListNewStudentSub = em.merge(oldSemesterIdOfStudentSubListNewStudentSub);
                    }
                }
            }
            for (InstructorSub instructorSubListNewInstructorSub : instructorSubListNew) {
                if (!instructorSubListOld.contains(instructorSubListNewInstructorSub)) {
                    Semester oldSemesterIdOfInstructorSubListNewInstructorSub = instructorSubListNewInstructorSub.getSemesterId();
                    instructorSubListNewInstructorSub.setSemesterId(semester);
                    instructorSubListNewInstructorSub = em.merge(instructorSubListNewInstructorSub);
                    if (oldSemesterIdOfInstructorSubListNewInstructorSub != null && !oldSemesterIdOfInstructorSubListNewInstructorSub.equals(semester)) {
                        oldSemesterIdOfInstructorSubListNewInstructorSub.getInstructorSubList().remove(instructorSubListNewInstructorSub);
                        oldSemesterIdOfInstructorSubListNewInstructorSub = em.merge(oldSemesterIdOfInstructorSubListNewInstructorSub);
                    }
                }
            }
            for (Instructors instructorsListNewInstructors : instructorsListNew) {
                if (!instructorsListOld.contains(instructorsListNewInstructors)) {
                    Semester oldSemesterIdOfInstructorsListNewInstructors = instructorsListNewInstructors.getSemesterId();
                    instructorsListNewInstructors.setSemesterId(semester);
                    instructorsListNewInstructors = em.merge(instructorsListNewInstructors);
                    if (oldSemesterIdOfInstructorsListNewInstructors != null && !oldSemesterIdOfInstructorsListNewInstructors.equals(semester)) {
                        oldSemesterIdOfInstructorsListNewInstructors.getInstructorsList().remove(instructorsListNewInstructors);
                        oldSemesterIdOfInstructorsListNewInstructors = em.merge(oldSemesterIdOfInstructorsListNewInstructors);
                    }
                }
            }
            for (Grade gradeListNewGrade : gradeListNew) {
                if (!gradeListOld.contains(gradeListNewGrade)) {
                    Semester oldSemesterIdOfGradeListNewGrade = gradeListNewGrade.getSemesterId();
                    gradeListNewGrade.setSemesterId(semester);
                    gradeListNewGrade = em.merge(gradeListNewGrade);
                    if (oldSemesterIdOfGradeListNewGrade != null && !oldSemesterIdOfGradeListNewGrade.equals(semester)) {
                        oldSemesterIdOfGradeListNewGrade.getGradeList().remove(gradeListNewGrade);
                        oldSemesterIdOfGradeListNewGrade = em.merge(oldSemesterIdOfGradeListNewGrade);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = semester.getId();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
                illegalOrphanMessages.add("This Semester (" + semester + ") cannot be destroyed since the Student " + studentListOrphanCheckStudent + " in its studentList field has a non-nullable semsterId field.");
            }
            List<StudentSub> studentSubListOrphanCheck = semester.getStudentSubList();
            for (StudentSub studentSubListOrphanCheckStudentSub : studentSubListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Semester (" + semester + ") cannot be destroyed since the StudentSub " + studentSubListOrphanCheckStudentSub + " in its studentSubList field has a non-nullable semesterId field.");
            }
            List<InstructorSub> instructorSubListOrphanCheck = semester.getInstructorSubList();
            for (InstructorSub instructorSubListOrphanCheckInstructorSub : instructorSubListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Semester (" + semester + ") cannot be destroyed since the InstructorSub " + instructorSubListOrphanCheckInstructorSub + " in its instructorSubList field has a non-nullable semesterId field.");
            }
            List<Instructors> instructorsListOrphanCheck = semester.getInstructorsList();
            for (Instructors instructorsListOrphanCheckInstructors : instructorsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Semester (" + semester + ") cannot be destroyed since the Instructors " + instructorsListOrphanCheckInstructors + " in its instructorsList field has a non-nullable semesterId field.");
            }
            List<Grade> gradeListOrphanCheck = semester.getGradeList();
            for (Grade gradeListOrphanCheckGrade : gradeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Semester (" + semester + ") cannot be destroyed since the Grade " + gradeListOrphanCheckGrade + " in its gradeList field has a non-nullable semesterId field.");
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

    public Semester findSemester(Integer id) {
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
