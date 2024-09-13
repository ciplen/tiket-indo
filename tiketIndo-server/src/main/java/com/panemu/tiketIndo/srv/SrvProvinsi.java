/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.panemu.tiketIndo.srv;

import com.panemu.tiketIndo.rcd.Provinsi;
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
public class SrvProvinsi extends SrvBase {

    private Logger logger = LoggerFactory.getLogger(SrvProvinsi.class);
    @PersistenceContext
    protected EntityManager em;

    public Provinsi insert(Provinsi rcd) {
        return super.insert(rcd);
    }

    public Provinsi update(Provinsi rcd) {
        return super.update(rcd);
    }

    public void delete(Provinsi rcd) {
        super.delete(rcd);
    }

    public void deleteProvinsi(List<Provinsi> lstProvinsi) {
        super.delete(lstProvinsi);
    }

    public Provinsi findById(int id) {
        TypedQuery<Provinsi> q = em.createQuery("Select r from Provinsi r where r.id = :id ", Provinsi.class);
        q.setParameter("id", id);
        try {
            return q.getSingleResult();
        } catch (Exception ex) {
            logger.error("Unable to retrieve role id: " + id + ". " + ex.getMessage(), ex);
            return null;
        }
    }

    public List<Provinsi> getLstProvinsi() {
        TypedQuery<Provinsi> q = em.createQuery("Select r from Provinsi r", Provinsi.class);
        try {
            return q.getResultList();
        } catch (Exception ex) {
            logger.error("Unable to retrieve venue data. " + ex.getMessage(), ex);
            return null;
        }
    }
}
