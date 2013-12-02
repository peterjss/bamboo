/*
 * @(#)BaseDomainEntity.java
 *
 * Copyright (c) 2012 iASPEC. All Rights Reserved.
 */


package com.pt.javaee.core.entity;


import java.util.Date;
import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.pt.javaee.core.AbstractValueObject;


/**
 * To share common properties through a technical or a business superclass without 
 * including it as a regular mapped entity (ie no specific table for this entity).
 * <p>The embedded superclass property mappings are copied into their entity subclasses.
 * Remember that the embeddable superclass is not the root of the hierarchy though.
 *
 * @version 1.00 2012-7-2
 * @author <a href="mailto:alex.zhang@pt.com">Alex Zhang</a>
 */

@MappedSuperclass
public class BaseDomainEntity<ID extends Serializable> extends AbstractValueObject
{
    private static final long serialVersionUID = 1148750099142971324L;

    @Id
    @GeneratedValue(generator="identity-increment")
    @GenericGenerator(name="identity-increment", strategy="identity")
    @Column(nullable=false)
    protected ID id;

    @Column(length=255)
    protected String description;

    @Column(length=255)
    protected String remark;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date createTime;

    @Column(length=60)
    protected String creator;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastUpdateTime;

    @Column(length=60)
    protected String lastUpdater;

    protected Integer optimisticLock;


    public BaseDomainEntity()
    {
    }


    /**
     * Declares the identifier property of an entity bean.
     * 
     * @return the id
     */

    public ID getId()
    {
        return id;
    }

    /**
     * @return the description
     */

    public String getDescription()
    {
        return description;
    }

    /**
     * @return the remark
     */

    public String getRemark()
    {
        return remark;
    }

    /**
     * @return the createTime
     */

    public Date getCreateTime()
    {
        return createTime;
    }

    /**
     * @return the creator
     */

    public String getCreator()
    {
        return creator;
    }

    /**
     * @return the lastUpdateTime
     */

    public Date getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    /**
     * @return the lastUpdater
     */

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
     * @param id the id to set
     */
    public void setId(ID id)
    {
        this.id = id;
    }

    /**
     * @param description - the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @param remark - the remark to set
     */
    public void setRemark(String remark)
    {
        this.remark = remark;
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

}
