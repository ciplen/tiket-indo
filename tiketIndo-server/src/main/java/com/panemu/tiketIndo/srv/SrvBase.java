/*
 * Intelectual Property of Panemu
 * http://panemu.com
 */
package com.panemu.tiketIndo.srv;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
public class SrvBase {

	@PersistenceContext
	protected EntityManager em;
	private Logger log = LoggerFactory.getLogger(SrvBase.class);

	public <R> List<R> retrieveAll(Class<R> recordClass) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<R> cq = builder.createQuery(recordClass);
		Root<R> root = cq.from(recordClass);
		cq.select(root);
		TypedQuery<R> typedQuery = em.createQuery(cq);

		List<R> result = typedQuery.getResultList();
		return result;
	}

	protected <R> R insert(R record) {
		if (record == null) {
			return record;
		}

		em.persist(record);
		return record;
	}

	protected <R> List<R> insert(List<R> records) {
		if (records == null || records.size() == 0) {
			return records;
		}
		for (R record : records) {
			em.persist(record);
		}
		return records;
	}

	protected <R> R update(R record) {
		if (record == null) {
			return record;
		}
		record = em.merge(record);
		return record;
	}

	protected <R> List<R> update(List<R> records) {
		if (records == null || records.size() == 0) {
			return records;
		}
		List<R> result = new ArrayList<>();
		for (R record : records) {
			result.add(em.merge(record));
		}
		return result;
	}

	protected <R> R delete(R record) {
		if (record == null) {
			return record;
		}
		em.remove(em.contains(record) ? record : em.merge(record));
		return record;
	}

	protected <R> List<R> delete(List<R> records) {
		if (records == null || records.size() == 0) {
			return records;
		}
		for (R record : records) {
			em.remove(em.merge(record));
		}
		return records;
	}

	public void detach(Object obj) {
		em.detach(obj);
	}

	public <T> T findById(Class<T> clazz, int id) {
		return em.find(clazz, id);
	}

	public void flush() {
		em.flush();
	}
}
