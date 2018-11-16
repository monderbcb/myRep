/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university.Entites;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author monder
 */
@Entity
@Table(catalog = "university", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"code"}),
    @UniqueConstraint(columnNames = {"prerequisite1", "prerequisite2", "prerequisite3", "prerequisite4"}),
    @UniqueConstraint(columnNames = {"name"})})
@NamedQueries({
    @NamedQuery(name = "Subject.findAll", query = "SELECT s FROM Subject s"),
    @NamedQuery(name = "Subject.findById", query = "SELECT s FROM Subject s WHERE s.id = :id"),
    @NamedQuery(name = "Subject.findByName", query = "SELECT s FROM Subject s WHERE s.name = :name"),
    @NamedQuery(name = "Subject.findByCode", query = "SELECT s FROM Subject s WHERE s.code = :code"),
    @NamedQuery(name = "Subject.findByUnits", query = "SELECT s FROM Subject s WHERE s.units = :units")})
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Short id;
    @Basic(optional = false)
    @Column(nullable = false, length = 20)
    private String name;
    @Basic(optional = false)
    @Column(nullable = false, length = 5)
    private String code;
    @Basic(optional = false)
    @Column(nullable = false)
    private short units;
    @OneToMany(mappedBy = "prerequisite1", fetch = FetchType.LAZY)
    private List<Subject> subjectList;
    @JoinColumn(name = "prerequisite1", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Subject prerequisite1;
    @OneToMany(mappedBy = "prerequisite2", fetch = FetchType.LAZY)
    private List<Subject> subjectList1;
    @JoinColumn(name = "prerequisite2", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Subject prerequisite2;
    @OneToMany(mappedBy = "prerequisite3", fetch = FetchType.LAZY)
    private List<Subject> subjectList2;
    @JoinColumn(name = "prerequisite3", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Subject prerequisite3;
    @OneToMany(mappedBy = "prerequisite4", fetch = FetchType.LAZY)
    private List<Subject> subjectList3;
    @JoinColumn(name = "prerequisite4", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Subject prerequisite4;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subjectId", fetch = FetchType.LAZY)
    private List<Course> courseList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subjectId", fetch = FetchType.LAZY)
    private List<Section> sectionList;

    public Subject() {
    }

    public Subject(Short id) {
        this.id = id;
    }

    public Subject(Short id, String name, String code, short units) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.units = units;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public short getUnits() {
        return units;
    }

    public void setUnits(short units) {
        this.units = units;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public Subject getPrerequisite1() {
        return prerequisite1;
    }

    public void setPrerequisite1(Subject prerequisite1) {
        this.prerequisite1 = prerequisite1;
    }

    public List<Subject> getSubjectList1() {
        return subjectList1;
    }

    public void setSubjectList1(List<Subject> subjectList1) {
        this.subjectList1 = subjectList1;
    }

    public Subject getPrerequisite2() {
        return prerequisite2;
    }

    public void setPrerequisite2(Subject prerequisite2) {
        this.prerequisite2 = prerequisite2;
    }

    public List<Subject> getSubjectList2() {
        return subjectList2;
    }

    public void setSubjectList2(List<Subject> subjectList2) {
        this.subjectList2 = subjectList2;
    }

    public Subject getPrerequisite3() {
        return prerequisite3;
    }

    public void setPrerequisite3(Subject prerequisite3) {
        this.prerequisite3 = prerequisite3;
    }

    public List<Subject> getSubjectList3() {
        return subjectList3;
    }

    public void setSubjectList3(List<Subject> subjectList3) {
        this.subjectList3 = subjectList3;
    }

    public Subject getPrerequisite4() {
        return prerequisite4;
    }

    public void setPrerequisite4(Subject prerequisite4) {
        this.prerequisite4 = prerequisite4;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subject)) {
            return false;
        }
        Subject other = (Subject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "university.Entites.Subject[ id=" + id + " ]";
    }
    
}
