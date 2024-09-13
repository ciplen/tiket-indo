package com.panemu.tiketIndo.ws;

import com.panemu.tiketIndo.common.CommonUtil;
import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.dto.DtoCountryData;
import com.panemu.tiketIndo.rcd.CountryData;
import com.panemu.tiketIndo.rpt.RptCountryList;
import com.panemu.tiketIndo.srv.SrvCountry;
import com.panemu.search.TableQuery;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mubin
 */
@Path("/country")
@Produces(MediaType.APPLICATION_JSON)
@Stateless

public class WsCountry {

	private Logger log = LoggerFactory.getLogger(WsCountry.class);

	@Inject
	private SrvCountry srvCountry;

	@POST
	@RequiresPermissions(value = {"country:read", "country:write"}, logical = Logical.OR)
	public TableData<CountryData> findAll(
			  @QueryParam("start") int startIndex,
			  @QueryParam("max") int maxRecord,
			  TableQuery tq) {
		TableData<CountryData> lstResult = srvCountry.find(tq, startIndex, maxRecord);

		lstResult.getRows().forEach(rcd -> {
			srvCountry.detach(rcd);
		});
		return lstResult;
	}

	@GET
	@Path("{id}")
	@RequiresPermissions(value = {"country:read", "country:write"}, logical = Logical.OR)
	public DtoCountryData getCountry(@PathParam("id") int id) {
		DtoCountryData dto = null;
		if (id > 0) {
			CountryData rcd = srvCountry.findById(CountryData.class, id);
			dto = DtoCountryData.create(rcd);
		}
		return dto;
	}

	@DELETE
	@Path("{id}")
	@RequiresPermissions(value = {"country:write"})
	public void delete(@PathParam("id") Integer id) {
		CountryData rcd = srvCountry.findById(CountryData.class, id);
		srvCountry.delete(rcd);
	}

	@PUT
	@Path("{id}")
	@RequiresPermissions(value = {"country:write"})
	public void saveCountry(@PathParam("id") Integer id, DtoCountryData dto) {
		CountryData rcd = null;
		if (id > 0) {
			rcd = srvCountry.findById(CountryData.class, id);
			srvCountry.detach(rcd);//detach it to enable optimistic locking
		} else {
			rcd = new CountryData();
		}
		rcd.setName(dto.name);
		rcd.setCapital(dto.capital);
		rcd.setContinent(dto.continent);
		rcd.setIndependence(dto.independence);
		rcd.setPopulation(dto.population);
		rcd.setVersion(dto.version);
		if (id > 0) {
			srvCountry.update(rcd);
		} else {
			srvCountry.insert(rcd);
		}
	}

	@POST
	@Path("xls")
	@RequiresPermissions(value = {"country:read", "country:write"}, logical = Logical.OR)
	public Response export(
			  @QueryParam("start") int startIndex,
			  @QueryParam("max") int maxRecord,
			  TableQuery tq) {
		TableData<CountryData> data = this.findAll(startIndex, maxRecord, tq);
		RptCountryList rpt = new RptCountryList(data, startIndex, maxRecord);

		return CommonUtil.buildExcelResponse(rpt, "country");
	}
}
