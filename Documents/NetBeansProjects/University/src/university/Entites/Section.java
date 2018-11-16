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

/**
 *
 * @author monder
 */
@Entity
@Table(catalog = "university", schema = "")
@NamedQueries({
    @NamedQuery(name = "Section.findAll", query = "SELECT s FROM Section s"),
    @NamedQuery(name = "Section.findById", query = "SELECT s FROM Section s WHERE s.id = :id"),
    @NamedQuery(name = "Section.findByAcademicYear", query = "SELECT s FROM Section s WHERE s.academicYear = :academicYear")})
public class Section implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "academic_year", nullable = false)
    private boolean academicYear;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "section", fetch = FetchType.LAZY)
    private List<Student> studentList;
    @JoinColumn(name = "sub_department_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SubDepartment subDepartmentId;
    @JoinColumn(name = "subject_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Subject subjectId;

    public Section() {
    }

    public Section(Integer id) {
        this.id = id;
    }

    public Section(Integer id, boolean academicYear) {
        this.id = id;
        this.academicYear = academicYear;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(boolean academicYear) {
        this.academicYear = academicYear;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public SubDepartment getSubDepartmentId() {
        return subDepartmentId;
    }

    public void setSubDepartmentId(SubDepartment subDepartmentId) {
        this.subDepartmentId = subDepartmentId;
    }

    public Subject getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Subject subjectId) {
        this.subjectId = subjectId;
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
        if (!(object instanceof Section)) {
            return false;
        }
        Section other = (Section) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "university.Entites.Section[ id=" + id + " ]";
    }
    
}
