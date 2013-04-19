<!DOCTYPE html>
<%@page import="java.util.ArrayList"%>
<%@page import="ece1779.appengine.*"%>
<%@page import="com.google.appengine.api.datastore.PreparedQuery"%>
<%@page import="com.google.appengine.api.datastore.Entity"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
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
        HashMap<String,Integer> wonList = new HashMap<String,Integer>();
        wonList.put("Nobody", 0);
        HashMap<String,Integer> lostList = new HashMap<String,Integer>();
        lostList.put("Nobody", 0);
        HashMap<String,Integer> drawnList = new HashMap<String,Integer>();
        drawnList.put("Nobody", 0);
        String opponentName = "";
        int winner=-1;
        int myUserPos = -1;
        String keyWonMost="", keyLostMost="", keyDrawnMost="";
        int wonMost=0, lostMost=0, drawnMost=0;
        User user1=null, user2=null;
        String email="";
        int val=0;
        
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
	        
	        ArrayList<Entity> finishedGames = Helper.getPreviousGames(1000);
            
            for(Entity e : finishedGames)
            {
            	//TODO compile into won/lost list and display
                myUserPos=-1;
            	winner=-1;
            	try
            	{
            		   winner=((Integer)e.getProperty("winner")).intValue();
            	}
            	catch(NumberFormatException nfe)
            	{
            		winner=-1;
            	}
            	
            	user=(User)e.getProperty("user1");
            	user2=(User)e.getProperty("user2");
            	if(user1.compareTo(user)==0)
            	{
            		myUserPos=1;
            	}
            	else if(user2.compareTo(user)==0)
                {
            		myUserPos=2;
                }
            	
            	if(myUserPos!=-1)
            	{
            		if(winner==myUserPos)
            		{
            			//Win for current user
            			if(winner==1)
            			{
            				email=user2.getEmail();
            			}
            			else if(winner==2)
            			{
                            email=user1.getEmail();
            			}
                        val=(wonList.containsKey(email) ? wonList.get(email) : 0);
                        wonList.put(email, ++val);
            		}
            		else if(winner==0)
            		{
            			//Draw
                        if(myUserPos==1)
                        {
                            email=user2.getEmail();
                        }
                        else if(myUserPos==2)
                        {
                            email=user1.getEmail();
                        }
                        val=(drawnList.containsKey(email) ? drawnList.get(email) : 0);
                        drawnList.put(email, ++val);
            		}
            		else
            		{
            			//Loss for current user
                        if(winner==1)
                        {
                            email=user1.getEmail();
                        }
                        else if(winner==2)
                        {
                            email=user2.getEmail();
                        }
                        val=(lostList.containsKey(email) ? lostList.get(email) : 0);
                        lostList.put(email, ++val);
            		}
            	}
            }
            
            //Determine who user won the most games against
            for(java.util.Map.Entry<String,Integer> entry : wonList.entrySet())
            {
            	if(entry.getValue().intValue() > wonMost)
            	{
            		keyWonMost = entry.getKey();
            	}
            }
            
            //Determine who user lost the most games against
            for(java.util.Map.Entry<String,Integer> entry : lostList.entrySet())
            {
                if(entry.getValue().intValue() > lostMost)
                {
                    keyLostMost = entry.getKey();
                }
            }
            
            //Determine who user has drawn the most games against
            for(java.util.Map.Entry<String,Integer> entry : drawnList.entrySet())
            {
                if(entry.getValue().intValue() > drawnMost)
                {
                    keyDrawnMost = entry.getKey();
                }
            }
        }
        else
        {
            response.sendRedirect("/site/welcome.jsp");
        }
		

%>
<div class="accordion-group">
	<div class="accordion-heading">
		<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapsePercStats">
			User Statistics
		</a>
	</div>
	<div id="collapsePercStats" class="accordion-body collapse in">
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
	   <div class="accordion-heading">
        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseMostList">
            Head-to-head User Statistics
        </a>
    </div>
    <div id="collapseMostList" class="accordion-body collapse in">
        <div class="accordion-inner">
            <table class="table table-striped">
                <tr>
                    <th>Action category</th>
                    <th>User Email</th>
                    <th>Number of Games</th>
                </tr>
                <tr>
                    <td>Won the most against</td>
                    <td><%= keyWonMost %></td>
                    <td><%= wonMost %><%= (wonMost>1)?"games":"game" %></td>
                </tr>
                <tr>
                    <td>Lost the most against</td>
                    <td><%= keyLostMost %></td>
                    <td><%= lostMost %><%= (lostMost>1)?"games":"game" %></td>
                </tr>
                <tr>
                    <td>Drawn the most games with</td>
                    <td><%= keyDrawnMost %></td>
                    <td><%= drawnMost %><%= (drawnMost>1)?"games":"game" %></td>
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