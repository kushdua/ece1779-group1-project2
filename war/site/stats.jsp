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
        PreparedQuery pq = null;
        Entity result = null;
        long won=0, lost=0, drawn=0, total=0;
        float percWon=0.0f, percLost=0.0f, percDrawn=0.0f;
        
        if(user!=null)
        {
			pq = Stats.queryUsers();
			result = pq.asSingleEntity();
			won = (Long)result.getProperty("GamesWon")  ;
			lost = (Long)result.getProperty("GamesLost");
			drawn = (Long) result.getProperty("GamesDrawn");
			total = won+lost+drawn;
			percWon = ((total == 0)?0.0f:((float)won*100/total));
	        percLost = ((total == 0)?0.0f:((float)lost*100/total));
	        percDrawn = ((total == 0)?0.0f:((float)drawn*100/total));
        }
        else
        {
            response.sendRedirect("/site/welcome.jsp");
        }
		

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
				            <th> </th>
				            <th>Won</th>
				            <th>Lost</th>
				            <th>Drawn</th>
				        </tr>
				    	<tr>
				    	    <td>Games</td>
				    		<td><%= won %></td>
                            <td><%= lost %></td>
                            <td><%= drawn %></td>
				    	</tr>
				    	<tr>
				    	   <td>Percentage</td>
				    	   <td><%= percWon %>%</td>
				    	   <td><%= percLost %>%</td>
				    	   <td><%= percDrawn %>%</td>
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