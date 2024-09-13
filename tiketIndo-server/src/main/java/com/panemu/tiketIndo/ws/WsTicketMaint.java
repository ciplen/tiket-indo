package com.panemu.tiketIndo.ws;

import com.panemu.search.TableQuery;
import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.dto.DtoTicketMaint;
import com.panemu.tiketIndo.error.ErrorCode;
import com.panemu.tiketIndo.error.ErrorEntity;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.DiscountTicket;
import com.panemu.tiketIndo.rcd.TicketMaint;
import com.panemu.tiketIndo.srv.SrvDiscountTicket;
import com.panemu.tiketIndo.srv.SrvTicketDtl;
import com.panemu.tiketIndo.srv.SrvTicketMaint;
import com.panemu.tiketIndo.srv.SrvVenueMaint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nur Mubin <nur.mubin@panemu.com>
 */
@Path("/tm")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class WsTicketMaint {

	private Logger logger = LoggerFactory.getLogger(WsTicketMaint.class);

	@Inject
	private SrvTicketMaint srvTicket;
	@Inject
	private SrvDiscountTicket srvDscntTick;
	@Inject
	private SrvTicketDtl srvTicketDl;
	@Inject
	private SrvVenueMaint srvVenue;
	@PersistenceContext
	EntityManager em;

	@POST
	@Produces({"application/json"})
//	@RequiresAuthentication
	public TableData<TicketMaint> findAll(
			  @QueryParam("start") int startIndex,
			  @QueryParam("max") int maxRecord,
			  TableQuery tq) {
//		Subject currentUser = SecurityUtils.getSubject();
		TableData<TicketMaint> lstResult = srvTicket.find(tq, startIndex, maxRecord);
		for (TicketMaint tic : lstResult.getRows()) {
			Long totalTerjual = srvTicket.getTotalTerjual(tic.getId());
			tic.setJumlahTerjual(totalTerjual.intValue());
		}
		return lstResult;
	}

	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_JSON})
//	@RequiresAuthentication
	public DtoTicketMaint getTicketMaintById(@PathParam("id") int id) {
		DtoTicketMaint dto = null;
		TicketMaint rcd = null;
		if (id > 0) {
			rcd = srvTicket.findById(id);
		}
		dto = DtoTicketMaint.create(rcd);
		return dto;
	}

	@GET
	@Path("check/{type}/{qty}")
	public void checkAvailability(@PathParam("type") String type, @PathParam("qty") int qty) {
		TicketMaint ticket = srvTicket.findByType(type);
		int availability = srvTicketDl.checkAvailableTicket(ticket.getVenueId(), ticket.getId());
		if (availability < 1) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "Maaf, tiket tipe (" + ticket.getType() + ") sudah habis"));
		} else if (availability < qty) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "Maaf, tiket tipe (" + ticket.getType() + ") tinggal " + availability));
		}
	}

	@GET
	@Path("check/{id}")
	public Integer checkAvailableTicket(@PathParam("id") int id) {
		TicketMaint ticket = srvTicket.findById(id);
		int availability = srvTicketDl.checkAvailableTicket(ticket.getVenueId(), ticket.getId());
		if (availability < 1) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "Maaf, tiket tipe (" + ticket.getType() + ") sudah habis"));
		}
		return availability;
	}

	@GET
	@Path("venueId/{id}")
	@Produces({MediaType.APPLICATION_JSON})
