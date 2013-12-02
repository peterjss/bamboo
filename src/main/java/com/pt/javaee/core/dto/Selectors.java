/*
 * @(#)Selectors.java
 *
 * Copyright (c) 2012 iASPEC. All Rights Reserved.
 */


package com.pt.javaee.core.dto;


import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;


/**
 * Class description goes here.
 *
 * @version 1.00 2012-7-2
 * @author <a href="mailto:alex.zhang@pt.com">Alex Zhang</a>
 */

public class Selectors implements Serializable
{
    private static final long serialVersionUID = -233393608661451693L;

    @SuppressWarnings({"unchecked", "unused"})
    private Set set;
    
    
    @SuppressWarnings("unchecked")
    public Selectors()
    {
        this.set = new HashSet();
    }
    
    @SuppressWarnings("unchecked")
    public Selectors(int capacity)
    {
        this.set = new HashSet(capacity);
    }
    

}
