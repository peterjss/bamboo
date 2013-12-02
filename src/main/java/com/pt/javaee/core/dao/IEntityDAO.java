/*
 * @(#)IEntityDAO.java
 *
 * Copyright (c) 2012 iASPEC. All Rights Reserved.
 */


package com.pt.javaee.core.dao;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * Base Entity Data Access Object (DAO) interface.
 * This is an base interface used to tag our DAO classes and to provide
 * common methods(CRUD) to all DAOs.
 *
 * @version 1.00 2012-7-2
 * @author <a href="mailto:alex.zhang@pt.com">Alex Zhang</a>
 */

public interface IEntityDAO<T>
{
    /**
     * Generic method to persist an entity into database.
     * 
     * @param entity the entity to be persisted
     */
    public void save(T entity);

    /**
     * Generic method to save changes made to a persistent object.
     * 
     * @param entity the object to be updated
     */
    public void update(T entity);

    /**
     * Update all given persistent instances,
     * according to its id (matching the configured "unsaved-value"?).
     * Associates the instances with the current Hibernate <code>Session</code>.
     * 
     * @param entities the persistent instances to save or update
     * (to be associated with the Hibernate <code>Session</code>)
     */
    public void updateAll(Collection<T> entities);

    /**
     * Copy the state of the given object onto the persistent object
     * with the same identifier. Follows JSR-220 semantics.
     * 
     * @param entity the object to merge with the corresponding persistence instance
     * 
     * @return the updated, registered persistent instance
     */
    public Object merge(T entity);

    /**
     * Copy the state of all the given objects onto the persistent objects
     * with the same identifier. Follows JSR-220 semantics.
     * <p>Similar to <code>saveOrUpdate</code>, but never associates the given
     * object with the current Hibernate Session. In case of a new entity,
     * the state will be copied over as well.
     * 
     * @param entities the persistent instances to save or update
     *        (to be associated with the Hibernate <code>Session</code>)
     *        
     * @return the updated, registered persistent instances
     */
    public Collection<Object> mergeAll(Collection<T> entities);

    /**
     * Generic method to remove an object from persistent storage in the database.
     * 
     * @param entity model class to lookup and delete.
     */
    public void delete(T entity);

    /**
     * Delete all persistent instances.
     * <p>This can be combined with any of the find methods to delete by query
     * in two lines of code.
     */
    public void deleteAll();

    /**
     * Delete all given persistent instances.
     * <p>This can be combined with any of the find methods to delete by query
     * in two lines of code.
     * 
     * @param entities the persistent instances to delete
     */
    public void deleteAll(Collection<T> entities);

    /**
     * Generic method to retrieve an object that was previously persisted to the database
     *   using the indicated id as primary key.
     * 
     * @param id the identifier (primary key) of the class
     * 
     * @return a populated object
     */
    public T findById(Serializable id);
    
    /**
     * Generic method to retrieve an object that was previously persisted to the database
     *   using the indicated id as primary key.
     * 
     * @param id the identifier of the persistent instance
     * @param lock the lock flag
     * 
     * @return the persistent instance, or <code>null</code> if not found
     */
    public T findById(Serializable id, boolean lock);
    
    /**
     * Generic method to get all objects of a particular type, this is the same as lookup up
     *   all rows in a table.
     * 
     * @return a {@link List} of populated persistent instances
     */
    public List<T> findAll();
    
    /**
     * Execute a query based on the given example entity object.
     *
     * @param exampleInstance an instance of the desired entity, serving as example for "query-by-example"
     * @param excludedProperties the excluded properties of the exampleInstance
     * 
     * @return a {@link List} of populated persistent instances
     */
    public List<T> findByExample(T exampleInstance, String... excludedProperties);
    
    /**
     * Generic method to get all objects of a particular type, based on the given property name and value.
     *
     * @param propertyName the name of the given property
     * @param value the value of the given property
     *
     * @return a {@link List} of populated persistent instances
     */
    public List<T> findBy(String propertyName, Object value);
    
    /**
     * Generic method to get all objects of a particular type based on the given property name and value,
     *   with an ordering to be applied to the results.
     *
     * @param propertyName the name of the given property
     * @param value the value of the given property
     * @param orderBy the property name with an ordering to be applied
     * @param isAsc the ordering bool flag. true=ASC, false=DESC
     *
     * @return a {@link List} of populated persistent instances
     */
    public List<T> findBy(String propertyName, Object value, String orderBy, boolean isAsc);
    
    /**
     * Generic method to retrieve the persistent instance of the given entity class 
     *   with the given property name and value.
     *
     * @param propertyName the name of the given property
     * @param value the value of the given property
     *
     * @return a {@link List} of populated persistent instances
     */
    public T findUniqueBy(String propertyName, Object value);
    
    /**
     * Judge the given properties' values is unique or not.
     *
     * @param entity the given object
     * @param uniquePropertyNames the given properties list, split by comma, such as "name,loginid,password"
     * 
     * @return true=unique, false=not unique
     */
    public boolean isUnique(T entity, String uniquePropertyNames);
}