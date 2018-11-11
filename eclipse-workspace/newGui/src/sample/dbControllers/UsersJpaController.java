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
import sample.DbTables.Log;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sample.DbTables.Users;
import sample.dbControllers.exceptions.IllegalOrphanException;
import sample.dbControllers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) {
        if (users.getLogList() == null) {
            users.setLogList(new ArrayList<Log>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Log> attachedLogList = new ArrayList<Log>();
            for (Log logListLogToAttach : users.getLogList()) {
                logListLogToAttach = em.getReference(logListLogToAttach.getClass(), logListLogToAttach.getId());
                attachedLogList.add(logListLogToAttach);
            }
            users.setLogList(attachedLogList);
            em.persist(users);
            for (Log logListLog : users.getLogList()) {
                Users oldUserIdOfLogListLog = logListLog.getUserId();
                logListLog.setUserId(users);
                logListLog = em.merge(logListLog);
                if (oldUserIdOfLogListLog != null) {
                    oldUserIdOfLogListLog.getLogList().remove(logListLog);
                    oldUserIdOfLogListLog = em.merge(oldUserIdOfLogListLog);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getId());
            List<Log> logListOld = persistentUsers.getLogList();
            List<Log> logListNew = users.getLogList();
            List<String> illegalOrphanMessages = null;
            for (Log logListOldLog : logListOld) {
                if (!logListNew.contains(logListOldLog)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Log " + logListOldLog + " since its userId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Log> attachedLogListNew = new ArrayList<Log>();
            for (Log logListNewLogToAttach : logListNew) {
                logListNewLogToAttach = em.getReference(logListNewLogToAttach.getClass(), logListNewLogToAttach.getId());
                attachedLogListNew.add(logListNewLogToAttach);
            }
            logListNew = attachedLogListNew;
            users.setLogList(logListNew);
            users = em.merge(users);
            for (Log logListNewLog : logListNew) {
                if (!logListOld.contains(logListNewLog)) {
                    Users oldUserIdOfLogListNewLog = logListNewLog.getUserId();
                    logListNewLog.setUserId(users);
                    logListNewLog = em.merge(logListNewLog);
                    if (oldUserIdOfLogListNewLog != null && !oldUserIdOfLogListNewLog.equals(users)) {
                        oldUserIdOfLogListNewLog.getLogList().remove(logListNewLog);
                        oldUserIdOfLogListNewLog = em.merge(oldUserIdOfLogListNewLog);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = users.getId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
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
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Log> logListOrphanCheck = users.getLogList();
            for (Log logListOrphanCheckLog : logListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the Log " + logListOrphanCheckLog + " in its logList field has a non-nullable userId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
