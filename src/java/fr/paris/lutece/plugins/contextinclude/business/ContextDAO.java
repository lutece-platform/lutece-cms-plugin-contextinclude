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
package fr.paris.lutece.plugins.contextinclude.business;

import fr.paris.lutece.plugins.contextinclude.util.OperatorEnum;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * ContextDAO
 *
 */
public class ContextDAO implements IContextDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_context ) FROM context ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO context (id_context, html, nb_params, priority, strict, active) VALUES ( ?,?,?,?,?,? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM context WHERE id_context = ? ";
    private static final String SQL_QUERY_SELECT = " SELECT id_context, html, nb_params, priority, strict, active FROM context WHERE id_context = ? ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT id_context, html, nb_params, priority, strict, active FROM context ";
    private static final String SQL_QUERY_UPDATE = " UPDATE context SET html = ?, nb_params = ?, priority = ?, strict = ?, active = ? WHERE id_context = ? ";
    private static final String SQL_WHERE = " WHERE ";
    private static final String SQL_FILTER_PRIORITY = " priority ";
    private static final String SQL_FILTER_ACTIVE = " active = ? ";
    private static final String SQL_ORDER_BY_PRIORITY = " ORDER BY priority ASC ";
    private static final String QUESTION_MARK = " ? ";

    // PARAMS

    /**
     * New primary key
     * @param plugin the plugin
     * @return a new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey = 1;

        if ( daoUtil.next(  ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free(  );

        return nKey;
    }

    /**
         * {@inheritDoc}
         */
    @Override
    public synchronized int insert( Context context, Plugin plugin )
    {
        int nIndex = 1;
        int nIdContext = newPrimaryKey( plugin );
        context.setIdContext( nIdContext );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( nIndex++, context.getIdContext(  ) );
        daoUtil.setString( nIndex++, context.getHtml(  ) );
        daoUtil.setInt( nIndex++, context.getNbParams(  ) );
        daoUtil.setInt( nIndex++, context.getPriority(  ) );
        daoUtil.setBoolean( nIndex++, context.isStrict(  ) );
        daoUtil.setBoolean( nIndex++, context.isActive(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return nIdContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Context context, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;

        daoUtil.setString( nIndex++, context.getHtml(  ) );
        daoUtil.setInt( nIndex++, context.getNbParams(  ) );
        daoUtil.setInt( nIndex++, context.getPriority(  ) );
        daoUtil.setBoolean( nIndex++, context.isStrict(  ) );
        daoUtil.setBoolean( nIndex++, context.isActive(  ) );

        daoUtil.setInt( nIndex++, context.getIdContext(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdContext, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdContext );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Context load( int nIdContext, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nIdContext );
        daoUtil.executeQuery(  );

        Context context = new Context(  );

        int nIndex = 1;

        if ( daoUtil.next(  ) )
        {
            context.setIdContext( daoUtil.getInt( nIndex++ ) );
            context.setHtml( daoUtil.getString( nIndex++ ) );
            context.setNbParams( daoUtil.getInt( nIndex++ ) );
            context.setPriority( daoUtil.getInt( nIndex++ ) );
            context.setStrict( daoUtil.getBoolean( nIndex++ ) );
            context.setActive( daoUtil.getBoolean( nIndex++ ) );
        }

        daoUtil.free(  );

        return context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Context> findAll( Plugin plugin )
    {
        List<Context> listContexts = new ArrayList<Context>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL + SQL_ORDER_BY_PRIORITY, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            Context context = new Context(  );
            context.setIdContext( daoUtil.getInt( nIndex++ ) );
            context.setHtml( daoUtil.getString( nIndex++ ) );
            context.setNbParams( daoUtil.getInt( nIndex++ ) );
            context.setPriority( daoUtil.getInt( nIndex++ ) );
            context.setStrict( daoUtil.getBoolean( nIndex++ ) );
            context.setActive( daoUtil.getBoolean( nIndex++ ) );

            listContexts.add( context );
        }

        daoUtil.free(  );

        return listContexts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Context> findActiveContexts( Plugin plugin )
    {
        List<Context> listContexts = new ArrayList<Context>(  );
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_ALL );
        sbSQL.append( SQL_WHERE ).append( SQL_FILTER_ACTIVE );
        sbSQL.append( SQL_ORDER_BY_PRIORITY );

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString(  ), plugin );
        daoUtil.setBoolean( 1, true );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            Context context = new Context(  );
            context.setIdContext( daoUtil.getInt( nIndex++ ) );
            context.setHtml( daoUtil.getString( nIndex++ ) );
            context.setNbParams( daoUtil.getInt( nIndex++ ) );
            context.setPriority( daoUtil.getInt( nIndex++ ) );
            context.setStrict( daoUtil.getBoolean( nIndex++ ) );
            context.setActive( daoUtil.getBoolean( nIndex++ ) );

            listContexts.add( context );
        }

        daoUtil.free(  );

        return listContexts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Context> findByRelativePriority( OperatorEnum operator, int nPriority, Plugin plugin )
    {
        List<Context> listContexts = new ArrayList<Context>(  );
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_ALL );
        sbSQL.append( SQL_WHERE ).append( SQL_FILTER_PRIORITY );
        sbSQL.append( operator ).append( QUESTION_MARK );
        sbSQL.append( SQL_ORDER_BY_PRIORITY );

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString(  ), plugin );
        daoUtil.setInt( 1, nPriority );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            Context context = new Context(  );
            context.setIdContext( daoUtil.getInt( nIndex++ ) );
            context.setHtml( daoUtil.getString( nIndex++ ) );
            context.setNbParams( daoUtil.getInt( nIndex++ ) );
            context.setPriority( daoUtil.getInt( nIndex++ ) );
            context.setStrict( daoUtil.getBoolean( nIndex++ ) );
            context.setActive( daoUtil.getBoolean( nIndex++ ) );

            listContexts.add( context );
        }

        daoUtil.free(  );

        return listContexts;
    }
}
