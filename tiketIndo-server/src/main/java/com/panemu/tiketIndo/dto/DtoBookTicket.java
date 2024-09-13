package com.panemu.tiketIndo.dto;

import com.panemu.tiketIndo.rcd.BookTicket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alfin <ahmad.alfin@panemu.com>
 */
public class DtoBookTicket {

	public List<Identity> identityList;
	public int id;
	public int venueId;
	public int idTicMaint;
	public String email;
	public String noTelepon;
	public String umur;
	public String kotaAsal;
	public int hargaTotal;
	public int hargaCode;
	public int hargaPeriode1;
	public int hargaPeriode5;
	public int hargaCodeTaxDis;
	public String ticketCode;
	public String ticketToken;
	public String pathPicture;
	public String status;
	public String noRek;
	public String buyerName;
	public String event;
	public String type;
	public String an;
	public String rekTujuan;
	public String qty;
	public int codeUnique;
	public String merchandise;

	public static List<BookTicket> createRecord(DtoBookTicket dto) {
		List<BookTicket> lst = new ArrayList<>();

		for (Identity identity : dto.identityList) {
			BookTicket bookTicket = new BookTicket();
			bookTicket.setNama(identity.nama);
			bookTicket.setNoKtp(identity.ktp);
			bookTicket.setNoTelepon(dto.noTelepon);
			bookTicket.setEmail(dto.email);
			bookTicket.setUmur(dto.umur);
			bookTicket.setKotaAsal(dto.kotaAsal);
			bookTicket.setHarga(dto.hargaCodeTaxDis);
			bookTicket.setEvent(dto.event);
			bookTicket.setTypeTicket(dto.type);
			bookTicket.setTicketCode(dto.ticketCode);
			bookTicket.setTicketToken(dto.ticketToken);
			bookTicket.setVenueId(dto.venueId);
			bookTicket.setMaintId(dto.idTicMaint);
			bookTicket.setNoRek(dto.noRek);
			bookTicket.setRekTujuan(dto.rekTujuan);
			bookTicket.setKodeUnik(dto.codeUnique);
			bookTicket.setQty(Integer.parseInt(dto.qty));
			bookTicket.setMerchandise(identity.tipeKaos + "," + identity.ukuranKaos);
			lst.add(bookTicket);
		}
		return lst;
	}

	public static class Identity {

		public String nama;
		public String ktp;
		public String tipeKaos;
		public String ukuranKaos;
	}
}
