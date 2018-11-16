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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author monder
 */
@Entity
@Table(catalog = "university", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"social_number"})})
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s"),
    @NamedQuery(name = "Student.findById", query = "SELECT s FROM Student s WHERE s.id = :id"),
    @NamedQuery(name = "Student.findByFname", query = "SELECT s FROM Student s WHERE s.fname = :fname"),
    @NamedQuery(name = "Student.findByMname", query = "SELECT s FROM Student s WHERE s.mname = :mname"),
    @NamedQuery(name = "Student.findByLname", query = "SELECT s FROM Student s WHERE s.lname = :lname"),
    @NamedQuery(name = "Student.findByNationalty", query = "SELECT s FROM Student s WHERE s.nationalty = :nationalty"),
    @NamedQuery(name = "Student.findByCity", query = "SELECT s FROM Student s WHERE s.city = :city"),
    @NamedQuery(name = "Student.findByAddress", query = "SELECT s FROM Student s WHERE s.address = :address"),
    @NamedQuery(name = "Student.findBySocialNumber", query = "SELECT s FROM Student s WHERE s.socialNumber = :socialNumber"),
    @NamedQuery(name = "Student.findByPhoneNumber", query = "SELECT s FROM Student s WHERE s.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "Student.findByStatus", query = "SELECT s FROM Student s WHERE s.status = :status"),
    @NamedQuery(name = "Student.findByImage", query = "SELECT s FROM Student s WHERE s.image = :image"),
    @NamedQuery(name = "Student.findByNotes", query = "SELECT s FROM Student s WHERE s.notes = :notes"),
    @NamedQuery(name = "Student.findByMedicalRecord", query = "SELECT s FROM Student s WHERE s.medicalRecord = :medicalRecord")})
public class Student implements Serializable {

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
    @Column(nullable = false, length = 30)
    private String nationalty;
    @Basic(optional = false)
    @Column(nullable = false, length = 20)
    private String city;
    @Basic(optional = false)
    @Column(nullable = false, length = 50)
    private String address;
    @Basic(optional = false)
    @Column(name = "social_number", nullable = false, length = 13)
    private String socialNumber;
    @Basic(optional = false)
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;
    @Basic(optional = false)
    @Column(nullable = false)
    private boolean status;
    @Basic(optional = false)
    @Column(nullable = false, length = 50)
    private String image;
    @Basic(optional = false)
    @Column(nullable = false, length = 250)
    private String notes;
    @Basic(optional = false)
    @Column(name = "medical_record", nullable = false, length = 50)
    private String medicalRecord;
    @JoinColumns({
        @JoinColumn(name = "academic_year", referencedColumnName = "academic_year", nullable = false),
        @JoinColumn(name = "sub_department_id", referencedColumnName = "sub_department_id", nullable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Section section;
    @JoinColumn(name = "semester_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Semester semesterId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentId", fetch = FetchType.LAZY)
    private List<StudentCourse> studentCourseList;

    public Student() {
    }

    public Student(Integer id) {
        this.id = id;
    }

    public Student(Integer id, String fname, String mname, String lname, String nationalty, String city, String address, String socialNumber, String phoneNumber, boolean status, String image, String notes, String medicalRecord) {
        this.id = id;
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.nationalty = nationalty;
        this.city = city;
        this.address = address;
        this.socialNumber = socialNumber;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.image = image;
        this.notes = notes;
        this.medicalRecord = medicalRecord;
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

    public String getNationalty() {
        return nationalty;
    }

    public void setNationalty(String nationalty) {
        this.nationalty = nationalty;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSocialNumber() {
        return socialNumber;
    }

    public void setSocialNumber(String socialNumber) {
        this.socialNumber = socialNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(String medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Semester getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Semester semesterId) {
        this.semesterId = semesterId;
    }

    public List<StudentCourse> getStudentCourseList() {
        return studentCourseList;
    }

    public void setStudentCourseList(List<StudentCourse> studentCourseList) {
        this.studentCourseList = studentCourseList;
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
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "university.Entites.Student[ id=" + id + " ]";
    }
    
}
