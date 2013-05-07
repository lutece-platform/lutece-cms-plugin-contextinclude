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
package fr.paris.lutece.plugins.contextinclude.web;

import fr.paris.lutece.plugins.contextinclude.business.Context;
import fr.paris.lutece.plugins.contextinclude.business.parameter.IContextParameter;
import fr.paris.lutece.plugins.contextinclude.service.ContextService;
import fr.paris.lutece.plugins.contextinclude.service.IContextService;
import fr.paris.lutece.plugins.contextinclude.service.parameter.ContextParameterService;
import fr.paris.lutece.plugins.contextinclude.service.parameter.IContextParameterService;
import fr.paris.lutece.plugins.contextinclude.web.action.ContextSearchFields;
import fr.paris.lutece.plugins.contextinclude.web.action.IContextPluginAction;
import fr.paris.lutece.plugins.contextinclude.web.action.IContextSearchFields;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.PluginActionManager;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.ConstraintViolation;


/**
 *
 * ContextJspBean
 *
 */
public class ContextJspBean extends PluginAdminPageJspBean
{
    /** The Constant RIGHT_MANAGE_CONTEXT. */
    public static final String RIGHT_MANAGE_CONTEXT = "CONTEXT_INCLUDE_MANAGEMENT";

    // PROPERTIES
    private static final String PROPERTY_MANAGE_CONTEXTS_PAGE_TITLE = "contextinclude.manage_contexts.pageTitle";
    private static final String PROPERTY_CREATE_CONTEXT_PAGE_TITLE = "contextinclude.create_context.pageTitle";
    private static final String PROPERTY_MODIFY_CONTEXT_PAGE_TITLE = "contextinclude.modify_context.pageTitle";
    private static final String PROPERTY_ERROR_PAGE_TITLE = "contextinclude.error.pageTitle";

    // MESSAGES
    private static final String MESSAGE_ERROR_GENERIC_MESSAGE = "contextinclude.message.error.genericMessage";
    private static final String MESSAGE_CONFIRM_REMOVE_CONTEXT = "contextinclude.message.confirm.removeContext";

    // PARAMETERS
    private static final String PARAMETER_ID_CONTEXT = "idContext";
    private static final String PARAMETER_CANCEL = "cancel";
    private static final String PARAMETER_APPLY = "apply";
    private static final String PARAMETER_PARAM_KEY = "paramKey";
    private static final String PARAMETER_PARAM_VALUE = "paramValue";

    // MARKS
    private static final String MARK_LIST_CONTEXTS = "listContexts";
    private static final String MARK_CONTEXT = "context";
    private static final String MARK_ERROR = "error";
    private static final String MARK_CONTEXT_ACTIONS = "contextActions";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_IS_WELL_ORDERED = "isWellOrdered";
    private static final String MARK_CONTEXT_PARAMETER = "contextParameter";

    // TEMPLATES
    private static final String TEMPLATE_MANAGE_CONTEXTS = "admin/plugins/contextinclude/manage_contexts.html";
    private static final String TEMPLATE_CREATE_CONTEXT = "admin/plugins/contextinclude/create_context.html";
    private static final String TEMPLATE_MODIFY_CONTEXT = "admin/plugins/contextinclude/modify_context.html";
    private static final String TEMPLATE_ERROR = "admin/plugins/contextinclude/error.html";
    private static final String TEMPLATE_MANAGE_ADVANCED_PARAMETERS = "admin/plugins/contextinclude/manage_advanced_parameters.html";

    // JSP
    private static final String JSP_MANAGE_CONTEXTS = "ManageContexts.jsp";
    private static final String JSP_MODIFY_CONTEXT = "ModifyContext.jsp";
    private static final String JSP_MANAGE_ADVANCED_PARAMETERS = "ManageAdvancedParameters.jsp";
    private static final String JSP_URL_DO_REMOVE_CONTEXT = "jsp/admin/plugins/contextinclude/DoRemoveContext.jsp";

    // CONSTANTS
    private static final String ANCHOR_CONTEXT = "context-";
    private final IContextService _contextService = SpringContextService.getBean( ContextService.BEAN_SERVICE );
    private final IContextSearchFields _searchFields = new ContextSearchFields(  );
    private final IContextParameterService _contextParameterService = SpringContextService.getBean( ContextParameterService.BEAN_SERVICE );

    // GET

