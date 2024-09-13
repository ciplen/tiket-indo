/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.panemu.tiketIndo.dto;

import com.panemu.tiketIndo.rcd.Provinsi;

/**
 *
 * @author khilmee
 */
public class DtoProvinsi {

    public int id;
    public String namaProvinsi;

    public static DtoProvinsi create(Provinsi rcd) {
        DtoProvinsi dto = new DtoProvinsi();
        if (rcd != null) {
            dto.id = rcd.getId();
            dto.namaProvinsi = rcd.getNamaProvinsi();
        }
        return dto;
    }
}
