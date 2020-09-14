package server.dataAccess;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import server.domain.models.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Properties;

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


    public static int getStudentPoints(String studentID) {

        return 0;
    }

    public static void setStudentPoints(String studentID, int newPoints) {
    }

    public static List<Slot> getSlotsForFacultyMember(String courseID, int groupID) {
        return null;
    }

    public static List<Slot> getAllSlotsOfStudent(String studentID) {
        return null;
    }

    public static List<Assignings> getAssigningsOfStudent(String studentID) {
        // TODO: filter by 13-9-2020 - 25-9-2020

        return null;
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
        return null;
    }

    public static void setStudentBid(String studentID, int slotID, int percentage) {
    }

    public static String getStudentID(String email, String password){
        return null;
    }

    public static Bid getBidForSlotOfStudent(int slotID, String studentID) {
        return null;
    }
}