    /**
     * Get manage contexts
     * @param request the HTTP request
     * @param response the response
     * @return the HTML code
     * @throws AccessDeniedException access denied if not authorized
     */
    public IPluginActionResult getManageContexts( HttpServletRequest request, HttpServletResponse response )
        throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MANAGE_CONTEXTS_PAGE_TITLE );

        // first - see if there is an invoked action
        IContextPluginAction action = PluginActionManager.getPluginAction( request, IContextPluginAction.class );

        if ( action != null )
        {
            AppLogService.debug( "Processing contextinclude action " + action.getName(  ) );

            return action.process( request, response, getUser(  ), _searchFields );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_LIST_CONTEXTS, _contextService.findAll(  ) );
        model.put( MARK_IS_WELL_ORDERED, _contextService.isWellOrdered(  ) );
        model.put( MARK_CONTEXT_PARAMETER, _contextParameterService.find(  ) );
        PluginActionManager.fillModel( request, getUser(  ), model, IContextPluginAction.class, MARK_CONTEXT_ACTIONS );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_CONTEXTS, getLocale(  ), model );

        IPluginActionResult result = new DefaultPluginActionResult(  );
        result.setHtmlContent( getAdminPage( template.getHtml(  ) ) );

        return result;
    }

    /**
     * Get create context
     * @param request the HTTP request
     * @return the HTML code
     */
    public String getCreateContext( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_CREATE_CONTEXT_PAGE_TITLE );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, this.getLocale(  ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_CREATE_CONTEXT, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Get modify context
     * @param request the HTTP request
     * @return the HTML code
     */
    public String getModifyContext( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MODIFY_CONTEXT_PAGE_TITLE );

        String strIdContext = request.getParameter( PARAMETER_ID_CONTEXT );

        if ( StringUtils.isNotBlank( strIdContext ) && StringUtils.isNumeric( strIdContext ) )
        {
            int nIdContext = Integer.parseInt( strIdContext );
            Map<String, Object> model = new HashMap<String, Object>(  );
            model.put( MARK_CONTEXT, _contextService.findByPrimaryKey( nIdContext ) );
            model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
            model.put( MARK_LOCALE, this.getLocale(  ) );

            HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MODIFY_CONTEXT, getLocale(  ), model );

            return getAdminPage( templateList.getHtml(  ) );
        }

        return getErrorPage( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, this.getLocale(  ) ) );
    }

    /**
     * Get confirm remove context
     * @param request the HTTP request
     * @return the HTML code
     */
    public String getConfirmRemoveContext( HttpServletRequest request )
    {
        String strIdContext = request.getParameter( PARAMETER_ID_CONTEXT );

        if ( StringUtils.isBlank( strIdContext ) || !StringUtils.isNumeric( strIdContext ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        UrlItem url = new UrlItem( JSP_URL_DO_REMOVE_CONTEXT );
        url.addParameter( PARAMETER_ID_CONTEXT, strIdContext );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CONTEXT, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Gets the manage advanced parameters.
     *
     * @param request the request
     * @return the manage advanced parameters
     */
    public String getManageAdvancedParameters( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_CONTEXT_PARAMETER, _contextParameterService.find(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_ADVANCED_PARAMETERS, getLocale(  ),
                model );

        return getAdminPage( template.getHtml(  ) );
    }

    // DO

    /**
     * Do create a context
     * @param request the HTTP request
     * @return the JSP return
     */
    public String doCreateContext( HttpServletRequest request )
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );

        if ( StringUtils.isNotBlank( strCancel ) )
        {
            return JSP_MANAGE_CONTEXTS;
        }

        Context context = new Context(  );

        // Populate the bean
        populate( context, request );

        // Check mandatory fields
        Set<ConstraintViolation<Context>> constraintViolations = BeanValidationUtil.validate( context );

        if ( constraintViolations.size(  ) > 0 )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Populate the context parameters
        this.setContextData( context, request );

        try
        {
            _contextService.create( context );
        }
        catch ( Exception ex )
        {
            // Something wrong happened... a database check might be needed
            AppLogService.error( ex.getMessage(  ) + " when creating a context ", ex );
            // Revert
            _contextService.remove( context.getIdContext(  ) );

            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR );
        }

        String strApply = request.getParameter( PARAMETER_APPLY );

        if ( StringUtils.isNotBlank( strApply ) )
        {
            UrlItem url = new UrlItem( JSP_MODIFY_CONTEXT );
            url.addParameter( PARAMETER_ID_CONTEXT, context.getIdContext(  ) );

            return url.getUrl(  );
        }

        return JSP_MANAGE_CONTEXTS;
    }

    /**
     * Do update a context
     * @param request the HTTP request
     * @return the JSP return
     */
    public String doModifyContext( HttpServletRequest request )
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );

        if ( StringUtils.isNotBlank( strCancel ) )
        {
            return JSP_MANAGE_CONTEXTS;
        }

        String strJspReturn = JSP_MANAGE_CONTEXTS;
        String strIdContext = request.getParameter( PARAMETER_ID_CONTEXT );

        if ( StringUtils.isNotBlank( strIdContext ) && StringUtils.isNumeric( strIdContext ) )
        {
            int nIdContext = Integer.parseInt( strIdContext );
            Context context = _contextService.findByPrimaryKey( nIdContext );

            // Populate the bean
            populate( context, request );

            // Check mandatory fields
            Set<ConstraintViolation<Context>> constraintViolations = BeanValidationUtil.validate( context );

            if ( constraintViolations.size(  ) > 0 )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            // Populate the context parameters
            this.setContextData( context, request );

            try
            {
                _contextService.update( context );
            }
            catch ( Exception ex )
            {
                // Something wrong happened... a database check might be needed
                AppLogService.error( ex.getMessage(  ) + " when updating a context ", ex );

                strJspReturn = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE,
                        AdminMessage.TYPE_ERROR );
            }

            String strApply = request.getParameter( PARAMETER_APPLY );

            if ( StringUtils.isNotBlank( strApply ) )
            {
                UrlItem url = new UrlItem( JSP_MODIFY_CONTEXT );
                url.addParameter( PARAMETER_ID_CONTEXT, context.getIdContext(  ) );

                strJspReturn = url.getUrl(  );
            }
        }
        else
        {
            strJspReturn = AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS,
                    AdminMessage.TYPE_ERROR );
        }

        return strJspReturn;
    }

    /**
     * Do remove a context
     * @param request the HTTP request
     * @return the JSP return
     */
    public String doRemoveContext( HttpServletRequest request )
    {
        String strIdContext = request.getParameter( PARAMETER_ID_CONTEXT );

        if ( StringUtils.isBlank( strIdContext ) || !StringUtils.isNumeric( strIdContext ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nIdContext = Integer.parseInt( strIdContext );

        try
        {
            _contextService.remove( nIdContext );
        }
        catch ( Exception ex )
        {
            // Something wrong happened... a database check might be needed
            AppLogService.error( ex.getMessage(  ) + " when deleting a context ", ex );

            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR );
        }

        return JSP_MANAGE_CONTEXTS;
    }

    /**
     * Do reorder the contexts
     * @param request {@link HttpServletRequest}
     * @return the jsp URL to display the form to manage contexts
     */
    public String doReorderContexts( HttpServletRequest request )
    {
        _contextService.doReorderContexts(  );

        return JSP_MANAGE_CONTEXTS;
    }

    /**
     * Move up the priority of the attribute field
     * @param request HttpServletRequest
     * @return The Jsp URL of the process result
     */
    public String doMoveUpContext( HttpServletRequest request )
    {
        String strIdContext = request.getParameter( PARAMETER_ID_CONTEXT );

        if ( StringUtils.isNotBlank( strIdContext ) && StringUtils.isNumeric( strIdContext ) )
        {
            int nIdContext = Integer.parseInt( strIdContext );

            List<Context> listContexts = _contextService.findAll(  );
            Context previousContext = null;
            Context currentContext = null;

            Iterator<Context> it = listContexts.iterator(  );
            previousContext = it.next(  );
            currentContext = it.next(  );

            while ( it.hasNext(  ) && ( currentContext.getIdContext(  ) != nIdContext ) )
            {
                previousContext = currentContext;
                currentContext = it.next(  );
            }

            int previousAttributePriority = previousContext.getPriority(  );
            int currentAttributePriority = currentContext.getPriority(  );
            previousContext.setPriority( currentAttributePriority );
            currentContext.setPriority( previousAttributePriority );

            try
            {
                _contextService.update( previousContext );
                _contextService.update( currentContext );
            }
            catch ( Exception ex )
            {
                // Something wrong happened... a database check might be needed
                AppLogService.error( ex.getMessage(  ) + " when moving up a context ", ex );

                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE,
                    AdminMessage.TYPE_ERROR );
            }

            UrlItem url = new UrlItem( JSP_MANAGE_CONTEXTS );
            url.setAnchor( ANCHOR_CONTEXT + strIdContext );

            return url.getUrl(  );
        }

        return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
    }

    /**
     * Move down the priority of the attribute field
     * @param request HttpServletRequest
     * @return The Jsp URL of the process result
     */
    public String doMoveDownContext( HttpServletRequest request )
    {
        String strIdContext = request.getParameter( PARAMETER_ID_CONTEXT );

        if ( StringUtils.isNotBlank( strIdContext ) && StringUtils.isNumeric( strIdContext ) )
        {
            int nIdContext = Integer.parseInt( strIdContext );

            List<Context> listContexts = _contextService.findAll(  );
            Context nextContext = null;
            Context currentContext = null;

            Iterator<Context> it = listContexts.iterator(  );
            currentContext = it.next(  );
            nextContext = it.next(  );

            while ( it.hasNext(  ) && ( currentContext.getIdContext(  ) != nIdContext ) )
            {
                currentContext = nextContext;
                nextContext = it.next(  );
            }

            int nextAttributePriority = nextContext.getPriority(  );
            int currentAttributePriority = currentContext.getPriority(  );
            nextContext.setPriority( currentAttributePriority );
            currentContext.setPriority( nextAttributePriority );

            try
            {
                _contextService.update( nextContext );
                _contextService.update( currentContext );
            }
            catch ( Exception ex )
            {
                // Something wrong happened... a database check might be needed
                AppLogService.error( ex.getMessage(  ) + " when moving down a context ", ex );

                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE,
                    AdminMessage.TYPE_ERROR );
            }

            UrlItem url = new UrlItem( JSP_MANAGE_CONTEXTS );
            url.setAnchor( ANCHOR_CONTEXT + strIdContext );

            return url.getUrl(  );
        }

        return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
    }

    /**
     * Do activate/deactive a context
     * @param request the HTTP request
     * @return the jsp URL
     */
    public String doActivateDeactivateContext( HttpServletRequest request )
    {
        String strIdContext = request.getParameter( PARAMETER_ID_CONTEXT );

        if ( StringUtils.isNotBlank( strIdContext ) && StringUtils.isNumeric( strIdContext ) )
        {
            int nIdContext = Integer.parseInt( strIdContext );

            Context context = _contextService.findByPrimaryKey( nIdContext );

            if ( context != null )
            {
                context.setActive( !context.isActive(  ) );

                try
                {
                    _contextService.update( context );
                }
                catch ( Exception ex )
                {
                    // Something wrong happened... a database check might be needed
                    AppLogService.error( ex.getMessage(  ) + " when activating/deactivating a context ", ex );

                    return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE,
                        AdminMessage.TYPE_ERROR );
                }

                UrlItem url = new UrlItem( JSP_MANAGE_CONTEXTS );
                url.setAnchor( ANCHOR_CONTEXT + strIdContext );

                return url.getUrl(  );
            }

            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_STOP );
        }

        return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
    }

    /**
     * Do modify context parameters.
     *
     * @param request the request
     * @return the string
     */
    public String doModifyContextParameters( HttpServletRequest request )
    {
        IContextParameter contextParameter = _contextParameterService.find(  );
        populate( contextParameter, request );
        _contextParameterService.update(  );

        return JSP_MANAGE_ADVANCED_PARAMETERS;
    }

    // PRIVATE METHODS

    /**
     * Get error page
     * @param strErrorMessage the error message
     * @return the HTML code
     */
    private String getErrorPage( String strErrorMessage )
    {
        setPageTitleProperty( PROPERTY_ERROR_PAGE_TITLE );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_ERROR, strErrorMessage );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_ERROR, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Set context data
     * @param context the context
     * @param request the HTTP request
     */
    private void setContextData( Context context, HttpServletRequest request )
    {
        Map<String, List<String>> map = new HashMap<String, List<String>>(  );

        if ( context.getNbParams(  ) > 0 )
        {
            for ( int nIndex = 1; nIndex <= context.getNbParams(  ); nIndex++ )
            {
                String strParamKey = request.getParameter( PARAMETER_PARAM_KEY + nIndex );
                String strParamValue = request.getParameter( PARAMETER_PARAM_VALUE + nIndex );

                if ( StringUtils.isNotBlank( strParamKey ) && StringUtils.isNotBlank( strParamValue ) )
                {
                    List<String> listValues = map.get( strParamKey );

                    if ( listValues == null )
                    {
                        listValues = new ArrayList<String>(  );
                    }

                    listValues.add( strParamValue );
                    map.put( strParamKey, listValues );
                }
            }
        }

        context.setMapParameters( map );
    }
}
