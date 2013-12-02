/*
 * @(#)BaseAction.java
 *
 * Copyright (c) 2012 iASPEC. All Rights Reserved.
 */


package com.pt.javaee.web.struts2.action;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ParameterAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.pt.javaee.core.exception.ApplicationException;
import com.pt.javaee.core.exception.BaseExceptionMessages;


/**
 * This class is intended to be a base class for all Struts action classes.
 * 
 * @version 1.00 2012-7-2
 * @author <a href="mailto:alex.zhang@pt.com">Alex Zhang</a>
 */

public abstract class BaseAction extends ActionSupport implements ServletResponseAware, ParameterAware
{
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(BaseAction.class);
    
    protected HttpServletResponse servletResponse;

    protected Map<String, String[]> parameters;

    protected boolean success = true;
    protected String errorMessage = null;

    public BaseAction()
    {
    }


    /**
     * The action execution was successful.Show result view to the end user.
     * 
     * @return String
     */
    protected String processSuccess()
    {
        this.success = true;
        return SUCCESS;
    }


    protected String processException(Exception ex)
    {    	    	
    	if (ex instanceof ApplicationException)
        {
            ApplicationException ae = (ApplicationException)ex;
            
            List<String> errorMessages = ae.getMessages();
            if (errorMessages != null && !errorMessages.isEmpty())
            {
            	List<String> messages = new ArrayList<String>();
            	for (String message : errorMessages)
            	{
            		messages.add(getText(message));
            		logger.error(getText(message));
            	}

            	this.setActionErrors(messages);
            }
            
            errorMessage = ae.getMessage();
            if (errorMessage != null && !errorMessage.trim().equals(""))
            {
            	addActionError(getText(errorMessage));
                logger.error(getText(errorMessage));
            }
        }
    	else
    	{
    		errorMessage = getText(BaseExceptionMessages.EXCEPTION_SYSTEM);
        	addActionError(errorMessage);
            logger.error(errorMessage);
    		
            ex.printStackTrace();
    	}
        success = false;
        return SUCCESS;
//        return ERROR;
    }


    /**
     * Set the HTTP response object in implementing classes.
     * 
     * @param servletResponse - the servletResponse to set
     */
    public void setServletResponse(HttpServletResponse servletResponse)
    {
        this.servletResponse = servletResponse;
    }

    @Override
    public void setParameters(Map<String, String[]> stringMap)
    {
        this.parameters = stringMap;
    }

    public boolean getSuccess()
    {
        return success;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }
}