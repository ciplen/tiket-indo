package com.panemu.tiketIndo.srv;

import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.error.ErrorCode;
import com.panemu.tiketIndo.error.ErrorEntity;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.VenueMaint;
import com.panemu.search.SortingInfo;
import com.panemu.search.TableCriteria;
import com.panemu.search.TableQuery;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nur Mubin <nur.mubin@panemu.com>
 */
@Stateless
public class SrvVenueMaint extends SrvBase {

	private static Logger logger = LoggerFactory.getLogger(SrvVenueMaint.class);

	public VenueMaint insertVenueMaint(VenueMaint venue) {
		return super.insert(venue);
	}

	public VenueMaint updateVenueMaint(VenueMaint venue) {
		return super.update(venue);
	}

	public void deleteVenueMaint(VenueMaint venue) {
		super.delete(venue);
	}

	public void deleteVenueMaint(List<VenueMaint> lstVenue) {
		super.delete(lstVenue);
	}

	public TableData<VenueMaint> find(TableQuery query, int startIndex, int maxRecord) {
		try {
			for (TableCriteria crit : query.getTableCriteria()) {
				crit.setTableAlias("ui");
				if ("tanggalAwal".equals(crit.getAttributeName()) || "tanggalAwal".equals(crit.getAttributeName())) {
					crit.setSearchModeToDate();
				}
			}
			for (SortingInfo si : query.getSortingInfos()) {
				si.setTableAlias("ui");
			}

			String whereClause = query.generateWhereClause(true);
			String orderClause = query.generateOrderByClause(true);
			TypedQuery<Long> countQuery = em.createQuery("SELECT count(ui) from VenueMaint ui" + whereClause, Long.class);
			query.applyParameter(countQuery);
			long totalRow = countQuery.getSingleResult();

			TypedQuery<VenueMaint> typedQuery = em.createQuery("SELECT ui from VenueMaint ui " + whereClause + orderClause, VenueMaint.class);
			query.applyParameter(typedQuery);
			typedQuery.setFirstResult(startIndex);
			if (maxRecord > 0) {
				typedQuery.setMaxResults(maxRecord);
			}
			List<VenueMaint> resultList = typedQuery.getResultList();
			TableData<VenueMaint> td = new TableData<>(resultList, totalRow);
			return td;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, ex.getClass().getSimpleName()));
		}
	}

	public VenueMaint findById(int id) {
		TypedQuery<VenueMaint> q = em.createQuery("Select r from VenueMaint r where r.id = :id ", VenueMaint.class);
		q.setParameter("id", id);
		try {
			return q.getSingleResult();
		} catch (Exception ex) {
			logger.error("Unable to retrieve role id: " + id + ". " + ex.getMessage(), ex);
			return null;
		}
	}

	public List<VenueMaint> getLstName() {
		TypedQuery<VenueMaint> q = em.createQuery("Select r from VenueMaint r", VenueMaint.class);
		try {
			return q.getResultList();
		} catch (Exception ex) {
			logger.error("Unable to retrieve venue data. " + ex.getMessage(), ex);
			return null;
		}
	}

	public List<VenueMaint> validatingVenue() {
		TypedQuery<VenueMaint> q = em.createQuery("Select r from VenueMaint r where type_venue = 'Wisata'", VenueMaint.class);
		try {
			return q.getResultList();
		} catch (Exception ex) {
			logger.error("Unable to retrieve venue data. " + ex.getMessage(), ex);
			return null;
		}
	}

	public List<VenueMaint> getUpComingVenue() {
		TypedQuery<VenueMaint> q = em.createQuery("Select r from VenueMaint r where tanggal_awal > CURDATE() AND type_venue != 'Wisata'", VenueMaint.class);
		try {
			return q.getResultList();
		} catch (Exception ex) {
			logger.error("Unable to retrieve venue data. " + ex.getMessage(), ex);
			return null;
		}
	}
}
