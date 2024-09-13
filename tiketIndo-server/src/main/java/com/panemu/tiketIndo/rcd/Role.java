package com.panemu.tiketIndo.rcd;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author amrullah
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "id")
	private Integer id;
	@Basic(optional = false)
   @NotNull
   @Size(min = 1, max = 20)
   @Column(name = "name")
	private String name;
	@Size(max = 255)
   @Column(name = "description")
	private String description;
	@Basic(optional = false)
   @NotNull
   @Size(min = 1, max = 50)
   @Column(name = "modified_by")
	private String modifiedBy;
	@Basic(optional = false)
   @NotNull
   @Column(name = "modified_date")
   @Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	@Version
	private int version;
	@JoinTable(name = "role_permission", joinColumns = {
   	@JoinColumn(name = "role", referencedColumnName = "name")}, inverseJoinColumns = {
   	@JoinColumn(name = "permission", referencedColumnName = "code")})
   @ManyToMany
	private List<Permission> permissionList;

	public Role() {
	}

	public Role(Integer id) {
		this.id = id;
	}

	public Role(Integer id, String name, String modifiedBy, Date modifiedDate, int version) {
		this.id = id;
		this.name = name;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.version = version;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<Permission> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
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
		if (!(object instanceof Role)) {
			return false;
		}
		Role other = (Role) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.panemu.panict.rcd.Role[ id=" + id + " ]";
	}
	
}
