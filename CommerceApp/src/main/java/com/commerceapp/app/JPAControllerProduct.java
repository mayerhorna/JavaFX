package com.commerceapp.app;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import com.commerceapp.model.Product;

public class JPAControllerProduct {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

    // Método para crear un nuevo producto
    public void crearProducto(Product producto) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(producto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Product buscarProductoPorCodigo(String codigo) {
        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p WHERE p.code = :codigo",
                Product.class);
        query.setParameter("codigo", codigo);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // No se encontró ningún producto con ese código
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Product buscarProductoPorID(String id) {
        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p WHERE p.tb_product_id = :id",
                Product.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // No se encontró ningún producto con ese código
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para leer un producto por su ID
    public Product leerProducto(Long id) {
        Product producto = null;
        try {
            producto = entityManager.find(Product.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return producto;
    }

    // Método para actualizar un producto existente
    public void actualizarProducto(Product producto) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(producto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Método para eliminar un producto
    public void eliminarProducto(Product producto) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(entityManager.contains(producto) ? producto : entityManager.merge(producto));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Product> obtenerTodosProductos() {
        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p", Product.class);
        return query.getResultList();
    }

    public List<Product> buscarProductoPorNombre(String nombre) {
        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p WHERE p.name LIKE :namePattern",
                Product.class);
        query.setParameter("namePattern", "%" + nombre + "%");
        return query.getResultList();
    }
    
    public Long obtenerIdProductoPorCodigo(String codigo) {
        Long idProducto = null;
        try {
            TypedQuery<Long> query = entityManager.createQuery("SELECT p.id FROM Product p WHERE p.code = :codigo", Long.class);
            query.setParameter("codigo", codigo);
            idProducto = query.getSingleResult();
        } catch (NoResultException e) {
            // Si no se encuentra ningún producto con ese código, devolvemos null
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idProducto;
    }

    // Método para validar la conexión con el main
    public boolean validarConexion() {
        return entityManager != null && entityManager.isOpen();
    }
}
