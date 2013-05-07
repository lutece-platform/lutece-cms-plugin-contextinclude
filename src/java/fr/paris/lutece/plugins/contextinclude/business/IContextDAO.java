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
package fr.paris.lutece.plugins.contextinclude.business;

import fr.paris.lutece.plugins.contextinclude.util.OperatorEnum;
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 *
 * IContextDAO
 *
 */
public interface IContextDAO
{
    /**
     * Insert a new context
     * @param context the context
     * @param plugin the plugin
     * @return the new primary key
     */
    int insert( Context context, Plugin plugin );

    /**
     * Store a context
     * @param context the context
     * @param plugin the plugin
     */
    void store( Context context, Plugin plugin );

    /**
     * Delete the context
     * @param nIdContext the id context
     * @param plugin the plugin
     */
    void delete( int nIdContext, Plugin plugin );

    /**
     * Load a {@link Context}
     * @param nIdContext the id context
     * @param plugin the plugin
     * @return a {@link Context}
     */
    Context load( int nIdContext, Plugin plugin );

    /**
     * Find all contexts
     * @param plugin the plugin
     * @return a list of {@link Context}
     */
    List<Context> findAll( Plugin plugin );

    /**
     * Find active contexts
     * @param plugin the plugin
     * @return a list of {@link Context}
     */
    List<Context> findActiveContexts( Plugin plugin );

    /**
     * Find by relative priority
     * @param operator the operator
     * @param nPriority the priority to compare to
     * @param plugin the plugin
     * @return a list of {@link Context}
     */
    List<Context> findByRelativePriority( OperatorEnum operator, int nPriority, Plugin plugin );
}
