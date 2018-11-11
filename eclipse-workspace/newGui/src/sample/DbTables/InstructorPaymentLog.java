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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author monder
 */
@Entity
@Table(name = "instructor_payment_log", catalog = "newuni", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstructorPaymentLog.findAll", query = "SELECT i FROM InstructorPaymentLog i"),
    @NamedQuery(name = "InstructorPaymentLog.findById", query = "SELECT i FROM InstructorPaymentLog i WHERE i.id = :id"),
    @NamedQuery(name = "InstructorPaymentLog.findByPaymentDate", query = "SELECT i FROM InstructorPaymentLog i WHERE i.paymentDate = :paymentDate"),
    @NamedQuery(name = "InstructorPaymentLog.findByPaymentAmount", query = "SELECT i FROM InstructorPaymentLog i WHERE i.paymentAmount = :paymentAmount")})
public class InstructorPaymentLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "payment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;
    @Basic(optional = false)
    @Column(name = "payment_amount")
    private int paymentAmount;
    @JoinColumn(name = "id2", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Instructors id2;

    public InstructorPaymentLog() {
    }

    public InstructorPaymentLog(Integer id) {
        this.id = id;
    }

    public InstructorPaymentLog(Integer id, Date paymentDate, int paymentAmount) {
        this.id = id;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
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

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Instructors getId2() {
        return id2;
    }

    public void setId2(Instructors id2) {
        this.id2 = id2;
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
        if (!(object instanceof InstructorPaymentLog)) {
            return false;
        }
        InstructorPaymentLog other = (InstructorPaymentLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.InstructorPaymentLog[ id=" + id + " ]";
    }
    
}
