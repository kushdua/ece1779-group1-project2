<%@page import="com.google.appengine.api.users.User" %>
<%@page import="com.google.appengine.api.users.UserService" %>
<%@page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@page import="ece1779.appengine.*" %>

<%
   boolean loggedIn=false;
   boolean onWelcomeAlready=false;
   String userName = "";
   boolean logoutRequest = false;
   
   UserService userService = UserServiceFactory.getUserService();
   User user = userService.getCurrentUser();
   UserPrefs userPrefs = null;
   
   if(user!=null)
   {
   		userPrefs = UserPrefs.getPrefsForUser(user);
   }
%>

<div class="navbar navbar-inverse">
    <div class="navbar-inner">
        <a class="brand" href="#">ECE1779 AppEngine Project</a>
        <ul class="nav">
            <%= (request==null || request.getRequestURI().contains("welcome.jsp"))?
            		"<li><a href='welcome.jsp' class='active'>Home</a></li>" : 
            		"<li><a href='welcome.jsp'>Home</a></li>" %>
        <% if(request!=null) { %>
            <%= (request.getRequestURI().contains("stats.jsp"))?
                    "<li><a href='upload.jsp' class='active'>Statistics</a></li>" : 
                    "<li><a href='upload.jsp'>Upload</a></li>" %>
            <%= (request.getRequestURI().contains("play.jsp"))?
                    "<li><a href='manager.jsp' class='active'>Play game</a></li>" : 
                    "<li><a href='manager.jsp'>Manager UI</a></li>" %>
            <%= (request.getRequestURI().contains("view_games.jsp"))?
                    "<li><a href='view.jsp' class='active'>View Games</a></li>" : 
                    "<li><a href='view.jsp'>View Gallery</a></li>" %>
            <li><a href="<%= userService.createLogoutURL("/") %>">Logout</a></li>
            <%-- <%= (request!=null && request.getRequestURI().contains("welcome.jsp?logout=true"))?
                    "<li><a href='welcome.jsp?logout=true' class='active'>Logout</a></li>" : 
                    "<li><a href='welcome.jsp?logout=true'>Logout</a></li>" %> --%>
        <% } %>
        </ul>
    </div>
</div>

<% if(request!=null && userPrefs!=null && userPrefs.getErrorMessages().length()!=0) { %>
<div class="alert alert-error">  
  <a class="close" data-dismiss="alert">×</a>  
  <strong>Error: </strong> <%= userPrefs.getErrorMessages() %> 
  <% userPrefs.setErrorMessages("");
  	 userPrefs.save(); %> 
</div> 
<% } else if(request!=null && userPrefs != null && userPrefs.getSuccessMessages().length()!=0) { %>
<div class="alert alert-success">  
  <a class="close" data-dismiss="alert">×</a>  
  <strong>Success: </strong> <%= userPrefs.getSuccessMessages() %>  
  <% userPrefs.setSuccessMessages("");
  	 userPrefs.save(); %>
</div> 
<% } %>