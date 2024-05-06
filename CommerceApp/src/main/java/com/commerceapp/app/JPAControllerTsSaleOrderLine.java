package com.commerceapp.app;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.commerceapp.model.TsSaleOrderLine;

public class JPAControllerTsSaleOrderLine {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

    // Método para crear una nueva línea de orden de venta
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

    // Método para leer una línea de orden de venta por su ID
    public TsSaleOrderLine leerLineaOrdenVenta(int id) {
        TsSaleOrderLine lineaOrdenVenta = null;
        try {
            lineaOrdenVenta = entityManager.find(TsSaleOrderLine.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lineaOrdenVenta;
    }

    // Método para actualizar una línea de orden de venta existente
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

    // Método para eliminar una línea de orden de venta
    public void eliminarLineaOrdenVenta(TsSaleOrderLine lineaOrdenVenta) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(entityManager.contains(lineaOrdenVenta) ? lineaOrdenVenta : entityManager.merge(lineaOrdenVenta));
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
        TypedQuery<TsSaleOrderLine> query = entityManager.createQuery("SELECT l FROM TsSaleOrderLine l", TsSaleOrderLine.class);
        return query.getResultList();
    }

}