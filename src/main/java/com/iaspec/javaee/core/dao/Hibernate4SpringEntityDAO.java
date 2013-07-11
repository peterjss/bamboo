package com.pt.javaee.core.dao;


import com.pt.commons.util.Assert;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class description goes here.
 * User: Peter
 *
 * @author <a href="mailto:yang.li@iaspec.com">Peter</a>
 * @version 1.00 13-4-18
 */
public class Hibernate4SpringEntityDAO<T extends Serializable> implements IEntityDAO<T>
{
    @Autowired
    private SessionFactory sessionFactory;

    private Class<T> entityClass;

    public Hibernate4SpringEntityDAO()
    {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//        sessionFactory = HibernateUtil.getSessionFactory();
    }


    /* (non-Javadoc)
     * @see com.iaspec.javaee.core.dao.IEntityDAO#save(java.lang.Object)
     */
    @Override
    public void save(T entity) throws HibernateException
    {
        sessionFactory.getCurrentSession().save(entity);
    }

    /* (non-Javadoc)
 * @see com.iaspec.javaee.core.dao.IEntityDAO#update(java.lang.Object)
 */
    @Override
    public void update(T entity) throws DataAccessException
    {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    public void updateAll(Collection<T> entities)
    {
        for(T entity : entities){
            sessionFactory.getCurrentSession().saveOrUpdate(entity);
        }
    }

    @Override
    public Object merge(T entity)
    {
        return sessionFactory.getCurrentSession().merge(entity);
    }

    @Override
    public Collection<Object> mergeAll(Collection<T> entities)
    {
        List<Object> updatedPersistentInstances = null;

        if (entities != null && entities.size() > 0)
        {
            updatedPersistentInstances = new ArrayList<Object>();
            for (Object entity : entities) {
                updatedPersistentInstances.add(sessionFactory.getCurrentSession().merge(entity));
            }
        }

        return updatedPersistentInstances;
    }

    @Override
    public void delete(T entity)
    {
        sessionFactory.getCurrentSession().delete(entity);
    }

    @Override
    public void deleteAll()
    {
        List<T> entities = findByCriteria();

        for(T entity : entities){
            sessionFactory.getCurrentSession().delete(entity);
        }
    }

    @Override
    public void deleteAll(Collection<T> entities)
    {
        for(T entity : entities){
            sessionFactory.getCurrentSession().delete(entity);
        }
    }

    @Override
    public T findById(Serializable id)
    {
        return (T) sessionFactory.getCurrentSession().get(getEntityClass(), id);
    }

    @Override
    public T findById(Serializable id, boolean lock)
    {
        T entity;
        if (lock) {
            entity = (T)sessionFactory.getCurrentSession().get(getEntityClass(), id, LockOptions.UPGRADE);
        }
        else {
            entity = (T)sessionFactory.getCurrentSession().get(getEntityClass(), id);
        }

        return entity;
    }

    @Override
    public List<T> findAll()
    {
        return findByCriteria();
    }

    @Override
    public List<T> findByExample(T exampleInstance, String... excludedProperties)
    {
        Example example = Example.create(exampleInstance);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
        criteria.add(example);

        return criteria.list();
    }

    @Override
    public List<T> findBy(String propertyName, Object value)
    {
        Assert.hasText(propertyName);
        return findByCriteria(Restrictions.eq(propertyName, value));
    }

    @Override
    public List<T> findBy(String propertyName, Object value, String orderBy, boolean isAsc)
    {
        Assert.hasText(propertyName);
        Assert.hasText(orderBy);
        return findByCriteria(orderBy, isAsc, Restrictions.eq(propertyName, value));
    }

    @Override
    public T findUniqueBy(String propertyName, Object value)
    {
        Assert.hasText(propertyName);
        return (T) createCriteria(Restrictions.eq(propertyName, value)).uniqueResult();
    }

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

            String idName = getIdName();

            Serializable id = getId(entity);

            if (id != null) {
                criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
            }
        }
        catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }

        return (Long)criteria.uniqueResult() == 0;
    }

    /**
     * Use this inside subclasses as a convenience method.
     *
     * @param criterions
     * @return
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterions)
    {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
        for (Criterion criterion : criterions) {
            criteria.add(criterion);
        }

        return criteria.list();
    }

    /**
     * Use this inside subclasses as a convenience method.
     *
     * @param criterions
     * @return
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(String orderBy, boolean isAsc, Criterion... criterions)
    {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());

        for (Criterion criterion : criterions) {
            criteria.add(criterion);
        }

        if (isAsc) {
            criteria.addOrder(Order.asc(orderBy));
        }
        else {
            criteria.addOrder(Order.desc(orderBy));
        }

        return criteria.list();
    }

    /**
     * A convenience method to create a Criteria instance managed by the current transactional Session.
     *
     * @param criterions
     * @return Criteria
     */
    protected Criteria createCriteria(Criterion... criterions)
    {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
        for (Criterion criterion : criterions) {
            criteria.add(criterion);
        }

        return criteria;
    }

    /**
     * A convenience method to create a Criteria instance managed by the current transactional Session,
     *   and with an ordering.
     *
     * @return Criteria
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

        ClassMetadata meta = sessionFactory.getClassMetadata(getEntityClass());
        Assert.notNull(meta, "Class " + getEntityClass() + " not define in hibernate session factory.");
        String idName = meta.getIdentifierPropertyName();
        Assert.hasText(idName, getEntityClass().getSimpleName() + " has no identifier property define.");

        return idName;
    }

    @SuppressWarnings("rawtypes")
    public Class getEntityClass()
    {
        return entityClass;
    }

    @SuppressWarnings("rawtypes")
    public void setEntityClass(Class entityClass)
    {
        this.entityClass = entityClass;
    }

    public SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }
}
