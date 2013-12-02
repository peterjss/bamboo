/*
 * @(#)BaseCustomizableEntity.java
 *
 * Copyright (c) 2012 iASPEC. All Rights Reserved.
 */


package com.pt.javaee.core.entity;


import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.pt.javaee.core.AbstractValueObject;


/**
 * To share common properties through a technical or a business superclass without 
 * including it as a regular mapped entity (ie no specific table for this entity).
 * <p>The embedded superclass property mappings are copied into their entity subclasses.
 * Remember that the embeddable superclass is not the root of the hierarchy though.
 * 
 * A custom field is an attribute of an object which has not been created by the system developer
 * at the development stage but that has been added by the system user into the object when actually
 * using the system not introducing any changes into the source code of the application.
 * 
 * We will have to implement a mechanism allowing for creating/deleting custom fields in real time
 * avoiding the application restart, add a value into it and make sure the value is present in the
 * application database. Besides we will have to make sure that the custom field can be used in queries.
 *
 * @version 1.00 2012-7-2
 * @author <a href="mailto:alex.zhang@pt.com">Alex Zhang</a>
 */

@MappedSuperclass
public class BaseCustomizableEntity<ID extends Serializable> extends AbstractValueObject
{
    private static final long serialVersionUID = -7097143918437276423L;

    protected ID id;
    protected Date createTime;
    protected String creator;
    protected Date lastUpdateTime;
    protected String lastUpdater;
    protected Integer optimisticLock;
    
    protected Map<String, Object> customProperties;


    public BaseCustomizableEntity()
    {
    }

    
    public Object getValueOfCustomField(String name)
    {
        return getCustomProperties().get(name);
    }
    
    public void setValueOfCustomField(String name, Object value)
    {
        getCustomProperties().put(name, value);
    }
    
    

    /**
     * Declares the identifier property of an entity bean.
     * 
     * @return the id
     */
    @Id
    @GeneratedValue(generator="system-increment")
    @GenericGenerator(name="system-increment", strategy="increment")
    @Column(nullable=false)
    public ID getId()
    {
        return id;
    }

    /**
     * @return the createTime
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime()
    {
        return createTime;
    }

    /**
     * @return the creator
     */
    @Column(length=60)
    public String getCreator()
    {
        return creator;
    }

    /**
     * @return the lastUpdateTime
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    /**
     * @return the lastUpdater
     */
    @Column(length=60)
    public String getLastUpdater()
    {
        return lastUpdater;
    }
    
    /**
     * Adds optimistic locking capability.
     * <p>The optimisticLock property will be mapped to the optimisticLock column,
     * and the entity manager will use it to detect conflicting updates.
     * 
     * @return the optimisticLock
     */
    public Integer getOptimisticLock()
    {
        return optimisticLock;
    }

    /**
     * @return the customProperties
     */
    @Transient
    public Map<String, Object> getCustomProperties()
    {
        if (customProperties == null) {
            customProperties = new HashMap<String, Object>();
        }
        
        return customProperties;
    }
    

    /**
     * @param id the id to set
     */
    public void setId(ID id)
    {
        this.id = id;
    }

    /**
     * @param createTime - the createTime to set
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    /**
     * @param creator - the creator to set
     */
    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    /**
     * @param lastUpdateTime - the lastUpdateTime to set
     */
    public void setLastUpdateTime(Date lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @param lastUpdater - the lastUpdater to set
     */
    public void setLastUpdater(String lastUpdater)
    {
        this.lastUpdater = lastUpdater;
    }
    
    /**
     * @param optimisticLock the optimisticLock to set
     */
    public void setOptimisticLock(Integer optimisticLock)
    {
        this.optimisticLock = optimisticLock;
    }

    /**
     * @param customProperties - the customProperties to set
     */
    public void setCustomProperties(Map<String, Object> customProperties)
    {
        this.customProperties = customProperties;
    }

}
