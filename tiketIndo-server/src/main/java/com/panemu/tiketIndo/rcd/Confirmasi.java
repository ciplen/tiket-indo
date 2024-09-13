package com.panemu.tiketIndo.rcd;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Alfin <ahmad.alfin@panemu.com>
 */
@Entity
@Table(name = "confirmations")
public class Confirmasi implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Column(name = "id_booking")
	private Integer idBooking;
	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	@Column(name = "kode_unik")
	private Integer kodeUnik;
	@Column(name = "email")
	private String email;
	@Column(name = "path_picture")
	private String pathPicture;

	public Confirmasi() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdBooking() {
		return idBooking;
	}

	public void setIdBooking(Integer idBooking) {
		this.idBooking = idBooking;
	}

	public Integer getKodeUnik() {
		return kodeUnik;
	}

	public void setKodeUnik(Integer kodeUnik) {
		this.kodeUnik = kodeUnik;
	}

	public String getPathPicture() {
		return pathPicture;
	}

	public void setPathPicture(String pathPicture) {
		this.pathPicture = pathPicture;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
