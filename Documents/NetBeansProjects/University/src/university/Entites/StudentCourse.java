/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university.Entites;

import java.io.Serializable;
import javax.persistence.Basic;
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
import javax.persistence.Table;

/**
 *
 * @author monder
 */
@Entity
@Table(name = "student_course", catalog = "university", schema = "")
@NamedQueries({
    @NamedQuery(name = "StudentCourse.findAll", query = "SELECT s FROM StudentCourse s"),
    @NamedQuery(name = "StudentCourse.findById", query = "SELECT s FROM StudentCourse s WHERE s.id = :id"),
    @NamedQuery(name = "StudentCourse.findByMidTermGrade", query = "SELECT s FROM StudentCourse s WHERE s.midTermGrade = :midTermGrade"),
    @NamedQuery(name = "StudentCourse.findByFinalExamGrade", query = "SELECT s FROM StudentCourse s WHERE s.finalExamGrade = :finalExamGrade"),
    @NamedQuery(name = "StudentCourse.findByPass", query = "SELECT s FROM StudentCourse s WHERE s.pass = :pass")})
public class StudentCourse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(name = "mid_term_grade")
    private Short midTermGrade;
    @Column(name = "final_exam_grade")
    private Short finalExamGrade;
    private Boolean pass;
    @JoinColumn(name = "course_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Course courseId;
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Student studentId;

    public StudentCourse() {
    }

    public StudentCourse(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getMidTermGrade() {
        return midTermGrade;
    }

    public void setMidTermGrade(Short midTermGrade) {
        this.midTermGrade = midTermGrade;
    }

    public Short getFinalExamGrade() {
        return finalExamGrade;
    }

    public void setFinalExamGrade(Short finalExamGrade) {
        this.finalExamGrade = finalExamGrade;
    }

    public Boolean getPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }

    public Course getCourseId() {
        return courseId;
    }

    public void setCourseId(Course courseId) {
        this.courseId = courseId;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
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
        if (!(object instanceof StudentCourse)) {
            return false;
        }
        StudentCourse other = (StudentCourse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "university.Entites.StudentCourse[ id=" + id + " ]";
    }
    
}
