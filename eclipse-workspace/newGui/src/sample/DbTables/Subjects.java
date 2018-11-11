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
@Table(name = "subjects", catalog = "newuni", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subjects.findAll", query = "SELECT s FROM Subjects s"),
    @NamedQuery(name = "Subjects.findById", query = "SELECT s FROM Subjects s WHERE s.id = :id"),
    @NamedQuery(name = "Subjects.findBySubjectId", query = "SELECT s FROM Subjects s WHERE s.subjectId = :subjectId"),
    @NamedQuery(name = "Subjects.findByUnits", query = "SELECT s FROM Subjects s WHERE s.units = :units"),
    @NamedQuery(name = "Subjects.findBySubjectName", query = "SELECT s FROM Subjects s WHERE s.subjectName = :subjectName")})
public class Subjects implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "subject_id")
    private String subjectId;
    @Basic(optional = false)
    @Column(name = "units")
    private String units;
    @Basic(optional = false)
    @Column(name = "subject_name")
    private String subjectName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subjectId")
    private List<StudentSub> studentSubList;
    @JoinColumn(name = "major", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SubDepartment major;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subjectId")
    private List<InstructorSub> instructorSubList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subjectId")
    private List<Prerequisites> prerequisitesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prerequisite")
    private List<Prerequisites> prerequisitesList1;

    public Subjects() {
    }

    public Subjects(Short id) {
        this.id = id;
    }

    public Subjects(Short id, String subjectId, String units, String subjectName) {
        this.id = id;
        this.subjectId = subjectId;
        this.units = units;
        this.subjectName = subjectName;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @XmlTransient
    public List<StudentSub> getStudentSubList() {
        return studentSubList;
    }

    public void setStudentSubList(List<StudentSub> studentSubList) {
        this.studentSubList = studentSubList;
    }

    public SubDepartment getMajor() {
        return major;
    }

    public void setMajor(SubDepartment major) {
        this.major = major;
    }

    @XmlTransient
    public List<InstructorSub> getInstructorSubList() {
        return instructorSubList;
    }

    public void setInstructorSubList(List<InstructorSub> instructorSubList) {
        this.instructorSubList = instructorSubList;
    }

    @XmlTransient
    public List<Prerequisites> getPrerequisitesList() {
        return prerequisitesList;
    }

    public void setPrerequisitesList(List<Prerequisites> prerequisitesList) {
        this.prerequisitesList = prerequisitesList;
    }

    @XmlTransient
    public List<Prerequisites> getPrerequisitesList1() {
        return prerequisitesList1;
    }

    public void setPrerequisitesList1(List<Prerequisites> prerequisitesList1) {
        this.prerequisitesList1 = prerequisitesList1;
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
        if (!(object instanceof Subjects)) {
            return false;
        }
        Subjects other = (Subjects) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.Subjects[ id=" + id + " ]";
    }
    
}
