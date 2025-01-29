package repositories;

import model.Rent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.OptimisticLockException;


public class RentRepository {
    private static SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public void save(Rent rent) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(rent);
            transaction.commit();
        } catch (OptimisticLockException ole) {
            // Handle optimistic locking exception
            if (transaction != null) {
                transaction.rollback();
            }
            throw ole; // Rethrow for further handling
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public Rent get(int rentId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Rent.class, rentId);
        }
    }
}