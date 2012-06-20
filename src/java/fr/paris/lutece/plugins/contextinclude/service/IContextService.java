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
import fr.paris.lutece.plugins.contextinclude.util.OperatorEnum;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 *
 * IContextService
 *
 */
public interface IContextService
{
    /**
     * Create a new context
     * @param context the context
     */
    @Transactional( ContextIncludePlugin.TRANSACTION_MANAGER )
    void create( Context context );

    /**
     * Update a context
     * @param context a context
     */
    @Transactional( ContextIncludePlugin.TRANSACTION_MANAGER )
    void update( Context context );

    /**
     * Remove a context
     * @param nIdContext the id context
     */
    @Transactional( ContextIncludePlugin.TRANSACTION_MANAGER )
    void remove( int nIdContext );

    /**
     * Find context by its primary key
     * @param nIdContext the id context
     * @return a {@link Context}
     */
    Context findByPrimaryKey( int nIdContext );

    /**
     * Find all contexts
     * @return a list of {@link Context}
     */
    List<Context> findAll(  );

    /**
     * Find active contexts
     * @return a list of {@link Context}
     */
    List<Context> findActiveContexts(  );

    /**
     * Find by relative priority
     * @param operator the operator
     * @param nPriority the priority to compare to
     * @return a list of {@link Context}
     */
    List<Context> findByRelativePriority( OperatorEnum operator, int nPriority );

    // CHECKS

    /**
    * Check if the list of contexts are well ordered
    * @return true if it is well ordered, false otherwise
    */
    boolean isWellOrdered(  );

    // DO

    /**
     * Reorder all demand types
     */
    void doReorderContexts(  );

    /**
     * Reorder the contexts that are greater or equal to the nCurrentPriority.
     * <br />
     * Those context orders are move up by 1.
     * @param nCurrentPriority the current priority
     */
    void doReorderContextsGreaterPriority( int nCurrentPriority );
}
