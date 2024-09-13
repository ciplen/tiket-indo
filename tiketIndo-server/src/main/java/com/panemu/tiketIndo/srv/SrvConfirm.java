package com.panemu.tiketIndo.srv;

import com.panemu.tiketIndo.rcd.Confirmasi;
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
public class SrvConfirm extends SrvBase {

	private static Logger logger = LoggerFactory.getLogger(SrvConfirm.class);

	public Confirmasi insert(Confirmasi dtl) {
		return super.insert(dtl);
	}

	public Confirmasi update(Confirmasi dtl) {
		return super.update(dtl);
	}

	public void delete(Confirmasi dtl) {
		super.delete(dtl);
	}

	public Confirmasi findByBookingId(int idBooking) {
		Query q = em.createQuery("SELECT e FROM " + Confirmasi.class.getName() + " e WHERE id_booking = :idBooking");
		q.setParameter("idBooking", idBooking);
		try {
			return (Confirmasi) q.getSingleResult();
		} catch (NoResultException exc) {
			return null;
		}
	}

}
