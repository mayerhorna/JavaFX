package com.commerceapp.app;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import com.commerceapp.model.Customer;
import com.commerceapp.model.TsSaleOrderLine;

public class JPAControllerTsSaleOrderLine {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

    public void crearLineaOrdenVenta(TsSaleOrderLine lineaOrdenVenta) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(lineaOrdenVenta);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public TsSaleOrderLine leerLineaOrdenVenta(int id) {
        TsSaleOrderLine lineaOrdenVenta = null;
        try {
            lineaOrdenVenta = entityManager.find(TsSaleOrderLine.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lineaOrdenVenta;
    }

    public void actualizarLineaOrdenVenta(TsSaleOrderLine lineaOrdenVenta) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(lineaOrdenVenta);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void eliminarLineasOrdenVentaPorId(int id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            TypedQuery<TsSaleOrderLine> query = entityManager.createQuery(
                    "SELECT l FROM TsSaleOrderLine l WHERE l.tsSaleOrderId = :id", TsSaleOrderLine.class);
            query.setParameter("id", id);
            List<TsSaleOrderLine> lineasOrdenVenta = query.getResultList();
            for (TsSaleOrderLine lineaOrdenVenta : lineasOrdenVenta) {
                entityManager.remove(lineaOrdenVenta);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarLineaOrdenVenta(TsSaleOrderLine lineaOrdenVenta) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(
                    entityManager.contains(lineaOrdenVenta) ? lineaOrdenVenta : entityManager.merge(lineaOrdenVenta));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Método para obtener todas las líneas de orden de venta
    public List<TsSaleOrderLine> obtenerTodasLineasOrdenVenta() {
        TypedQuery<TsSaleOrderLine> query = entityManager.createQuery("SELECT l FROM TsSaleOrderLine l",
                TsSaleOrderLine.class);
        return query.getResultList();
    }
    
    public List<TsSaleOrderLine> buscarDetallePorId(int Id) {
        TypedQuery<TsSaleOrderLine> query = entityManager.createQuery("SELECT l FROM TsSaleOrderLine l WHERE l.tsSaleOrderId = :Id",
                TsSaleOrderLine.class);   
        query.setParameter("Id", Id);
        return query.getResultList();
    }

}
