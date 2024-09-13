package com.panemu.tiketIndo.rcd;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author amrullah
 */
@Entity
@Table(name = "transfer_code")
public class TransferCode implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
   @Basic(optional = false)
   @NotNull
   @Column(name = "id")
	private Integer id;
	@Basic(optional = false)
   @NotNull
   @Size(min = 1, max = 10)
   @Column(name = "status")
	private String status;
	@Basic(optional = false)
   @NotNull
   @Column(name = "modified_on")
   @Temporal(TemporalType.TIMESTAMP)
	private Date modifiedOn;
	@Basic(optional = false)
   @NotNull
   @Column(name = "version")
	private int version;

	public TransferCode() {
	}

	public TransferCode(Integer id) {
		this.id = id;
	}

	public TransferCode(Integer id, String status, Date modifiedOn, int version) {
		this.id = id;
		this.status = status;
		this.modifiedOn = modifiedOn;
		this.version = version;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