//	@RequiresAuthentication
	public List<DtoTicketMaint> getTicketMaintByVenueId(@PathParam("id") int id) {
		List<DtoTicketMaint> lstDto = new ArrayList<>();
		DtoTicketMaint dto = null;
		List<TicketMaint> lst = new ArrayList<>();
		if (id > 0) {
			lst = srvTicket.findByVenueId(id);
		}
		if (lst.size() > 0) {
			for (TicketMaint tm : lst) {
				Long totalTerjual = srvTicket.getTotalTerjual(tm.getId());
				tm.setJumlahTerjual(totalTerjual.intValue());
				dto = DtoTicketMaint.create(tm);
				lstDto.add(dto);
			}
		}
		return lstDto;

	}

	@GET
	@Path("penjualan/venueId/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	public List<Integer> getDataPenjualan(@PathParam("id") int id) {
		List<Integer> lstData = new ArrayList<>();
		List<TicketMaint> lst = new ArrayList<>();
		if (id > 0) {
			lst = srvTicket.findByVenueId(id);
		}
		String modifiedByAdmin = "td.modifiedBy like '%admin%'";
		String modifiedByReseller = "td.modifiedBy not like '%admin%'";
		int web = srvTicket.getDataPenjualan(id, modifiedByAdmin).intValue();
		int apps = srvTicket.getDataPenjualan(id, modifiedByReseller).intValue();

		lstData.add(web);
		lstData.add(apps);

		return lstData;
	}

	@Path("byId/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public DtoTicketMaint getTictMainById(@PathParam("id") int id) {
		DtoTicketMaint dto = null;
		TicketMaint rcd = null;
		if (id > 0) {
			rcd = srvTicket.findById(id);
		}
		dto = DtoTicketMaint.create(rcd);
		return dto;
	}

	@PUT
	@Path("{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public TicketMaint saveTicketMaint(@PathParam("id") Integer id, DtoTicketMaint dto) {
		Subject currentUser = SecurityUtils.getSubject();
		TicketMaint rcd = null;
		DiscountTicket rcdDscntTick = null;
		if (id > 0) {
			rcd = srvTicket.findById(dto.id);
			rcd.setVersion(dto.version);
		} else {
			rcd = new TicketMaint();
			rcdDscntTick = new DiscountTicket();

		}
		rcd.setType(dto.type);
		rcd.setVenueId(dto.venueId);
		rcd.setKeterangan(dto.keterangan);
		rcd.setAmount(dto.amount);
		rcd.setTanggalPeriode1(dto.tanggalPeriode1);
		rcd.setTanggalPeriode2(dto.tanggalPeriode2);
		rcd.setTanggalPeriode3(dto.tanggalPeriode3);
		rcd.setTanggalPeriode4(dto.tanggalPeriode4);
		rcd.setTanggalPeriode5(dto.tanggalPeriode5);
		rcd.setHargaPeriode1(dto.hargaPeriode1);
		rcd.setHargaPeriode2(dto.hargaPeriode2);
		rcd.setHargaPeriode3(dto.hargaPeriode3);
		rcd.setHargaPeriode4(dto.hargaPeriode4);
		rcd.setHargaPeriode5(dto.hargaPeriode5);

		rcd.setDiscount(dto.diskon);
		rcd.setMinOrder(dto.minOrder);

		rcdDscntTick.setMinOrder(dto.minOrder);
		rcdDscntTick.setDiskon(dto.diskon);
		rcdDscntTick.setKodeDiskon(2323232);
		rcdDscntTick.setTypeTicket(rcd.getType());
		rcdDscntTick.setExpDiskon(new Date());

		rcd.setModifiedBy(currentUser.getPrincipal() + "");
		rcd.setModifiedDate(new Date());
		srvDscntTick.detach(rcdDscntTick);
		srvTicket.detach(rcd);
		if (id > 0) {
			srvTicket.updateTicketMaint(rcd);
			srvDscntTick.updateDscntTick(rcdDscntTick);
		} else {
			srvTicket.insertTicketMaint(rcd);
			if (dto.diskon > 0) {
				rcdDscntTick.setMaintId(rcd.getId());
				srvDscntTick.insertDscntTick(rcdDscntTick);
			}
		}
		return rcd;
	}

	@DELETE
	@Path("{id}/{version}")
	public void delete(@PathParam("id") int id, @PathParam("version") int version) {
		TicketMaint rcd = srvTicket.findById(TicketMaint.class, id);
		srvTicket.deleteTicketMaint(rcd);
	}

}
