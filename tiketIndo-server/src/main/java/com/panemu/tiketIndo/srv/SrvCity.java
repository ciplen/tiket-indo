package com.panemu.tiketIndo.srv;

import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.error.ErrorCode;
import com.panemu.tiketIndo.error.ErrorEntity;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.City;
import com.panemu.search.SortingInfo;
import com.panemu.search.TableCriteria;
import com.panemu.search.TableQuery;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mubin
 */
@Stateless
public class SrvCity extends SrvBase {

	private Logger log = LoggerFactory.getLogger(SrvCity.class);
	@PersistenceContext
	protected EntityManager em;

	public City insert(City rcd) {
		return super.insert(rcd);
	}

	public City update(City rcd) {
		return super.update(rcd);
	}

	public void delete(City rcd) {
		super.delete(rcd);
	}

	public TableData<City> find(TableQuery query, int startIndex, int maxRecord) {
		try {
			for (TableCriteria crit : query.getTableCriteria()) {
				crit.setTableAlias("ui");
				if ("country".equals(crit.getAttributeName())) {
					crit.setAttributeName("country.id");
					crit.setSearchModeToInt();
				} else if ("continent".equals(crit.getAttributeName())) {
					crit.setAttributeName("country.continent");
				}
			}
			for (SortingInfo si : query.getSortingInfos()) {
				si.setTableAlias("ui");
				if ("countryName".equals(si.getAttributeName())) {
					si.setAttributeName("country.name");
				} else if ("continent".equals(si.getAttributeName())) {
					si.setAttributeName("country.continent");
				}
			}

			String whereClause = query.generateWhereClause(true);
			String orderClause = query.generateOrderByClause(true);
			TypedQuery<Long> countQuery = em.createQuery("SELECT count(ui) from City ui LEFT JOIN ui.country uo " + whereClause, Long.class);
			query.applyParameter(countQuery);
			long totalRow = countQuery.getSingleResult();

			TypedQuery<City> typedQuery = em.createQuery("SELECT ui from City ui LEFT JOIN FETCH ui.country uo " + whereClause + orderClause, City.class);
			query.applyParameter(typedQuery);
			typedQuery.setFirstResult(startIndex);
			if (maxRecord > 0) {
				typedQuery.setMaxResults(maxRecord);
			}
			List<City> resultList = typedQuery.getResultList();
			TableData<City> td = new TableData<>(resultList, totalRow);
			return td;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, ex.getClass().getSimpleName()));
		}
	}

}
