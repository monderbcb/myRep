/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.DbTables;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author monder
 */
@Entity
@Table(name = "department", catalog = "newuni", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Department.findAll", query = "SELECT d FROM Department d"),
    @NamedQuery(name = "Department.findById", query = "SELECT d FROM Department d WHERE d.id = :id"),
    @NamedQuery(name = "Department.findByMajor", query = "SELECT d FROM Department d WHERE d.major = :major")})
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "major")
    private String major;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "majorName")
    private List<SubDepartment> subDepartmentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "department")
    private List<Instructors> instructorsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "major")
    private List<Deans> deansList;

    public Department() {
    }

    public Department(Short id) {
        this.id = id;
    }

    public Department(Short id, String major) {
        this.id = id;
        this.major = major;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @XmlTransient
    public List<SubDepartment> getSubDepartmentList() {
        return subDepartmentList;
    }

    public void setSubDepartmentList(List<SubDepartment> subDepartmentList) {
        this.subDepartmentList = subDepartmentList;
    }

    @XmlTransient
    public List<Instructors> getInstructorsList() {
        return instructorsList;
    }

    public void setInstructorsList(List<Instructors> instructorsList) {
        this.instructorsList = instructorsList;
    }

    @XmlTransient
    public List<Deans> getDeansList() {
        return deansList;
    }

    public void setDeansList(List<Deans> deansList) {
        this.deansList = deansList;
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
        if (!(object instanceof Department)) {
            return false;
        }
        Department other = (Department) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.Department[ id=" + id + " ]";
    }
    
}
