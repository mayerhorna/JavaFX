package com.commerceapp.app;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import com.commerceapp.model.BaUser;
import com.commerceapp.model.Customer;

public class JPAControllerCustomer {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

    // Método para crear un nuevo usuario
    public void crearCliente(Customer cliente) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(cliente);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Método para leer un usuario por su ID
    public Customer leerCliente(int id) {
        Customer cliente = null;
        try {
            cliente = entityManager.find(Customer.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cliente;
    }

    // Método para actualizar un usuario existente
    public void actualizarCliente(Customer cliente) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(cliente);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Método para eliminar un usuario
    public void eliminarCliente(Customer cliente) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(entityManager.contains(cliente) ? cliente : entityManager.merge(cliente));
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
    public boolean buscarClientePorCredenciales(String login, String password) throws Exception {
        if (validarConexion()) {
            Customer cliente = null;
            try {
                Query query = entityManager.createQuery(
                        "SELECT u FROM BaUser u WHERE u.login_user = :login AND u.password_user = :password");
                query.setParameter("login", login);
                query.setParameter("password", password);
                cliente = (Customer) query.getSingleResult();
            } catch (NoResultException e) {

            } catch (Exception e) {

            }
            return cliente != null;
        }
        return false;
    }

    public List<Customer> obtenerTodosClientes() {
        TypedQuery<Customer> query = entityManager.createQuery("SELECT u FROM Customer u", Customer.class);
        return query.getResultList();
    }

    public List<Customer> buscarClientePorNombre(String nombre) {
        TypedQuery<Customer> query = entityManager.createQuery("SELECT u FROM Customer u WHERE u.name LIKE :namePattern",
                Customer.class);
        query.setParameter("namePattern", "%" + nombre + "%");
        return query.getResultList();
    }
}
