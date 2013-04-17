<!DOCTYPE html>
<%@page import="ece1779.appengine.*"%>
<%@page import="com.google.appengine.api.datastore.PreparedQuery"%>
<%@page import="com.google.appengine.api.datastore.Entity"%>
<%@page import="java.util.List"%>
<html>
<head>
<title>User Statistics</title>
<!-- Sign in template from Bootstrap site modified for ECE1779 AWS project -->
<!-- Bootstrap -->
</head>
<body>

<%@ include file="header.jsp" %>

<div class="container">


<%   
		PreparedQuery pq = Stats.query();
		Entity result = pq.asSingleEntity();
		long won = (Long)result.getProperty("GamesWon")  ;
		long lost = (Long)result.getProperty("GamesLost");
		long drawn = (Long) result.getProperty("GamesDrawn");
		long totalGames = won + lost + drawn;
		

%>
<div class="accordion-group">
			<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseWorkers">
					User Statistics
				</a>
			</div>
			 <div id="collapseWorkers" class="accordion-body collapse in">
				<div class="accordion-inner">
				    <table class="table table-striped">
				    	<tr>
				    		<th>Games Won :<%= won %></th>
				    	</tr>
				    	<tr>
				    		<th>Games Lost :<%= lost %></th>
				    	</tr>
				    	<tr>
				    		<th>Games Drawn :<%= drawn %></th>
				    	</tr>
				    	<tr>
				    		<th>Total Games Played : <%= totalGames %></th>
				    	</tr>
				    </table>
				</div>
			</div>
		</div>



</div> <!-- /container -->

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
</body>
</html>