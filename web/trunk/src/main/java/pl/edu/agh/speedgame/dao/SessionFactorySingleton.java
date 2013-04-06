package pl.edu.agh.speedgame.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class SessionFactorySingleton implements AutoCloseable {
    private static SessionFactorySingleton soleInstance;
    private SessionFactory sessionFactory;

    private SessionFactorySingleton() {
        Configuration configuration = new Configuration();
        configuration.configure();

        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .buildServiceRegistry();

        sessionFactory = configuration
                .buildSessionFactory(serviceRegistry);
    }


    public synchronized static SessionFactorySingleton getInstance() {
        if(soleInstance == null) {
            soleInstance = new SessionFactorySingleton();
        }

        return soleInstance;
    }

    public OurSessionReplacement createSessionReplacement() {
        return new OurSessionReplacement(sessionFactory.openSession());
    }

    public void close() {
        sessionFactory.close();
    }

}
