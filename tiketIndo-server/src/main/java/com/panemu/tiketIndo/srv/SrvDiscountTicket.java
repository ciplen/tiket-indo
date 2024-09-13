package com.panemu.tiketIndo.srv;

import com.panemu.tiketIndo.rcd.DiscountTicket;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alfin <ahmad.alfin@panemu.com>
 */
@Stateless
public class SrvDiscountTicket extends SrvBase {

	public static final int MAX_RECORD_PER_PAGE = 20;

	private static Logger logger = LoggerFactory.getLogger(SrvTicketMaint.class);
	private SrvTicketMaint srvTicketMaint;

	public DiscountTicket insertDscntTick(DiscountTicket discount) {
		return super.insert(discount);
	}

	public DiscountTicket updateDscntTick(DiscountTicket discount) {
		return super.update(discount);
	}

	public void deleteDscntTick(DiscountTicket discount) {
		super.delete(discount);
	}

	public void deleteDscntTick(List<DiscountTicket> lstDscntTick) {
		super.delete(lstDscntTick);
	}

	public DiscountTicket getDiscount(int id) {
		Query tq = em.createQuery("SELECT dc from " + DiscountTicket.class.getName() + " dc where maintId = :id");
		tq.setParameter("id", id);
		try {
			return (DiscountTicket) tq.getSingleResult();
		} catch (NoResultException exc) {
			return null;
		}

	}
}
