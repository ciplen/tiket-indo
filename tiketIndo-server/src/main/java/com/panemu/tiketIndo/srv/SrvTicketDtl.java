package com.panemu.tiketIndo.srv;

import com.panemu.search.SortingInfo;
import com.panemu.search.TableCriteria;
import com.panemu.search.TableQuery;
import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.dto.DtoSellingList;
import com.panemu.tiketIndo.dto.DtoSellingListReseller;
import com.panemu.tiketIndo.dto.DtoVisitorEntered;
import com.panemu.tiketIndo.error.ErrorCode;
import com.panemu.tiketIndo.error.ErrorEntity;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.TicketDtl;
import java.util.ArrayList;
import java.util.Date;
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
public class SrvTicketDtl extends SrvBase {

	private static Logger logger = LoggerFactory.getLogger(SrvTicketDtl.class);

	public TicketDtl insertTicketDtl(TicketDtl dtl) {
		return super.insert(dtl);
	}

	public TicketDtl updateTicketDtl(TicketDtl dtl) {
		return super.update(dtl);
	}

	public void deleteTicketDtl(TicketDtl dtl) {
		super.delete(dtl);
	}

	public void deleteListTD(List<TicketDtl> lst) {
		super.delete(lst);
	}

	public TableData<TicketDtl> find(TableQuery query, int startIndex, int maxRecord, String penjualan) {
		try {
			if (penjualan.equalsIgnoreCase("web")) {

			}
			for (TableCriteria crit : query.getTableCriteria()) {
				crit.setTableAlias("ui");
				if ("venueId".equals(crit.getAttributeName()) || "resellerId".equals(crit.getAttributeName())) {
					crit.setSearchModeToInt();
				}
			}
			for (SortingInfo si : query.getSortingInfos()) {
				si.setTableAlias("ui");
			}
			String operation = "";
			if (query.getTableCriteria().size() > 0) {
				operation = " AND ";
			} else {
				operation = " WHERE ";
			}
			String whereClause = query.generateWhereClause(true);
			if ("web".equals(penjualan)) {
				whereClause = whereClause + operation + " ui.modifiedBy like '%admin%'";
			}
			if ("app".equals(penjualan)) {
				whereClause = whereClause + operation + " ui.modifiedBy not like '%admin%'";
			}
			String orderClause = query.generateOrderByClause(true);
			TypedQuery<Long> countQuery = em.createQuery("SELECT count(ui) from TicketDtl ui" + whereClause, Long.class);
			query.applyParameter(countQuery);
			long totalRow = countQuery.getSingleResult();

			Query typedQuery = em.createQuery("SELECT ui.id, ui.buyerName, ui.noKtp, ui.buyerEmail, ui.status, tm.type, ui.modifiedDate, ui.modifiedBy, ui.verifier, ui.qty, ui.phone, ui.kotaAsal, ui.umur from TicketDtl ui JOIN TicketMaint tm ON ui.ticketId = tm.id " + whereClause + orderClause);

			query.applyParameter(typedQuery);
			typedQuery.setFirstResult(startIndex);
			if (maxRecord > 0) {
				typedQuery.setMaxResults(maxRecord);
			}
			List<TicketDtl> resultList = new ArrayList<>();
			for (Object object : typedQuery.getResultList()) {
				TicketDtl dtl = new TicketDtl();
				Object[] row = (Object[]) object;
				dtl.setId((Integer) row[0]);
				dtl.setBuyerName((String) row[1]);
				dtl.setNoKtp((String) row[2]);
				dtl.setBuyerEmail((String) row[3]);
				dtl.setStatus((String) row[4]);
				dtl.setType((String) row[5]);
				dtl.setModifiedDate((Date) row[6]);
				dtl.setModifiedBy((String) row[7]);
				dtl.setVerifier((String) row[8]);
				dtl.setQty((Integer) row[9]);
				dtl.setPhone((String) row[10]);
				dtl.setKotaAsal((String) row[11]);
				dtl.setUmur((Integer) row[12]);
				
				resultList.add(dtl);
			}
			TableData<TicketDtl> td = new TableData<>(resultList, totalRow);
			return td;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, ex.getClass().getSimpleName()));
		}
	}

	public int checkAvailableTicket(int venueId, int typeId) {
		TypedQuery<Integer> tq = em.createQuery("SELECT ui.amount from TicketMaint ui where ui.id= :typeId", Integer.class);
		tq.setParameter("typeId", typeId);
		Integer total = tq.getSingleResult();

		TypedQuery<Long> q = em.createQuery("SELECT COALESCE(SUM(ui.qty),0) from TicketDtl ui where ui.venueId= :venueId AND ui.ticketId= :typeId", Long.class);
		q.setParameter("venueId", venueId);
		q.setParameter("typeId", typeId);
		long totalRow = q.getSingleResult();
		int available = total - (int) totalRow;
		return available;
	}

	public List<DtoSellingList> countSellingTotal(int venueId, int resellerId) {
		Query tq = em.createQuery("SELECT tm.type, COALESCE(SUM(ui.qty),0) from TicketDtl ui LEFT JOIN TicketMaint tm ON ui.ticketId = tm.id where ui.venueId= :venueId AND ui.resellerId= :resellerId GROUP BY ui.ticketId");
		tq.setParameter("venueId", venueId);
		tq.setParameter("resellerId", resellerId);
		List lst = tq.getResultList();

		List<DtoSellingList> lstDto = new ArrayList<>();
		DtoSellingList dto;
		for (Object object : lst) {
			dto = new DtoSellingList();
			Object[] row = (Object[]) object;
			dto.setTicketType((String) row[0]);
			dto.setTotalCount((Long) row[1]);
			lstDto.add(dto);
			logger.info(row[0].getClass().getSimpleName() + " - " + row[0] + " ---- " + row[1].getClass().getSimpleName() + " - " + row[1]);
		}
		return lstDto;
	}

	public List<DtoSellingListReseller> countResellerSelling(int venueId, String koordinator) {
		Query tq = em.createQuery("SELECT m.username, count(m), COALESCE(SUM(ui.qty),0) from TicketDtl ui LEFT JOIN TicketMaint tm ON ui.ticketId = tm.id LEFT JOIN Member m on ui.resellerId=m.id where ui.venueId= :venueId AND m.koordinator= :koordinator GROUP BY m.username");
		tq.setParameter("venueId", venueId);
		tq.setParameter("koordinator", koordinator);
		List lst = tq.getResultList();

		List<DtoSellingListReseller> lstDto = new ArrayList<>();
		DtoSellingListReseller dto;
		for (Object object : lst) {
			dto = new DtoSellingListReseller();
			Object[] row = (Object[]) object;
			dto.setReseller((String) row[0]);
			dto.setTotalCount((Long) row[1]);
			lstDto.add(dto);
			logger.info(row[0].getClass().getSimpleName() + " - " + row[0] + " ---- " + row[1].getClass().getSimpleName() + " - " + row[1]);
		}
		return lstDto;
	}

	public List<DtoVisitorEntered> countVisitorEntered(int venueId) {
		Query tq = em.createQuery("SELECT tm.type, COALESCE(SUM(dtl.qty),0) from TicketDtl dtl LEFT JOIN TicketMaint tm on dtl.ticketId=tm.id  where dtl.venueId=:venueId AND dtl.status ='VERIFIED' group by tm.type");
		tq.setParameter("venueId", venueId);
		List lst = tq.getResultList();

		List<DtoVisitorEntered> lstDto = new ArrayList<>();
		DtoVisitorEntered dto;
		for (Object object : lst) {
			dto = new DtoVisitorEntered();
			Object[] row = (Object[]) object;
			dto.setType((String) row[0]);
			dto.setTotalCount((Long) row[1]);
			lstDto.add(dto);
			logger.info(row[0].getClass().getSimpleName() + " - " + row[0] + " ---- " + row[1].getClass().getSimpleName() + " - " + row[1]);
		}
		return lstDto;
	}

	public List<TicketDtl> findExpPendingTicket(Date expiredDate) {
		TypedQuery<TicketDtl> tq = em.createQuery("SELECT td from TicketDtl td where td.status = 'PENDING' and td.modifiedDate <= :expiredDate", TicketDtl.class);
		tq.setParameter("expiredDate", expiredDate);
		return tq.getResultList();
	}
}
