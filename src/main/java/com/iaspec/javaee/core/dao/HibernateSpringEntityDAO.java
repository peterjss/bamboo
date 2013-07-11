/*
 * @(#)HibernateSpringEntityDAO.java
 *
 * Copyright (c) 2012 iASPEC. All Rights Reserved.
 */


package com.pt.javaee.core.dao;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.beanutils.PropertyUtils;

import org.hibernate.LockMode;
import org.hibernate.Criteria;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Example;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.dao.DataAccessException;
import org.springframework.util.ReflectionUtils;

import com.pt.commons.util.Assert;


/**
 * Implementation of the IEntityDAO interface using the Spring Hibernate template API.
 * HibernateDaoSupport is a convenient super class for Hibernate-based data access objects.
 *
 * @version 1.00 2012-7-2
 * @author <a href="mailto:alex.zhang@iaspec.com">Alex Zhang</a>
 */

public abstract class HibernateSpringEntityDAO<T> extends HibernateDaoSupport implements IEntityDAO<T>
{
    private Class<T> entityClass;


    @SuppressWarnings("unchecked")
    public HibernateSpringEntityDAO()
    {
        this.entityClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    
    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#save(java.lang.Object)
     */
    @Override
    public void save(T entity) throws DataAccessException
    {
        getHibernateTemplate().save(entity);
    }


    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#update(java.lang.Object)
     */
    @Override
    public void update(T entity) throws DataAccessException
    {
        getHibernateTemplate().saveOrUpdate(entity);
    }


    /*
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#saveOrUpdateAll(java.util.Collection)
     */
    @Override
    public void updateAll(Collection<T> entities) throws DataAccessException
    {
        getHibernateTemplate().saveOrUpdateAll(entities);
    }


    /*
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#merge(java.lang.Object)
     */
    @Override
    public Object merge(T entity) throws DataAccessException
    {
        return getHibernateTemplate().merge(entity);
    }


    /*
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#mergeAll(java.util.Collection)
     */
    @Override
    public Collection<Object> mergeAll(Collection<T> entities) throws DataAccessException
    {
        List<Object> updatedPersistentInstances = null;
        
        if (entities != null && entities.size() > 0)
        {
            updatedPersistentInstances = new ArrayList<Object>();
            for (Object entity : entities) {
                updatedPersistentInstances.add(getHibernateTemplate().merge(entity));
            }
        }

        return updatedPersistentInstances;
    }


    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#delete(java.lang.Object)
     */
    @Override
    public void delete(T entity) throws DataAccessException
    {
        getHibernateTemplate().delete(entity);
    }


    /*
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#deleteAll()
     */
    @Override
    public void deleteAll() throws DataAccessException
    {
        getHibernateTemplate().deleteAll(findByCriteria());
    }


    /*
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#deleteAll(java.util.Collection)
     */
    @Override
    public void deleteAll(Collection<T> entities)
    {
        getHibernateTemplate().deleteAll(entities);
    }


    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#findById(java.io.Serializable)
     */
    @Override
    public T findById(Serializable id) throws DataAccessException
    {
        return (T)getHibernateTemplate().get(getEntityClass(), id);
    }


    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#findById(java.io.Serializable, boolean)
     */
    @Override
    @SuppressWarnings("deprecation")
    public T findById(Serializable id, boolean lock) throws DataAccessException
    {
        T entity;
        if (lock) {
            entity = (T)getHibernateTemplate().get(getEntityClass(), id, LockMode.UPGRADE);
        }
        else {
            entity = (T)getHibernateTemplate().get(getEntityClass(), id);
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


    @SuppressWarnings("unchecked")   
    public List<T> findByExample(T exampleInstance)
    {   
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getEntityClass());
        Example example = Example.create(exampleInstance);
        detachedCriteria.add(example);
        
        return getHibernateTemplate().findByCriteria(detachedCriteria);
    }   


    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#findByExample(java.lang.Object, java.lang.String[])
     */
    @Override
    @SuppressWarnings("unchecked")   
    public List<T> findByExample(T exampleInstance, String... excludedProperties)
    {   
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getEntityClass());
        Example example = Example.create(exampleInstance);
        for (String excludedProperty : excludedProperties) {
            example.excludeProperty(excludedProperty);
        }
        detachedCriteria.add(example);
        
        return getHibernateTemplate().findByCriteria(detachedCriteria);
    }  


    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#findBy(java.lang.String, java.lang.Object)
     */
    @Override
    public List<T> findBy(String propertyName, Object value)
    {
        Assert.hasText(propertyName);
        return findByCriteria(Restrictions.eq(propertyName, value));
    }


    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#findBy(java.lang.String, java.lang.Object, java.lang.String, boolean)
     */
    @Override
    public List<T> findBy(String propertyName, Object value, String orderBy, boolean isAsc)
    {
        Assert.hasText(propertyName);
        Assert.hasText(orderBy);
        return findByCriteria(orderBy, isAsc, Restrictions.eq(propertyName, value));
    }


    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#findUniqueBy(java.lang.String, java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T findUniqueBy(String propertyName, Object value)
    {
        Assert.hasText(propertyName);
        return (T) createCriteria(Restrictions.eq(propertyName, value)).uniqueResult();
    }


    /* (non-Javadoc)
     * @see com.asiasoft.javaee.core.dao.IEntityDAO#isUnique(java.lang.Object, java.lang.String)
     */
    @Override
    public boolean isUnique(T entity, String uniquePropertyNames)
    {
        Assert.hasText(uniquePropertyNames);

        Criteria criteria = createCriteria().setProjection(Projections.rowCount());
        String[] namesList = uniquePropertyNames.split(",");
        
        try
        {
            for (String name : namesList) {
                criteria.add(Restrictions.eq(name, PropertyUtils.getProperty(entity, name)));
            }

            /* 
             * 以下代码为了如果是update的情冄1�7,排除entity自身.
             */
            String idName = getIdName();

            // 取得entity的主键�1�7�1�7
            Serializable id = getId(entity);

            // 如果id!=null,说明对象已存圄1�7,该操作为update,加入排除自身的判斄1�7
            if (id != null) {
                criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
            }
        }
        catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }
        
        return (Integer)criteria.uniqueResult() == 0;
    }


