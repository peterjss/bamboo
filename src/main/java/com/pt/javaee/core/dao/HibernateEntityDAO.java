/*
 * @(#)HibernateEntityDAO.java
 *
 * Copyright (c) 2012 iASPEC. All Rights Reserved.
 */


package com.pt.javaee.core.dao;


import java.lang.reflect.ParameterizedType;
import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

import com.pt.javaee.util.HibernateUtil;


/**
 * Implementation of the IEntityDAO interface using the Hibernate API.
 *
 * @version 1.00 2012-7-2
 * @author <a href="mailto:alex.zhang@pt.com">Alex Zhang</a>
 */


public abstract class HibernateEntityDAO<T> implements IEntityDAO<T>
{
    private Class<T> entityClass;
    private Session session;

    
    @SuppressWarnings("unchecked")
    public HibernateEntityDAO()
    {
        this.entityClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }


    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#save(java.lang.Object)
     */
    @Override
    public void save(T entity)
    {
        getSession().save(entity);
    }

    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#update(java.lang.Object)
     */
    @Override
    public void update(T entity)
    {
        getSession().saveOrUpdate(entity);
    }

    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#delete(java.lang.Object)
     */
    @Override
    public void delete(T entity)
    {
        getSession().delete(entity);
    }
    
    
    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#findById(java.io.Serializable)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T findById(Serializable id)
    {
        return (T)getSession().load(getEntityClass(), id);
    }
    
    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#findById(java.io.Serializable, boolean)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T findById(Serializable id, boolean lock)
    {
        T entity;
        if (lock) {
            entity = (T)getSession().load(getEntityClass(), id, LockOptions.UPGRADE);
        }
        else {
            entity = (T)getSession().load(getEntityClass(), id);
        }
        
        return entity;
    }
    
    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#findAll()
     */
    @Override
    public List<T> findAll()
    {
        return findByCriteria();
    }
    
    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#findByExample(java.lang.Object, java.lang.String[])
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByExample(T exampleInstance, String... excludedProperties)
    {
        Criteria criteria = getSession().createCriteria(getEntityClass());
        Example example = Example.create(exampleInstance);
        for (String excludedProperty : excludedProperties) {
            example.excludeProperty(excludedProperty);
        }
        criteria.add(example);
        
        return criteria.list();
    }

    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#findBy(java.lang.String, java.lang.Object)
     */
    @Override
    public List<T> findBy(String propertyName, Object value)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#findBy(java.lang.String, java.lang.Object, java.lang.String, boolean)
     */
    @Override
    public List<T> findBy(String propertyName, Object value, String orderBy, boolean isAsc)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#findUniqueBy(java.lang.String, java.lang.Object)
     */
    @Override
    public T findUniqueBy(String propertyName, Object value)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#isUnique(java.lang.Object, java.lang.String)
     */
    @Override
    public boolean isUnique(Object entity, String uniquePropertyNames)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    public void flush()
    {
        getSession().flush();
    }
    
    public void clear()
    {
        getSession().clear();
    }
    

    /**
     * Use this inside subclasses as a convenience method. 
     *
     * @param criterion
     * @return
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterions)
    {
        Criteria criteria = getSession().createCriteria(getEntityClass());
        for (Criterion criterion : criterions) {
            criteria.add(criterion);
        }
        
        return criteria.list();
    }
    
    
    /**
     * @return the entityClass
     */
    public Class<T> getEntityClass()
    {
        return entityClass;
    }


    /**
     * @return the session
     */
    public Session getSession()
    {
        if (session == null) {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        }

        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(Session session)
    {
        this.session = session;
    }
}
