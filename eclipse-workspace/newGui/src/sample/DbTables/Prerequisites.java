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
@Table(name = "prerequisites", catalog = "newuni", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prerequisites.findAll", query = "SELECT p FROM Prerequisites p"),
    @NamedQuery(name = "Prerequisites.findById", query = "SELECT p FROM Prerequisites p WHERE p.id = :id")})
public class Prerequisites implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Subjects subjectId;
    @JoinColumn(name = "prerequisite", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Subjects prerequisite;

    public Prerequisites() {
    }

    public Prerequisites(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Subjects getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Subjects subjectId) {
        this.subjectId = subjectId;
    }

    public Subjects getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(Subjects prerequisite) {
        this.prerequisite = prerequisite;
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
        if (!(object instanceof Prerequisites)) {
            return false;
        }
        Prerequisites other = (Prerequisites) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.Prerequisites[ id=" + id + " ]";
    }
    
}
