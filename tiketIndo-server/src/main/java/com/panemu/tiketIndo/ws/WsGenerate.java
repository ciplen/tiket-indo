package com.panemu.tiketIndo.ws;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.panemu.search.TableQuery;
import com.panemu.tiketIndo.common.CommonUtil;
import com.panemu.tiketIndo.common.JwtUtil;
import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.dto.DtoSellingList;
import com.panemu.tiketIndo.dto.DtoSellingListReseller;
import com.panemu.tiketIndo.dto.DtoTicketDtl;
import com.panemu.tiketIndo.dto.DtoVisitorEntered;
import com.panemu.tiketIndo.error.ErrorCode;
import com.panemu.tiketIndo.error.ErrorEntity;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.BookTicket;
import com.panemu.tiketIndo.rcd.Member;
import com.panemu.tiketIndo.rcd.TicketDtl;
import com.panemu.tiketIndo.rcd.TicketMaint;
import com.panemu.tiketIndo.rcd.VenueMaint;
import com.panemu.tiketIndo.rpt.RptTicketDetail;
import com.panemu.tiketIndo.rpt.RptTiketPdf;
import com.panemu.tiketIndo.srv.SrvBookTicket;
import com.panemu.tiketIndo.srv.SrvMember;
import com.panemu.tiketIndo.srv.SrvTicketDtl;
import com.panemu.tiketIndo.srv.SrvTicketMaint;
import com.panemu.tiketIndo.srv.SrvVenueMaint;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nur Mubin <nur.mubin@panemu.com>
 */
