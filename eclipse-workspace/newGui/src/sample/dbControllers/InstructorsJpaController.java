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
import sample.DbTables.Semester;
import sample.DbTables.InstructorPaymentInfo;
import sample.DbTables.InstructorPaymentLog;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sample.DbTables.InstructorSub;
import sample.DbTables.Deans;
import sample.DbTables.Instructors;
import sample.dbControllers.exceptions.IllegalOrphanException;
import sample.dbControllers.exceptions.NonexistentEntityException;
import sample.dbControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author monder
 */
public class InstructorsJpaController implements Serializable {

    public InstructorsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Instructors instructors) throws PreexistingEntityException, Exception {
        if (instructors.getInstructorPaymentLogList() == null) {
            instructors.setInstructorPaymentLogList(new ArrayList<InstructorPaymentLog>());
        }
        if (instructors.getInstructorSubList() == null) {
            instructors.setInstructorSubList(new ArrayList<InstructorSub>());
        }
        if (instructors.getDeansList() == null) {
            instructors.setDeansList(new ArrayList<Deans>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Department department = instructors.getDepartment();
            if (department != null) {
                department = em.getReference(department.getClass(), department.getId());
                instructors.setDepartment(department);
            }
            Semester semesterId = instructors.getSemesterId();
            if (semesterId != null) {
                semesterId = em.getReference(semesterId.getClass(), semesterId.getId());
                instructors.setSemesterId(semesterId);
            }
            InstructorPaymentInfo instructorPaymentInfo = instructors.getInstructorPaymentInfo();
            if (instructorPaymentInfo != null) {
                instructorPaymentInfo = em.getReference(instructorPaymentInfo.getClass(), instructorPaymentInfo.getId());
                instructors.setInstructorPaymentInfo(instructorPaymentInfo);
            }
            List<InstructorPaymentLog> attachedInstructorPaymentLogList = new ArrayList<InstructorPaymentLog>();
            for (InstructorPaymentLog instructorPaymentLogListInstructorPaymentLogToAttach : instructors.getInstructorPaymentLogList()) {
                instructorPaymentLogListInstructorPaymentLogToAttach = em.getReference(instructorPaymentLogListInstructorPaymentLogToAttach.getClass(), instructorPaymentLogListInstructorPaymentLogToAttach.getId());
                attachedInstructorPaymentLogList.add(instructorPaymentLogListInstructorPaymentLogToAttach);
            }
            instructors.setInstructorPaymentLogList(attachedInstructorPaymentLogList);
            List<InstructorSub> attachedInstructorSubList = new ArrayList<InstructorSub>();
            for (InstructorSub instructorSubListInstructorSubToAttach : instructors.getInstructorSubList()) {
                instructorSubListInstructorSubToAttach = em.getReference(instructorSubListInstructorSubToAttach.getClass(), instructorSubListInstructorSubToAttach.getId());
                attachedInstructorSubList.add(instructorSubListInstructorSubToAttach);
            }
            instructors.setInstructorSubList(attachedInstructorSubList);
            List<Deans> attachedDeansList = new ArrayList<Deans>();
            for (Deans deansListDeansToAttach : instructors.getDeansList()) {
                deansListDeansToAttach = em.getReference(deansListDeansToAttach.getClass(), deansListDeansToAttach.getId());
                attachedDeansList.add(deansListDeansToAttach);
            }
            instructors.setDeansList(attachedDeansList);
            em.persist(instructors);
            if (department != null) {
                department.getInstructorsList().add(instructors);
                department = em.merge(department);
            }
            if (semesterId != null) {
                semesterId.getInstructorsList().add(instructors);
                semesterId = em.merge(semesterId);
            }
            if (instructorPaymentInfo != null) {
                Instructors oldInstructorsOfInstructorPaymentInfo = instructorPaymentInfo.getInstructors();
                if (oldInstructorsOfInstructorPaymentInfo != null) {
                    oldInstructorsOfInstructorPaymentInfo.setInstructorPaymentInfo(null);
                    oldInstructorsOfInstructorPaymentInfo = em.merge(oldInstructorsOfInstructorPaymentInfo);
                }
                instructorPaymentInfo.setInstructors(instructors);
                instructorPaymentInfo = em.merge(instructorPaymentInfo);
            }
            for (InstructorPaymentLog instructorPaymentLogListInstructorPaymentLog : instructors.getInstructorPaymentLogList()) {
                Instructors oldId2OfInstructorPaymentLogListInstructorPaymentLog = instructorPaymentLogListInstructorPaymentLog.getId2();
                instructorPaymentLogListInstructorPaymentLog.setId2(instructors);
                instructorPaymentLogListInstructorPaymentLog = em.merge(instructorPaymentLogListInstructorPaymentLog);
                if (oldId2OfInstructorPaymentLogListInstructorPaymentLog != null) {
                    oldId2OfInstructorPaymentLogListInstructorPaymentLog.getInstructorPaymentLogList().remove(instructorPaymentLogListInstructorPaymentLog);
                    oldId2OfInstructorPaymentLogListInstructorPaymentLog = em.merge(oldId2OfInstructorPaymentLogListInstructorPaymentLog);
                }
            }
            for (InstructorSub instructorSubListInstructorSub : instructors.getInstructorSubList()) {
                Instructors oldInstructorIdOfInstructorSubListInstructorSub = instructorSubListInstructorSub.getInstructorId();
                instructorSubListInstructorSub.setInstructorId(instructors);
                instructorSubListInstructorSub = em.merge(instructorSubListInstructorSub);
                if (oldInstructorIdOfInstructorSubListInstructorSub != null) {
                    oldInstructorIdOfInstructorSubListInstructorSub.getInstructorSubList().remove(instructorSubListInstructorSub);
                    oldInstructorIdOfInstructorSubListInstructorSub = em.merge(oldInstructorIdOfInstructorSubListInstructorSub);
                }
            }
            for (Deans deansListDeans : instructors.getDeansList()) {
                Instructors oldInstructorIdOfDeansListDeans = deansListDeans.getInstructorId();
                deansListDeans.setInstructorId(instructors);
                deansListDeans = em.merge(deansListDeans);
                if (oldInstructorIdOfDeansListDeans != null) {
                    oldInstructorIdOfDeansListDeans.getDeansList().remove(deansListDeans);
                    oldInstructorIdOfDeansListDeans = em.merge(oldInstructorIdOfDeansListDeans);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findInstructors(instructors.getId()) != null) {
                throw new PreexistingEntityException("Instructors " + instructors + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Instructors instructors) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Instructors persistentInstructors = em.find(Instructors.class, instructors.getId());
            Department departmentOld = persistentInstructors.getDepartment();
            Department departmentNew = instructors.getDepartment();
            Semester semesterIdOld = persistentInstructors.getSemesterId();
            Semester semesterIdNew = instructors.getSemesterId();
            InstructorPaymentInfo instructorPaymentInfoOld = persistentInstructors.getInstructorPaymentInfo();
            InstructorPaymentInfo instructorPaymentInfoNew = instructors.getInstructorPaymentInfo();
            List<InstructorPaymentLog> instructorPaymentLogListOld = persistentInstructors.getInstructorPaymentLogList();
            List<InstructorPaymentLog> instructorPaymentLogListNew = instructors.getInstructorPaymentLogList();
            List<InstructorSub> instructorSubListOld = persistentInstructors.getInstructorSubList();
            List<InstructorSub> instructorSubListNew = instructors.getInstructorSubList();
            List<Deans> deansListOld = persistentInstructors.getDeansList();
            List<Deans> deansListNew = instructors.getDeansList();
            List<String> illegalOrphanMessages = null;
            if (instructorPaymentInfoOld != null && !instructorPaymentInfoOld.equals(instructorPaymentInfoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain InstructorPaymentInfo " + instructorPaymentInfoOld + " since its instructors field is not nullable.");
            }
            for (InstructorPaymentLog instructorPaymentLogListOldInstructorPaymentLog : instructorPaymentLogListOld) {
                if (!instructorPaymentLogListNew.contains(instructorPaymentLogListOldInstructorPaymentLog)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain InstructorPaymentLog " + instructorPaymentLogListOldInstructorPaymentLog + " since its id2 field is not nullable.");
                }
            }
            for (InstructorSub instructorSubListOldInstructorSub : instructorSubListOld) {
                if (!instructorSubListNew.contains(instructorSubListOldInstructorSub)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain InstructorSub " + instructorSubListOldInstructorSub + " since its instructorId field is not nullable.");
                }
            }
            for (Deans deansListOldDeans : deansListOld) {
                if (!deansListNew.contains(deansListOldDeans)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Deans " + deansListOldDeans + " since its instructorId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (departmentNew != null) {
                departmentNew = em.getReference(departmentNew.getClass(), departmentNew.getId());
                instructors.setDepartment(departmentNew);
            }
            if (semesterIdNew != null) {
                semesterIdNew = em.getReference(semesterIdNew.getClass(), semesterIdNew.getId());
                instructors.setSemesterId(semesterIdNew);
            }
            if (instructorPaymentInfoNew != null) {
                instructorPaymentInfoNew = em.getReference(instructorPaymentInfoNew.getClass(), instructorPaymentInfoNew.getId());
                instructors.setInstructorPaymentInfo(instructorPaymentInfoNew);
            }
            List<InstructorPaymentLog> attachedInstructorPaymentLogListNew = new ArrayList<InstructorPaymentLog>();
            for (InstructorPaymentLog instructorPaymentLogListNewInstructorPaymentLogToAttach : instructorPaymentLogListNew) {
                instructorPaymentLogListNewInstructorPaymentLogToAttach = em.getReference(instructorPaymentLogListNewInstructorPaymentLogToAttach.getClass(), instructorPaymentLogListNewInstructorPaymentLogToAttach.getId());
                attachedInstructorPaymentLogListNew.add(instructorPaymentLogListNewInstructorPaymentLogToAttach);
            }
            instructorPaymentLogListNew = attachedInstructorPaymentLogListNew;
            instructors.setInstructorPaymentLogList(instructorPaymentLogListNew);
            List<InstructorSub> attachedInstructorSubListNew = new ArrayList<InstructorSub>();
            for (InstructorSub instructorSubListNewInstructorSubToAttach : instructorSubListNew) {
                instructorSubListNewInstructorSubToAttach = em.getReference(instructorSubListNewInstructorSubToAttach.getClass(), instructorSubListNewInstructorSubToAttach.getId());
                attachedInstructorSubListNew.add(instructorSubListNewInstructorSubToAttach);
            }
            instructorSubListNew = attachedInstructorSubListNew;
            instructors.setInstructorSubList(instructorSubListNew);
            List<Deans> attachedDeansListNew = new ArrayList<Deans>();
            for (Deans deansListNewDeansToAttach : deansListNew) {
                deansListNewDeansToAttach = em.getReference(deansListNewDeansToAttach.getClass(), deansListNewDeansToAttach.getId());
                attachedDeansListNew.add(deansListNewDeansToAttach);
            }
            deansListNew = attachedDeansListNew;
            instructors.setDeansList(deansListNew);
            instructors = em.merge(instructors);
            if (departmentOld != null && !departmentOld.equals(departmentNew)) {
                departmentOld.getInstructorsList().remove(instructors);
                departmentOld = em.merge(departmentOld);
            }
            if (departmentNew != null && !departmentNew.equals(departmentOld)) {
                departmentNew.getInstructorsList().add(instructors);
                departmentNew = em.merge(departmentNew);
            }
            if (semesterIdOld != null && !semesterIdOld.equals(semesterIdNew)) {
                semesterIdOld.getInstructorsList().remove(instructors);
                semesterIdOld = em.merge(semesterIdOld);
            }
            if (semesterIdNew != null && !semesterIdNew.equals(semesterIdOld)) {
                semesterIdNew.getInstructorsList().add(instructors);
                semesterIdNew = em.merge(semesterIdNew);
            }
            if (instructorPaymentInfoNew != null && !instructorPaymentInfoNew.equals(instructorPaymentInfoOld)) {
                Instructors oldInstructorsOfInstructorPaymentInfo = instructorPaymentInfoNew.getInstructors();
                if (oldInstructorsOfInstructorPaymentInfo != null) {
                    oldInstructorsOfInstructorPaymentInfo.setInstructorPaymentInfo(null);
                    oldInstructorsOfInstructorPaymentInfo = em.merge(oldInstructorsOfInstructorPaymentInfo);
                }
                instructorPaymentInfoNew.setInstructors(instructors);
                instructorPaymentInfoNew = em.merge(instructorPaymentInfoNew);
            }
            for (InstructorPaymentLog instructorPaymentLogListNewInstructorPaymentLog : instructorPaymentLogListNew) {
                if (!instructorPaymentLogListOld.contains(instructorPaymentLogListNewInstructorPaymentLog)) {
                    Instructors oldId2OfInstructorPaymentLogListNewInstructorPaymentLog = instructorPaymentLogListNewInstructorPaymentLog.getId2();
                    instructorPaymentLogListNewInstructorPaymentLog.setId2(instructors);
                    instructorPaymentLogListNewInstructorPaymentLog = em.merge(instructorPaymentLogListNewInstructorPaymentLog);
                    if (oldId2OfInstructorPaymentLogListNewInstructorPaymentLog != null && !oldId2OfInstructorPaymentLogListNewInstructorPaymentLog.equals(instructors)) {
                        oldId2OfInstructorPaymentLogListNewInstructorPaymentLog.getInstructorPaymentLogList().remove(instructorPaymentLogListNewInstructorPaymentLog);
                        oldId2OfInstructorPaymentLogListNewInstructorPaymentLog = em.merge(oldId2OfInstructorPaymentLogListNewInstructorPaymentLog);
                    }
                }
            }
            for (InstructorSub instructorSubListNewInstructorSub : instructorSubListNew) {
                if (!instructorSubListOld.contains(instructorSubListNewInstructorSub)) {
                    Instructors oldInstructorIdOfInstructorSubListNewInstructorSub = instructorSubListNewInstructorSub.getInstructorId();
                    instructorSubListNewInstructorSub.setInstructorId(instructors);
                    instructorSubListNewInstructorSub = em.merge(instructorSubListNewInstructorSub);
                    if (oldInstructorIdOfInstructorSubListNewInstructorSub != null && !oldInstructorIdOfInstructorSubListNewInstructorSub.equals(instructors)) {
                        oldInstructorIdOfInstructorSubListNewInstructorSub.getInstructorSubList().remove(instructorSubListNewInstructorSub);
                        oldInstructorIdOfInstructorSubListNewInstructorSub = em.merge(oldInstructorIdOfInstructorSubListNewInstructorSub);
                    }
                }
            }
            for (Deans deansListNewDeans : deansListNew) {
                if (!deansListOld.contains(deansListNewDeans)) {
                    Instructors oldInstructorIdOfDeansListNewDeans = deansListNewDeans.getInstructorId();
                    deansListNewDeans.setInstructorId(instructors);
                    deansListNewDeans = em.merge(deansListNewDeans);
                    if (oldInstructorIdOfDeansListNewDeans != null && !oldInstructorIdOfDeansListNewDeans.equals(instructors)) {
                        oldInstructorIdOfDeansListNewDeans.getDeansList().remove(deansListNewDeans);
                        oldInstructorIdOfDeansListNewDeans = em.merge(oldInstructorIdOfDeansListNewDeans);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = instructors.getId();
                if (findInstructors(id) == null) {
                    throw new NonexistentEntityException("The instructors with id " + id + " no longer exists.");
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
            Instructors instructors;
            try {
                instructors = em.getReference(Instructors.class, id);
                instructors.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The instructors with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            InstructorPaymentInfo instructorPaymentInfoOrphanCheck = instructors.getInstructorPaymentInfo();
            if (instructorPaymentInfoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Instructors (" + instructors + ") cannot be destroyed since the InstructorPaymentInfo " + instructorPaymentInfoOrphanCheck + " in its instructorPaymentInfo field has a non-nullable instructors field.");
            }
            List<InstructorPaymentLog> instructorPaymentLogListOrphanCheck = instructors.getInstructorPaymentLogList();
            for (InstructorPaymentLog instructorPaymentLogListOrphanCheckInstructorPaymentLog : instructorPaymentLogListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Instructors (" + instructors + ") cannot be destroyed since the InstructorPaymentLog " + instructorPaymentLogListOrphanCheckInstructorPaymentLog + " in its instructorPaymentLogList field has a non-nullable id2 field.");
            }
            List<InstructorSub> instructorSubListOrphanCheck = instructors.getInstructorSubList();
            for (InstructorSub instructorSubListOrphanCheckInstructorSub : instructorSubListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Instructors (" + instructors + ") cannot be destroyed since the InstructorSub " + instructorSubListOrphanCheckInstructorSub + " in its instructorSubList field has a non-nullable instructorId field.");
            }
            List<Deans> deansListOrphanCheck = instructors.getDeansList();
            for (Deans deansListOrphanCheckDeans : deansListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Instructors (" + instructors + ") cannot be destroyed since the Deans " + deansListOrphanCheckDeans + " in its deansList field has a non-nullable instructorId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Department department = instructors.getDepartment();
            if (department != null) {
                department.getInstructorsList().remove(instructors);
                department = em.merge(department);
            }
            Semester semesterId = instructors.getSemesterId();
            if (semesterId != null) {
                semesterId.getInstructorsList().remove(instructors);
                semesterId = em.merge(semesterId);
            }
            em.remove(instructors);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Instructors> findInstructorsEntities() {
        return findInstructorsEntities(true, -1, -1);
    }

    public List<Instructors> findInstructorsEntities(int maxResults, int firstResult) {
        return findInstructorsEntities(false, maxResults, firstResult);
    }

    private List<Instructors> findInstructorsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Instructors.class));
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

    public Instructors findInstructors(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Instructors.class, id);
        } finally {
            em.close();
        }
    }

    public int getInstructorsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Instructors> rt = cq.from(Instructors.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
