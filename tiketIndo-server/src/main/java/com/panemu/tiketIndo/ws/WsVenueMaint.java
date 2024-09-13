package com.panemu.tiketIndo.ws;

import com.panemu.search.TableCriteria;
import com.panemu.search.TableQuery;
import com.panemu.tiketIndo.common.CommonUtil;
import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.dto.DtoVenueMaint;
import com.panemu.tiketIndo.error.ErrorCode;
import com.panemu.tiketIndo.error.ErrorEntity;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.VenueMaint;
import com.panemu.tiketIndo.srv.SrvVenueMaint;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nur Mubin <nur.mubin@panemu.com>
 */
@Path("/vm")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class WsVenueMaint {

	private Logger logger = LoggerFactory.getLogger(WsVenueMaint.class);

	@Inject
	private SrvVenueMaint srvVenueMaint;
	@PersistenceContext
	EntityManager em;
	@Resource(lookup = "java:global/tiketindo/picture-folder")
	private String pictureFolder;

	@POST
	@Produces({"application/json"})
//	@RequiresAuthentication
	public TableData<VenueMaint> findAll(
			  @QueryParam("start") int startIndex,
			  @QueryParam("max") int maxRecord,
			  TableQuery tq) {
//		Subject currentUser = SecurityUtils.getSubject();
//		AuthorizationInfo subjectInfo = SaltedJdbcRealm.getInstance().getSubjectInfo(currentUser);
		TableData<VenueMaint> lstResult = srvVenueMaint.find(tq, startIndex, maxRecord);
		return lstResult;
	}

	@GET
	@Path("lstUpComingVanue")
	@Produces({MediaType.APPLICATION_JSON})
//	@RequiresAuthentication
	public List<DtoVenueMaint> lstUpComingVanue(@PathParam("id") int id) {
		List<DtoVenueMaint> lstdto = new ArrayList<>();
		DtoVenueMaint dto = null;
		List<VenueMaint> lst = srvVenueMaint.getUpComingVenue();
		for (VenueMaint rcd : lst) {
			dto = DtoVenueMaint.create(rcd);
			lstdto.add(dto);
		}
		return lstdto;
	}

	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_JSON})
