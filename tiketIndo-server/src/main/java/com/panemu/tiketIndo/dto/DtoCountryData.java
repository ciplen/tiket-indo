package com.panemu.tiketIndo.dto;

import com.panemu.tiketIndo.rcd.CountryData;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author mubin
 */
public class DtoCountryData {

	public int id;
	public String name;
	public String capital;
	public String continent;
	public Date independence;
	public BigInteger population;
	public int version;
	public static DtoCountryData create(CountryData rcd) {
		DtoCountryData dto = new DtoCountryData();
		dto.id = rcd.getId();
		dto.name = rcd.getName();
		dto.capital = rcd.getCapital();
		dto.continent = rcd.getContinent();
		dto.independence = rcd.getIndependence();
		dto.population = rcd.getPopulation();
		dto.version = rcd.getVersion();
		return dto;
	}
}
