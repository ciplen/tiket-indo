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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author bastomi
 */
@Entity
@Table(name = "member")
public class Member implements Serializable {

	@Version
	private int version;

	@Transient
	private List<MemberSetting> memberSettingList;

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "username")
	private String username;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "password")
	private String password;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "password_salt")
	private String passwordSalt;
	// @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
	@Size(max = 255)
	@Column(name = "email")
	private String email;
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
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 20)
	@Column(name = "role")
	private String role;
	@Column(name = "koordinator")
	private String koordinator;
	@Column(name = "path_picture")
	private String pathPicture;

	public Member() {
	}

	public Member(Integer id) {
		this.id = id;
	}

	public Member(Integer id, String username, String password, String passwordSalt, int version, String modifiedBy, Date modifiedDate, String role) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.passwordSalt = passwordSalt;
		this.version = version;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.role = role;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getKoordinator() {
		return koordinator;
	}

	public void setKoordinator(String koordinator) {
		this.koordinator = koordinator;
	}

	public String getPathPicture() {
		return pathPicture;
	}

	public void setPathPicture(String pathPicture) {
		this.pathPicture = pathPicture;
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
		if (!(object instanceof Member)) {
			return false;
		}
		Member other = (Member) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.panemu.panict.rcd.Member[ id=" + id + " ]";
	}

	public List<MemberSetting> getMemberSettingList() {
		return memberSettingList;
	}

	public void setMemberSettingList(List<MemberSetting> memberSettingList) {
		this.memberSettingList = memberSettingList;
	}
}
