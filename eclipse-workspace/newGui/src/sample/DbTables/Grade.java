/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.DbTables;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author monder
 */
@Entity
@Table(name = "grade", catalog = "newuni", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grade.findAll", query = "SELECT g FROM Grade g"),
    @NamedQuery(name = "Grade.findById", query = "SELECT g FROM Grade g WHERE g.id = :id"),
    @NamedQuery(name = "Grade.findByMiddleExam", query = "SELECT g FROM Grade g WHERE g.middleExam = :middleExam"),
    @NamedQuery(name = "Grade.findByFinalExam", query = "SELECT g FROM Grade g WHERE g.finalExam = :finalExam"),
    @NamedQuery(name = "Grade.findByFinalResault", query = "SELECT g FROM Grade g WHERE g.finalResault = :finalResault")})
public class Grade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "middle_exam")
    private short middleExam;
    @Basic(optional = false)
    @Column(name = "final_exam")
    private short finalExam;
    @Basic(optional = false)
    @Column(name = "final_resault")
    private String finalResault;
    @JoinColumn(name = "semester_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Semester semesterId;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private StudentSub studentSub;

    public Grade() {
    }

    public Grade(Integer id) {
        this.id = id;
    }

    public Grade(Integer id, short middleExam, short finalExam, String finalResault) {
        this.id = id;
        this.middleExam = middleExam;
        this.finalExam = finalExam;
        this.finalResault = finalResault;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getMiddleExam() {
        return middleExam;
    }

    public void setMiddleExam(short middleExam) {
        this.middleExam = middleExam;
    }

    public short getFinalExam() {
        return finalExam;
    }

    public void setFinalExam(short finalExam) {
        this.finalExam = finalExam;
    }

    public String getFinalResault() {
        return finalResault;
    }

    public void setFinalResault(String finalResault) {
        this.finalResault = finalResault;
    }

    public Semester getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Semester semesterId) {
        this.semesterId = semesterId;
    }

    public StudentSub getStudentSub() {
        return studentSub;
    }

    public void setStudentSub(StudentSub studentSub) {
        this.studentSub = studentSub;
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
        if (!(object instanceof Grade)) {
            return false;
        }
        Grade other = (Grade) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.Grade[ id=" + id + " ]";
    }
    
}
