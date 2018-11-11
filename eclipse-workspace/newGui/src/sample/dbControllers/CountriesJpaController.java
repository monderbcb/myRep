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
import sample.DbTables.Countries;
import sample.dbControllers.exceptions.IllegalOrphanException;
import sample.dbControllers.exceptions.NonexistentEntityException;
import sample.dbControllers.exceptions.PreexistingEntityException;

/**
 *
 * @author monder
 */
public class CountriesJpaController implements Serializable {

    public CountriesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Countries countries) throws PreexistingEntityException, Exception {
        if (countries.getStudentList() == null) {
            countries.setStudentList(new ArrayList<Student>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Student> attachedStudentList = new ArrayList<Student>();
            for (Student studentListStudentToAttach : countries.getStudentList()) {
                studentListStudentToAttach = em.getReference(studentListStudentToAttach.getClass(), studentListStudentToAttach.getStudentId());
                attachedStudentList.add(studentListStudentToAttach);
            }
            countries.setStudentList(attachedStudentList);
            em.persist(countries);
            for (Student studentListStudent : countries.getStudentList()) {
                Countries oldNationalityOfStudentListStudent = studentListStudent.getNationality();
                studentListStudent.setNationality(countries);
                studentListStudent = em.merge(studentListStudent);
                if (oldNationalityOfStudentListStudent != null) {
                    oldNationalityOfStudentListStudent.getStudentList().remove(studentListStudent);
                    oldNationalityOfStudentListStudent = em.merge(oldNationalityOfStudentListStudent);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCountries(countries.getNumCode()) != null) {
                throw new PreexistingEntityException("Countries " + countries + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Countries countries) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Countries persistentCountries = em.find(Countries.class, countries.getNumCode());
            List<Student> studentListOld = persistentCountries.getStudentList();
            List<Student> studentListNew = countries.getStudentList();
            List<String> illegalOrphanMessages = null;
            for (Student studentListOldStudent : studentListOld) {
                if (!studentListNew.contains(studentListOldStudent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Student " + studentListOldStudent + " since its nationality field is not nullable.");
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
            countries.setStudentList(studentListNew);
            countries = em.merge(countries);
            for (Student studentListNewStudent : studentListNew) {
                if (!studentListOld.contains(studentListNewStudent)) {
                    Countries oldNationalityOfStudentListNewStudent = studentListNewStudent.getNationality();
                    studentListNewStudent.setNationality(countries);
                    studentListNewStudent = em.merge(studentListNewStudent);
                    if (oldNationalityOfStudentListNewStudent != null && !oldNationalityOfStudentListNewStudent.equals(countries)) {
                        oldNationalityOfStudentListNewStudent.getStudentList().remove(studentListNewStudent);
                        oldNationalityOfStudentListNewStudent = em.merge(oldNationalityOfStudentListNewStudent);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = countries.getNumCode();
                if (findCountries(id) == null) {
                    throw new NonexistentEntityException("The countries with id " + id + " no longer exists.");
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
            Countries countries;
            try {
                countries = em.getReference(Countries.class, id);
                countries.getNumCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The countries with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Student> studentListOrphanCheck = countries.getStudentList();
            for (Student studentListOrphanCheckStudent : studentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Countries (" + countries + ") cannot be destroyed since the Student " + studentListOrphanCheckStudent + " in its studentList field has a non-nullable nationality field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(countries);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Countries> findCountriesEntities() {
        return findCountriesEntities(true, -1, -1);
    }

    public List<Countries> findCountriesEntities(int maxResults, int firstResult) {
        return findCountriesEntities(false, maxResults, firstResult);
    }

    private List<Countries> findCountriesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Countries.class));
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

    public Countries findCountries(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Countries.class, id);
        } finally {
            em.close();
        }
    }

    public int getCountriesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Countries> rt = cq.from(Countries.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
