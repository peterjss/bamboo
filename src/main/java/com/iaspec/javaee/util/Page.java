/*
 * @(#)Page.java
 *
 * Copyright (c) 2012 iASPEC. All Rights Reserved.
 */


package com.pt.javaee.util;


import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Class description goes here.
 *
 * @version 1.00 2012-7-2
 * @author <a href="mailto:alex.zhang@iaspec.com">Alex Zhang</a>
 */

@SuppressWarnings("unchecked")
public class Page<E> implements Serializable
{
    private static final long serialVersionUID = 7194890424117323504L;

    private static int DEFAULT_PAGE_SIZE = 10;

    private int pageNumber;
    private int pageSize = DEFAULT_PAGE_SIZE;
    private long totalRecordsCount;
    private long totalPagesCount;

    private List<E> objectsList;

    /**
     * The empty page (immutable).  This page is serializable.
     */
    public static final Page EMPTY_PAGE = new Page(0, DEFAULT_PAGE_SIZE, 0, Collections.emptyList());


    public Page()
    {
        this.pageNumber = 0;
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.totalRecordsCount = 0;
        this.objectsList = new ArrayList<E>();
    }

    public Page(int pageNumber, int pageSize, long totalRecordsCount, List<E> objectsList)
    {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalRecordsCount = totalRecordsCount;
        this.objectsList = new ArrayList<E>(objectsList);
    }


    public String toString()
    {
        StringBuilder toString = new StringBuilder();
        
        toString.append("\npageNumber = ");
        toString.append(pageNumber);
        
        toString.append("\npageSize = ");
        toString.append(pageSize);
        
        toString.append("\ntotalRecordsCount = ");
        toString.append(totalRecordsCount);
        
        toString.append("\ntotalPagesCount = ");
        toString.append(this.getTotalPagesCount());
        
        toString.append("\nhasNextPage = ");
        toString.append(this.hasNextPage());
        
        toString.append("\nhasPreviousPage = ");
        toString.append(this.hasPreviousPage());
        
        toString.append("\nstartIndex = ");
        toString.append(Page.getStartIndex(this.pageNumber, this.pageSize));
        
        toString.append("\nobjectsList = ");
        toString.append(objectsList == null ? "No info." : objectsList.toString());
                
        toString.append("\n");
        
        return new String(toString);
    }
    
    
    /**
     * @return Returns the totalPagesCount.
     */
    public long getTotalPagesCount()
    {
        if (totalRecordsCount % pageSize > 0)
            totalPagesCount = totalRecordsCount / pageSize + 1;
        else
            totalPagesCount = totalRecordsCount / pageSize;
        
        return totalPagesCount;
    }

    public void setTotalPagesCount(int totalPagesCount)
    {
        this.totalPagesCount = totalPagesCount;
    }


    /**
     * @return Returns the pageNumber.
     */
    public int getPageNumber()
    {
        return this.pageNumber;
    }

    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }


    /**
     *
     * @return Returns the totalRecordsCount.
     */

    public long getTotalRecordsCount()
    {
        return this.totalRecordsCount;
    }

    public void setTotalRecordsCount(long totalRecordsCount)
    {
        this.totalRecordsCount = totalRecordsCount;
    }


    /**
     * @return Returns the objectsList.
     */
    public List<E> getObjectsList()
    {
        return objectsList;
    }

    public void setObjectsList(List<E> objectsList)
    {
        this.objectsList = objectsList;
    }


    public boolean hasNextPage()
    {
        return getPageNumber() < getTotalPagesCount();
    }

    public boolean hasPreviousPage()
    {
        return getPageNumber() > 1;
    }


    /**
     * @return the pageSize
     */
    public int getPageSize()
    {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    
    public static int getStartIndex(int pageNumber, int pageSize)
    {
        int startIndex = (pageNumber - 1) * pageSize;
        if (startIndex < 0) {
            startIndex = 0;
        }
        
        return startIndex;
    }


    /**
     * Returns <tt>true</tt> if this page contains no elements.
     *
     * @return <tt>true</tt> if this page contains no elements
     */
    public boolean isEmpty()
    {
        if (this.objectsList==null || this.objectsList.isEmpty()) {
            return true;
        }
        
        return false;
    }


    /**
     * @return the pageSize
     */
    public static Page emptyPage()
    {
        return new Page();
    }
}