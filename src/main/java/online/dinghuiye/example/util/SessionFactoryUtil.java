package online.dinghuiye.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * @author Strangeen on 2017/08/30
 */
public class SessionFactoryUtil {

    public static SessionFactory factory;

    // factory可以通过Spring进行注入，这里只做演示
    public static SessionFactory getSessionFactory() {
        if (factory == null) {
            Configuration configuration = new Configuration().configure("hbm.cfg.xml");
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            factory = configuration.buildSessionFactory(serviceRegistry);
        }
        return factory;
    }

    public static void closeSessionFactory(SessionFactory factory) {
        if (factory != null) {
            factory.close();
            factory = null;
        }
    }
}
