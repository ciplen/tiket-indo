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
@Table(name = "book_ticket")
public class BookTicket implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Size(max = 50)
	@Column(name = "nama_pemesan")
	private String nama;
	@Column(name = "no_ktp")
	private String noKtp;
	@Column(name = "no_telepon")
	private String noTelepon;
	@Column(name = "email")
	private String email;
	@Column(name = "umur")
	private String umur;
	@Column(name = "kota_asal")
	private String kotaAsal;
	@Column(name = "rek_tujuan")
	private String rekTujuan;
	@Column(name = "no_rek")
	private String noRek;
	@Column(name = "harga")
	private Integer harga;
	@Column(name = "kode_unik")
	private Integer codeUnique;
	@Column(name = "status")
	private String status;
	@Basic(optional = false)
	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	@Basic(optional = false)
	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	@Basic(optional = false)
	@Column(name = "modified_by")
	private String modifiedBy;
	@Version
	private int version;
//	@NotNull
//	@Size(min = 1, max = 45)
	@Column(name = "ticket_code")
	private String ticketCode;
//	@Size(min = 1, max = 500)
	@Column(name = "ticket_token")
	private String ticketToken;
//	@Basic(optional = false)
//	@Size(min = 1, max = 45)
	@Column(name = "idTicDtl")
	private int idTicDtl;
	@Column(name = "qty")
	private int qty;
	@Column(name = "id_venue")
	private int venueId;
	@Column(name = "event")
	private String event;
	@Column(name = "type_ticket")
	private String typeTicket;
	@Column(name = "id_maint")
	private int maintId;
	@Column(name = "merchandise")
	private String merchandise;

	public BookTicket() {
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

	public String getNoKtp() {
		return noKtp;
	}

	public void setNoKtp(String noKtp) {
		this.noKtp = noKtp;
	}

	public String getNoTelepon() {
		return noTelepon;
	}

	public void setNoTelepon(String noTelepon) {
		this.noTelepon = noTelepon;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUmur() {
		return umur;
	}

	public void setUmur(String umur) {
		this.umur = umur;
	}

	public String getKotaAsal() {
		return kotaAsal;
	}

	public void setKotaAsal(String kotaAsal) {
		this.kotaAsal = kotaAsal;
	}

	public String getRekTujuan() {
		return rekTujuan;
	}

	public void setRekTujuan(String rekTujuan) {
		this.rekTujuan = rekTujuan;
	}

	public String getNoRek() {
		return noRek;
	}

	public void setNoRek(String noRek) {
		this.noRek = noRek;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getTypeTicket() {
		return typeTicket;
	}

	public void setTypeTicket(String typeTicket) {
		this.typeTicket = typeTicket;
	}

	public Integer getHarga() {
		return harga;
	}

	public void setHarga(Integer harga) {
		this.harga = harga;
	}

	public Integer getKodeUnik() {
		return codeUnique;
	}

	public void setKodeUnik(Integer noBooking) {
		this.codeUnique = noBooking;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date createdDate) {
		this.modifiedDate = createdDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVenueId() {
		return venueId;
	}

	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}

	public int getMaintId() {
		return maintId;
	}

	public void setMaintId(int maintId) {
		this.maintId = maintId;
	}

	public int getIdTicDtl() {
		return idTicDtl;
	}

	public void setIdTicDtl(int idTicDtl) {
		this.idTicDtl = idTicDtl;
	}

	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public String getTicketToken() {
		return ticketToken;
	}

	public void setTicketToken(String ticketToken) {
		this.ticketToken = ticketToken;
	}

	public String getMerchandise() {
		return merchandise;
	}

	public void setMerchandise(String merchandise) {
		this.merchandise = merchandise;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}
}
