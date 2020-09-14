package server.dataAccess;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import server.domain.models.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Controller {

    // Create the SessionFactory when you start the application.
    private static SessionFactory SESSION_FACTORY;

    public static void closeFactory() {
        SESSION_FACTORY.close();
    }

    /**
          * Initialize the SessionFactory instance.
          */
    public static void initiate() {
        // Create a Configuration object.
        Configuration config = new Configuration();

        config.addAnnotatedClass(Assignings.class);
        config.addAnnotatedClass(Bid.class);
        config.addAnnotatedClass(Schedule.class);
        config.addAnnotatedClass(Slot.class);
        config.addAnnotatedClass(SlotCapacity.class);
        config.addAnnotatedClass(SlotDates.class);
        config.addAnnotatedClass(Student.class);

        // Configure using the application resource named hibernate.cfg.xml.
        config.configure("hibernate.cfg.xml");

        // Extract the properties from the configuration file.
        Properties prop = config.getProperties();

        // Create StandardServiceRegistryBuilder using the properties.
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(prop);

        // Build a ServiceRegistry
        ServiceRegistry registry = builder.build();

        // Create the SessionFactory using the ServiceRegistry
        SESSION_FACTORY = config.buildSessionFactory(registry);
    }

    public static void create(Object model) {
        // Create a session
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = null;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();

            // Save the product
            session.save(model);
            // Commit the transaction
            transaction.commit();
        } catch (Exception ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
    }


    public static void delete(Object model) {
        // Create a session
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = null;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();

            // Delete the object
            session.delete(model);

            // Commit the transaction
            transaction.commit();
        } catch (Exception ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
    }


    public static void update(Object updatedModel) {
        // Create a session
        Session session = SESSION_FACTORY.openSession();

        Transaction transaction = null;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();

            // Update the student
            session.update(updatedModel);

            // Commit the transaction
            transaction.commit();
        } catch (HibernateException ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
    }

    public static int getStudentPoints(String studentID) {

        int points = 0;
        List<Student> students;
        // Create a session
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = null;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Student> cr = cb.createQuery(Student.class);
            Root<Student> root = cr.from(Student.class);
            cr.select(root).where(cb.equal(root.get("studentID"), studentID));
            Query<Student> query = session.createQuery(cr);
            students = query.getResultList();

            if(students == null || students.isEmpty()){
                throw new SQLException("wrong student id");
            }

            points = students.get(0).getTotalPoints();

            // Commit the transaction
            transaction.commit();
        } catch (Exception ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
        return points;
    }

    public static void setStudentPoints(String studentID, int newPoints) {
        // Create a session
        Session session = SESSION_FACTORY.openSession();
        List<Student> students;
        Transaction transaction = null;

        try {
            // Begin a transaction
            transaction = session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Student> cr = cb.createQuery(Student.class);
            Root<Student> root = cr.from(Student.class);
            cr.select(root).where(cb.equal(root.get("studentID"), studentID));
            Query<Student> query = session.createQuery(cr);
            students = query.getResultList();

            if (students == null || students.isEmpty()) {
                throw new SQLException("wrong student id");
            }
            Student st = students.get(0);

            st.setTotalPoints(newPoints);

            // Update the student
            session.update(st);

            // Commit the transaction
            transaction.commit();
        } catch (HibernateException | SQLException ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
    }

    public static List<Slot> getSlotsForFacultyMember(String courseID, int groupID) {
        return null;
    }

    public static List<Slot> getAllSlotsOfStudent(String studentID) {
        return null;
    }

    public static List<Assignings> getAssigningsOfStudent(String studentID) {
        // TODO: filter by 13-9-2020 - 25-9-2020
        List<Assignings> as = new LinkedList<>();

        // Create a session
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = null;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Assignings> cr = cb.createQuery(Assignings.class);
            Root<Assignings> root = cr.from(Assignings.class);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fromDate = df.parse("2020-09-13 00:00:00");
            Date toDate = df.parse("2020-09-25 00:00:00");

            cr.where(cb.and(cb.equal(root.get("studentID"), studentID), cb.between(root.get("date"), fromDate, toDate)));
            Query<Assignings> query = session.createQuery(cr);
            as = query.getResultList();

            // Commit the transaction
            transaction.commit();
        } catch (Exception ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
        return as;
    }

    public static List<SlotDates> getSlotsDatesOfStudent(String studentID) {
        // TODO: filter by 13-9-2020 - 25-9-2020

        return null;
    }

    public static Slot getSlotOfSlotDate(SlotDates slotDate) {
        // TODO: return slot of specific slot date
        return null;
    }

    public static List<Assignings> getAssigningsOfSlot(int slotID) {
        // TODO: filter by 13-9-2020 - 25-9-2020 ???????????????????????????? needs to have a date
        List<Assignings> as = new LinkedList<>();

        // Create a session
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = null;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Assignings> cr = cb.createQuery(Assignings.class);
            Root<Assignings> root = cr.from(Assignings.class);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fromDate = df.parse("2020-09-13 00:00:00");
            Date toDate = df.parse("2020-09-25 00:00:00");

            cr.where(cb.and(cb.equal(root.get("slotID"), slotID), cb.between(root.get("date"), fromDate, toDate)));
            Query<Assignings> query = session.createQuery(cr);
            as = query.getResultList();

            // Commit the transaction
            transaction.commit();
        } catch (Exception ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
        return as;
    }

    public static void setStudentBid(String studentID, int slotID, int percentage) {
        // Create a session
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = null;

        try {
            // Begin a transaction
            transaction = session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Bid> cr = cb.createQuery(Bid.class);
            Root<Bid> root = cr.from(Bid.class);
            cr.where(cb.and(cb.equal(root.get("studentID"), studentID), cb.equal(root.get("slotID"), slotID)));
            Query<Bid> query = session.createQuery(cr);
            List<Bid> bids = query.getResultList();

            if (bids == null || bids.isEmpty()) {
                throw new SQLException("wrong student id");
            }
            Bid bid = bids.get(0);
            bid.setPercentage(percentage);

            // Update the student
            session.update(bid);

            // Commit the transaction
            transaction.commit();
        } catch (HibernateException | SQLException ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
    }

    public static String getStudentID(String email, String password){
        //TODO:??????????????????No Table
        return null;
    }

    public static Bid getBidForSlotOfStudent(int slotID, String studentID) {

        Bid bid = null;
        // Create a session
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = null;

        try {
            // Begin a transaction
            transaction = session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Bid> cr = cb.createQuery(Bid.class);
            Root<Bid> root = cr.from(Bid.class);
            cr.where(cb.and(cb.equal(root.get("studentID"), studentID), cb.equal(root.get("slotID"), slotID)));
            Query<Bid> query = session.createQuery(cr);
            List<Bid> bids = query.getResultList();

            if (bids == null || bids.isEmpty()) {
                throw new SQLException("wrong student id");
            }
            bid = bids.get(0);

            // Commit the transaction
            transaction.commit();
        } catch (HibernateException | SQLException ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
        return bid;
    }
}
