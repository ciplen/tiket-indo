package com.panemu.tiketIndo.ws;

import com.panemu.tiketIndo.srv.SrvDiscountTicket;
import com.panemu.tiketIndo.rcd.DiscountTicket;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alfin <ahmad.alfin@panemu.com>
 */
@Path("/discount")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class WsDiscountTIcket {

	private Logger logger = LoggerFactory.getLogger(WsDiscountTIcket.class);

	@Inject
	private SrvDiscountTicket srvDiscount;

	@PUT
	@Path("getDiscount/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public DiscountTicket getDiscount(@PathParam("id") int id) {
		DiscountTicket rcd = null;
		rcd = srvDiscount.getDiscount(id);
		return rcd;
	}

}
