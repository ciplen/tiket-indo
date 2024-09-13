package com.panemu.tiketIndo.dto;

import com.panemu.tiketIndo.rcd.TicketDtl;

/**
 *
 * @author Nur Mubin <nur.mubin@panemu.com>
 */
public class DtoTicketDtl {

	public int id;
	public int venueId;
	public int ticketId;
	public int resellerId;
	public String buyerName;
	public String buyerEmail;
	public String noKtp;
	public String phone;
	public String ticketCode;
	public String ticketToken;
	public String status;
	public int version;
	public int qty;

	public static DtoTicketDtl create(TicketDtl rcd) {
		DtoTicketDtl dto = new DtoTicketDtl();
		if (rcd != null) {
			dto.id = rcd.getId();
			dto.venueId = rcd.getVenueId();
			dto.resellerId = rcd.getResellerId();
			dto.buyerName = rcd.getBuyerName();
			dto.buyerEmail = rcd.getBuyerEmail();
			dto.noKtp = rcd.getNoKtp();
			dto.phone = rcd.getPhone();
			dto.ticketCode = rcd.getTicketCode();
			dto.ticketToken = rcd.getTicketToken();
			dto.status = rcd.getStatus();
			dto.version = rcd.getVersion();
			dto.qty = rcd.getQty();
		}
		return dto;
	}

}
