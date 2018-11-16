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
import university.Entites.Section;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import university.Entites.SubDepartment;
import university.JPAcontrollers.exceptions.IllegalOrphanException;
import university.JPAcontrollers.exceptions.NonexistentEntityException;

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
        if (subDepartment.getSectionList() == null) {
            subDepartment.setSectionList(new ArrayList<Section>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Department departmentId = subDepartment.getDepartmentId();
            if (departmentId != null) {
                departmentId = em.getReference(departmentId.getClass(), departmentId.getId());
                subDepartment.setDepartmentId(departmentId);
            }
            List<Section> attachedSectionList = new ArrayList<Section>();
            for (Section sectionListSectionToAttach : subDepartment.getSectionList()) {
                sectionListSectionToAttach = em.getReference(sectionListSectionToAttach.getClass(), sectionListSectionToAttach.getId());
                attachedSectionList.add(sectionListSectionToAttach);
            }
            subDepartment.setSectionList(attachedSectionList);
            em.persist(subDepartment);
            if (departmentId != null) {
                departmentId.getSubDepartmentList().add(subDepartment);
                departmentId = em.merge(departmentId);
            }
            for (Section sectionListSection : subDepartment.getSectionList()) {
                SubDepartment oldSubDepartmentIdOfSectionListSection = sectionListSection.getSubDepartmentId();
                sectionListSection.setSubDepartmentId(subDepartment);
                sectionListSection = em.merge(sectionListSection);
                if (oldSubDepartmentIdOfSectionListSection != null) {
                    oldSubDepartmentIdOfSectionListSection.getSectionList().remove(sectionListSection);
                    oldSubDepartmentIdOfSectionListSection = em.merge(oldSubDepartmentIdOfSectionListSection);
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
            Department departmentIdOld = persistentSubDepartment.getDepartmentId();
            Department departmentIdNew = subDepartment.getDepartmentId();
            List<Section> sectionListOld = persistentSubDepartment.getSectionList();
            List<Section> sectionListNew = subDepartment.getSectionList();
            List<String> illegalOrphanMessages = null;
            for (Section sectionListOldSection : sectionListOld) {
                if (!sectionListNew.contains(sectionListOldSection)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Section " + sectionListOldSection + " since its subDepartmentId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (departmentIdNew != null) {
                departmentIdNew = em.getReference(departmentIdNew.getClass(), departmentIdNew.getId());
                subDepartment.setDepartmentId(departmentIdNew);
            }
            List<Section> attachedSectionListNew = new ArrayList<Section>();
            for (Section sectionListNewSectionToAttach : sectionListNew) {
                sectionListNewSectionToAttach = em.getReference(sectionListNewSectionToAttach.getClass(), sectionListNewSectionToAttach.getId());
                attachedSectionListNew.add(sectionListNewSectionToAttach);
            }
            sectionListNew = attachedSectionListNew;
            subDepartment.setSectionList(sectionListNew);
            subDepartment = em.merge(subDepartment);
            if (departmentIdOld != null && !departmentIdOld.equals(departmentIdNew)) {
                departmentIdOld.getSubDepartmentList().remove(subDepartment);
                departmentIdOld = em.merge(departmentIdOld);
            }
            if (departmentIdNew != null && !departmentIdNew.equals(departmentIdOld)) {
                departmentIdNew.getSubDepartmentList().add(subDepartment);
                departmentIdNew = em.merge(departmentIdNew);
            }
            for (Section sectionListNewSection : sectionListNew) {
                if (!sectionListOld.contains(sectionListNewSection)) {
                    SubDepartment oldSubDepartmentIdOfSectionListNewSection = sectionListNewSection.getSubDepartmentId();
                    sectionListNewSection.setSubDepartmentId(subDepartment);
                    sectionListNewSection = em.merge(sectionListNewSection);
                    if (oldSubDepartmentIdOfSectionListNewSection != null && !oldSubDepartmentIdOfSectionListNewSection.equals(subDepartment)) {
                        oldSubDepartmentIdOfSectionListNewSection.getSectionList().remove(sectionListNewSection);
                        oldSubDepartmentIdOfSectionListNewSection = em.merge(oldSubDepartmentIdOfSectionListNewSection);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = subDepartment.getId();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<Section> sectionListOrphanCheck = subDepartment.getSectionList();
            for (Section sectionListOrphanCheckSection : sectionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SubDepartment (" + subDepartment + ") cannot be destroyed since the Section " + sectionListOrphanCheckSection + " in its sectionList field has a non-nullable subDepartmentId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Department departmentId = subDepartment.getDepartmentId();
            if (departmentId != null) {
                departmentId.getSubDepartmentList().remove(subDepartment);
                departmentId = em.merge(departmentId);
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

    public SubDepartment findSubDepartment(Integer id) {
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
