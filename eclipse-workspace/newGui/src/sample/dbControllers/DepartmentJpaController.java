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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sample.DbTables.Instructors;
import sample.DbTables.Deans;
import sample.DbTables.Department;
import sample.dbControllers.exceptions.IllegalOrphanException;
import sample.dbControllers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class DepartmentJpaController implements Serializable {

    public DepartmentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Department department) {
        if (department.getSubDepartmentList() == null) {
            department.setSubDepartmentList(new ArrayList<SubDepartment>());
        }
        if (department.getInstructorsList() == null) {
            department.setInstructorsList(new ArrayList<Instructors>());
        }
        if (department.getDeansList() == null) {
            department.setDeansList(new ArrayList<Deans>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<SubDepartment> attachedSubDepartmentList = new ArrayList<SubDepartment>();
            for (SubDepartment subDepartmentListSubDepartmentToAttach : department.getSubDepartmentList()) {
                subDepartmentListSubDepartmentToAttach = em.getReference(subDepartmentListSubDepartmentToAttach.getClass(), subDepartmentListSubDepartmentToAttach.getId());
                attachedSubDepartmentList.add(subDepartmentListSubDepartmentToAttach);
            }
            department.setSubDepartmentList(attachedSubDepartmentList);
            List<Instructors> attachedInstructorsList = new ArrayList<Instructors>();
            for (Instructors instructorsListInstructorsToAttach : department.getInstructorsList()) {
                instructorsListInstructorsToAttach = em.getReference(instructorsListInstructorsToAttach.getClass(), instructorsListInstructorsToAttach.getId());
                attachedInstructorsList.add(instructorsListInstructorsToAttach);
            }
            department.setInstructorsList(attachedInstructorsList);
            List<Deans> attachedDeansList = new ArrayList<Deans>();
            for (Deans deansListDeansToAttach : department.getDeansList()) {
                deansListDeansToAttach = em.getReference(deansListDeansToAttach.getClass(), deansListDeansToAttach.getId());
                attachedDeansList.add(deansListDeansToAttach);
            }
            department.setDeansList(attachedDeansList);
            em.persist(department);
            for (SubDepartment subDepartmentListSubDepartment : department.getSubDepartmentList()) {
                Department oldMajorNameOfSubDepartmentListSubDepartment = subDepartmentListSubDepartment.getMajorName();
                subDepartmentListSubDepartment.setMajorName(department);
                subDepartmentListSubDepartment = em.merge(subDepartmentListSubDepartment);
                if (oldMajorNameOfSubDepartmentListSubDepartment != null) {
                    oldMajorNameOfSubDepartmentListSubDepartment.getSubDepartmentList().remove(subDepartmentListSubDepartment);
                    oldMajorNameOfSubDepartmentListSubDepartment = em.merge(oldMajorNameOfSubDepartmentListSubDepartment);
                }
            }
            for (Instructors instructorsListInstructors : department.getInstructorsList()) {
                Department oldDepartmentOfInstructorsListInstructors = instructorsListInstructors.getDepartment();
                instructorsListInstructors.setDepartment(department);
                instructorsListInstructors = em.merge(instructorsListInstructors);
                if (oldDepartmentOfInstructorsListInstructors != null) {
                    oldDepartmentOfInstructorsListInstructors.getInstructorsList().remove(instructorsListInstructors);
                    oldDepartmentOfInstructorsListInstructors = em.merge(oldDepartmentOfInstructorsListInstructors);
                }
            }
            for (Deans deansListDeans : department.getDeansList()) {
                Department oldMajorOfDeansListDeans = deansListDeans.getMajor();
                deansListDeans.setMajor(department);
                deansListDeans = em.merge(deansListDeans);
                if (oldMajorOfDeansListDeans != null) {
                    oldMajorOfDeansListDeans.getDeansList().remove(deansListDeans);
                    oldMajorOfDeansListDeans = em.merge(oldMajorOfDeansListDeans);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Department department) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Department persistentDepartment = em.find(Department.class, department.getId());
            List<SubDepartment> subDepartmentListOld = persistentDepartment.getSubDepartmentList();
            List<SubDepartment> subDepartmentListNew = department.getSubDepartmentList();
            List<Instructors> instructorsListOld = persistentDepartment.getInstructorsList();
            List<Instructors> instructorsListNew = department.getInstructorsList();
            List<Deans> deansListOld = persistentDepartment.getDeansList();
            List<Deans> deansListNew = department.getDeansList();
            List<String> illegalOrphanMessages = null;
            for (SubDepartment subDepartmentListOldSubDepartment : subDepartmentListOld) {
                if (!subDepartmentListNew.contains(subDepartmentListOldSubDepartment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SubDepartment " + subDepartmentListOldSubDepartment + " since its majorName field is not nullable.");
                }
            }
            for (Instructors instructorsListOldInstructors : instructorsListOld) {
                if (!instructorsListNew.contains(instructorsListOldInstructors)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Instructors " + instructorsListOldInstructors + " since its department field is not nullable.");
                }
            }
            for (Deans deansListOldDeans : deansListOld) {
                if (!deansListNew.contains(deansListOldDeans)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Deans " + deansListOldDeans + " since its major field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<SubDepartment> attachedSubDepartmentListNew = new ArrayList<SubDepartment>();
            for (SubDepartment subDepartmentListNewSubDepartmentToAttach : subDepartmentListNew) {
                subDepartmentListNewSubDepartmentToAttach = em.getReference(subDepartmentListNewSubDepartmentToAttach.getClass(), subDepartmentListNewSubDepartmentToAttach.getId());
                attachedSubDepartmentListNew.add(subDepartmentListNewSubDepartmentToAttach);
            }
            subDepartmentListNew = attachedSubDepartmentListNew;
            department.setSubDepartmentList(subDepartmentListNew);
            List<Instructors> attachedInstructorsListNew = new ArrayList<Instructors>();
            for (Instructors instructorsListNewInstructorsToAttach : instructorsListNew) {
                instructorsListNewInstructorsToAttach = em.getReference(instructorsListNewInstructorsToAttach.getClass(), instructorsListNewInstructorsToAttach.getId());
                attachedInstructorsListNew.add(instructorsListNewInstructorsToAttach);
            }
            instructorsListNew = attachedInstructorsListNew;
            department.setInstructorsList(instructorsListNew);
            List<Deans> attachedDeansListNew = new ArrayList<Deans>();
            for (Deans deansListNewDeansToAttach : deansListNew) {
                deansListNewDeansToAttach = em.getReference(deansListNewDeansToAttach.getClass(), deansListNewDeansToAttach.getId());
                attachedDeansListNew.add(deansListNewDeansToAttach);
            }
            deansListNew = attachedDeansListNew;
            department.setDeansList(deansListNew);
            department = em.merge(department);
            for (SubDepartment subDepartmentListNewSubDepartment : subDepartmentListNew) {
                if (!subDepartmentListOld.contains(subDepartmentListNewSubDepartment)) {
                    Department oldMajorNameOfSubDepartmentListNewSubDepartment = subDepartmentListNewSubDepartment.getMajorName();
                    subDepartmentListNewSubDepartment.setMajorName(department);
                    subDepartmentListNewSubDepartment = em.merge(subDepartmentListNewSubDepartment);
                    if (oldMajorNameOfSubDepartmentListNewSubDepartment != null && !oldMajorNameOfSubDepartmentListNewSubDepartment.equals(department)) {
                        oldMajorNameOfSubDepartmentListNewSubDepartment.getSubDepartmentList().remove(subDepartmentListNewSubDepartment);
                        oldMajorNameOfSubDepartmentListNewSubDepartment = em.merge(oldMajorNameOfSubDepartmentListNewSubDepartment);
                    }
                }
            }
            for (Instructors instructorsListNewInstructors : instructorsListNew) {
                if (!instructorsListOld.contains(instructorsListNewInstructors)) {
                    Department oldDepartmentOfInstructorsListNewInstructors = instructorsListNewInstructors.getDepartment();
                    instructorsListNewInstructors.setDepartment(department);
                    instructorsListNewInstructors = em.merge(instructorsListNewInstructors);
                    if (oldDepartmentOfInstructorsListNewInstructors != null && !oldDepartmentOfInstructorsListNewInstructors.equals(department)) {
                        oldDepartmentOfInstructorsListNewInstructors.getInstructorsList().remove(instructorsListNewInstructors);
                        oldDepartmentOfInstructorsListNewInstructors = em.merge(oldDepartmentOfInstructorsListNewInstructors);
                    }
                }
            }
            for (Deans deansListNewDeans : deansListNew) {
                if (!deansListOld.contains(deansListNewDeans)) {
                    Department oldMajorOfDeansListNewDeans = deansListNewDeans.getMajor();
                    deansListNewDeans.setMajor(department);
                    deansListNewDeans = em.merge(deansListNewDeans);
                    if (oldMajorOfDeansListNewDeans != null && !oldMajorOfDeansListNewDeans.equals(department)) {
                        oldMajorOfDeansListNewDeans.getDeansList().remove(deansListNewDeans);
                        oldMajorOfDeansListNewDeans = em.merge(oldMajorOfDeansListNewDeans);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = department.getId();
                if (findDepartment(id) == null) {
                    throw new NonexistentEntityException("The department with id " + id + " no longer exists.");
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
            Department department;
            try {
                department = em.getReference(Department.class, id);
                department.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The department with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SubDepartment> subDepartmentListOrphanCheck = department.getSubDepartmentList();
            for (SubDepartment subDepartmentListOrphanCheckSubDepartment : subDepartmentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Department (" + department + ") cannot be destroyed since the SubDepartment " + subDepartmentListOrphanCheckSubDepartment + " in its subDepartmentList field has a non-nullable majorName field.");
            }
            List<Instructors> instructorsListOrphanCheck = department.getInstructorsList();
            for (Instructors instructorsListOrphanCheckInstructors : instructorsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Department (" + department + ") cannot be destroyed since the Instructors " + instructorsListOrphanCheckInstructors + " in its instructorsList field has a non-nullable department field.");
            }
            List<Deans> deansListOrphanCheck = department.getDeansList();
            for (Deans deansListOrphanCheckDeans : deansListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Department (" + department + ") cannot be destroyed since the Deans " + deansListOrphanCheckDeans + " in its deansList field has a non-nullable major field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(department);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Department> findDepartmentEntities() {
        return findDepartmentEntities(true, -1, -1);
    }

    public List<Department> findDepartmentEntities(int maxResults, int firstResult) {
        return findDepartmentEntities(false, maxResults, firstResult);
    }

    private List<Department> findDepartmentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Department.class));
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

    public Department findDepartment(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Department.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartmentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Department> rt = cq.from(Department.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
