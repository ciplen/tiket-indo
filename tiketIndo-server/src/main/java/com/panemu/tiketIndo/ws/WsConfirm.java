package com.panemu.tiketIndo.ws;

import com.panemu.tiketIndo.common.CommonUtil;
import com.panemu.tiketIndo.rcd.Confirmasi;
import com.panemu.tiketIndo.srv.SrvConfirm;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alfin <ahmad.alfin@panemu.com>
 */
@Path("/confirm")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class WsConfirm {

	private Logger logger = LoggerFactory.getLogger(WsConfirm.class);

	@Context
	private ServletContext context;
	@Inject
	private SrvConfirm srvConfirm;
	@Resource(lookup = "java:global/tiketindo/picture-folder")
	private String pictureFolder;
	@PersistenceContext
	EntityManager em;

	@POST
	@Path("/uploadProfilePicture/{bookingId}")
	@Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Confirmasi uploadProfilePicture(MultipartFormDataInput input, @PathParam("bookingId") int bookingId) throws IOException {
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		Confirmasi userToUpdate = srvConfirm.findByBookingId(bookingId);

		List<InputPart> inputParts = uploadForm.get("attachment");
		if (CollectionUtils.isEmpty(inputParts)) {
			inputParts = uploadForm.get("file");
		}
		if (!CollectionUtils.isEmpty(inputParts)) {
			InputStream inputStream = null;
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hhmm");
				String date = df.format(new Date());

				String fileIdentifier = date + "_" + bookingId + "";
				String path = CommonUtil.saveToUploadFolder(input, "attachment", pictureFolder, "bukti_transfer", fileIdentifier);
				userToUpdate.setPathPicture(path);
				userToUpdate.setModifiedDate(new Date());

				srvConfirm.insert(userToUpdate);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}
		return userToUpdate;
	}

	@GET
	@Path("pict/{id}")
	public Response getPict(@PathParam("id") int id) {
		Confirmasi usr = srvConfirm.findByBookingId(id);
		if (usr == null || usr.getPathPicture() == null || usr.getPathPicture().trim().isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		File pict = new File(pictureFolder + usr.getPathPicture());
		Response.ResponseBuilder response = Response.ok((Object) pict);
		response.header("Content-Disposition", "attachment; filename=" + pict.getName());
		return response.build();
	}

}
