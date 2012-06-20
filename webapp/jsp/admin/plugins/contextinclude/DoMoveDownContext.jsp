<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="context" scope="session" class="fr.paris.lutece.plugins.contextinclude.web.ContextJspBean" />
<% 
	context.init( request, context.RIGHT_MANAGE_CONTEXT );
 	response.sendRedirect( context.doMoveDownContext( request ) );
%>
