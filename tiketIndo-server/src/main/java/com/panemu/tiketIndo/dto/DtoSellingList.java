package com.panemu.tiketIndo.dto;

/**
 *
 * @author bastomi
 */
public class DtoSellingList {

	private String ticketType;
	private Long totalCount;

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
}
