package com.panemu.tiketIndo.rcd;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ticket_maint")
public class TicketMaint implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Size(max = 50)
	@Column(name = "type")
	private String type;
	@Size(max = 500)
	@Column(name = "keterangan")
	private String keterangan;
	@Column(name = "diskon_id")
	private Integer diskonId;
	@Column(name = "min_order")
	private Integer minOrder;
	@Column(name = "discount")
	private Integer discount;
	@Column(name = "amount")
	private Integer amount;
	@Column(name = "jumlah_terjual")
	private Integer jumlahTerjual;
	@Column(name = "tanggal_periode1")
	@Temporal(TemporalType.DATE)
	private Date tanggalPeriode1;
	@Column(name = "tanggal_periode2")
	@Temporal(TemporalType.DATE)
	private Date tanggalPeriode2;
	@Column(name = "tanggal_periode3")
	@Temporal(TemporalType.DATE)
	private Date tanggalPeriode3;
	@Column(name = "tanggal_periode4")
	@Temporal(TemporalType.DATE)
	private Date tanggalPeriode4;
	@Column(name = "tanggal_periode5")
	@Temporal(TemporalType.DATE)
	private Date tanggalPeriode5;
	@Column(name = "discount_price")
	private Integer discountPrice;
	@Column(name = "harga_periode1")
	private Integer hargaPeriode1;
	@Column(name = "harga_periode2")
	private Integer hargaPeriode2;
	@Column(name = "harga_periode3")
	private Integer hargaPeriode3;
	@Column(name = "harga_periode4")
	private Integer hargaPeriode4;
	@Column(name = "harga_periode5")
	private Integer hargaPeriode5;
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
	@Version
	private int version;
	@Column(name = "venue_id")
	private int venueId;
	@JoinColumn(name = "venue_id", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne //(fetch = FetchType.LAZY)
	private VenueMaint venue;

	public TicketMaint() {
	}

	public TicketMaint(Integer id) {
		this.id = id;
	}

	public TicketMaint(Integer id, Date modifiedDate, String modifiedBy, int version) {
		this.id = id;
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

	public Integer getDiskonId() {
		return diskonId;
	}

	public void setDiskonId(Integer diskonId) {
		this.diskonId = diskonId;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public Integer getMinOrder() {
		return minOrder;
	}

	public void setMinOrder(Integer minOrder) {
		this.minOrder = minOrder;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getJumlahTerjual() {
		return jumlahTerjual;
	}

	public void setJumlahTerjual(Integer jumlahTerjual) {
		this.jumlahTerjual = jumlahTerjual;
	}

	public Date getTanggalPeriode1() {
		return tanggalPeriode1;
	}

	public void setTanggalPeriode1(Date tanggalPeriode1) {
		this.tanggalPeriode1 = tanggalPeriode1;
	}

	public Date getTanggalPeriode2() {
		return tanggalPeriode2;
	}

	public void setTanggalPeriode2(Date tanggalPeriode2) {
		this.tanggalPeriode2 = tanggalPeriode2;
	}

	public Date getTanggalPeriode3() {
		return tanggalPeriode3;
	}

	public void setTanggalPeriode3(Date tanggalPeriode3) {
		this.tanggalPeriode3 = tanggalPeriode3;
	}

	public Date getTanggalPeriode4() {
		return tanggalPeriode4;
	}

	public void setTanggalPeriode4(Date tanggalPeriode4) {
		this.tanggalPeriode4 = tanggalPeriode4;
	}

	public Date getTanggalPeriode5() {
		return tanggalPeriode5;
	}

	public void setTanggalPeriode5(Date tanggalPeriode5) {
		this.tanggalPeriode5 = tanggalPeriode5;
	}

	public Integer getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Integer discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Integer getHargaPeriode1() {
		return hargaPeriode1;
	}

	public void setHargaPeriode1(Integer hargaPeriode1) {
		this.hargaPeriode1 = hargaPeriode1;
	}

	public Integer getHargaPeriode2() {
		return hargaPeriode2;
	}

	public void setHargaPeriode2(Integer hargaPeriode2) {
		this.hargaPeriode2 = hargaPeriode2;
	}

	public Integer getHargaPeriode3() {
		return hargaPeriode3;
	}

	public void setHargaPeriode3(Integer hargaPeriode3) {
		this.hargaPeriode3 = hargaPeriode3;
	}

	public Integer getHargaPeriode4() {
		return hargaPeriode4;
	}

	public void setHargaPeriode4(Integer hargaPeriode4) {
		this.hargaPeriode4 = hargaPeriode4;
	}

	public Integer getHargaPeriode5() {
		return hargaPeriode5;
	}

	public void setHargaPeriode5(Integer hargaPeriode5) {
		this.hargaPeriode5 = hargaPeriode5;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public VenueMaint getVenue() {
		return venue;
	}

	public void setVenue(VenueMaint venueId) {
		this.venue = venueId;
	}

	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}

	public int getVenueId() {
		return venueId;
	}

}
