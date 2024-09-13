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
 * @author Nur Mubin <nur.mubin@panemu.com>
 */
@Entity
@Table(name = "venue_maint")
public class VenueMaint implements Serializable {

	@Version
	private int version;

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Size(max = 50)
	@Column(name = "nama")
	private String nama;
	@Size(max = 20)
	@Column(name = "type_venue")
	private String typeVenue;
	@Size(max = 50)
	@Column(name = "tempat")
	private String tempat;
	@Column(name = "tanggal_awal")
	@Temporal(TemporalType.DATE)
	private Date tanggalAwal;
	@Column(name = "tanggal_akhir")
	@Temporal(TemporalType.DATE)
	private Date tanggalAkhir;
        @Size(max = 500)
        @Column(name = "small_banner")
        private String smallBanner;
        @Size(max = 500)
        @Column(name = "big_banner")
        private String bigBanner;
	@Basic(optional = false)
	@NotNull
	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "modified_by")
	private String modifiedBy;

	public VenueMaint() {
	}

	public VenueMaint(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getTypeVenue() {
		return typeVenue;
	}

	public void setTypeVenue(String typeVenue) {
		this.typeVenue = typeVenue;
	}

	public String getTempat() {
		return tempat;
	}

	public void setTempat(String tempat) {
		this.tempat = tempat;
	}

	public Date getTanggalAwal() {
		return tanggalAwal;
	}

	public void setTanggalAwal(Date tanggalAwal) {
		this.tanggalAwal = tanggalAwal;
	}

	public Date getTanggalAkhir() {
		return tanggalAkhir;
	}

	public void setTanggalAkhir(Date tanggalAkhir) {
		this.tanggalAkhir = tanggalAkhir;
	}
        
        public String getSmallBanner() {
		return smallBanner;
	}

	public void setSmallBanner(String smallBanner) {
		this.smallBanner = smallBanner;
	}
        
        public String getBigBanner() {
		return bigBanner;
	}

	public void setBigBanner(String bigBanner) {
		this.bigBanner = bigBanner;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
