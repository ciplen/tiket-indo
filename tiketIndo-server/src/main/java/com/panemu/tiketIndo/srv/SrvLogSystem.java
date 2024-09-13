package com.panemu.tiketIndo.srv;

import com.panemu.tiketIndo.rcd.LogSystem;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mubin
 */
@Stateless
public class SrvLogSystem extends SrvBase {

	@PersistenceContext
	private EntityManager em;
	private Logger log = LoggerFactory.getLogger(SrvLogSystem.class);

	public LogSystem insertLogSystem(LogSystem lg) {
		if (lg.getDateTime() == null) {
			Date actionDate = new Date();
			lg.setDateTime(actionDate);

		}
		return super.insert(lg);
	}
}
