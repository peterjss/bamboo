/*
 * @(#)AbstractValueObject.java
 *
 * Copyright (c) 2012 iASPEC. All Rights Reserved.
 */


package com.pt.javaee.core;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;


/**
 * Class description goes here.
 *
 * @version 1.00 2012-7-2
 * @author <a href="mailto:alex.zhang@iaspec.com">Alex Zhang</a>
 */

@SuppressWarnings("serial")
public abstract class AbstractValueObject implements IValueObject
{
    @Override
    public String toString()
    {
    	return ReflectionToStringBuilder.toString(this);
    }
}
