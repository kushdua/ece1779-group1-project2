<!DOCTYPE html>
<%@page import="ece1779.appengine.*"%>
<%@page import="com.google.appengine.api.datastore.PreparedQuery"%>
<%@page import="com.google.appengine.api.datastore.Entity"%>
<%@page import="java.util.ArrayList"%>
<html>
<head>
<title>Select New Player</title>
<!-- Sign in template from Bootstrap site modified for ECE1779 AWS project -->
<!-- Bootstrap -->
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="bootstrap/css/bootstrap-responsive.css" rel="stylesheet">

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->

    <link href="select2/select2.css" rel="stylesheet"/>
    <script src="select2/select2.js"></script>
    <script>
        $(document).ready(function() { $("#select1").select2(); });
    </script>
    
</head>
<body>

<%@ include file="header.jsp" %>


<div class="container">
  
  Please select a new player from the list shown below:
  
  <br />
</div> <!-- /container -->


<script type="text/javascript">
	function sendgameinvite()
	{
		//TODO: Take the selected user i.e the name and id of that player
		// and start a new game -- make a call to servelet and then pass the arguments
		//user1 = you (header.jsp has JSP variable for user). user2=other guy
		var e = document.getElementById("select1");
	    var strSel = "The Value is: " + e.options[e.selectedIndex].value + " and text is: " + e.options[e.selectedIndex].text;
		
		$.post("/StartNewGame", { user2: e.options[e.selectedIndex].text } );
		<%
		//Grab contents of specified game.
		String gameId = request.getParameter("gameID");
		
		if(gameId == null || gameId.length()==0)
		{%>
			window.location="/view_games.jsp";
		<%}
		%>
		var site = "/site/play.jsp?gameID=";
	    var gameID = "<%= gameId %>";

		window.location=site+gameID;

	}
</script>

<%
ArrayList<Entity> names=Helper.getUsers();//new ArrayList<String>();
int totalUsers = names.size();
Entity en;
User user1;

%>
<select id ="select1" size="1" name="names" >
<%
for(int i=0;i<totalUsers;i++) 
{ 
	
	en=names.get(i);
	user1=((User)en.getProperty("user"));
	//TODO make value userID (servlet giving you data needs to give you <userID,userName>)
	%><option value<%=user1.getEmail()%>><%=user1.getEmail()%></option><%
		
}
%>
</select>


<button class="btn btn-small btn-primary" name="newInvitebtn"  onclick="sendgameinvite()">Send Game Invitation</button>

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
</body>
</html>