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
@Table(name = "countries", catalog = "newuni", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Countries.findAll", query = "SELECT c FROM Countries c"),
    @NamedQuery(name = "Countries.findByNumCode", query = "SELECT c FROM Countries c WHERE c.numCode = :numCode"),
    @NamedQuery(name = "Countries.findByAlpha2Code", query = "SELECT c FROM Countries c WHERE c.alpha2Code = :alpha2Code"),
    @NamedQuery(name = "Countries.findByAlpha3Code", query = "SELECT c FROM Countries c WHERE c.alpha3Code = :alpha3Code"),
    @NamedQuery(name = "Countries.findByEnShortName", query = "SELECT c FROM Countries c WHERE c.enShortName = :enShortName"),
    @NamedQuery(name = "Countries.findByNationality", query = "SELECT c FROM Countries c WHERE c.nationality = :nationality")})
public class Countries implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "num_code")
    private Integer numCode;
    @Column(name = "alpha_2_code")
    private String alpha2Code;
    @Column(name = "alpha_3_code")
    private String alpha3Code;
    @Column(name = "en_short_name")
    private String enShortName;
    @Column(name = "nationality")
    private String nationality;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nationality")
    private List<Student> studentList;

    public Countries() {
    }

    public Countries(Integer numCode) {
        this.numCode = numCode;
    }

    public Integer getNumCode() {
        return numCode;
    }

    public void setNumCode(Integer numCode) {
        this.numCode = numCode;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }

    public void setAlpha3Code(String alpha3Code) {
        this.alpha3Code = alpha3Code;
    }

    public String getEnShortName() {
        return enShortName;
    }

    public void setEnShortName(String enShortName) {
        this.enShortName = enShortName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @XmlTransient
    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numCode != null ? numCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Countries)) {
            return false;
        }
        Countries other = (Countries) object;
        if ((this.numCode == null && other.numCode != null) || (this.numCode != null && !this.numCode.equals(other.numCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.Countries[ numCode=" + numCode + " ]";
    }
    
}
