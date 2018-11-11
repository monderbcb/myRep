/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.DbTables;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "mac_pc", catalog = "newuni", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MacPc.findAll", query = "SELECT m FROM MacPc m"),
    @NamedQuery(name = "MacPc.findById", query = "SELECT m FROM MacPc m WHERE m.id = :id"),
    @NamedQuery(name = "MacPc.findByMacAddress", query = "SELECT m FROM MacPc m WHERE m.macAddress = :macAddress"),
    @NamedQuery(name = "MacPc.findByPcName", query = "SELECT m FROM MacPc m WHERE m.pcName = :pcName")})
public class MacPc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "mac_address")
    private String macAddress;
    @Basic(optional = false)
    @Column(name = "pc_name")
    private String pcName;
    @OneToMany(mappedBy = "pcMac")
    private List<Log> logList;

    public MacPc() {
    }

    public MacPc(Integer id) {
        this.id = id;
    }

    public MacPc(Integer id, String macAddress, String pcName) {
        this.id = id;
        this.macAddress = macAddress;
        this.pcName = pcName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    @XmlTransient
    public List<Log> getLogList() {
        return logList;
    }

    public void setLogList(List<Log> logList) {
        this.logList = logList;
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
        if (!(object instanceof MacPc)) {
            return false;
        }
        MacPc other = (MacPc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.DbTables.MacPc[ id=" + id + " ]";
    }
    
}
