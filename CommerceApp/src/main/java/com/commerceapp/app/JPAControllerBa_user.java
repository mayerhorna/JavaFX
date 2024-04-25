package com.commerceapp.app;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.commerceapp.model.BaUser;

public class JPAControllerBa_user {
	EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

	// Método para crear un nuevo usuario
	public void crearUsuario(BaUser usuario) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.persist(usuario);
			transaction.commit();
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	// Método para leer un usuario por su ID
	public BaUser leerUsuario(int id) {
		BaUser usuario = null;
		try {
			usuario = entityManager.find(BaUser.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usuario;
	}

	// Método para actualizar un usuario existente
	public void actualizarUsuario(BaUser usuario) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.merge(usuario);
			transaction.commit();
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	// Método para eliminar un usuario
	public void eliminarUsuario(BaUser usuario) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.remove(entityManager.contains(usuario) ? usuario : entityManager.merge(usuario));
			transaction.commit();
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	// Método para validar la conexión con el main
	public boolean validarConexion() {
		return entityManager != null && entityManager.isOpen();
	}

	// Método para buscar un usuario por su login y contraseña
	public boolean buscarUsuarioPorCredenciales(String login, String password) throws Exception {
		if (validarConexion()) {
			BaUser usuario = null;
			try {
				Query query = entityManager.createQuery(
						"SELECT u FROM BaUser u WHERE u.login_user = :login AND u.password_user = :password");
				query.setParameter("login", login);
				query.setParameter("password", password);
				usuario = (BaUser) query.getSingleResult();
			} catch (NoResultException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (usuario != null) {
				return true;
			} else {
				return false;
			}
		}
		return false;

	}

}
