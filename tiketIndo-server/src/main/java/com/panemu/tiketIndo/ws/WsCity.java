package com.panemu.tiketIndo.ws;

import com.panemu.tiketIndo.common.CommonUtil;
import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.dto.DtoCity;
import com.panemu.tiketIndo.dto.DtoCountryOption;
import com.panemu.tiketIndo.error.ErrorCode;
import com.panemu.tiketIndo.error.ErrorEntity;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.City;
import com.panemu.tiketIndo.rcd.CountryData;
import com.panemu.tiketIndo.rpt.RptCityList;
import com.panemu.tiketIndo.srv.SrvCity;
import com.panemu.tiketIndo.srv.SrvCountry;
import com.panemu.search.SortType;
import com.panemu.search.SortingInfo;
import com.panemu.search.TableCriteria;
import com.panemu.search.TableQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mubin
 */
@Path("/city")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class WsCity {

	private Logger log = LoggerFactory.getLogger(WsCity.class);

	@Inject
	private SrvCity srvCity;
	@Inject
	private SrvCountry srvCountry;

	@POST
	@RequiresPermissions(value = {"city:read", "city:write"}, logical = Logical.OR)
	public TableData<DtoCity> findAll(@QueryParam("start") int startIndex, @QueryParam("max") int maxRecord, TableQuery tq) {
		TableData<City> lstResult = srvCity.find(tq, startIndex, maxRecord);
		List<DtoCity> lst = new ArrayList<>();
		lstResult.getRows().forEach(row -> {
			lst.add(DtoCity.create(row));
		});
		TableData<DtoCity> result = new TableData<>(lst, lstResult.getTotalRows());
		return result;
	}
	
	@GET
	@Path("countryList")
	@RequiresPermissions(value = {"city:read", "city:write"}, logical = Logical.OR)
	public List<DtoCountryOption> getCountryByContinent(@QueryParam("continent") String continent) {
		TableQuery tq = new TableQuery();
		if (StringUtils.isNotBlank(continent)) {
			tq.getTableCriteria().add(new TableCriteria("continent", "\"" + continent + "\""));//use quote for exact match
		}
		tq.getSortingInfos().add(new SortingInfo("name", SortType.asc));
		TableData<CountryData> lstResult = srvCountry.find(tq, 0, 1000);

		List<DtoCountryOption> lstDto = lstResult.getRows().stream().map(item -> DtoCountryOption.create(item)).collect(Collectors.toList());
		return lstDto;
	}

	@GET
	@Path("{id}")
	@RequiresPermissions(value = {"city:read", "city:write"}, logical = Logical.OR)
	public DtoCity getCity(@PathParam("id") Integer id) {
		DtoCity dto = null;
		if (id > 0) {
			City rcd = srvCity.findById(City.class, id);
			dto = DtoCity.create(rcd);
		}
		return dto;
	}

	@PUT
	@Path("{id}")
	@RequiresPermissions(value = {"city:write"})
	public DtoCity saveCity(@PathParam("id") Integer id, DtoCity dto) {
		City city = null;
		if (id > 0) {
			city = srvCity.findById(City.class, id);
			srvCity.detach(city);//detach it to enable optimistic locking
		} else {
			city = new City();
		}
		city.setName(dto.name);
		city.setCountryDataid(dto.countryId);
		city.setVersion(dto.version);
		if (id > 0) {
			srvCity.update(city);
		} else {
			srvCity.insert(city);
		}
		DtoCity result = DtoCity.create(city);
		return result;
	}

	@DELETE
	@Path("{id}")
	@RequiresPermissions(value = {"city:write"})
	public void delete(@PathParam("id") Integer id) {
		City rcd = srvCity.findById(City.class, id);
		srvCity.delete(rcd);
	}

	@POST
	@Path("xls")
	@RequiresPermissions(value = {"city:read", "city:write"}, logical = Logical.OR)
	public Response export(
			  @QueryParam("start") int startIndex,
			  @QueryParam("max") int maxRecord,
			  TableQuery tq) {
		TableData<DtoCity> data = this.findAll(startIndex, maxRecord, tq);
		RptCityList rpt = new RptCityList(data, startIndex, maxRecord);
		return CommonUtil.buildExcelResponse(rpt, "city");
	}
	
	@GET
	@Path("errornoparam")
	public void testError() {
		throw GeneralException.create(new ErrorEntity(ErrorCode.ER0001, null));
	}
	
	@GET
	@Path("errorwithparam")
	public void testError2() {
		String name = "Tomi";
		int age = 25;
		throw GeneralException.create(new ErrorEntity(ErrorCode.ER0002, name, age+""));
	}
}