//	@RequiresAuthentication
	public DtoVenueMaint getVenueMaintById(@PathParam("id") int id) {
		DtoVenueMaint dto = null;
		VenueMaint rcd = null;
		if (id > 0) {
			rcd = srvVenueMaint.findById(id);
		}
		dto = DtoVenueMaint.create(rcd);
		return dto;
	}

	@GET
	@Path("lst")
	@Produces({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	public List<DtoVenueMaint> getVenueName() {
		List<DtoVenueMaint> lstdto = new ArrayList<>();
		DtoVenueMaint dto = null;
		List<VenueMaint> lst = srvVenueMaint.getLstName();
		for (VenueMaint rcd : lst) {
			dto = DtoVenueMaint.create(rcd);
			lstdto.add(dto);
		}
		return lstdto;
	}

	@GET
	@Path("lstVanue")
	@Produces({MediaType.APPLICATION_JSON})
//	@RequiresAuthentication
	public List<DtoVenueMaint> lstVanue() {
		List<DtoVenueMaint> lstdto = new ArrayList<>();
		DtoVenueMaint dto = null;
		List<VenueMaint> lst = srvVenueMaint.getLstName();
		for (VenueMaint rcd : lst) {
			dto = DtoVenueMaint.create(rcd);
			lstdto.add(dto);
		}
		return lstdto;
	}

	@GET
	@Path("lstValidatingVenue")
	@Produces({MediaType.APPLICATION_JSON})
//	@RequiresAuthentication
	public List<DtoVenueMaint> lstValidatingVenue() {
		List<DtoVenueMaint> lstdto = new ArrayList<>();
		DtoVenueMaint dto = null;

		List<VenueMaint> lst = srvVenueMaint.validatingVenue();
		for (VenueMaint rcd : lst) {
			dto = DtoVenueMaint.create(rcd);
			lstdto.add(dto);
		}
		return lstdto;
	}

	@PUT
	@Path("{id}")
	@Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public VenueMaint saveVenueMaint(@PathParam("id") Integer id, DtoVenueMaint dtoVenueMaint) throws IOException {
		Subject currentUser = SecurityUtils.getSubject();
		VenueMaint rcd = null;

		// Insert to Database
		if (id > 0) {
			rcd = srvVenueMaint.findById(dtoVenueMaint.id);
			rcd.setVersion(dtoVenueMaint.version);
		} else {
			rcd = new VenueMaint();
		}
		rcd.setNama(dtoVenueMaint.nama);
		rcd.setTempat(dtoVenueMaint.tempat);
		rcd.setTypeVenue(dtoVenueMaint.typeVenue);
		rcd.setTanggalAwal(dtoVenueMaint.tanggalAwal);
		rcd.setTanggalAkhir(dtoVenueMaint.tanggalAkhir);
		rcd.setSmallBanner(dtoVenueMaint.smallBanner);
		rcd.setBigBanner(dtoVenueMaint.bigBanner);
		rcd.setModifiedBy(currentUser.getPrincipal() + "");
		rcd.setModifiedDate(new Date());
		//rcd.setSmallBanner(smallPath);
		//rcd.setBigBanner(bigPath);

		srvVenueMaint.detach(rcd);
		if (id > 0) {
			srvVenueMaint.updateVenueMaint(rcd);
		} else {
			srvVenueMaint.insertVenueMaint(rcd);
		}

		return rcd;
	}

	@PUT
	@Path("/savesmallbanner/{venueId}")
	@Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public VenueMaint uploadSmallBanner(MultipartFormDataInput input, @PathParam("venueId") int venueId) throws IOException {
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		//VenueMaint rcd = null;
		VenueMaint venueToUpdate = srvVenueMaint.findById(venueId);

		List<InputPart> inputParts = uploadForm.get("attachment");
		if (CollectionUtils.isEmpty(inputParts)) {
			inputParts = uploadForm.get("file");
		}
		if (!CollectionUtils.isEmpty(inputParts)) {
			InputStream inputStream = null;
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hhmm");
				String date = df.format(new Date());

				String fileIdentifier = date + "_" + venueId + "smallBanner";
				String path = CommonUtil.saveToUploadFolder(input, "attachment", pictureFolder, "venue", fileIdentifier);

				venueToUpdate.setSmallBanner(path);

				srvVenueMaint.insertVenueMaint(venueToUpdate);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}
		return venueToUpdate;
	}

	@PUT
	@Path("/savebigbanner/{venueId}")
	@Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public VenueMaint uploadBigBanner(MultipartFormDataInput input, @PathParam("venueId") int venueId) throws IOException {
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		//VenueMaint rcd = null;
		VenueMaint venueToUpdate = srvVenueMaint.findById(venueId);

		List<InputPart> inputParts = uploadForm.get("attachment");
		if (CollectionUtils.isEmpty(inputParts)) {
			inputParts = uploadForm.get("file");
		}
		if (!CollectionUtils.isEmpty(inputParts)) {
			InputStream inputStream = null;
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hhmm");
				String date = df.format(new Date());

				String fileIdentifier = date + "_" + venueId + "bigBanner";
				String path = CommonUtil.saveToUploadFolder(input, "attachment", pictureFolder, "venue", fileIdentifier);

				venueToUpdate.setBigBanner(path);

				srvVenueMaint.insertVenueMaint(venueToUpdate);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}
		return venueToUpdate;
	}

	@DELETE
	@Path("{id}/{version}")
	public void delete(@PathParam("id") int id, @PathParam("version") int version) {
		VenueMaint rcd = srvVenueMaint.findById(VenueMaint.class, id);
		srvVenueMaint.deleteVenueMaint(rcd);
	}

	@GET
	@Path("pict/small/{venueid}")
	public Response getSmallBanner(@PathParam("venueid") int venueId) {
		VenueMaint venueSmall = srvVenueMaint.findById(venueId);
		if (venueSmall == null || venueSmall.getSmallBanner() == null || venueSmall.getSmallBanner().trim().isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		File pict = new File(pictureFolder + venueSmall.getSmallBanner());
		Response.ResponseBuilder response = Response.ok((Object) pict);
		response.header("Content-Disposition", "attachment; filename=" + pict.getName());
		return response.build();
	}

	@GET
	@Path("pict/big/{venueid}")
	public Response getBigBanner(@PathParam("venueid") int venueId) {
		VenueMaint venueBig = srvVenueMaint.findById(venueId);
		if (venueBig == null || venueBig.getBigBanner() == null || venueBig.getBigBanner().trim().isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		File pict = new File(pictureFolder + venueBig.getBigBanner());
		Response.ResponseBuilder response = Response.ok((Object) pict);
		response.header("Content-Disposition", "attachment; filename=" + pict.getName());
		return response.build();
	}
}
