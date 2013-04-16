<%@page import="com.google.appengine.api.users.User" %>
<%@page import="com.google.appengine.api.users.UserService" %>
<%@page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@page import="ece1779.appengine.*" %>

<!-- Bootstrap -->
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->

<%
   UserService userService = UserServiceFactory.getUserService();
/* if(userService == null) response.getWriter().println("user service is null");
 */   /* if(Helper.currentLoggedInUser==null){
	    Helper.currentLoggedInUser = userService.getCurrentUser();	 
	  } */
      User user = userService.getCurrentUser(); //Helper.currentLoggedInUser;
/*    if(user == null) response.getWriter().println("user is null");
 */   UserPrefs userPrefs = null;
   String onLoadLoginHandler = "";
   
   if(user!=null)
   {
   		userPrefs = UserPrefs.getPrefsForUser(user,true);
   		onLoadLoginHandler = "$.post('/AuthenticateUpdate', {userID: '" + user.getEmail() +"', action: 'login'});";
   }
%>

<script type="text/javascript"><%= onLoadLoginHandler %></script>

<div class="navbar navbar-inverse" onload="<%= onLoadLoginHandler %>">
    <div class="navbar-inner">
        <a class="brand" href="#">ECE1779 AppEngine Project</a>
        <ul class="nav">
            <%= (request==null || (request!=null && request.getRequestURI()==null) ||
                (request!=null && request.getRequestURI().contains("welcome.jsp")))?
            		"<li><a href='welcome.jsp' class='active'>Home</a></li>" : 
            		"<li><a href='welcome.jsp'>Home</a></li>" %>
        <% if(request!=null && request.getRequestURI()!=null) { %>
            <%= (request.getRequestURI().contains("stats.jsp"))?
                    "<li><a href='stats.jsp' class='active'>Statistics</a></li>" : 
                    "<li><a href='stats.jsp'>Statistics</a></li>" %>
<%--             <%= (request.getRequestURI().contains("play.jsp"))?
                    "<li><a href='play.jsp' class='active'>Play Game</a></li>" : 
                    "<li><a href='play.jsp'>Play Game</a></li>" %> --%>
            <%= (request.getRequestURI().contains("view_games.jsp"))?
                    "<li><a href='view_games.jsp' class='active'>View Games</a></li>" : 
                    "<li><a href='view_games.jsp'>View Games</a></li>" %>
            <% if(user!=null && userService!=null)
            { %>
            	<li><a href="<%= userService.createLogoutURL("/") %>" onclick="$.post('/AuthenticateUpdate',{userID: '<%= user.getEmail() %>', action: 'logout'});">Logout</a></li>
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