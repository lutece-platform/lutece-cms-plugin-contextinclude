/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.contextinclude.service.parameter;

import fr.paris.lutece.plugins.contextinclude.business.parameter.ContextParameter;
import fr.paris.lutece.plugins.contextinclude.business.parameter.IContextParameter;
import fr.paris.lutece.plugins.contextinclude.business.parameter.IContextParameterDAO;
import fr.paris.lutece.plugins.contextinclude.service.ContextIncludePlugin;
import fr.paris.lutece.plugins.contextinclude.util.annotation.ContextAttribute;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.commons.beanutils.BeanUtils;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;


/**
 * The Class ContextParameterService.
 */
public class ContextParameterService implements IContextParameterService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = "contextinclude.contextParameterService";

    /** The _context parameter dao. */
    @Inject
    private IContextParameterDAO _contextParameterDAO;

    /** The _context parameter. */
    @Inject
    private IContextParameter _contextParameter;

    /**
     * {@inheritDoc}
     */
    @Override
    public IContextParameter find(  )
    {
        Map<String, Object> mapParameters = _contextParameterDAO.load( ContextIncludePlugin.getPlugin(  ) );

        if ( mapParameters != null )
        {
            try
            {
                BeanUtils.populate( _contextParameter, mapParameters );
            }
            catch ( IllegalAccessException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
            catch ( InvocationTargetException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
        }

        return _contextParameter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ContextIncludePlugin.TRANSACTION_MANAGER )
    public void update(  )
    {
        // First remove the parameters
        _contextParameterDAO.remove( ContextIncludePlugin.getPlugin(  ) );

        Map<String, Object> mapAttributes = depopulate( _contextParameter );

        // Then add the parameters
        for ( Entry<String, Object> attribute : mapAttributes.entrySet(  ) )
        {
            _contextParameterDAO.insert( attribute.getKey(  ), attribute.getValue(  ),
                ContextIncludePlugin.getPlugin(  ) );
        }
    }

    // PRIVATE METHODS

    /**
     * Depopulate.
     *
     * @param contextParameter the context parameter
     * @return the map
     */
    private static Map<String, Object> depopulate( IContextParameter contextParameter )
    {
        Map<String, Object> mapAttributes = new HashMap<String, Object>(  );

        for ( java.lang.reflect.Field field : ContextParameter.class.getDeclaredFields(  ) )
        {
            ContextAttribute attribute = field.getAnnotation( ContextAttribute.class );

            if ( attribute != null )
            {
                String strAttributeKey = attribute.value(  );

                try
                {
                    field.setAccessible( true );

                    Object attributeValue = ReflectionUtils.getField( field, contextParameter );
                    mapAttributes.put( strAttributeKey, attributeValue );
                }
                catch ( SecurityException e )
                {
                    AppLogService.error( e );
                }
            }
        }

        return mapAttributes;
    }
}
