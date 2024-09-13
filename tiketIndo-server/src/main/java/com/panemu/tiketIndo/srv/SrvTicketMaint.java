package com.panemu.tiketIndo.srv;

import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.error.ErrorCode;
import com.panemu.tiketIndo.error.ErrorEntity;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.TicketMaint;
import com.panemu.search.SortingInfo;
import com.panemu.search.TableCriteria;
import com.panemu.search.TableQuery;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nur Mubin <nur.mubin@panemu.com>
 */
@Stateless
public class SrvTicketMaint extends SrvBase {

	public static final int MAX_RECORD_PER_PAGE = 20;

	private static Logger logger = LoggerFactory.getLogger(SrvTicketMaint.class);
	private SrvVenueMaint srvVenue;

	public TicketMaint insertTicketMaint(TicketMaint ticket) {
		return super.insert(ticket);
	}

	public TicketMaint updateTicketMaint(TicketMaint ticket) {
		return super.update(ticket);
	}

	public void deleteTicketMaint(TicketMaint ticket) {
		super.delete(ticket);
	}

	public void deleteTicketMaint(List<TicketMaint> lstTicket) {
		super.delete(lstTicket);
	}

	public TableData<TicketMaint> find(TableQuery query, int startIndex, int maxRecord) {
		try {
			for (TableCriteria crit : query.getTableCriteria()) {
				crit.setTableAlias("ui");
				if ("namaEvent".equals(crit.getAttributeName())) {
					crit.setAttributeName("nama");
					crit.setTableAlias("v");
				}
			}
			for (SortingInfo si : query.getSortingInfos()) {
				si.setTableAlias("ui");
			}

			String whereClause = query.generateWhereClause(true);
			String orderClause = query.generateOrderByClause(true);
			TypedQuery<Long> countQuery = em.createQuery("SELECT count(ui) from TicketMaint ui LEFT JOIN ui.venue v" + whereClause, Long.class);
			query.applyParameter(countQuery);
			long totalRow = countQuery.getSingleResult();

			TypedQuery<TicketMaint> typedQuery = em.createQuery("SELECT ui from TicketMaint ui LEFT JOIN FETCH ui.venue v " + whereClause + orderClause, TicketMaint.class);
//			TypedQuery<TicketMaint> typedQuery = em.createQuery("SELECT ui from TicketMaint ui LEFT JOIN VenueMaint v ON ui.venueId=v.id LEFT JOIN DiscountTicket dt ON dt.maintId=ui.id" + whereClause + orderClause, TicketMaint.class);
			query.applyParameter(typedQuery);
			typedQuery.setFirstResult(startIndex);
			if (maxRecord > 0) {
				typedQuery.setMaxResults(maxRecord);
			}
			List<TicketMaint> resultList = typedQuery.getResultList();
			TableData<TicketMaint> td = new TableData<>(resultList, totalRow);
			return td;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, ex.getClass().getSimpleName()));
		}
	}

	public TicketMaint findById(int id) {
		TypedQuery<TicketMaint> q = em.createQuery("Select r from TicketMaint r where r.id = :id ", TicketMaint.class);
		q.setParameter("id", id);
		try {
			return q.getSingleResult();
		} catch (Exception ex) {
			logger.error("Unable to retrieve role id: " + id + ". " + ex.getMessage(), ex);
			return null;
		}
	}

	public TicketMaint findByType(String type) {
		TypedQuery<TicketMaint> q = em.createQuery("Select r from TicketMaint r where r.type = :type ", TicketMaint.class);
		q.setParameter("type", type);
		try {
			return q.getSingleResult();
		} catch (Exception ex) {
			logger.error("Unable to retrieve role id: " + type + ". " + ex.getMessage(), ex);
			return null;
		}
	}

	public Long getTotalTerjual(int ticketId) {
		TypedQuery<Long> countQuery = em.createQuery("SELECT COALESCE(SUM(td.qty),0) from TicketDtl td where td.ticketId = :ticketId AND (status is null or (status!='PENDING' AND status !='CONFIRM')) ", Long.class);
		countQuery.setParameter("ticketId", ticketId);
		return countQuery.getSingleResult();
	}

	public Long getDataPenjualan(int venueId, String modifiedBy) {
		TypedQuery<Long> countQuery = em.createQuery("SELECT COALESCE(SUM(td.qty),0) from TicketDtl td where td.venueId = :venueId AND " + modifiedBy + " AND (td.status is null or(td.status!='PENDING' AND td.status !='CONFIRM'))", Long.class);
		countQuery.setParameter("venueId", venueId);
		if (null == countQuery.getSingleResult()) {

		}
		return countQuery.getSingleResult();
	}

	public List<TicketMaint> findByVenueId(int venueId) {
		TypedQuery<TicketMaint> typedQuery = em.createQuery("SELECT ui from TicketMaint ui LEFT JOIN FETCH ui.venue v where ui.venueId=:venueId", TicketMaint.class);
		typedQuery.setParameter("venueId", venueId);
		try {
			return typedQuery.getResultList();
		} catch (Exception ex) {
			logger.error("Unable to retrieve Ticket by venueId: " + venueId + ". " + ex.getMessage(), ex);
			return null;
		}
	}

}
