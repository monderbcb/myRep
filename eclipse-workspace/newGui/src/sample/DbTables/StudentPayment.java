/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.DbTables;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author monder
 */
@Entity
@Table(name = "student_payment", catalog = "newuni", schema = "myUniSchema")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentPayment.findAll", query = "SELECT s FROM StudentPayment s"),
    @NamedQuery(name = "StudentPayment.findById", query = "SELECT s FROM StudentPayment s WHERE s.id = :id"),
    @NamedQuery(name = "StudentPayment.findByPaymentDate", query = "SELECT s FROM StudentPayment s WHERE s.paymentDate = :paymentDate"),
    @NamedQuery(name = "StudentPayment.findByPaymentAmount", query = "SELECT s FROM StudentPayment s WHERE s.paymentAmount = :paymentAmount"),
    @NamedQuery(name = "StudentPayment.findByStutes", query = "SELECT s FROM StudentPayment s WHERE s.stutes = :stutes")})
public class StudentPayment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "payment_date")
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @Basic(optional = false)
    @Column(name = "payment_amount")
    private short paymentAmount;
    @Basic(optional = false)
    @Column(name = "stutes")
    private String stutes;
    @JoinColumn(name = "student_subject_row_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private StudentSub studentSubjectRowId;

    public StudentPayment() {
    }

    public StudentPayment(Integer id) {
        this.id = id;
    }

    public StudentPayment(Integer id, Date paymentDate, short paymentAmount, String stutes) {
        this.id = id;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.stutes = stutes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public short getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(short paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getStutes() {
        return stutes;
    }

    public void setStutes(String stutes) {
        this.stutes = stutes;
    }

    public StudentSub getStudentSubjectRowId() {
        return studentSubjectRowId;
    }

    public void setStudentSubjectRowId(StudentSub studentSubjectRowId) {
        this.studentSubjectRowId = studentSubjectRowId;
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
        if (!(object instanceof StudentPayment)) {
            return false;
        }
        StudentPayment other = (StudentPayment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.StudentPayment[ id=" + id + " ]";
    }
    
}
