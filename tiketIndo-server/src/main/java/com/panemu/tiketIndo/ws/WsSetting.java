package com.panemu.tiketIndo.ws;

import com.panemu.tiketIndo.srv.SrvSetting;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
@Path("/setting")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class WsSetting {

	private Logger log = LoggerFactory.getLogger(WsSetting.class);
	@Inject
	private SrvSetting srvSetting;

	@GET
	@Path("clear")
	@RequiresAuthentication
	public Map clearCache() {
		log.info("clearing setting cache");
		srvSetting.clearCache();
		Map<String, String> m = new HashMap<>();
		m.put("success", "true");
		return m;
	}
	
	@GET
	@Path("clear2")
	public String clearCache2() {
		return "test api";
	}
}
