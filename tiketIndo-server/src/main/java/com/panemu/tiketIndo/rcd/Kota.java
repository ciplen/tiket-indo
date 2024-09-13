/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.validation.constraints.Size;

/**
 *
 * @author khilmee
 */
@Entity
@Table(name = "kota")
public class Kota implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Size(max = 50)
    @Column(name = "nama_kota")
    private String namaKota;
    
    @Column(name = "provinsi_id")
    private int provinsiId;
    @JoinColumn(name = "provinsi_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne //(fetch = FetchType.LAZY)
    private Provinsi provinsi;
    
    public Kota() {
    }

    public Kota(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNamaKota() {
        return namaKota;
    }
    
    public void setNamaKota(String namaKota) {
        this.namaKota = namaKota;
    }
    
    public Provinsi getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(Provinsi provinsiId) {
        this.provinsi = provinsiId;
    }

    public void setProvinsiId(int provinsiId) {
	this.provinsiId = provinsiId;
    }

    public int getProvinsiId() {
	return provinsiId;
    }
}
