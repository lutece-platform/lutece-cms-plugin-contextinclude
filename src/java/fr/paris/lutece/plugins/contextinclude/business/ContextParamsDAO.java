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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * ContextParamsDAO
 *
 */
public class ContextParamsDAO implements IContextParamsDAO
{
    private static final String SQL_QUERY_SELECT = " SELECT param_key, param_value FROM contextinclude_context_params WHERE id_context = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO contextinclude_context_params ( id_context, param_key, param_value ) VALUES ( ?,?,? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM contextinclude_context_params WHERE id_context = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( int nIdContext, String strParamKey, String strParamValue, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, nIdContext );
        daoUtil.setString( nIndex++, strParamKey );
        daoUtil.setString( nIndex++, strParamValue );

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
    public Map<String, List<String>> load( int nIdContext, Plugin plugin )
    {
        Map<String, List<String>> map = new HashMap<String, List<String>>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nIdContext );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            String strKey = daoUtil.getString( nIndex++ );
            String strValue = daoUtil.getString( nIndex++ );
            List<String> listValues = map.get( strKey );

            if ( listValues == null )
            {
                listValues = new ArrayList<String>(  );
            }

            listValues.add( strValue );
            map.put( strKey, listValues );
        }

        daoUtil.free(  );

        return map;
    }
}
