/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.DbTables;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author monder
 */
@Entity
@Table(name = "semester", catalog = "newuni", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Semester.findAll", query = "SELECT s FROM Semester s"),
    @NamedQuery(name = "Semester.findById", query = "SELECT s FROM Semester s WHERE s.id = :id"),
    @NamedQuery(name = "Semester.findBySemesterName", query = "SELECT s FROM Semester s WHERE s.semesterName = :semesterName"),
    @NamedQuery(name = "Semester.findBySemesterStart", query = "SELECT s FROM Semester s WHERE s.semesterStart = :semesterStart"),
    @NamedQuery(name = "Semester.findBySemesterEnd", query = "SELECT s FROM Semester s WHERE s.semesterEnd = :semesterEnd")})
public class Semester implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "semester_name")
    private String semesterName;
    @Basic(optional = false)
    @Column(name = "semester_start")
    @Temporal(TemporalType.DATE)
    private Date semesterStart;
    @Basic(optional = false)
    @Column(name = "semester_end")
    @Temporal(TemporalType.DATE)
    private Date semesterEnd;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semsterId")
    private List<Student> studentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semesterId")
    private List<StudentSub> studentSubList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semesterId")
    private List<InstructorSub> instructorSubList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semesterId")
    private List<Instructors> instructorsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semesterId")
    private List<Grade> gradeList;

    public Semester() {
    }

    public Semester(Integer id) {
        this.id = id;
    }

    public Semester(Integer id, String semesterName, Date semesterStart, Date semesterEnd) {
        this.id = id;
        this.semesterName = semesterName;
        this.semesterStart = semesterStart;
        this.semesterEnd = semesterEnd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public Date getSemesterStart() {
        return semesterStart;
    }

    public void setSemesterStart(Date semesterStart) {
        this.semesterStart = semesterStart;
    }

    public Date getSemesterEnd() {
        return semesterEnd;
    }

    public void setSemesterEnd(Date semesterEnd) {
        this.semesterEnd = semesterEnd;
    }

    @XmlTransient
    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    @XmlTransient
    public List<StudentSub> getStudentSubList() {
        return studentSubList;
    }

    public void setStudentSubList(List<StudentSub> studentSubList) {
        this.studentSubList = studentSubList;
    }

    @XmlTransient
    public List<InstructorSub> getInstructorSubList() {
        return instructorSubList;
    }

    public void setInstructorSubList(List<InstructorSub> instructorSubList) {
        this.instructorSubList = instructorSubList;
    }

    @XmlTransient
    public List<Instructors> getInstructorsList() {
        return instructorsList;
    }

    public void setInstructorsList(List<Instructors> instructorsList) {
        this.instructorsList = instructorsList;
    }

    @XmlTransient
    public List<Grade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
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
        if (!(object instanceof Semester)) {
            return false;
        }
        Semester other = (Semester) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.Semester[ id=" + id + " ]";
    }
    
}
