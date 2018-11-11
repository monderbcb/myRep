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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author monder
 */
@Entity
@Table(name = "student_sub", catalog = "newuni", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentSub.findAll", query = "SELECT s FROM StudentSub s"),
    @NamedQuery(name = "StudentSub.findById", query = "SELECT s FROM StudentSub s WHERE s.id = :id"),
    @NamedQuery(name = "StudentSub.findByGroupNumber", query = "SELECT s FROM StudentSub s WHERE s.groupNumber = :groupNumber"),
    @NamedQuery(name = "StudentSub.findByGrade", query = "SELECT s FROM StudentSub s WHERE s.grade = :grade")})
public class StudentSub implements Serializable {

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "studentSubjectRowId")
    private StudentPayment studentPayment;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "group_number")
    private int groupNumber;
    @Column(name = "grade")
    private Boolean grade;
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    @ManyToOne(optional = false)
    private Student studentId;
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Subjects subjectId;
    @JoinColumn(name = "semester_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Semester semesterId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentSubjectRowId")
    private List<StudentPayment> studentPaymentList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "studentSub")
    private Grade grade1;

    public StudentSub() {
    }

    public StudentSub(Integer id) {
        this.id = id;
    }

    public StudentSub(Integer id, int groupNumber) {
        this.id = id;
        this.groupNumber = groupNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public Boolean getGrade() {
        return grade;
    }

    public void setGrade(Boolean grade) {
        this.grade = grade;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public Subjects getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Subjects subjectId) {
        this.subjectId = subjectId;
    }

    public Semester getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Semester semesterId) {
        this.semesterId = semesterId;
    }

    @XmlTransient
    public List<StudentPayment> getStudentPaymentList() {
        return studentPaymentList;
    }

    public void setStudentPaymentList(List<StudentPayment> studentPaymentList) {
        this.studentPaymentList = studentPaymentList;
    }

    public Grade getGrade1() {
        return grade1;
    }

    public void setGrade1(Grade grade1) {
        this.grade1 = grade1;
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
        if (!(object instanceof StudentSub)) {
            return false;
        }
        StudentSub other = (StudentSub) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.StudentSub[ id=" + id + " ]";
    }

    public StudentPayment getStudentPayment() {
        return studentPayment;
    }

    public void setStudentPayment(StudentPayment studentPayment) {
        this.studentPayment = studentPayment;
    }
    
}