    /**
     * A convenience method to get the identifier of the persistent instance.
     * 
     * @param entity
     * 
     * @return
     */
    protected Serializable getId(T entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        Assert.notNull(entity);
        Assert.notNull(entityClass);

        return (Serializable) PropertyUtils.getProperty(entity, getIdName());
    }


    /**
     * A convenience method to get the identifier name of the persistent instance.
     * 
     * @return
     */
    protected String getIdName()
    {
        Assert.notNull(getEntityClass());
        
        ClassMetadata meta = getSessionFactory().getClassMetadata(getEntityClass());
        Assert.notNull(meta, "Class " + getEntityClass() + " not define in hibernate session factory.");
        String idName = meta.getIdentifierPropertyName();
        Assert.hasText(idName, getEntityClass().getSimpleName() + " has no identifier property define.");
        
        return idName;
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
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getEntityClass());
        for (Criterion criterion : criterions) {
            detachedCriteria.add(criterion);
        }
        
        return getHibernateTemplate().findByCriteria(detachedCriteria);
    }
    
    /**
     * Use this inside subclasses as a convenience method. 
     *
     * @param criterion
     * @return
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(String orderBy, boolean isAsc, Criterion... criterions)
    {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getEntityClass());
        
        for (Criterion criterion : criterions) {
            detachedCriteria.add(criterion);
        }
        
        if (isAsc) {
            detachedCriteria.addOrder(Order.asc(orderBy));
        }
        else {
            detachedCriteria.addOrder(Order.desc(orderBy));
        }
        
        return getHibernateTemplate().findByCriteria(detachedCriteria);
    }

    
    /**
     * A convenience method to create a Criteria instance managed by the current transactional Session.
     *
     * @param criterions
     */
    protected Criteria createCriteria(Criterion... criterions)
    {
        Criteria criteria = getSession(false).createCriteria(getEntityClass());
        for (Criterion criterion : criterions) {
            criteria.add(criterion);
        }

        return criteria;
    }

    /**
     * A convenience method to create a Criteria instance managed by the current transactional Session,
     *   and with an ordering.
     *
     * @see #createCriteria(Class,Criterion[])
     */
    protected Criteria createCriteria(String orderBy, boolean isAsc, Criterion... criterions)
    {
        Assert.hasText(orderBy);

        Criteria criteria = createCriteria(criterions);

        if (isAsc) {
            criteria.addOrder(Order.asc(orderBy));
        }
        else {
            criteria.addOrder(Order.desc(orderBy));
        }

        return criteria;
    }
    
    
    /**
     * @return the entityClass
     */
    public Class<T> getEntityClass()
    {
        return entityClass;
    }
}