package com.panemu.tiketIndo.rcd;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mubin
 */
@Entity
@Table(name = "city")
@XmlRootElement
public class City implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Size(max = 255)
	@Column(name = "name")
	private String name;
	@JoinColumn(name = "country_dataid", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne
	private CountryData country;
	@Column(name = "country_dataid")
	private int countryDataid;
	@Version
	private int version;
	
	public City() {
	}

	public City(Integer id) {
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

	public CountryData getCountry() {
		return country;
	}

	public void setCountry(CountryData countryDataid) {
		this.country = countryDataid;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getCountryDataid() {
		return countryDataid;
	}

	public void setCountryDataid(int countryDataid) {
		this.countryDataid = countryDataid;
	}
	
}
