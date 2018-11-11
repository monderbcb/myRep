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
import sample.DbTables.MacPc;
import sample.dbControllers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class MacPcJpaController implements Serializable {

    public MacPcJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MacPc macPc) {
        if (macPc.getLogList() == null) {
            macPc.setLogList(new ArrayList<Log>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Log> attachedLogList = new ArrayList<Log>();
            for (Log logListLogToAttach : macPc.getLogList()) {
                logListLogToAttach = em.getReference(logListLogToAttach.getClass(), logListLogToAttach.getId());
                attachedLogList.add(logListLogToAttach);
            }
            macPc.setLogList(attachedLogList);
            em.persist(macPc);
            for (Log logListLog : macPc.getLogList()) {
                MacPc oldPcMacOfLogListLog = logListLog.getPcMac();
                logListLog.setPcMac(macPc);
                logListLog = em.merge(logListLog);
                if (oldPcMacOfLogListLog != null) {
                    oldPcMacOfLogListLog.getLogList().remove(logListLog);
                    oldPcMacOfLogListLog = em.merge(oldPcMacOfLogListLog);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MacPc macPc) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MacPc persistentMacPc = em.find(MacPc.class, macPc.getId());
            List<Log> logListOld = persistentMacPc.getLogList();
            List<Log> logListNew = macPc.getLogList();
            List<Log> attachedLogListNew = new ArrayList<Log>();
            for (Log logListNewLogToAttach : logListNew) {
                logListNewLogToAttach = em.getReference(logListNewLogToAttach.getClass(), logListNewLogToAttach.getId());
                attachedLogListNew.add(logListNewLogToAttach);
            }
            logListNew = attachedLogListNew;
            macPc.setLogList(logListNew);
            macPc = em.merge(macPc);
            for (Log logListOldLog : logListOld) {
                if (!logListNew.contains(logListOldLog)) {
                    logListOldLog.setPcMac(null);
                    logListOldLog = em.merge(logListOldLog);
                }
            }
            for (Log logListNewLog : logListNew) {
                if (!logListOld.contains(logListNewLog)) {
                    MacPc oldPcMacOfLogListNewLog = logListNewLog.getPcMac();
                    logListNewLog.setPcMac(macPc);
                    logListNewLog = em.merge(logListNewLog);
                    if (oldPcMacOfLogListNewLog != null && !oldPcMacOfLogListNewLog.equals(macPc)) {
                        oldPcMacOfLogListNewLog.getLogList().remove(logListNewLog);
                        oldPcMacOfLogListNewLog = em.merge(oldPcMacOfLogListNewLog);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = macPc.getId();
                if (findMacPc(id) == null) {
                    throw new NonexistentEntityException("The macPc with id " + id + " no longer exists.");
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
            MacPc macPc;
            try {
                macPc = em.getReference(MacPc.class, id);
                macPc.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The macPc with id " + id + " no longer exists.", enfe);
            }
            List<Log> logList = macPc.getLogList();
            for (Log logListLog : logList) {
                logListLog.setPcMac(null);
                logListLog = em.merge(logListLog);
            }
            em.remove(macPc);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MacPc> findMacPcEntities() {
        return findMacPcEntities(true, -1, -1);
    }

    public List<MacPc> findMacPcEntities(int maxResults, int firstResult) {
        return findMacPcEntities(false, maxResults, firstResult);
    }

    private List<MacPc> findMacPcEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MacPc.class));
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

    public MacPc findMacPc(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MacPc.class, id);
        } finally {
            em.close();
        }
    }

    public int getMacPcCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MacPc> rt = cq.from(MacPc.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
