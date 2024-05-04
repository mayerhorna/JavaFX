package com.commerceapp.app;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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

	// Método para validar la conexión con el main
	public boolean validarConexion() {
		return entityManager != null && entityManager.isOpen();
	}
}
