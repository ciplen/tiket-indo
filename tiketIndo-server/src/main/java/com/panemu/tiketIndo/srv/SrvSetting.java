package com.panemu.tiketIndo.srv;

import com.panemu.tiketIndo.rcd.Setting;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bastomi
 */
@Stateless
public class SrvSetting extends SrvBase {

	private final Logger logger = LoggerFactory.getLogger(SrvSetting.class);
	@PersistenceContext
	private EntityManager em;
	private static final List<Setting> lstSetting = new ArrayList<>();

	private void checkCached() {
		synchronized (lstSetting) {
			if (lstSetting.isEmpty()) {
				lstSetting.addAll(retrieveAll(Setting.class));
			}
		}
	}

	public Setting getCachedSetting(String code) {
		checkCached();
		for (Setting s : lstSetting) {
			if (code.equals(s.getCode())) {
				return s;
			}
		}
		return null;
	}

//	public License getLicense() {
//		final Setting setting = getCachedSetting("LICENSE");
//		return License.valueOf(setting.getValue());
//	}
	public void refreshCache(Setting setting) {
		checkCached();
		for (Setting s : lstSetting) {
			if (StringUtils.equals(s.getCode(), setting.getCode())) {
				lstSetting.remove(s);
				lstSetting.add(setting);
				return;
			}
		}
		lstSetting.add(setting);
	}

	public void clearCache() {
		lstSetting.clear();
		checkCached();
	}

	public void update(Setting s) {
		em.merge(s);
	}

	public String getPsnIncrement() {
		Query q = em.createNativeQuery("SELECT value FROM setting s WHERE s.code = :code");
		q.setParameter("code", "PSN INCREMENT");
		Object obj = q.getSingleResult();
		logger.info("obje: " + obj);
		try {
			return (String) obj;
		} catch (NoResultException exc) {
			return null;
		}
	}

	public int updateIncrementPSN(String newValue, String oldValue) {
		Query q = em.createQuery("UPDATE Setting set value = :newValue where value = :oldValue AND code = 'PSN INCREMENT'");
		q.setParameter("newValue", newValue);
		q.setParameter("oldValue", oldValue);
		return q.executeUpdate();
	}
}
