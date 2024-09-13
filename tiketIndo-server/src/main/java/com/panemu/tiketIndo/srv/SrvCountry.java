package com.panemu.tiketIndo.srv;

import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.error.ErrorCode;
import com.panemu.tiketIndo.error.ErrorEntity;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.CountryData;
import com.panemu.search.InvalidSearchCriteria;
import com.panemu.search.SortingInfo;
import com.panemu.search.TableCriteria;
import com.panemu.search.TableQuery;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mubin
 */
@Stateless
public class SrvCountry extends SrvBase {

	private Logger log = LoggerFactory.getLogger(SrvCountry.class);
	@PersistenceContext
	protected EntityManager em;

	public CountryData insert(CountryData rcd) {
		return super.insert(rcd);
	}

	public CountryData update(CountryData rcd) {
		return super.update(rcd);
	}

	public void delete(CountryData rcd) {
		super.delete(rcd);
	}

	public TableData<CountryData> find(TableQuery query, int startIndex, int maxRecord) {
		try {
			for (TableCriteria crit : query.getTableCriteria()) {
				crit.setTableAlias("ui");
				if ("country".equals(crit.getAttributeName())) {
					crit.setAttributeName("id");
					crit.setSearchModeToInt();
				}
				if ("independence".equals(crit.getAttributeName())) {
					crit.setSearchModeToDate();
				}
				if ("population".equals(crit.getAttributeName())) {
					crit.setSearchModeToBigInteger();
				}
			}
			for (SortingInfo si : query.getSortingInfos()) {
				si.setTableAlias("ui");
			}

			String whereClause = query.generateWhereClause(true);
			String orderClause = query.generateOrderByClause(true);
			TypedQuery<Long> countQuery = em.createQuery("SELECT count(ui) from CountryData ui " + whereClause, Long.class);
			query.applyParameter(countQuery);
			long totalRow = countQuery.getSingleResult();

			TypedQuery<CountryData> typedQuery = em.createQuery("SELECT ui from CountryData ui " + whereClause + orderClause, CountryData.class);
			query.applyParameter(typedQuery);
			typedQuery.setFirstResult(startIndex);
			if (maxRecord > 0) {
				typedQuery.setMaxResults(maxRecord);
			}
			List<CountryData> resultList = typedQuery.getResultList();
			TableData<CountryData> td = new TableData<>(resultList, totalRow);
			return td;
		} catch (InvalidSearchCriteria ex) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0010, ex.getColumnName(), ex.getValue()));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, ex.getClass().getSimpleName()));
		}
	}
}
