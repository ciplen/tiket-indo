package com.panemu.tiketIndo.srv;

import com.panemu.search.SortType;
import com.panemu.search.SortingInfo;
import com.panemu.search.TableCriteria;
import com.panemu.search.TableQuery;
import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.dto.DtoCountAge;
import com.panemu.tiketIndo.dto.DtoCountKota;
import com.panemu.tiketIndo.error.ErrorCode;
import com.panemu.tiketIndo.error.ErrorEntity;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.BookTicket;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alfin <ahmad.alfin@panemu.com>
 */
@Stateless
public class SrvBookTicket extends SrvBase {

    private static Logger logger = LoggerFactory.getLogger(SrvBookTicket.class);

    public BookTicket insert(BookTicket book) {
        return super.insert(book);
    }

    public BookTicket update(BookTicket book) {
        return super.update(book);
    }

    public void deleteBookingTic(BookTicket book) {
        super.delete(book);
    }

    public void deleteBookingTic(List<BookTicket> lstBook) {
        super.delete(lstBook);
    }

    public TableData<BookTicket> find(TableQuery query, int startIndex, int maxRecord) {
        try {
            for (TableCriteria crit : query.getTableCriteria()) {
                crit.setTableAlias("ui");
                if ("kodeUnik".equals(crit.getAttributeName())) {
                    crit.setAttributeName("codeUnique");
                    crit.setTableAlias("ui");
                    crit.setSearchModeToInt();
                } else if ("modifiedDate".equals(crit.getAttributeName()) || "createdDate".equals(crit.getAttributeName())) {
                    crit.setSearchModeToTimestamp(query.getUtcMinuteOffset());
                } else if ("venueId".equals(crit.getAttributeName())) {
                    crit.setSearchModeToInt();
                }
            }
            if (query.getSortingInfos().isEmpty()) {
                query.getSortingInfos().add(new SortingInfo("id", SortType.desc));
            }
            for (SortingInfo si : query.getSortingInfos()) {
                si.setTableAlias("ui");
                if ("kodeUnik".equals(si.getAttributeName())) {
                    si.setTableAlias("ui");
                    si.setAttributeName("codeUnique");
                }
            }

            String whereClause = query.generateWhereClause(true);
            String orderClause = query.generateOrderByClause(true);

            TypedQuery<Long> countQuery = em.createQuery("SELECT count(ui) from BookTicket ui " + whereClause, Long.class);
            query.applyParameter(countQuery);
            long totalRow = countQuery.getSingleResult();

            TypedQuery<BookTicket> typedQuery = em.createQuery("SELECT ui from BookTicket ui " + whereClause + orderClause, BookTicket.class);
            query.applyParameter(typedQuery);
            typedQuery.setFirstResult(startIndex);
            if (maxRecord > 0) {
                typedQuery.setMaxResults(maxRecord);
            }
            List<BookTicket> resultList = typedQuery.getResultList();
            TableData<BookTicket> td = new TableData<>(resultList, totalRow);
            return td;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, ex.getClass().getSimpleName()));
        }
    }

    public List<BookTicket> getUserByEmail(String email) {
        Query q = em.createQuery("SELECT e FROM " + BookTicket.class.getName() + " e WHERE email = :email");
        q.setParameter("email", email);
        try {
            List<BookTicket> resultList = q.getResultList();
            return resultList;
        } catch (NoResultException exc) {
            return null;
        }
    }

    public BookTicket findByTDId(int idTicDtl) {
        Query q = em.createQuery("SELECT e FROM " + BookTicket.class.getName() + " e WHERE idTicDtl = :idTicDtl");
        q.setParameter("idTicDtl", idTicDtl);
        try {
            return (BookTicket) q.getSingleResult();
        } catch (NoResultException exc) {
            return null;
        }
    }

    public Long countBookingStatusAll(String status) {
        Query tq = em.createQuery("select count(*) from BookTicket where status = :status");
        tq.setParameter("status", status);
        Long data = (Long) tq.getSingleResult();
        try {
            return data;
        } catch (NoResultException exc) {
            return null;
        }
    }

    public Long countBookingStatus(int venueId, String status) {
        Query tq = em.createQuery("select count(*) from BookTicket WHERE id_venue = :id_venue and status = :status");
        tq.setParameter("id_venue", venueId);
        tq.setParameter("status", status);
        Long data = (Long) tq.getSingleResult();
        try {
            return data;
        } catch (NoResultException exc) {
            return null;
        }
    }

    public List<DtoCountAge> countAgeOfBooking(int venueId) {
        Query tq = em.createQuery("SELECT umur, COUNT(*) AS count FROM BookTicket WHERE id_venue = :id_venue GROUP BY umur");
        tq.setParameter("id_venue", venueId);
        //tq.setMaxResults(5);
        List lst = tq.getResultList();

        List<DtoCountAge> lstDto = new ArrayList<>();
        DtoCountAge dto;
        for (Object object : lst) {
            dto = new DtoCountAge();
            Object[] row = (Object[]) object;
            dto.setUmur((String) row[0]);
            dto.setCount((Long) row[1]);
            lstDto.add(dto);
            logger.info(row[0].getClass().getSimpleName() + " - " + row[0] + " ---- " + row[1].getClass().getSimpleName() + " - " + row[1]);
        }
        return lstDto;
    }
    
    public List<DtoCountKota> countKotaOfBooking(int venueId) {
        Query tq = em.createQuery("SELECT kotaAsal, COUNT(*) AS count FROM BookTicket WHERE id_venue = :id_venue GROUP BY kotaAsal");
        tq.setParameter("id_venue", venueId);
        //tq.setMaxResults(5);
        List lst = tq.getResultList();

        List<DtoCountKota> lstDto = new ArrayList<>();
        DtoCountKota dto;
        for (Object object : lst) {
            dto = new DtoCountKota();
            Object[] row = (Object[]) object;
            dto.setKotaAsal((String) row[0]);
            dto.setCount((Long) row[1]);
            lstDto.add(dto);
            logger.info(row[0].getClass().getSimpleName() + " - " + row[0] + " ---- " + row[1].getClass().getSimpleName() + " - " + row[1]);
        }
        return lstDto;
    }
}
