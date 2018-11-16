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
import university.Entites.Subject;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import university.Entites.Course;
import university.Entites.Section;
import university.JPAcontrollers.exceptions.IllegalOrphanException;
import university.JPAcontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author monder
 */
public class SubjectJpaController implements Serializable {

    public SubjectJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Subject subject) {
        if (subject.getSubjectList() == null) {
            subject.setSubjectList(new ArrayList<Subject>());
        }
        if (subject.getSubjectList1() == null) {
            subject.setSubjectList1(new ArrayList<Subject>());
        }
        if (subject.getSubjectList2() == null) {
            subject.setSubjectList2(new ArrayList<Subject>());
        }
        if (subject.getSubjectList3() == null) {
            subject.setSubjectList3(new ArrayList<Subject>());
        }
        if (subject.getCourseList() == null) {
            subject.setCourseList(new ArrayList<Course>());
        }
        if (subject.getSectionList() == null) {
            subject.setSectionList(new ArrayList<Section>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subject prerequisite1 = subject.getPrerequisite1();
            if (prerequisite1 != null) {
                prerequisite1 = em.getReference(prerequisite1.getClass(), prerequisite1.getId());
                subject.setPrerequisite1(prerequisite1);
            }
            Subject prerequisite2 = subject.getPrerequisite2();
            if (prerequisite2 != null) {
                prerequisite2 = em.getReference(prerequisite2.getClass(), prerequisite2.getId());
                subject.setPrerequisite2(prerequisite2);
            }
            Subject prerequisite3 = subject.getPrerequisite3();
            if (prerequisite3 != null) {
                prerequisite3 = em.getReference(prerequisite3.getClass(), prerequisite3.getId());
                subject.setPrerequisite3(prerequisite3);
            }
            Subject prerequisite4 = subject.getPrerequisite4();
            if (prerequisite4 != null) {
                prerequisite4 = em.getReference(prerequisite4.getClass(), prerequisite4.getId());
                subject.setPrerequisite4(prerequisite4);
            }
            List<Subject> attachedSubjectList = new ArrayList<Subject>();
            for (Subject subjectListSubjectToAttach : subject.getSubjectList()) {
                subjectListSubjectToAttach = em.getReference(subjectListSubjectToAttach.getClass(), subjectListSubjectToAttach.getId());
                attachedSubjectList.add(subjectListSubjectToAttach);
            }
            subject.setSubjectList(attachedSubjectList);
            List<Subject> attachedSubjectList1 = new ArrayList<Subject>();
            for (Subject subjectList1SubjectToAttach : subject.getSubjectList1()) {
                subjectList1SubjectToAttach = em.getReference(subjectList1SubjectToAttach.getClass(), subjectList1SubjectToAttach.getId());
                attachedSubjectList1.add(subjectList1SubjectToAttach);
            }
            subject.setSubjectList1(attachedSubjectList1);
            List<Subject> attachedSubjectList2 = new ArrayList<Subject>();
            for (Subject subjectList2SubjectToAttach : subject.getSubjectList2()) {
                subjectList2SubjectToAttach = em.getReference(subjectList2SubjectToAttach.getClass(), subjectList2SubjectToAttach.getId());
                attachedSubjectList2.add(subjectList2SubjectToAttach);
            }
            subject.setSubjectList2(attachedSubjectList2);
            List<Subject> attachedSubjectList3 = new ArrayList<Subject>();
            for (Subject subjectList3SubjectToAttach : subject.getSubjectList3()) {
                subjectList3SubjectToAttach = em.getReference(subjectList3SubjectToAttach.getClass(), subjectList3SubjectToAttach.getId());
                attachedSubjectList3.add(subjectList3SubjectToAttach);
            }
            subject.setSubjectList3(attachedSubjectList3);
            List<Course> attachedCourseList = new ArrayList<Course>();
            for (Course courseListCourseToAttach : subject.getCourseList()) {
                courseListCourseToAttach = em.getReference(courseListCourseToAttach.getClass(), courseListCourseToAttach.getId());
                attachedCourseList.add(courseListCourseToAttach);
            }
            subject.setCourseList(attachedCourseList);
            List<Section> attachedSectionList = new ArrayList<Section>();
            for (Section sectionListSectionToAttach : subject.getSectionList()) {
                sectionListSectionToAttach = em.getReference(sectionListSectionToAttach.getClass(), sectionListSectionToAttach.getId());
                attachedSectionList.add(sectionListSectionToAttach);
            }
            subject.setSectionList(attachedSectionList);
            em.persist(subject);
            if (prerequisite1 != null) {
                prerequisite1.getSubjectList().add(subject);
                prerequisite1 = em.merge(prerequisite1);
            }
            if (prerequisite2 != null) {
                prerequisite2.getSubjectList().add(subject);
                prerequisite2 = em.merge(prerequisite2);
            }
            if (prerequisite3 != null) {
                prerequisite3.getSubjectList().add(subject);
                prerequisite3 = em.merge(prerequisite3);
            }
            if (prerequisite4 != null) {
                prerequisite4.getSubjectList().add(subject);
                prerequisite4 = em.merge(prerequisite4);
            }
            for (Subject subjectListSubject : subject.getSubjectList()) {
                Subject oldPrerequisite1OfSubjectListSubject = subjectListSubject.getPrerequisite1();
                subjectListSubject.setPrerequisite1(subject);
                subjectListSubject = em.merge(subjectListSubject);
                if (oldPrerequisite1OfSubjectListSubject != null) {
                    oldPrerequisite1OfSubjectListSubject.getSubjectList().remove(subjectListSubject);
                    oldPrerequisite1OfSubjectListSubject = em.merge(oldPrerequisite1OfSubjectListSubject);
                }
            }
            for (Subject subjectList1Subject : subject.getSubjectList1()) {
                Subject oldPrerequisite2OfSubjectList1Subject = subjectList1Subject.getPrerequisite2();
                subjectList1Subject.setPrerequisite2(subject);
                subjectList1Subject = em.merge(subjectList1Subject);
                if (oldPrerequisite2OfSubjectList1Subject != null) {
                    oldPrerequisite2OfSubjectList1Subject.getSubjectList1().remove(subjectList1Subject);
                    oldPrerequisite2OfSubjectList1Subject = em.merge(oldPrerequisite2OfSubjectList1Subject);
                }
            }
            for (Subject subjectList2Subject : subject.getSubjectList2()) {
                Subject oldPrerequisite3OfSubjectList2Subject = subjectList2Subject.getPrerequisite3();
                subjectList2Subject.setPrerequisite3(subject);
                subjectList2Subject = em.merge(subjectList2Subject);
                if (oldPrerequisite3OfSubjectList2Subject != null) {
                    oldPrerequisite3OfSubjectList2Subject.getSubjectList2().remove(subjectList2Subject);
                    oldPrerequisite3OfSubjectList2Subject = em.merge(oldPrerequisite3OfSubjectList2Subject);
                }
            }
            for (Subject subjectList3Subject : subject.getSubjectList3()) {
                Subject oldPrerequisite4OfSubjectList3Subject = subjectList3Subject.getPrerequisite4();
                subjectList3Subject.setPrerequisite4(subject);
                subjectList3Subject = em.merge(subjectList3Subject);
                if (oldPrerequisite4OfSubjectList3Subject != null) {
                    oldPrerequisite4OfSubjectList3Subject.getSubjectList3().remove(subjectList3Subject);
                    oldPrerequisite4OfSubjectList3Subject = em.merge(oldPrerequisite4OfSubjectList3Subject);
                }
            }
            for (Course courseListCourse : subject.getCourseList()) {
                Subject oldSubjectIdOfCourseListCourse = courseListCourse.getSubjectId();
                courseListCourse.setSubjectId(subject);
                courseListCourse = em.merge(courseListCourse);
                if (oldSubjectIdOfCourseListCourse != null) {
                    oldSubjectIdOfCourseListCourse.getCourseList().remove(courseListCourse);
                    oldSubjectIdOfCourseListCourse = em.merge(oldSubjectIdOfCourseListCourse);
                }
            }
            for (Section sectionListSection : subject.getSectionList()) {
                Subject oldSubjectIdOfSectionListSection = sectionListSection.getSubjectId();
                sectionListSection.setSubjectId(subject);
                sectionListSection = em.merge(sectionListSection);
                if (oldSubjectIdOfSectionListSection != null) {
                    oldSubjectIdOfSectionListSection.getSectionList().remove(sectionListSection);
                    oldSubjectIdOfSectionListSection = em.merge(oldSubjectIdOfSectionListSection);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Subject subject) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subject persistentSubject = em.find(Subject.class, subject.getId());
            Subject prerequisite1Old = persistentSubject.getPrerequisite1();
            Subject prerequisite1New = subject.getPrerequisite1();
            Subject prerequisite2Old = persistentSubject.getPrerequisite2();
            Subject prerequisite2New = subject.getPrerequisite2();
            Subject prerequisite3Old = persistentSubject.getPrerequisite3();
            Subject prerequisite3New = subject.getPrerequisite3();
            Subject prerequisite4Old = persistentSubject.getPrerequisite4();
            Subject prerequisite4New = subject.getPrerequisite4();
            List<Subject> subjectListOld = persistentSubject.getSubjectList();
            List<Subject> subjectListNew = subject.getSubjectList();
            List<Subject> subjectList1Old = persistentSubject.getSubjectList1();
            List<Subject> subjectList1New = subject.getSubjectList1();
            List<Subject> subjectList2Old = persistentSubject.getSubjectList2();
            List<Subject> subjectList2New = subject.getSubjectList2();
            List<Subject> subjectList3Old = persistentSubject.getSubjectList3();
            List<Subject> subjectList3New = subject.getSubjectList3();
            List<Course> courseListOld = persistentSubject.getCourseList();
            List<Course> courseListNew = subject.getCourseList();
            List<Section> sectionListOld = persistentSubject.getSectionList();
            List<Section> sectionListNew = subject.getSectionList();
            List<String> illegalOrphanMessages = null;
            for (Course courseListOldCourse : courseListOld) {
                if (!courseListNew.contains(courseListOldCourse)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Course " + courseListOldCourse + " since its subjectId field is not nullable.");
                }
            }
            for (Section sectionListOldSection : sectionListOld) {
                if (!sectionListNew.contains(sectionListOldSection)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Section " + sectionListOldSection + " since its subjectId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (prerequisite1New != null) {
                prerequisite1New = em.getReference(prerequisite1New.getClass(), prerequisite1New.getId());
                subject.setPrerequisite1(prerequisite1New);
            }
            if (prerequisite2New != null) {
                prerequisite2New = em.getReference(prerequisite2New.getClass(), prerequisite2New.getId());
                subject.setPrerequisite2(prerequisite2New);
            }
            if (prerequisite3New != null) {
                prerequisite3New = em.getReference(prerequisite3New.getClass(), prerequisite3New.getId());
                subject.setPrerequisite3(prerequisite3New);
            }
            if (prerequisite4New != null) {
                prerequisite4New = em.getReference(prerequisite4New.getClass(), prerequisite4New.getId());
                subject.setPrerequisite4(prerequisite4New);
            }
            List<Subject> attachedSubjectListNew = new ArrayList<Subject>();
            for (Subject subjectListNewSubjectToAttach : subjectListNew) {
                subjectListNewSubjectToAttach = em.getReference(subjectListNewSubjectToAttach.getClass(), subjectListNewSubjectToAttach.getId());
                attachedSubjectListNew.add(subjectListNewSubjectToAttach);
            }
            subjectListNew = attachedSubjectListNew;
            subject.setSubjectList(subjectListNew);
            List<Subject> attachedSubjectList1New = new ArrayList<Subject>();
            for (Subject subjectList1NewSubjectToAttach : subjectList1New) {
                subjectList1NewSubjectToAttach = em.getReference(subjectList1NewSubjectToAttach.getClass(), subjectList1NewSubjectToAttach.getId());
                attachedSubjectList1New.add(subjectList1NewSubjectToAttach);
            }
            subjectList1New = attachedSubjectList1New;
            subject.setSubjectList1(subjectList1New);
            List<Subject> attachedSubjectList2New = new ArrayList<Subject>();
            for (Subject subjectList2NewSubjectToAttach : subjectList2New) {
                subjectList2NewSubjectToAttach = em.getReference(subjectList2NewSubjectToAttach.getClass(), subjectList2NewSubjectToAttach.getId());
                attachedSubjectList2New.add(subjectList2NewSubjectToAttach);
            }
            subjectList2New = attachedSubjectList2New;
            subject.setSubjectList2(subjectList2New);
            List<Subject> attachedSubjectList3New = new ArrayList<Subject>();
            for (Subject subjectList3NewSubjectToAttach : subjectList3New) {
                subjectList3NewSubjectToAttach = em.getReference(subjectList3NewSubjectToAttach.getClass(), subjectList3NewSubjectToAttach.getId());
                attachedSubjectList3New.add(subjectList3NewSubjectToAttach);
            }
            subjectList3New = attachedSubjectList3New;
            subject.setSubjectList3(subjectList3New);
            List<Course> attachedCourseListNew = new ArrayList<Course>();
            for (Course courseListNewCourseToAttach : courseListNew) {
                courseListNewCourseToAttach = em.getReference(courseListNewCourseToAttach.getClass(), courseListNewCourseToAttach.getId());
                attachedCourseListNew.add(courseListNewCourseToAttach);
            }
            courseListNew = attachedCourseListNew;
            subject.setCourseList(courseListNew);
            List<Section> attachedSectionListNew = new ArrayList<Section>();
            for (Section sectionListNewSectionToAttach : sectionListNew) {
                sectionListNewSectionToAttach = em.getReference(sectionListNewSectionToAttach.getClass(), sectionListNewSectionToAttach.getId());
                attachedSectionListNew.add(sectionListNewSectionToAttach);
            }
            sectionListNew = attachedSectionListNew;
            subject.setSectionList(sectionListNew);
            subject = em.merge(subject);
            if (prerequisite1Old != null && !prerequisite1Old.equals(prerequisite1New)) {
                prerequisite1Old.getSubjectList().remove(subject);
                prerequisite1Old = em.merge(prerequisite1Old);
            }
            if (prerequisite1New != null && !prerequisite1New.equals(prerequisite1Old)) {
                prerequisite1New.getSubjectList().add(subject);
                prerequisite1New = em.merge(prerequisite1New);
            }
            if (prerequisite2Old != null && !prerequisite2Old.equals(prerequisite2New)) {
                prerequisite2Old.getSubjectList().remove(subject);
                prerequisite2Old = em.merge(prerequisite2Old);
            }
            if (prerequisite2New != null && !prerequisite2New.equals(prerequisite2Old)) {
                prerequisite2New.getSubjectList().add(subject);
                prerequisite2New = em.merge(prerequisite2New);
            }
            if (prerequisite3Old != null && !prerequisite3Old.equals(prerequisite3New)) {
                prerequisite3Old.getSubjectList().remove(subject);
                prerequisite3Old = em.merge(prerequisite3Old);
            }
            if (prerequisite3New != null && !prerequisite3New.equals(prerequisite3Old)) {
                prerequisite3New.getSubjectList().add(subject);
                prerequisite3New = em.merge(prerequisite3New);
            }
            if (prerequisite4Old != null && !prerequisite4Old.equals(prerequisite4New)) {
                prerequisite4Old.getSubjectList().remove(subject);
                prerequisite4Old = em.merge(prerequisite4Old);
            }
            if (prerequisite4New != null && !prerequisite4New.equals(prerequisite4Old)) {
                prerequisite4New.getSubjectList().add(subject);
                prerequisite4New = em.merge(prerequisite4New);
            }
            for (Subject subjectListOldSubject : subjectListOld) {
                if (!subjectListNew.contains(subjectListOldSubject)) {
                    subjectListOldSubject.setPrerequisite1(null);
                    subjectListOldSubject = em.merge(subjectListOldSubject);
                }
            }
            for (Subject subjectListNewSubject : subjectListNew) {
                if (!subjectListOld.contains(subjectListNewSubject)) {
                    Subject oldPrerequisite1OfSubjectListNewSubject = subjectListNewSubject.getPrerequisite1();
                    subjectListNewSubject.setPrerequisite1(subject);
                    subjectListNewSubject = em.merge(subjectListNewSubject);
                    if (oldPrerequisite1OfSubjectListNewSubject != null && !oldPrerequisite1OfSubjectListNewSubject.equals(subject)) {
                        oldPrerequisite1OfSubjectListNewSubject.getSubjectList().remove(subjectListNewSubject);
                        oldPrerequisite1OfSubjectListNewSubject = em.merge(oldPrerequisite1OfSubjectListNewSubject);
                    }
                }
            }
            for (Subject subjectList1OldSubject : subjectList1Old) {
                if (!subjectList1New.contains(subjectList1OldSubject)) {
                    subjectList1OldSubject.setPrerequisite2(null);
                    subjectList1OldSubject = em.merge(subjectList1OldSubject);
                }
            }
            for (Subject subjectList1NewSubject : subjectList1New) {
                if (!subjectList1Old.contains(subjectList1NewSubject)) {
                    Subject oldPrerequisite2OfSubjectList1NewSubject = subjectList1NewSubject.getPrerequisite2();
                    subjectList1NewSubject.setPrerequisite2(subject);
                    subjectList1NewSubject = em.merge(subjectList1NewSubject);
                    if (oldPrerequisite2OfSubjectList1NewSubject != null && !oldPrerequisite2OfSubjectList1NewSubject.equals(subject)) {
                        oldPrerequisite2OfSubjectList1NewSubject.getSubjectList1().remove(subjectList1NewSubject);
                        oldPrerequisite2OfSubjectList1NewSubject = em.merge(oldPrerequisite2OfSubjectList1NewSubject);
                    }
                }
            }
            for (Subject subjectList2OldSubject : subjectList2Old) {
                if (!subjectList2New.contains(subjectList2OldSubject)) {
                    subjectList2OldSubject.setPrerequisite3(null);
                    subjectList2OldSubject = em.merge(subjectList2OldSubject);
                }
            }
            for (Subject subjectList2NewSubject : subjectList2New) {
                if (!subjectList2Old.contains(subjectList2NewSubject)) {
                    Subject oldPrerequisite3OfSubjectList2NewSubject = subjectList2NewSubject.getPrerequisite3();
                    subjectList2NewSubject.setPrerequisite3(subject);
                    subjectList2NewSubject = em.merge(subjectList2NewSubject);
                    if (oldPrerequisite3OfSubjectList2NewSubject != null && !oldPrerequisite3OfSubjectList2NewSubject.equals(subject)) {
                        oldPrerequisite3OfSubjectList2NewSubject.getSubjectList2().remove(subjectList2NewSubject);
                        oldPrerequisite3OfSubjectList2NewSubject = em.merge(oldPrerequisite3OfSubjectList2NewSubject);
                    }
                }
            }
            for (Subject subjectList3OldSubject : subjectList3Old) {
                if (!subjectList3New.contains(subjectList3OldSubject)) {
                    subjectList3OldSubject.setPrerequisite4(null);
                    subjectList3OldSubject = em.merge(subjectList3OldSubject);
                }
            }
            for (Subject subjectList3NewSubject : subjectList3New) {
                if (!subjectList3Old.contains(subjectList3NewSubject)) {
                    Subject oldPrerequisite4OfSubjectList3NewSubject = subjectList3NewSubject.getPrerequisite4();
                    subjectList3NewSubject.setPrerequisite4(subject);
                    subjectList3NewSubject = em.merge(subjectList3NewSubject);
                    if (oldPrerequisite4OfSubjectList3NewSubject != null && !oldPrerequisite4OfSubjectList3NewSubject.equals(subject)) {
                        oldPrerequisite4OfSubjectList3NewSubject.getSubjectList3().remove(subjectList3NewSubject);
                        oldPrerequisite4OfSubjectList3NewSubject = em.merge(oldPrerequisite4OfSubjectList3NewSubject);
                    }
                }
            }
            for (Course courseListNewCourse : courseListNew) {
                if (!courseListOld.contains(courseListNewCourse)) {
                    Subject oldSubjectIdOfCourseListNewCourse = courseListNewCourse.getSubjectId();
                    courseListNewCourse.setSubjectId(subject);
                    courseListNewCourse = em.merge(courseListNewCourse);
                    if (oldSubjectIdOfCourseListNewCourse != null && !oldSubjectIdOfCourseListNewCourse.equals(subject)) {
                        oldSubjectIdOfCourseListNewCourse.getCourseList().remove(courseListNewCourse);
                        oldSubjectIdOfCourseListNewCourse = em.merge(oldSubjectIdOfCourseListNewCourse);
                    }
                }
            }
            for (Section sectionListNewSection : sectionListNew) {
                if (!sectionListOld.contains(sectionListNewSection)) {
                    Subject oldSubjectIdOfSectionListNewSection = sectionListNewSection.getSubjectId();
                    sectionListNewSection.setSubjectId(subject);
                    sectionListNewSection = em.merge(sectionListNewSection);
                    if (oldSubjectIdOfSectionListNewSection != null && !oldSubjectIdOfSectionListNewSection.equals(subject)) {
                        oldSubjectIdOfSectionListNewSection.getSectionList().remove(sectionListNewSection);
                        oldSubjectIdOfSectionListNewSection = em.merge(oldSubjectIdOfSectionListNewSection);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = subject.getId();
                if (findSubject(id) == null) {
                    throw new NonexistentEntityException("The subject with id " + id + " no longer exists.");
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
            Subject subject;
            try {
                subject = em.getReference(Subject.class, id);
                subject.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subject with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Course> courseListOrphanCheck = subject.getCourseList();
            for (Course courseListOrphanCheckCourse : courseListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Subject (" + subject + ") cannot be destroyed since the Course " + courseListOrphanCheckCourse + " in its courseList field has a non-nullable subjectId field.");
            }
            List<Section> sectionListOrphanCheck = subject.getSectionList();
            for (Section sectionListOrphanCheckSection : sectionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Subject (" + subject + ") cannot be destroyed since the Section " + sectionListOrphanCheckSection + " in its sectionList field has a non-nullable subjectId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Subject prerequisite1 = subject.getPrerequisite1();
            if (prerequisite1 != null) {
                prerequisite1.getSubjectList().remove(subject);
                prerequisite1 = em.merge(prerequisite1);
            }
            Subject prerequisite2 = subject.getPrerequisite2();
            if (prerequisite2 != null) {
                prerequisite2.getSubjectList().remove(subject);
                prerequisite2 = em.merge(prerequisite2);
            }
            Subject prerequisite3 = subject.getPrerequisite3();
            if (prerequisite3 != null) {
                prerequisite3.getSubjectList().remove(subject);
                prerequisite3 = em.merge(prerequisite3);
            }
            Subject prerequisite4 = subject.getPrerequisite4();
            if (prerequisite4 != null) {
                prerequisite4.getSubjectList().remove(subject);
                prerequisite4 = em.merge(prerequisite4);
            }
            List<Subject> subjectList = subject.getSubjectList();
            for (Subject subjectListSubject : subjectList) {
                subjectListSubject.setPrerequisite1(null);
                subjectListSubject = em.merge(subjectListSubject);
            }
            List<Subject> subjectList1 = subject.getSubjectList1();
            for (Subject subjectList1Subject : subjectList1) {
                subjectList1Subject.setPrerequisite2(null);
                subjectList1Subject = em.merge(subjectList1Subject);
            }
            List<Subject> subjectList2 = subject.getSubjectList2();
            for (Subject subjectList2Subject : subjectList2) {
                subjectList2Subject.setPrerequisite3(null);
                subjectList2Subject = em.merge(subjectList2Subject);
            }
            List<Subject> subjectList3 = subject.getSubjectList3();
            for (Subject subjectList3Subject : subjectList3) {
                subjectList3Subject.setPrerequisite4(null);
                subjectList3Subject = em.merge(subjectList3Subject);
            }
            em.remove(subject);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Subject> findSubjectEntities() {
        return findSubjectEntities(true, -1, -1);
    }

    public List<Subject> findSubjectEntities(int maxResults, int firstResult) {
        return findSubjectEntities(false, maxResults, firstResult);
    }

    private List<Subject> findSubjectEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Subject.class));
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

    public Subject findSubject(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Subject.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubjectCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Subject> rt = cq.from(Subject.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
