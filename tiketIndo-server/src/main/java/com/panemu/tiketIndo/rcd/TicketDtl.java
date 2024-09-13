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
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Nur Mubin <nur.mubin@panemu.com>
 */
@Entity
@Table(name = "ticket_dtl")
public class TicketDtl implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Basic(optional = false)
	@NotNull
	@Column(name = "venue_id")
	private int venueId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ticket_id")
	private int ticketId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "reseller_id")
	private int resellerId;
	@Size(max = 45)
	@Column(name = "buyer_name")
	private String buyerName;
	@Size(max = 255)
	@Column(name = "buyer_email")
	private String buyerEmail;
	@Size(max = 20)
	@Column(name = "no_ktp")
	private String noKtp;
	@Size(max = 20)
	@Column(name = "phone")
	private String phone;
	@Column(name = "kota_asal")
	private String kotaAsal;
	@Column(name = "umur")
	private int umur;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 45)
	@Column(name = "ticket_code")
	private String ticketCode;
//	@NotNull
	@Size(min = 1, max = 500)
	@Column(name = "ticket_token")
	private String ticketToken;
	@Basic(optional = false)
	@Size(min = 1, max = 45)
	@Column(name = "status")
	private String status;
	@Basic(optional = false)
	@NotNull
	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 45)
	@Column(name = "modified_by")
	private String modifiedBy;
	@Size(min = 1, max = 45)
	@Column(name = "verifier")
	private String verifier;
	@Basic(optional = false)
	@Column(name = "qty")
	private int qty;
	@Basic(optional = false)
	@NotNull
//	@Column(name = "version")
	@Version
	private int version;

	@Transient
	private String type;

	public TicketDtl() {
	}

	public TicketDtl(Integer id) {
		this.id = id;
	}

	public TicketDtl(Integer id, int venueId, int ticketId, int resellerId, String buyerName, String buyerEmail, String noKtp, String phone, String kotaAsal, int umur,
			  String ticketCode, String ticketToken, String status, Date modifiedDate, String modifiedBy, int version) {
		this.id = id;
		this.venueId = venueId;
		this.ticketId = ticketId;
		this.resellerId = resellerId;
		this.buyerName = buyerName;
		this.buyerEmail = buyerEmail;
		this.phone = phone;
		this.kotaAsal = kotaAsal;
		this.umur = umur;
		this.noKtp = noKtp;
		this.ticketCode = ticketCode;
		this.ticketToken = ticketToken;
		this.status = status;
		this.modifiedDate = modifiedDate;
		this.modifiedBy = modifiedBy;
		this.version = version;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getVenueId() {
		return venueId;
	}

	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}

	public int getTicketId() {
		return ticketId;
	}

	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}

	public int getResellerId() {
		return resellerId;
	}

	public void setResellerId(int resellerId) {
		this.resellerId = resellerId;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerEmail() {
		return buyerEmail;
	}

	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}

	public String getNoKtp() {
		return noKtp;
	}

	public void setNoKtp(String noKtp) {
		this.noKtp = noKtp;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getKotaAsal() {
		return kotaAsal;
	}

	public void setKotaAsal(String kotaAsal) {
		this.kotaAsal = kotaAsal;
	}

	public int getUmur() {
		return umur;
	}

	public void setUmur(int umur) {
		this.umur = umur;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
