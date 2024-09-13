/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.panemu.tiketIndo.dto;

import com.panemu.tiketIndo.rcd.Kota;

/**
 *
 * @author khilmee
 */
public class DtoKota {

    public int id;
    public int provinsiId;
    public String namaKota;
    public String namaProvinsi;

    public static DtoKota create(Kota rcd) {
        DtoKota dto = new DtoKota();
        if (rcd != null) {
            dto.id = rcd.getId();
            dto.provinsiId = rcd.getProvinsi().getId();
            dto.namaKota = rcd.getNamaKota();
            dto.namaProvinsi = rcd.getProvinsi().getNamaProvinsi();
        }
        return dto;
    }
}
