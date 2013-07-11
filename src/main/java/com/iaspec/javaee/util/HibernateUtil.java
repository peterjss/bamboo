/*
 * @(#)HibernateUtil.java
 *
 * Copyright (c) 2012 iASPEC. All Rights Reserved.
 */


package com.pt.javaee.util;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;


/**
 * Class description goes here.
 *
 * @version 1.00 2012-7-2
 * @author <a href="mailto:alex.zhang@iaspec.com">Alex Zhang</a>
 */

public class HibernateUtil
{
    private static final SessionFactory sessionFactory;

    static
    {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
//            sessionFactory = new Configuration().configure().buildSessionFactory();
            Configuration cfg = new Configuration();
            ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
            sessionFactory = cfg.configure().buildSessionFactory(serviceRegistry);
        }
        catch (Throwable ex)
        {
            // Make sure you log the exception, as it might be swallowed.
            System.out.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory()
    {
        // Alternatively, you could look up in JNDI here.
        return sessionFactory;
    }
}
