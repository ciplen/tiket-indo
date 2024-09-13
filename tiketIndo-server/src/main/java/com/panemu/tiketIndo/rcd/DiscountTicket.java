package com.panemu.tiketIndo.rcd;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Alfin <ahmad.alfin@panemu.com>
 */
@Entity
@Table(name = "diskon_tiket")
public class DiscountTicket implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;

	@Column(name = "diskon")
	private Integer diskon;
	@Column(name = "type_ticket")
	private String typeTicket;

	@Column(name = "exp_diskon")
	@Temporal(TemporalType.DATE)
	private Date expDiskon;
	@Column(name = "strt_diskon")
	@Temporal(TemporalType.DATE)
	private Date strtDiskon;

	@Column(name = "kode_diskon")
	private Integer kodeDiskon;
	@Column(name = "min_order")
	private Integer minOrder;

	@Column(name = "maint_id")
	private int maintId;
	@JoinColumn(name = "maint_id", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne //(fetch = FetchType.LAZY)
	private TicketMaint maint;

	public DiscountTicket() {

	}

	public DiscountTicket(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDiskon() {
		return diskon;
	}

	public void setDiskon(Integer diskon) {
		this.diskon = diskon;
	}

	public String getTypeTicket() {
		return typeTicket;
	}

	public void setTypeTicket(String typeTicket) {
		this.typeTicket = typeTicket;
	}

	public Date getExpDiskon() {
		return expDiskon;
	}

	public void setExpDiskon(Date expDiskon) {
		this.expDiskon = expDiskon;
	}

	public Date getStrtDiskon() {
		return strtDiskon;
	}

	public void setStrtDiskon(Date strtDiskon) {
		this.strtDiskon = strtDiskon;
	}

	public Integer getKodeDiskon() {
		return kodeDiskon;
	}

	public void setKodeDiskon(Integer kodeDiskon) {
		this.kodeDiskon = kodeDiskon;
	}

	public Integer getMinOrder() {
		return minOrder;
	}

	public void setMinOrder(Integer minOrder) {
		this.minOrder = minOrder;
	}

	public int getMaintId() {
		return maintId;
	}

	public void setMaintId(int maintId) {
		this.maintId = maintId;
	}

	public TicketMaint getMaint() {
		return maint;
	}

	public void setMaint(TicketMaint maint) {
		this.maint = maint;
	}

}
