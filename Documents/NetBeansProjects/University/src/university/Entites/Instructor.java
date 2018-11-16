/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university.Entites;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author monder
 */
@Entity
@Table(catalog = "university", schema = "")
@NamedQueries({
    @NamedQuery(name = "Instructor.findAll", query = "SELECT i FROM Instructor i"),
    @NamedQuery(name = "Instructor.findById", query = "SELECT i FROM Instructor i WHERE i.id = :id"),
    @NamedQuery(name = "Instructor.findByFname", query = "SELECT i FROM Instructor i WHERE i.fname = :fname"),
    @NamedQuery(name = "Instructor.findByMname", query = "SELECT i FROM Instructor i WHERE i.mname = :mname"),
    @NamedQuery(name = "Instructor.findByLname", query = "SELECT i FROM Instructor i WHERE i.lname = :lname"),
    @NamedQuery(name = "Instructor.findByDob", query = "SELECT i FROM Instructor i WHERE i.dob = :dob"),
    @NamedQuery(name = "Instructor.findByStartingDate", query = "SELECT i FROM Instructor i WHERE i.startingDate = :startingDate"),
    @NamedQuery(name = "Instructor.findByDegree", query = "SELECT i FROM Instructor i WHERE i.degree = :degree"),
    @NamedQuery(name = "Instructor.findBySocialnumber", query = "SELECT i FROM Instructor i WHERE i.socialnumber = :socialnumber"),
    @NamedQuery(name = "Instructor.findByAccountNumber", query = "SELECT i FROM Instructor i WHERE i.accountNumber = :accountNumber"),
    @NamedQuery(name = "Instructor.findByBankName", query = "SELECT i FROM Instructor i WHERE i.bankName = :bankName"),
    @NamedQuery(name = "Instructor.findByNationality", query = "SELECT i FROM Instructor i WHERE i.nationality = :nationality"),
    @NamedQuery(name = "Instructor.findByMedicalstate", query = "SELECT i FROM Instructor i WHERE i.medicalstate = :medicalstate")})
public class Instructor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String fname;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String mname;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String lname;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Basic(optional = false)
    @Column(name = "starting_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startingDate;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String degree;
    @Basic(optional = false)
    @Column(nullable = false)
    private int socialnumber;
    @Basic(optional = false)
    @Column(name = "account_number", nullable = false, length = 15)
    private String accountNumber;
    @Basic(optional = false)
    @Column(name = "bank_name", nullable = false, length = 20)
    private String bankName;
    @Basic(optional = false)
    @Column(nullable = false)
    private int nationality;
    @Column(length = 250)
    private String medicalstate;
    @JoinColumn(name = "department_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Department departmentId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instructorId", fetch = FetchType.LAZY)
    private List<Course> courseList;
    @OneToOne(mappedBy = "instructorId", fetch = FetchType.LAZY)
    private Department department;

    public Instructor() {
    }

    public Instructor(Integer id) {
        this.id = id;
    }

    public Instructor(Integer id, String fname, String mname, String lname, Date dob, Date startingDate, String degree, int socialnumber, String accountNumber, String bankName, int nationality) {
        this.id = id;
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.dob = dob;
        this.startingDate = startingDate;
        this.degree = degree;
        this.socialnumber = socialnumber;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.nationality = nationality;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public int getSocialnumber() {
        return socialnumber;
    }

    public void setSocialnumber(int socialnumber) {
        this.socialnumber = socialnumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getNationality() {
        return nationality;
    }

    public void setNationality(int nationality) {
        this.nationality = nationality;
    }

    public String getMedicalstate() {
        return medicalstate;
    }

    public void setMedicalstate(String medicalstate) {
        this.medicalstate = medicalstate;
    }

    public Department getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Department departmentId) {
        this.departmentId = departmentId;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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
        if (!(object instanceof Instructor)) {
            return false;
        }
        Instructor other = (Instructor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "university.Entites.Instructor[ id=" + id + " ]";
    }
    
}
