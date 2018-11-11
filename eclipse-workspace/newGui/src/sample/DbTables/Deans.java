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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author monder
 */
@Entity
@Table(name = "deans", catalog = "newuni", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Deans.findAll", query = "SELECT d FROM Deans d"),
    @NamedQuery(name = "Deans.findById", query = "SELECT d FROM Deans d WHERE d.id = :id")})
public class Deans implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "instructor_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Instructors instructorId;
    @JoinColumn(name = "major", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Department major;

    public Deans() {
    }

    public Deans(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instructors getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Instructors instructorId) {
        this.instructorId = instructorId;
    }

    public Department getMajor() {
        return major;
    }

    public void setMajor(Department major) {
        this.major = major;
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
        if (!(object instanceof Deans)) {
            return false;
        }
        Deans other = (Deans) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.Deans[ id=" + id + " ]";
    }
    
}
