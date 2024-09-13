/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.panemu.tiketIndo.ws;

import com.panemu.tiketIndo.dto.DtoProvinsi;
import com.panemu.tiketIndo.rcd.Provinsi;
import com.panemu.tiketIndo.srv.SrvProvinsi;
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
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author khilmee
 */
@Path("/provinsi")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class WsProvinsi {

    private Logger logger = LoggerFactory.getLogger(WsVenueMaint.class);

    @Inject
    private SrvProvinsi srvProvinsi;
    @PersistenceContext
    EntityManager em;

    @GET
    @Path("lstProvinsi")
    @Produces({MediaType.APPLICATION_JSON})
    //@RequiresAuthentication
    public List<DtoProvinsi> lstProvinsi() {
        List<DtoProvinsi> lstdto = new ArrayList<>();
        DtoProvinsi dto = null;
        List<Provinsi> lst = srvProvinsi.getLstProvinsi();
        for (Provinsi rcd : lst) {
            dto = DtoProvinsi.create(rcd);
            lstdto.add(dto);
        }
        return lstdto;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public DtoProvinsi getProvinsiById(@PathParam("id") int id) {
        DtoProvinsi dto = null;
        Provinsi rcd = null;
        if (id > 0) {
            rcd = srvProvinsi.findById(id);
        }
        dto = DtoProvinsi.create(rcd);
        return dto;
    }
}
