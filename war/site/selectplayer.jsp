<!DOCTYPE html>
<%@page import="ece1779.appengine.*"%>
<%@page import="com.google.appengine.api.datastore.PreparedQuery"%>
<%@page import="com.google.appengine.api.datastore.Entity"%>
<%@page import="java.util.ArrayList"%>
<html>
<head>
<title>Select New Player</title>
<!-- Sign in template from Bootstrap site modified for ECE1779 AWS project -->
<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->

    
</head>

<%@ include file="header.jsp" %>
 
    <link href="select2/select2.css" rel="stylesheet"/>
    <script id="script_e1" src="select2/select2.min.js"></script>
   
    <script>
        $(document).ready(function() { $("#select1").select2(); });
    </script>
    
    <script type="text/javascript">
    function sendgameinvite()
    {
        //TODO: Take the selected user i.e the name and id of that player
        // and start a new game -- make a call to servelet and then pass the arguments
        //user1 = you (header.jsp has JSP variable for user). user2=other guy
        var e = document.getElementById("select1");
        var strSel = "The Value is: " + e.options[e.selectedIndex].value + " and text is: " + e.options[e.selectedIndex].text;
        var gameID = -1;
        //console.log("About to send user2: "+e.options[e.selectedIndex].text+" to /StartNewGame");
        $.post("/StartNewGame", { user2: e.options[e.selectedIndex].value } ).done(function(data){
        	gameID=data;
        	if(gameID!=-1)
            {
                window.location="/site/play.jsp?gameID="+gameID;
            }
        });
    }
</script>
<body>



<div class="container">
  
  Please select a new player from the list shown below:
  
  <br />

<%
ArrayList<Entity> names=Helper.getUsers();//new ArrayList<String>();
int totalUsers = names.size();
long rating = -1;
Entity en;
User user1;

%>
<select id="select1">
<%
for(int i=0;i<totalUsers;i++) 
{ 
    
    en=names.get(i);
    user1=((User)en.getProperty("user"));
    rating=((Long)en.getProperty("rating")).longValue();
    //TODO make value userID (servlet giving you data needs to give you <userID,userName>)
    %><option value="<%=user1.getEmail()%>"><%=user1.getEmail()%> - rating <%=rating%></option><%
        
}
%>
</select>

<button class="btn btn-small btn-primary" name="newInvitebtn"  onclick="sendgameinvite()">Send Game Invitation</button>
</div> <!-- /container -->
</body>
</html>