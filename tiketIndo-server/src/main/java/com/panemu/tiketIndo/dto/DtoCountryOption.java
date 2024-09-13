package com.panemu.tiketIndo.dto;

import com.panemu.tiketIndo.rcd.CountryData;

/**
 *
 * @author amrullah
 */
public class DtoCountryOption {
	public Integer id;
	public String name;
	
	public static DtoCountryOption create(CountryData rcd) {
		DtoCountryOption dto = new DtoCountryOption();
		dto.id = rcd.getId();
		dto.name = rcd.getName();
		return dto;
	}
}
