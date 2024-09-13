package com.panemu.tiketIndo.dto;

import com.panemu.tiketIndo.rcd.VenueMaint;
import java.util.Date;

/**
 *
 * @author Nur Mubin <nur.mubin@panemu.com>
 */
public class DtoVenueMaint {

	public int id;
	public String nama;
	public String tempat;
	public String typeVenue;
	public Date tanggalAwal;
	public Date tanggalAkhir;
	public String smallBanner;
        public String bigBanner;
	public int version;

	public static DtoVenueMaint create(VenueMaint rcd) {
		DtoVenueMaint dto = new DtoVenueMaint();
		if (rcd != null) {
			dto.id = rcd.getId();
			dto.version = rcd.getVersion();
			dto.typeVenue = rcd.getTypeVenue();
			dto.nama = rcd.getNama();
			dto.tempat = rcd.getTempat();
			dto.tanggalAwal = rcd.getTanggalAwal();
			dto.tanggalAkhir = rcd.getTanggalAkhir();
                        dto.smallBanner = rcd.getSmallBanner();
                        dto.bigBanner = rcd.getBigBanner();
		}
		return dto;
	}
}
