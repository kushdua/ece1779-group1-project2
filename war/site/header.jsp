<%@page import="com.google.appengine.api.users.User" %>
<%@page import="com.google.appengine.api.users.UserService" %>
<%@page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@page import="ece1779.appengine.*" %>

<%
   UserService userService = UserServiceFactory.getUserService();
/* if(userService == null) response.getWriter().println("user service is null");
 */   User user = userService.getCurrentUser();
/*    if(user == null) response.getWriter().println("user is null");
 */   UserPrefs userPrefs = null;
   
   if(user!=null)
   {
   		userPrefs = UserPrefs.getPrefsForUser(user,true);
   }
%>

<div class="navbar navbar-inverse">
    <div class="navbar-inner">
        <a class="brand" href="#">ECE1779 AppEngine Project</a>
        <ul class="nav">
            <%= (request==null || (request!=null && request.getRequestURI()==null) ||
                (request!=null && request.getRequestURI().contains("welcome.jsp")))?
            		"<li><a href='welcome.jsp' class='active'>Home</a></li>" : 
            		"<li><a href='welcome.jsp'>Home</a></li>" %>
        <% if(request!=null && request.getRequestURI()!=null) { %>
            <%= (request.getRequestURI().contains("stats.jsp"))?
                    "<li><a href='upload.jsp' class='active'>Statistics</a></li>" : 
                    "<li><a href='upload.jsp'>Upload</a></li>" %>
            <%= (request.getRequestURI().contains("play.jsp"))?
                    "<li><a href='manager.jsp' class='active'>Play game</a></li>" : 
                    "<li><a href='manager.jsp'>Manager UI</a></li>" %>
            <%= (request.getRequestURI().contains("view_games.jsp"))?
                    "<li><a href='view.jsp' class='active'>View Games</a></li>" : 
                    "<li><a href='view.jsp'>View Gallery</a></li>" %>
            <% if(user!=null && userService!=null)
            { %>
            	<li><a href="<%= userService.createLogoutURL("/") %>">Logout</a></li>
            <% }  else if(user==null) { %> <li><a href="<%= userService.createLoginURL("/") %>">Login</a></li><% } %>
        <% } %>
        </ul>
    </div>
</div>

<% if(request!=null && userPrefs!=null && userPrefs.getErrorMessages()!=null && userPrefs.getErrorMessages().length()!=0) { %>
<div class="alert alert-error">  
  <a class="close" data-dismiss="alert">×</a>  
  <strong>Error: </strong> <%= userPrefs.getErrorMessages() %> 
  <% userPrefs.setErrorMessages("");
  	 userPrefs.save(); %> 
</div> 
<% } else if(request!=null && userPrefs != null && userPrefs.getSuccessMessages()!=null && userPrefs.getSuccessMessages().length()!=0) { %>
<div class="alert alert-success">  
  <a class="close" data-dismiss="alert">×</a>  
  <strong>Success: </strong> <%= userPrefs.getSuccessMessages() %>  
  <% userPrefs.setSuccessMessages("");
  	 userPrefs.save(); %>
</div> 
<% } %>