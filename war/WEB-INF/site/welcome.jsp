

<!DOCTYPE html>
<html>
<head>
<title>Welcome</title>
<!-- Sign in template from Bootstrap site modified for ECE1779 AWS project -->
<!-- Bootstrap -->
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="bootstrap/css/bootstrap-responsive.css" rel="stylesheet">

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
</head>
<body>

<%@ include file="header.jsp" %>

<div class="container">
  
  <% 
  UserService userService = UserServiceFactory.getUserService();
  User user = userService.getCurrentUser();
  
  if(user==null)
  {
  	response.sendRedirect(userService.createLoginURL(""));
  }
  
  %>
  
  Welcome to GTTT (Google Tic-Tac-Toe). Please use the navigation menu to browse the rest of the site.
  
</div> <!-- /container -->

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
</body>
</html>