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
import university.Entites.Instructor;
import university.Entites.SubDepartment;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import university.Entites.Department;
import university.JPAcontrollers.exceptions.IllegalOrphanException;
import university.JPAcontrollers.exceptions.NonexistentEntityException;

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
        if (department.getInstructorList() == null) {
            department.setInstructorList(new ArrayList<Instructor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Instructor instructorId = department.getInstructorId();
            if (instructorId != null) {
                instructorId = em.getReference(instructorId.getClass(), instructorId.getId());
                department.setInstructorId(instructorId);
            }
            List<SubDepartment> attachedSubDepartmentList = new ArrayList<SubDepartment>();
            for (SubDepartment subDepartmentListSubDepartmentToAttach : department.getSubDepartmentList()) {
                subDepartmentListSubDepartmentToAttach = em.getReference(subDepartmentListSubDepartmentToAttach.getClass(), subDepartmentListSubDepartmentToAttach.getId());
                attachedSubDepartmentList.add(subDepartmentListSubDepartmentToAttach);
            }
            department.setSubDepartmentList(attachedSubDepartmentList);
            List<Instructor> attachedInstructorList = new ArrayList<Instructor>();
            for (Instructor instructorListInstructorToAttach : department.getInstructorList()) {
                instructorListInstructorToAttach = em.getReference(instructorListInstructorToAttach.getClass(), instructorListInstructorToAttach.getId());
                attachedInstructorList.add(instructorListInstructorToAttach);
            }
            department.setInstructorList(attachedInstructorList);
            em.persist(department);
            if (instructorId != null) {
                Department oldDepartmentIdOfInstructorId = instructorId.getDepartmentId();
                if (oldDepartmentIdOfInstructorId != null) {
                    oldDepartmentIdOfInstructorId.setInstructorId(null);
                    oldDepartmentIdOfInstructorId = em.merge(oldDepartmentIdOfInstructorId);
                }
                instructorId.setDepartmentId(department);
                instructorId = em.merge(instructorId);
            }
            for (SubDepartment subDepartmentListSubDepartment : department.getSubDepartmentList()) {
                Department oldDepartmentIdOfSubDepartmentListSubDepartment = subDepartmentListSubDepartment.getDepartmentId();
                subDepartmentListSubDepartment.setDepartmentId(department);
                subDepartmentListSubDepartment = em.merge(subDepartmentListSubDepartment);
                if (oldDepartmentIdOfSubDepartmentListSubDepartment != null) {
                    oldDepartmentIdOfSubDepartmentListSubDepartment.getSubDepartmentList().remove(subDepartmentListSubDepartment);
                    oldDepartmentIdOfSubDepartmentListSubDepartment = em.merge(oldDepartmentIdOfSubDepartmentListSubDepartment);
                }
            }
            for (Instructor instructorListInstructor : department.getInstructorList()) {
                Department oldDepartmentIdOfInstructorListInstructor = instructorListInstructor.getDepartmentId();
                instructorListInstructor.setDepartmentId(department);
                instructorListInstructor = em.merge(instructorListInstructor);
                if (oldDepartmentIdOfInstructorListInstructor != null) {
                    oldDepartmentIdOfInstructorListInstructor.getInstructorList().remove(instructorListInstructor);
                    oldDepartmentIdOfInstructorListInstructor = em.merge(oldDepartmentIdOfInstructorListInstructor);
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
            Instructor instructorIdOld = persistentDepartment.getInstructorId();
            Instructor instructorIdNew = department.getInstructorId();
            List<SubDepartment> subDepartmentListOld = persistentDepartment.getSubDepartmentList();
            List<SubDepartment> subDepartmentListNew = department.getSubDepartmentList();
            List<Instructor> instructorListOld = persistentDepartment.getInstructorList();
            List<Instructor> instructorListNew = department.getInstructorList();
            List<String> illegalOrphanMessages = null;
            if (instructorIdOld != null && !instructorIdOld.equals(instructorIdNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Instructor " + instructorIdOld + " since its departmentId field is not nullable.");
            }
            for (SubDepartment subDepartmentListOldSubDepartment : subDepartmentListOld) {
                if (!subDepartmentListNew.contains(subDepartmentListOldSubDepartment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SubDepartment " + subDepartmentListOldSubDepartment + " since its departmentId field is not nullable.");
                }
            }
            for (Instructor instructorListOldInstructor : instructorListOld) {
                if (!instructorListNew.contains(instructorListOldInstructor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Instructor " + instructorListOldInstructor + " since its departmentId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (instructorIdNew != null) {
                instructorIdNew = em.getReference(instructorIdNew.getClass(), instructorIdNew.getId());
                department.setInstructorId(instructorIdNew);
            }
            List<SubDepartment> attachedSubDepartmentListNew = new ArrayList<SubDepartment>();
            for (SubDepartment subDepartmentListNewSubDepartmentToAttach : subDepartmentListNew) {
                subDepartmentListNewSubDepartmentToAttach = em.getReference(subDepartmentListNewSubDepartmentToAttach.getClass(), subDepartmentListNewSubDepartmentToAttach.getId());
                attachedSubDepartmentListNew.add(subDepartmentListNewSubDepartmentToAttach);
            }
            subDepartmentListNew = attachedSubDepartmentListNew;
            department.setSubDepartmentList(subDepartmentListNew);
            List<Instructor> attachedInstructorListNew = new ArrayList<Instructor>();
            for (Instructor instructorListNewInstructorToAttach : instructorListNew) {
                instructorListNewInstructorToAttach = em.getReference(instructorListNewInstructorToAttach.getClass(), instructorListNewInstructorToAttach.getId());
                attachedInstructorListNew.add(instructorListNewInstructorToAttach);
            }
            instructorListNew = attachedInstructorListNew;
            department.setInstructorList(instructorListNew);
            department = em.merge(department);
            if (instructorIdNew != null && !instructorIdNew.equals(instructorIdOld)) {
                Department oldDepartmentIdOfInstructorId = instructorIdNew.getDepartmentId();
                if (oldDepartmentIdOfInstructorId != null) {
                    oldDepartmentIdOfInstructorId.setInstructorId(null);
                    oldDepartmentIdOfInstructorId = em.merge(oldDepartmentIdOfInstructorId);
                }
                instructorIdNew.setDepartmentId(department);
                instructorIdNew = em.merge(instructorIdNew);
            }
            for (SubDepartment subDepartmentListNewSubDepartment : subDepartmentListNew) {
                if (!subDepartmentListOld.contains(subDepartmentListNewSubDepartment)) {
                    Department oldDepartmentIdOfSubDepartmentListNewSubDepartment = subDepartmentListNewSubDepartment.getDepartmentId();
                    subDepartmentListNewSubDepartment.setDepartmentId(department);
                    subDepartmentListNewSubDepartment = em.merge(subDepartmentListNewSubDepartment);
                    if (oldDepartmentIdOfSubDepartmentListNewSubDepartment != null && !oldDepartmentIdOfSubDepartmentListNewSubDepartment.equals(department)) {
                        oldDepartmentIdOfSubDepartmentListNewSubDepartment.getSubDepartmentList().remove(subDepartmentListNewSubDepartment);
                        oldDepartmentIdOfSubDepartmentListNewSubDepartment = em.merge(oldDepartmentIdOfSubDepartmentListNewSubDepartment);
                    }
                }
            }
            for (Instructor instructorListNewInstructor : instructorListNew) {
                if (!instructorListOld.contains(instructorListNewInstructor)) {
                    Department oldDepartmentIdOfInstructorListNewInstructor = instructorListNewInstructor.getDepartmentId();
                    instructorListNewInstructor.setDepartmentId(department);
                    instructorListNewInstructor = em.merge(instructorListNewInstructor);
                    if (oldDepartmentIdOfInstructorListNewInstructor != null && !oldDepartmentIdOfInstructorListNewInstructor.equals(department)) {
                        oldDepartmentIdOfInstructorListNewInstructor.getInstructorList().remove(instructorListNewInstructor);
                        oldDepartmentIdOfInstructorListNewInstructor = em.merge(oldDepartmentIdOfInstructorListNewInstructor);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = department.getId();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            Instructor instructorIdOrphanCheck = department.getInstructorId();
            if (instructorIdOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Department (" + department + ") cannot be destroyed since the Instructor " + instructorIdOrphanCheck + " in its instructorId field has a non-nullable departmentId field.");
            }
            List<SubDepartment> subDepartmentListOrphanCheck = department.getSubDepartmentList();
            for (SubDepartment subDepartmentListOrphanCheckSubDepartment : subDepartmentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Department (" + department + ") cannot be destroyed since the SubDepartment " + subDepartmentListOrphanCheckSubDepartment + " in its subDepartmentList field has a non-nullable departmentId field.");
            }
            List<Instructor> instructorListOrphanCheck = department.getInstructorList();
            for (Instructor instructorListOrphanCheckInstructor : instructorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Department (" + department + ") cannot be destroyed since the Instructor " + instructorListOrphanCheckInstructor + " in its instructorList field has a non-nullable departmentId field.");
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

    public Department findDepartment(Integer id) {
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
