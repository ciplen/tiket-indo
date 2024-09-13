/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.panemu.tiketIndo.srv;

import com.panemu.tiketIndo.rcd.Kota;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author khilmee
 */
@Stateless
public class SrvKota extends SrvBase {

    private Logger logger = LoggerFactory.getLogger(SrvProvinsi.class);
    @PersistenceContext
    protected EntityManager em;

    public Kota insert(Kota rcd) {
        return super.insert(rcd);
    }

    public Kota update(Kota rcd) {
        return super.update(rcd);
    }

    public void delete(Kota rcd) {
        super.delete(rcd);
    }

    public void deleteProvinsi(List<Kota> lstProvinsi) {
        super.delete(lstProvinsi);
    }

    public Kota findById(int id) {
        TypedQuery<Kota> q = em.createQuery("Select r from Kota r where r.id = :id ", Kota.class);
        q.setParameter("id", id);
        try {
            return q.getSingleResult();
        } catch (Exception ex) {
            logger.error("Unable to retrieve role id: " + id + ". " + ex.getMessage(), ex);
            return null;
        }
    }
    
    public List<Kota> getLstKotaByProvinsi(int provinsiId) {
        TypedQuery<Kota> q = em.createQuery("Select r from Kota r where r.provinsiId = :provinsiId", Kota.class);
        q.setParameter("provinsiId", provinsiId);
        try {
            return q.getResultList();
        } catch (Exception ex) {
            logger.error("Unable to retrieve venue data. " + ex.getMessage(), ex);
            return null;
        }
    }
}
