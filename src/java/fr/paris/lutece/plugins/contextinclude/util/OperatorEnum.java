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
package fr.paris.lutece.plugins.contextinclude.util;


/**
 * OperatorEnum.
 */
public enum OperatorEnum
{
    /** The equal. */
    EQUAL( 0, "=" ),
    /** The greater. */
    GREATER( 1, ">" ),
    /** The lower. */
    LOWER( 2, "<" ),
    /** The greater or equal. */
    GREATER_OR_EQUAL( 3, ">=" ),
    /** The lower or equal. */
    LOWER_OR_EQUAL( 4, "<=" );
    /** The _n id. */
    private int _nId;

    /** The _str operator. */
    private String _strOperator;

    /**
     * Constructor.
     *
     * @param nId the id of the operator
     * @param strOperator the string value of the operator
     */
    private OperatorEnum( int nId, String strOperator )
    {
        _nId = nId;
        _strOperator = strOperator;
    }

    /**
     * Get the id of the operator.
     *
     * @return the id of the operator
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Get the String value of the operator.
     *
     * @return the String value of the operator
     */
    @Override
    public String toString(  )
    {
        return _strOperator;
    }
}
