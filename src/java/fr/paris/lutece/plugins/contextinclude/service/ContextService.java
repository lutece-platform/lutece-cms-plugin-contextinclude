/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.contextinclude.service;

import fr.paris.lutece.plugins.contextinclude.business.Context;
import fr.paris.lutece.plugins.contextinclude.business.IContextDAO;
import fr.paris.lutece.plugins.contextinclude.business.IContextParamsDAO;
import fr.paris.lutece.plugins.contextinclude.util.OperatorEnum;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;


/**
 *
 * ContextService
 *
 */
public class ContextService implements IContextService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = "contextinclude.contextService";
    @Inject
    private IContextDAO _contextDAO;
    @Inject
    private IContextParamsDAO _contextParameterDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ContextIncludePlugin.TRANSACTION_MANAGER )
    public void create( Context context )
    {
        _contextDAO.insert( context, ContextIncludePlugin.getPlugin(  ) );
        insertParams( context );
        // Reorder the list
        doReorderContextsGreaterPriority( context.getPriority(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ContextIncludePlugin.TRANSACTION_MANAGER )
    public void update( Context context )
    {
        _contextDAO.store( context, ContextIncludePlugin.getPlugin(  ) );
        // First delete the parameters
        _contextParameterDAO.delete( context.getIdContext(  ), ContextIncludePlugin.getPlugin(  ) );
        // Then insert the parameters
        insertParams( context );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ContextIncludePlugin.TRANSACTION_MANAGER )
    public void remove( int nIdContext )
    {
        _contextParameterDAO.delete( nIdContext, ContextIncludePlugin.getPlugin(  ) );
        _contextDAO.delete( nIdContext, ContextIncludePlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Context findByPrimaryKey( int nIdContext )
    {
        Context context = _contextDAO.load( nIdContext, ContextIncludePlugin.getPlugin(  ) );

        if ( context != null )
        {
            context.setMapParameters( _contextParameterDAO.load( nIdContext, ContextIncludePlugin.getPlugin(  ) ) );
        }

        return context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Context> findAll(  )
    {
        List<Context> listContexts = _contextDAO.findAll( ContextIncludePlugin.getPlugin(  ) );

        if ( ( listContexts != null ) && !listContexts.isEmpty(  ) )
        {
            for ( Context context : listContexts )
            {
                context.setMapParameters( _contextParameterDAO.load( context.getIdContext(  ),
                        ContextIncludePlugin.getPlugin(  ) ) );
            }
        }

        return listContexts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Context> findActiveContexts(  )
    {
        List<Context> listContexts = _contextDAO.findActiveContexts( ContextIncludePlugin.getPlugin(  ) );

        if ( ( listContexts != null ) && !listContexts.isEmpty(  ) )
        {
            for ( Context context : listContexts )
            {
                context.setMapParameters( _contextParameterDAO.load( context.getIdContext(  ),
                        ContextIncludePlugin.getPlugin(  ) ) );
            }
        }

        return listContexts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Context> findByRelativePriority( OperatorEnum operator, int nPriority )
    {
        List<Context> listContexts = _contextDAO.findByRelativePriority( operator, nPriority,
                ContextIncludePlugin.getPlugin(  ) );

        if ( ( listContexts != null ) && !listContexts.isEmpty(  ) )
        {
            for ( Context context : listContexts )
            {
                context.setMapParameters( _contextParameterDAO.load( context.getIdContext(  ),
                        ContextIncludePlugin.getPlugin(  ) ) );
            }
        }

        return listContexts;
    }

    // CHECKS

    /**
     * Check if the list of contexts are well ordered
     * @return true if it is well ordered, false otherwise
     */
    @Override
    public boolean isWellOrdered(  )
    {
        int nPriority = 1;

        for ( Context context : findAll(  ) )
        {
            if ( context.getPriority(  ) != nPriority )
            {
                return false;
            }

            nPriority++;
        }

        return true;
    }

    // DO

    /**
     * {@inheritDoc}
     */
    @Override
    public void doReorderContexts(  )
    {
        int nPriority = 1;

        for ( Context context : findAll(  ) )
        {
            context.setPriority( nPriority++ );
            this.update( context );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doReorderContextsGreaterPriority( int nCurrentPriority )
    {
        // Move up all contexts that have an order >= nOrder (ie order + 1)
        int nPriority = nCurrentPriority + 1;

        for ( Context context : this.findByRelativePriority( OperatorEnum.GREATER_OR_EQUAL, nCurrentPriority ) )
        {
            context.setPriority( nPriority++ );
            this.update( context );
        }
    }

    // PRIVATE METHODS

    /**
     * Insert the parameters of the context
     * @param context the context
     */
    private void insertParams( Context context )
    {
        Map<String, List<String>> mapParams = context.getMapParameters(  );

        if ( mapParams != null )
        {
            for ( Entry<String, List<String>> param : mapParams.entrySet(  ) )
            {
                String strParamKey = param.getKey(  );

                for ( String strParamValue : param.getValue(  ) )
                {
                    _contextParameterDAO.insert( context.getIdContext(  ), strParamKey, strParamValue,
                        ContextIncludePlugin.getPlugin(  ) );
                }
            }
        }
    }
}
