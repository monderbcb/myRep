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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "sub_department", catalog = "newuni", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubDepartment.findAll", query = "SELECT s FROM SubDepartment s"),
    @NamedQuery(name = "SubDepartment.findById", query = "SELECT s FROM SubDepartment s WHERE s.id = :id"),
    @NamedQuery(name = "SubDepartment.findBySubMajorName", query = "SELECT s FROM SubDepartment s WHERE s.subMajorName = :subMajorName")})
public class SubDepartment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "sub_major_name")
    private String subMajorName;
    @JoinColumn(name = "major_name", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Department majorName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subMajor")
    private List<Student> studentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "major")
    private List<Subjects> subjectsList;

    public SubDepartment() {
    }

    public SubDepartment(Short id) {
        this.id = id;
    }

    public SubDepartment(Short id, String subMajorName) {
        this.id = id;
        this.subMajorName = subMajorName;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getSubMajorName() {
        return subMajorName;
    }

    public void setSubMajorName(String subMajorName) {
        this.subMajorName = subMajorName;
    }

    public Department getMajorName() {
        return majorName;
    }

    public void setMajorName(Department majorName) {
        this.majorName = majorName;
    }

    @XmlTransient
    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    @XmlTransient
    public List<Subjects> getSubjectsList() {
        return subjectsList;
    }

    public void setSubjectsList(List<Subjects> subjectsList) {
        this.subjectsList = subjectsList;
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
        if (!(object instanceof SubDepartment)) {
            return false;
        }
        SubDepartment other = (SubDepartment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.SubDepartment[ id=" + id + " ]";
    }
    
}