@Path("/td")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class WsGenerate {

	private Logger logger = LoggerFactory.getLogger(WsGenerate.class);

	@Inject
	private SrvTicketDtl srvTicketDtl;
	@PersistenceContext
	EntityManager em;
	@Inject
	private SrvVenueMaint srvVenueMaint;
	@Inject
	private SrvTicketMaint srvTicketMaint;
	@Inject
	private EmailSender es;
	@Inject
	private SrvMember srvMember;
	@Inject
	private SrvBookTicket srvBookTicket;

	@POST
	@Produces({"application/json"})
	@RequiresAuthentication
	public TableData<TicketDtl> findAll(
			  @QueryParam("start") int startIndex,
			  @QueryParam("max") int maxRecord,
			  @QueryParam("penjualan") String penjualan,
			  TableQuery tq) {
//		Subject currentUser = SecurityUtils.getSubject();
//		TableCriteria tc = new TableCriteria("venueId", "4");
//		tq.getTableCriteria().add(tc);
		TableData<TicketDtl> lstResult = srvTicketDtl.find(tq, startIndex, maxRecord, penjualan);
		return lstResult;
	}

	@POST
	@Path("generate")
	@Produces({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	@RequiresRoles(value = {"reseller", "koordinator", "supertester"}, logical = Logical.OR)
	public DtoTicketDtl saveTicketMaint(DtoTicketDtl dto, @Context HttpServletRequest request) {

		try {

			Subject currentUser = SecurityUtils.getSubject();
			int availability = srvTicketDtl.checkAvailableTicket(dto.venueId, dto.ticketId);
			TicketMaint ticket = srvTicketMaint.findById(dto.ticketId);
			if (availability < 1) {
				throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "Maaf, tiket tipe (" + ticket.getType() + ") sudah habis"));
			}
			//		throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "Maaf, Penjualan hanya via website sudah habis"));

			VenueMaint venue = srvVenueMaint.findById(dto.venueId);
			TicketDtl dtl = new TicketDtl();
			dtl.setVenueId(dto.venueId);
			dtl.setTicketId(dto.ticketId);
			dtl.setResellerId(dto.resellerId);
			dtl.setBuyerName(dto.buyerName);
			dtl.setBuyerEmail(dto.buyerEmail);
			dtl.setNoKtp(dto.noKtp);
			dtl.setPhone(dto.phone);
			dtl.setUmur(0);
			dtl.setKotaAsal("");
			dtl.setModifiedDate(new Date());
			dtl.setModifiedBy(currentUser.getPrincipal() + "");
			dtl.setTicketCode("-");
			dtl.setQty(1);
			dtl.setVersion(dto.version);

			dtl = srvTicketDtl.insertTicketDtl(dtl);
			String ticketToken = JwtUtil.signTicket(dtl.getId(), venue.getNama(), ticket.getType(), dto.ticketCode, dtl.getQty());
			dtl.setTicketToken(ticketToken);
			String ticketCode = "CODE:" + dtl.getId() + "/" + dtl.getVenueId() + "/" + dtl.getTicketId();
			dtl.setTicketCode(ticketCode);
			dtl = srvTicketDtl.updateTicketDtl(dtl);
			sendNotifEmail(dtl);
			DtoTicketDtl result = DtoTicketDtl.create(dtl);
			return result;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new RuntimeException(ex);
		}
	}

	private void sendNotifEmail(TicketDtl dtl) {
		try {
			Subject currentUser = SecurityUtils.getSubject();
			VenueMaint venue = srvVenueMaint.findById(dtl.getVenueId());
			TicketMaint ticket = srvTicketMaint.findById(dtl.getTicketId());
			BookTicket booked = srvBookTicket.findByTDId(dtl.getId());
			RptTiketPdf rpt = new RptTiketPdf(booked, dtl, venue, ticket);
			File tmpFile = rpt.export();
			Member loggedUser = srvMember.getMemberByMemberName(currentUser.getPrincipal() + "");
			String[] recipients = new String[]{loggedUser.getEmail(), dtl.getBuyerEmail()};
			String subject = "E-Ticket " + venue.getNama() + " - tiketindonesia.id";
			String mssg = "Halo " + dtl.getBuyerName() + ", silahkan download/cetak tiket kamu.";
			es.send(recipients, mssg, tmpFile, true, subject);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
//			throw new RuntimeException(e);
		}

	}

	@POST
	@Path("verify/{id}/{status}")
	@Consumes({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	@RequiresRoles(value = {"gate_keeper", "approver", "supertester"}, logical = Logical.OR)
	public void verify(@PathParam("id") int id, @PathParam("status") String status) {
		TicketDtl td = srvTicketDtl.findById(TicketDtl.class, id);
		Subject currentUser = SecurityUtils.getSubject();
		if (status.equals("VERIFIED")) {
			td.setStatus(status);
		} else if (status.equals("CANCEL")) {
			td.setStatus(null);
		}
		if (status.equals("VERIFIED") || status.equals("CANCEL")) {
			td.setModifiedBy(td.getModifiedBy());
			td.setVerifier(currentUser.getPrincipal() + "");
			srvTicketDtl.updateTicketDtl(td);
		}
	}

	@POST
	@Path("checkin")
	@Consumes({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	@RequiresRoles(value = {"gate_keeper", "supertester"}, logical = Logical.OR)
	public DtoTicketDtl checkin(Map<String, String> map) {
		String jwt = map.get("ticketToken");
		boolean valid = JwtUtil.verifyTicket(jwt);
		if (!valid) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "Invalid Tiket. Invalid Token."));
		}
		DecodedJWT dj = JWT.decode(jwt);
		int ticketId = NumberUtils.toInt(dj.getClaim("id").asString(), 0);
		TicketDtl ticketDtl = srvTicketDtl.findById(TicketDtl.class,
				  ticketId);
		if (ticketDtl == null) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "Invalid Tiket. Tiket tidak terdaftar. ID " + ticketId));
		} else if (StringUtils.isNotBlank(ticketDtl.getStatus())) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "Tiket sudah terpakai."));
		}
		return DtoTicketDtl.create(ticketDtl);
	}

	@GET
	@Path("countSellingList/{venueId}/{resellerId}")
	@Produces({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	public List<DtoSellingList> countSellingList(@PathParam("venueId") int venueId, @PathParam("resellerId") int resellerId) {
		List<DtoSellingList> lstCount = srvTicketDtl.countSellingTotal(venueId, resellerId);
		return lstCount;
	}

	@GET
	@Path("countVisitorEntered/{venueId}")
	@Produces({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	public List<DtoVisitorEntered> countVisitorEntered(@PathParam("venueId") int venueId) {
		List<DtoVisitorEntered> lstCount = srvTicketDtl.countVisitorEntered(venueId);
		return lstCount;
	}

	@GET
	@Path("countSellingListKoorId/{venueId}/{koor}")
	@Produces({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	public List<DtoSellingListReseller> countSellingListKoorId(@PathParam("venueId") int venueId, @PathParam("koor") String koor) {
		List<DtoSellingListReseller> lstCount = srvTicketDtl.countResellerSelling(venueId, koor);
		return lstCount;
	}

	@POST
	@Path("xls")
	public Response export(
			  @QueryParam("start") int startIndex,
			  @QueryParam("max") int maxRecord,
			  @QueryParam("penjualan") String penjualan,
			  TableQuery tq) {
		TableData<TicketDtl> data = this.findAll(startIndex, maxRecord, penjualan, tq);
		RptTicketDetail rpt = new RptTicketDetail(data, startIndex, maxRecord);
		return CommonUtil.buildExcelResponse(rpt, "ticketDetail");
	}

	@POST
	@Path("xlsAll")
	public Response exportAll(
			  @QueryParam("penjualan") String penjualan,
			  TableQuery tq) {
		TableData<TicketDtl> data = this.findAll(0, 0, penjualan, tq);
		RptTicketDetail rpt = new RptTicketDetail(data, 0, 0);
		return CommonUtil.buildExcelResponse(rpt, "ticketDetail");
	}
}
