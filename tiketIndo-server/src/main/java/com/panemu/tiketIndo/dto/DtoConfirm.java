package com.panemu.tiketIndo.dto;

import com.panemu.tiketIndo.rcd.Confirmasi;

/**
 *
 * @author Alfin <ahmad.alfin@panemu.com>
 */
public class DtoConfirm {

	public int codeUnique;
	public int idBooking;
	public String email;
	public String path_picture;

	public static DtoConfirm create(Confirmasi rcd) {
		DtoConfirm dto = new DtoConfirm();
		dto.idBooking = rcd.getIdBooking();
		dto.codeUnique = rcd.getKodeUnik();
		dto.path_picture = rcd.getPathPicture();

		return dto;
	}
}
