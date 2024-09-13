/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.panemu.tiketIndo.ws;

import com.panemu.tiketIndo.dto.DtoKota;
import com.panemu.tiketIndo.dto.DtoTicketMaint;
import com.panemu.tiketIndo.rcd.Kota;
import com.panemu.tiketIndo.rcd.TicketMaint;
import com.panemu.tiketIndo.srv.SrvKota;
import com.panemu.tiketIndo.srv.SrvProvinsi;
import com.panemu.tiketIndo.srv.SrvTicketDtl;
import com.panemu.tiketIndo.srv.SrvTicketMaint;
import com.panemu.tiketIndo.srv.SrvVenueMaint;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author khilmee
 */
@Path("/kota")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class WsKota {

    private Logger logger = LoggerFactory.getLogger(WsTicketMaint.class);

    @Inject
    private SrvKota srvKota;
    @Inject
    private SrvProvinsi srvProvinsi;
    @PersistenceContext
    EntityManager em;

    @GET
    @Path("provinsi/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<DtoKota> getKotaByProvinsiId(@PathParam("id") int id) {
        List<DtoKota> lstDto = new ArrayList<>();
        DtoKota dto = null;
        List<Kota> lst = srvKota.getLstKotaByProvinsi(id);
        for (Kota rcd : lst) {
            dto = DtoKota.create(rcd);
            lstDto.add(dto);
        }
        return lstDto;

    }
}
