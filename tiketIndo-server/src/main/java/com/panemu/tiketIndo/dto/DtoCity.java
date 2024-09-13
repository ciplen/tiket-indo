package com.panemu.tiketIndo.dto;

import com.panemu.tiketIndo.rcd.City;

/**
 *
 * @author mubin
 */
public class DtoCity {

	public int id;
	public String name;
	public int countryId;
	public String countryName;
	public String continent;
	public int version;

	public static DtoCity create(City rcd) {
		DtoCity dto = new DtoCity();
		dto.id = rcd.getId();
		dto.name = rcd.getName();
		dto.countryId = rcd.getCountryDataid();
		if (rcd.getCountry() != null) {
			dto.countryName = rcd.getCountry().getName();
			dto.continent = rcd.getCountry().getContinent();
		}
		dto.version = rcd.getVersion();
		return dto;
	}

}
