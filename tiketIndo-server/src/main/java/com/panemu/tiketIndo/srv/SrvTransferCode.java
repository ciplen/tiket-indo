package com.panemu.tiketIndo.srv;

import com.panemu.tiketIndo.rcd.TransferCode;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Alfin <ahmad.alfin@panemu.com>
 */
@Stateless
public class SrvTransferCode extends SrvBase {

	@PersistenceContext
	protected EntityManager em;

	public TransferCode update(TransferCode book) {
		return super.update(book);
	}

	public int getTransferCode() {
		//select min(id) from transfer_code where status = 'available';
		try {
			TypedQuery<TransferCode> tq = em.createQuery("SELECT tc from TransferCode tc where tc.status = 'AVAILABLE' AND tc.id > 110 order by id", TransferCode.class);
			tq.setMaxResults(1);
			TransferCode rcd = tq.getSingleResult();
//			rcd.setStatus("USED");
			rcd.setModifiedOn(new Date());
			em.merge(rcd);
			return rcd.getId();
		} catch (NoResultException ex) {
			return 0;
		}

	}

	public void release(int transferCode) {
		TransferCode rcd = super.findById(TransferCode.class, transferCode);
		rcd.setStatus("AVAILABLE");
		rcd.setModifiedOn(new Date());
		em.merge(rcd);
	}

	public List<TransferCode> findExpired(Date expiredDate) {
		TypedQuery<TransferCode> tq = em.createQuery("SELECT tc from TransferCode tc where tc.status = 'USED' and tc.modifiedOn <= :expiredDate", TransferCode.class);
		tq.setParameter("expiredDate", expiredDate);
		return tq.getResultList();
	}

	public TransferCode findByID(int id) {
		Query q = em.createQuery("SELECT e FROM " + TransferCode.class.getName() + " e WHERE id = :id");
		q.setParameter("id", id);
		try {
			return (TransferCode) q.getSingleResult();
		} catch (NoResultException exc) {
			return null;
		}
	}

}
