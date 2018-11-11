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
@Table(name = "instructors", catalog = "newuni", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Instructors.findAll", query = "SELECT i FROM Instructors i"),
    @NamedQuery(name = "Instructors.findById", query = "SELECT i FROM Instructors i WHERE i.id = :id"),
    @NamedQuery(name = "Instructors.findByName", query = "SELECT i FROM Instructors i WHERE i.name = :name"),
    @NamedQuery(name = "Instructors.findByDegree", query = "SELECT i FROM Instructors i WHERE i.degree = :degree"),
    @NamedQuery(name = "Instructors.findByMajoresIn", query = "SELECT i FROM Instructors i WHERE i.majoresIn = :majoresIn")})
public class Instructors implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "degree")
    private String degree;
    @Basic(optional = false)
    @Column(name = "majores_in")
    private String majoresIn;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id2")
    private List<InstructorPaymentLog> instructorPaymentLogList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instructorId")
    private List<InstructorSub> instructorSubList;
    @JoinColumn(name = "department", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Department department;
    @JoinColumn(name = "semester_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Semester semesterId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instructorId")
    private List<Deans> deansList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "instructors")
    private InstructorPaymentInfo instructorPaymentInfo;

    public Instructors() {
    }

    public Instructors(Short id) {
        this.id = id;
    }

    public Instructors(Short id, String name, String degree, String majoresIn) {
        this.id = id;
        this.name = name;
        this.degree = degree;
        this.majoresIn = majoresIn;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMajoresIn() {
        return majoresIn;
    }

    public void setMajoresIn(String majoresIn) {
        this.majoresIn = majoresIn;
    }

    @XmlTransient
    public List<InstructorPaymentLog> getInstructorPaymentLogList() {
        return instructorPaymentLogList;
    }

    public void setInstructorPaymentLogList(List<InstructorPaymentLog> instructorPaymentLogList) {
        this.instructorPaymentLogList = instructorPaymentLogList;
    }

    @XmlTransient
    public List<InstructorSub> getInstructorSubList() {
        return instructorSubList;
    }

    public void setInstructorSubList(List<InstructorSub> instructorSubList) {
        this.instructorSubList = instructorSubList;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Semester getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Semester semesterId) {
        this.semesterId = semesterId;
    }

    @XmlTransient
    public List<Deans> getDeansList() {
        return deansList;
    }

    public void setDeansList(List<Deans> deansList) {
        this.deansList = deansList;
    }

    public InstructorPaymentInfo getInstructorPaymentInfo() {
        return instructorPaymentInfo;
    }

    public void setInstructorPaymentInfo(InstructorPaymentInfo instructorPaymentInfo) {
        this.instructorPaymentInfo = instructorPaymentInfo;
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
        if (!(object instanceof Instructors)) {
            return false;
        }
        Instructors other = (Instructors) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.Instructors[ id=" + id + " ]";
    }
    
}
