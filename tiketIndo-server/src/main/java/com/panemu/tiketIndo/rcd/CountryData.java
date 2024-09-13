package com.panemu.tiketIndo.rcd;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Size;

/**
 *
 * @author mubin
 */
@Entity
@Table(name = "country_data")
public class CountryData implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "id")
	private Integer id;
	@Size(max = 255)
   @Column(name = "name")
	private String name;
	@Size(max = 255)
   @Column(name = "capital")
	private String capital;
	@Size(max = 255)
   @Column(name = "continent")
	private String continent;
	@Column(name = "independence")
   @Temporal(TemporalType.DATE)
	private Date independence;
	@Column(name = "population")
	private BigInteger population;
	@Version
	private int version;
	public CountryData() {
	}

	public CountryData(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public Date getIndependence() {
		return independence;
	}

	public void setIndependence(Date independence) {
		this.independence = independence;
	}

	public BigInteger getPopulation() {
		return population;
	}

	public void setPopulation(BigInteger population) {
		this.population = population;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
