<%@page import="fr.paris.lutece.portal.web.pluginaction.IPluginActionResult"%>

<jsp:useBean id="context" scope="session" class="fr.paris.lutece.plugins.contextinclude.web.ContextJspBean" />

<% 
	context.init( request, context.RIGHT_MANAGE_CONTEXT );
	IPluginActionResult result = context.getManageContexts( request, response );
	if ( result.getRedirect(  ) != null )
	{
		response.sendRedirect( result.getRedirect(  ) );
	}
	else if ( result.getHtmlContent(  ) != null )
	{
%>
		<%@ page errorPage="../../ErrorPage.jsp" %>
		<jsp:include page="../../AdminHeader.jsp" />

		<%= result.getHtmlContent(  ) %>

		<%@ include file="../../AdminFooter.jsp" %>
<%
	}
%>
