package com.panemu.tiketIndo.dto;

import com.panemu.tiketIndo.rcd.DiscountTicket;
import com.panemu.tiketIndo.rcd.TicketMaint;
import java.util.Date;

/**
 *
 * @author Nur Mubin <nur.mubin@panemu.com>
 */
public class DtoTicketMaint {

	public int id;
	public int venueId;
	public String tempat;
	public String type;
	public String keterangan;
	public String namaEvent;
	public int amount;
	public int jumlahTerjual;
	public Date tanggalPeriode1;
	public Date tanggalPeriode2;
	public Date tanggalPeriode3;
	public Date tanggalPeriode4;
	public Date tanggalPeriode5;
	public Date tanggalAwal;
	public int hargaPeriode1;
	public int discountPrice;
	public int hargaPeriode2;
	public int hargaPeriode3;
	public int hargaPeriode4;
	public int hargaPeriode5;
	public int version;

	public int minOrder;
	public int diskon;
	public String typeTicket;
	public int codeDiscount;
	public Date expDiskon;
	public Date strtDiskon;

	public static DtoTicketMaint create(TicketMaint rcd) {
		DtoTicketMaint dto = new DtoTicketMaint();
		if (rcd != null) {
			dto.id = rcd.getId();
			dto.venueId = rcd.getVenue().getId();
			dto.tanggalAwal = rcd.getVenue().getTanggalAwal();
			dto.type = rcd.getType();
			dto.typeTicket = rcd.getType();
			dto.expDiskon = rcd.getTanggalPeriode1();
			dto.tempat = rcd.getVenue().getTempat();
			dto.namaEvent = rcd.getVenue().getNama();
			dto.keterangan = rcd.getKeterangan();
			dto.amount = rcd.getAmount();
			dto.jumlahTerjual = rcd.getJumlahTerjual();
			dto.tanggalPeriode1 = rcd.getTanggalPeriode1();
			if (rcd.getDiscountPrice() != null) {
				dto.discountPrice = rcd.getDiscountPrice();
			}
			if (rcd.getTanggalPeriode2() != null) {
				dto.tanggalPeriode2 = rcd.getTanggalPeriode2();
			}
			if (rcd.getTanggalPeriode3() != null) {
				dto.tanggalPeriode3 = rcd.getTanggalPeriode3();
			}
			if (rcd.getTanggalPeriode4() != null) {
				dto.tanggalPeriode4 = rcd.getTanggalPeriode4();
			}
			if (rcd.getTanggalPeriode5() != null) {
				dto.tanggalPeriode5 = rcd.getTanggalPeriode5();
			}
			dto.hargaPeriode1 = rcd.getHargaPeriode1();
			dto.hargaPeriode2 = rcd.getHargaPeriode2();
			dto.hargaPeriode3 = rcd.getHargaPeriode3();
			dto.hargaPeriode4 = rcd.getHargaPeriode4();
			dto.hargaPeriode5 = rcd.getHargaPeriode5();
			dto.version = rcd.getVersion();
		}
		return dto;
	}
}
