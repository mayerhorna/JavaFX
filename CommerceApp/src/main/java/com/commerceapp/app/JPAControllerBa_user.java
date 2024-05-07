package com.commerceapp.app;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import com.commerceapp.model.BaUser;
import com.commerceapp.model.Product;

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

    public List<BaUser> obtenerTodosUsuarios() {
        TypedQuery<BaUser> query = entityManager.createQuery("SELECT u FROM BaUser u", BaUser.class);
        return query.getResultList();
    }
    
    public List<BaUser> buscarUsuarioPorNombre(String nombre) {
        TypedQuery<BaUser> query = entityManager.createQuery("SELECT u FROM BaUser u WHERE u.name LIKE :namePattern",
                BaUser.class);
        query.setParameter("namePattern", "%" + nombre + "%"); 
        return query.getResultList();
    }

    // Método para buscar un usuario por su login y contraseña
    public BaUser buscarUsuarioPorCredenciales(String login, String password) throws Exception {
        if (validarConexion()) {
            BaUser usuario = null;
            try {
                Query query = entityManager.createQuery(
                        "SELECT u FROM BaUser u WHERE u.login_user = :login AND u.password_user = :password");
                query.setParameter("login", login);
                query.setParameter("password", password);
                usuario = (BaUser) query.getSingleResult();
            } catch (NoResultException e) {

            } catch (Exception e) {

            }
            return usuario;
        }
        return null;
    }
}
