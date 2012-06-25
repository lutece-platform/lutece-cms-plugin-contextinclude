<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />
<jsp:useBean id="context" scope="session" class="fr.paris.lutece.plugins.contextinclude.web.ContextJspBean" />

<% context.init( request, context.RIGHT_MANAGE_CONTEXT ); %>
<%= context.getManageAdvancedParameters( request ) %>

<%@ include file="../../AdminFooter.jsp" %>