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
import javax.persistence.Lob;
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
@Table(name = "student", catalog = "newuni", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s"),
    @NamedQuery(name = "Student.findByStudentId", query = "SELECT s FROM Student s WHERE s.studentId = :studentId"),
    @NamedQuery(name = "Student.findByName", query = "SELECT s FROM Student s WHERE s.name = :name"),
    @NamedQuery(name = "Student.findByAddress", query = "SELECT s FROM Student s WHERE s.address = :address"),
    @NamedQuery(name = "Student.findByPhone", query = "SELECT s FROM Student s WHERE s.phone = :phone"),
    @NamedQuery(name = "Student.findBySocialNumber", query = "SELECT s FROM Student s WHERE s.socialNumber = :socialNumber"),
    @NamedQuery(name = "Student.findByNotes", query = "SELECT s FROM Student s WHERE s.notes = :notes"),
    @NamedQuery(name = "Student.findByStatus", query = "SELECT s FROM Student s WHERE s.status = :status"),
    @NamedQuery(name = "Student.findBySemesterCount", query = "SELECT s FROM Student s WHERE s.semesterCount = :semesterCount")})
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "student_id")
    private Integer studentId;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @Column(name = "phone")
    private int phone;
    @Column(name = "social_number")
    private String socialNumber;
    @Basic(optional = false)
    @Column(name = "notes")
    private String notes;
    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;
    @Basic(optional = false)
    @Column(name = "semester_count")
    private String semesterCount;
    @Lob
    @Column(name = "image")
    private String image;
    @JoinColumn(name = "sub_major", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SubDepartment subMajor;
    @JoinColumn(name = "nationality", referencedColumnName = "num_code")
    @ManyToOne(optional = false)
    private Countries nationality;
    @JoinColumn(name = "semster_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Semester semsterId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentId")
    private List<StudentSub> studentSubList;

    public Student() {
    }

    public Student(Integer studentId) {
        this.studentId = studentId;
    }

    public Student(Integer studentId, String name, String address, int phone, String notes, boolean status, String semesterCount) {
        this.studentId = studentId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.notes = notes;
        this.status = status;
        this.semesterCount = semesterCount;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getSocialNumber() {
        return socialNumber;
    }

    public void setSocialNumber(String socialNumber) {
        this.socialNumber = socialNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getSemesterCount() {
        return semesterCount;
    }

    public void setSemesterCount(String semesterCount) {
        this.semesterCount = semesterCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public SubDepartment getSubMajor() {
        return subMajor;
    }

    public void setSubMajor(SubDepartment subMajor) {
        this.subMajor = subMajor;
    }

    public Countries getNationality() {
        return nationality;
    }

    public void setNationality(Countries nationality) {
        this.nationality = nationality;
    }

    public Semester getSemsterId() {
        return semsterId;
    }

    public void setSemsterId(Semester semsterId) {
        this.semsterId = semsterId;
    }

    @XmlTransient
    public List<StudentSub> getStudentSubList() {
        return studentSubList;
    }

    public void setStudentSubList(List<StudentSub> studentSubList) {
        this.studentSubList = studentSubList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (studentId != null ? studentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.studentId == null && other.studentId != null) || (this.studentId != null && !this.studentId.equals(other.studentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.Student[ studentId=" + studentId + " ]";
    }
    
}
