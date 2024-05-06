package com.commerceapp.app;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.commerceapp.model.TsSaleOrder;
import com.commerceapp.model.TsSaleOrderLine;

public class JPAControllerTsSaleOrder {
	EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

	public Integer obtenerIdOrdenVentaPorCodigo(String codigo) {
		Integer idOrdenVenta = null;
		try {
			TypedQuery<Integer> query = entityManager
					.createQuery("SELECT o.id FROM TsSaleOrder o WHERE o.code = :codigo", Integer.class);
			query.setParameter("codigo", codigo);
			idOrdenVenta = query.getSingleResult();
		} catch (NoResultException e) {
			// Si no se encuentra ninguna orden de venta con ese código, devolvemos null
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idOrdenVenta;
	}

	public void eliminarOrdenVentaPorCodigo(String codigo) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			TypedQuery<TsSaleOrder> query = entityManager
					.createQuery("SELECT o FROM TsSaleOrder o WHERE o.code = :codigo", TsSaleOrder.class);
			query.setParameter("codigo", codigo);
			TsSaleOrder ordenVenta = query.getSingleResult();
			entityManager.remove(ordenVenta);
			transaction.commit();
		} catch (NoResultException e) {
			e.printStackTrace();
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	// Método para crear una nueva orden de venta
	public String crearOrdenVenta(TsSaleOrder ordenVenta) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.persist(ordenVenta);
			transaction.commit();

			// Después de que la transacción se haya confirmado, recuperamos el próximo
			// valor de la secuencia.
			Query query = entityManager.createNativeQuery(
					"SELECT seq_count FROM se_tb_ts_sale_order WHERE seq_name = 'ts_sale_order_seq'");
			Integer nextSequenceValue = (Integer) query.getSingleResult();
			int nextSequence = nextSequenceValue.intValue();

			// Generamos el código utilizando el valor de la secuencia recuperada.
			String codeGenerado = String.format("%d%04d", LocalDate.now().getYear(), nextSequence);
			return codeGenerado;
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return null;
	}

	// Método para leer una orden de venta por su ID
	public TsSaleOrder leerOrdenVenta(int id) {
		TsSaleOrder ordenVenta = null;
		try {
			ordenVenta = entityManager.find(TsSaleOrder.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ordenVenta;
	}

	// Método para actualizar una orden de venta existente
	public void actualizarOrdenVenta(TsSaleOrder ordenVenta) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.merge(ordenVenta);
			transaction.commit();
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	// Método para eliminar una orden de venta
	public void eliminarOrdenVenta(TsSaleOrder ordenVenta) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.remove(entityManager.contains(ordenVenta) ? ordenVenta : entityManager.merge(ordenVenta));
			transaction.commit();
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	// Método para obtener todas las órdenes de venta
	public List<TsSaleOrder> obtenerTodasOrdenesVenta() {
		TypedQuery<TsSaleOrder> query = entityManager.createQuery("SELECT o FROM TsSaleOrder o", TsSaleOrder.class);
		return query.getResultList();
	}

}
