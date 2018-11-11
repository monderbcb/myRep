/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.dbControllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sample.DbTables.Log;
import sample.DbTables.Users;
import sample.DbTables.MacPc;
import sample.dbControllers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class LogJpaController implements Serializable {

    public LogJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Log log) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users userId = log.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                log.setUserId(userId);
            }
            MacPc pcMac = log.getPcMac();
            if (pcMac != null) {
                pcMac = em.getReference(pcMac.getClass(), pcMac.getId());
                log.setPcMac(pcMac);
            }
            em.persist(log);
            if (userId != null) {
                userId.getLogList().add(log);
                userId = em.merge(userId);
            }
            if (pcMac != null) {
                pcMac.getLogList().add(log);
                pcMac = em.merge(pcMac);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Log log) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Log persistentLog = em.find(Log.class, log.getId());
            Users userIdOld = persistentLog.getUserId();
            Users userIdNew = log.getUserId();
            MacPc pcMacOld = persistentLog.getPcMac();
            MacPc pcMacNew = log.getPcMac();
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                log.setUserId(userIdNew);
            }
            if (pcMacNew != null) {
                pcMacNew = em.getReference(pcMacNew.getClass(), pcMacNew.getId());
                log.setPcMac(pcMacNew);
            }
            log = em.merge(log);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getLogList().remove(log);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getLogList().add(log);
                userIdNew = em.merge(userIdNew);
            }
            if (pcMacOld != null && !pcMacOld.equals(pcMacNew)) {
                pcMacOld.getLogList().remove(log);
                pcMacOld = em.merge(pcMacOld);
            }
            if (pcMacNew != null && !pcMacNew.equals(pcMacOld)) {
                pcMacNew.getLogList().add(log);
                pcMacNew = em.merge(pcMacNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = log.getId();
                if (findLog(id) == null) {
                    throw new NonexistentEntityException("The log with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Log log;
            try {
                log = em.getReference(Log.class, id);
                log.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The log with id " + id + " no longer exists.", enfe);
            }
            Users userId = log.getUserId();
            if (userId != null) {
                userId.getLogList().remove(log);
                userId = em.merge(userId);
            }
            MacPc pcMac = log.getPcMac();
            if (pcMac != null) {
                pcMac.getLogList().remove(log);
                pcMac = em.merge(pcMac);
            }
            em.remove(log);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Log> findLogEntities() {
        return findLogEntities(true, -1, -1);
    }

    public List<Log> findLogEntities(int maxResults, int firstResult) {
        return findLogEntities(false, maxResults, firstResult);
    }

    private List<Log> findLogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Log.class));
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

    public Log findLog(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Log.class, id);
        } finally {
            em.close();
        }
    }

    public int getLogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Log> rt = cq.from(Log.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
