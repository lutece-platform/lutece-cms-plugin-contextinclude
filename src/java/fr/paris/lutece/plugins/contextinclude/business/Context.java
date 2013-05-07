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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import javax.validation.constraints.Min;


/**
 *
 * Context
 *
 */
public class Context
{
    private int _nIdContext;
    private String _strHtml;
    @Min( 0 )
    private int _nNbParams;
    private int _nPriority;
    private boolean _bStrict;
    private boolean _bActive;
    private Map<String, List<String>> _mapParameters;

    /**
     * Sets the id context.
     *
     * @param nIdContext the _nIdContext to set
     */
    public void setIdContext( int nIdContext )
    {
        this._nIdContext = nIdContext;
    }

    /**
     * Gets the id context.
     *
     * @return the _nIdContext
     */
    public int getIdContext(  )
    {
        return _nIdContext;
    }

    /**
     * Sets the html.
     *
     * @param strHtml the _strHtml to set
     */
    public void setHtml( String strHtml )
    {
        this._strHtml = strHtml;
    }

    /**
     * Gets the html.
     *
     * @return the _strHtml
     */
    public String getHtml(  )
    {
        return _strHtml;
    }

    /**
     * Sets the nb params.
     *
     * @param nNbParams the _nNbParams to set
     */
    public void setNbParams( int nNbParams )
    {
        this._nNbParams = nNbParams;
    }

    /**
     * Gets the nb params.
     *
     * @return the _nNbParams
     */
    public int getNbParams(  )
    {
        return _nNbParams;
    }

    /**
     * Sets the priority.
     *
     * @param nPriority the _nPriority to set
     */
    public void setPriority( int nPriority )
    {
        this._nPriority = nPriority;
    }

    /**
     * Gets the priority.
     *
     * @return the _nPriority
     */
    public int getPriority(  )
    {
        return _nPriority;
    }

    /**
     * Sets the strict.
     *
     * @param bStrict the _bStrict to set
     */
    public void setStrict( boolean bStrict )
    {
        this._bStrict = bStrict;
    }

    /**
     * Checks if is strict.
     *
     * @return the _bStrict
     */
    public boolean isStrict(  )
    {
        return _bStrict;
    }

    /**
     * Sets the active.
     *
     * @param bActive the _bActive to set
     */
    public void setActive( boolean bActive )
    {
        this._bActive = bActive;
    }

    /**
     * Checks if is active.
     *
     * @return the _bActive
     */
    public boolean isActive(  )
    {
        return _bActive;
    }

    /**
     * Sets the map parameters.
     *
     * @param mapParameters the mapParameters to set
     */
    public void setMapParameters( Map<String, List<String>> mapParameters )
    {
        this._mapParameters = mapParameters;
    }

    /**
     * Gets the map parameters.
     *
     * @return the mapParameters
     */
    public Map<String, List<String>> getMapParameters(  )
    {
        return _mapParameters;
    }

    /**
     * Check if this context is invoked.
     *
     * @param request the HTTP request
     * @return true if it context, false otherwise
     */
    public boolean isInvoked( HttpServletRequest request )
    {
        // Check request for daemons
        if ( request == null )
        {
            return false;
        }

        // No parameters
        if ( _mapParameters == null )
        {
            // In strict mode, the request must not have any parameters
            if ( _bStrict )
            {
                return ( request.getParameterMap(  ) == null ) || request.getParameterMap(  ).isEmpty(  );
            }

            // Otherwise, always return true
            return true;
        }

        // In strict mode, check the size of the parameter map
        if ( _bStrict )
        {
            if ( request.getParameterMap(  ) == null )
            {
                return false;
            }

            if ( _mapParameters.size(  ) != request.getParameterMap(  ).size(  ) )
            {
                return false;
            }
        }

        // Check each parameter
        for ( Entry<String, List<String>> params : _mapParameters.entrySet(  ) )
        {
            // Configured parameters
            String strKey = params.getKey(  );
            List<String> listValues = params.getValue(  );

            // Request parameters
            String[] listRequestValues = request.getParameterValues( strKey );

            if ( listRequestValues != null )
            {
                // In strict mode, the size of the configured and request parameters must be equal
                if ( _bStrict )
                {
                    if ( listRequestValues.length != listValues.size(  ) )
                    {
                        return false;
                    }
                }

                // Check if the configured parameter is contained in the list of request parameters
                for ( String strValue : listValues )
                {
                    if ( !Arrays.asList( listRequestValues ).contains( strValue ) )
                    {
                        return false;
                    }
                }
            }
            else
            {
                return false;
            }
        }

        return true;
    }
}
