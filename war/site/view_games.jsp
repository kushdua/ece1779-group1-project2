<!DOCTYPE html>
<%@page import="ece1779.appengine.*"%>
<%@page import="com.google.appengine.api.datastore.PreparedQuery"%>
<%@page import="com.google.appengine.api.datastore.Entity"%>

<html>
<head>
<title>View Games</title>
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

<!-- if parameter not specified to page, all uploaded images displayed; else only transformations for specified image are displayed by server side script which writes out page -->
<div class="container">
		<div class="accordion-group">
			<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseManualSet">
					Available Games
				</a>
			</div>
			 <div id="collapseManualSet" class="accordion-body collapse">
				<div class="accordion-inner">
					<table class="table table-striped">
				    	<tr>
				    		<th>Game ID</th>
				    		<th>Opponent</th>
				    		<th></th>
				    		<th></th>
				    	</tr>
<%
PreparedQuery pq = Helper.getInvitedGames();
		String key;
		String gameId;
		String email;
		for (Entity result : pq.asIterable()) {	
			key = result.getKey().toString();
			gameId = key.substring(8, key.length()-1);
			
%>	
				    	<tr>
	                      <td><%= gameId %></td>
	        		      <td><%= ((User)result.getProperty("user1")) %></td>
	        		      <td><button class="btn btn-small btn-primary" name="newGameBtn" type="button" >Accept</button></td>
	        		      <td><button class="btn btn-small btn-primary" name="newGameBtn" type="button" >Reject</button></td>
                   		</tr>
                   		<%
                   		}
                   		%>
				    </table>
				</div>
			</div>
		</div>
		<div class="accordion-group">
			<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseAutoScale">
					Past Games
				</a>
			</div>
			 <div id="collapseAutoScale" class="accordion-body collapse">
				<table class="table table-striped">
				    	<tr>
				    		<th>Game ID</th>
				    		<th>Opponent</th>
				    		<th>Winner</th>
				    		<th>btnRematch</th>
				    	</tr>
				    	<tr>
	                      <td>Game 1</td>
	        		      <td>1</td>
	        		      <td></td>
	        		      <td></td>
                   		</tr>
                   		<tr>
	                      <td>Game 2</td>
	        		      <td>2</td>
	        		      <td>20</td>
	        		      <td></td>
                   		</tr>
				  </table>
			</div>
		</div>
		<form class="form-welcome" >
			<button class="btn btn-small btn-primary" name="newGameBtn" type="submit">Start New Game</button>
		</form>
	</div>
</div> <!-- /container -->

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
</body>
</html>

