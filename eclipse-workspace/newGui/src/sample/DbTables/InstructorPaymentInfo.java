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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "instructor_payment_info", catalog = "newuni", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstructorPaymentInfo.findAll", query = "SELECT i FROM InstructorPaymentInfo i"),
    @NamedQuery(name = "InstructorPaymentInfo.findById", query = "SELECT i FROM InstructorPaymentInfo i WHERE i.id = :id"),
    @NamedQuery(name = "InstructorPaymentInfo.findByInstructorId", query = "SELECT i FROM InstructorPaymentInfo i WHERE i.instructorId = :instructorId"),
    @NamedQuery(name = "InstructorPaymentInfo.findByAccountNumber", query = "SELECT i FROM InstructorPaymentInfo i WHERE i.accountNumber = :accountNumber"),
    @NamedQuery(name = "InstructorPaymentInfo.findByBankName", query = "SELECT i FROM InstructorPaymentInfo i WHERE i.bankName = :bankName")})
public class InstructorPaymentInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "instructor_id")
    private short instructorId;
    @Basic(optional = false)
    @Column(name = "account_number")
    private int accountNumber;
    @Basic(optional = false)
    @Column(name = "bank_name")
    private String bankName;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Instructors instructors;

    public InstructorPaymentInfo() {
    }

    public InstructorPaymentInfo(Short id) {
        this.id = id;
    }

    public InstructorPaymentInfo(Short id, short instructorId, int accountNumber, String bankName) {
        this.id = id;
        this.instructorId = instructorId;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public short getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(short instructorId) {
        this.instructorId = instructorId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Instructors getInstructors() {
        return instructors;
    }

    public void setInstructors(Instructors instructors) {
        this.instructors = instructors;
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
        if (!(object instanceof InstructorPaymentInfo)) {
            return false;
        }
        InstructorPaymentInfo other = (InstructorPaymentInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.InstructorPaymentInfo[ id=" + id + " ]";
    }
    
}
