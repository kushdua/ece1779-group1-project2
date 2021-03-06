<!DOCTYPE html>
<%@page import="ece1779.appengine.*"%>
<%@page import="com.google.appengine.api.datastore.PreparedQuery"%>
<%@page import="com.google.appengine.api.datastore.Entity"%>
<%@page import="java.util.ArrayList"%>

<html>
<head>
<title>View Games</title>
<!-- Sign in template from Bootstrap site modified for ECE1779 AWS project -->
<!-- Bootstrap -->
</head>
<body onload="$('#replayContainer').hide();">

<%@ include file="header.jsp" %>

<script type="text/javascript">
	function startNewGame()
	{
		window.location="/site/selectplayer.jsp";
	}
    
    var currentMove = 0;
    var currentGameMoves = new Array();
    var imageO = "assets/o.png";
    var imageX = "assets/x.png";
    var imageEmpty = "assets/empty.png";
	
	function setupBoard(gameBoard) {
		//console.log("updating board with contents " + gameBoard);
		if (gameBoard != null && gameBoard.length > 0) {
			for ( var i = 0; i < gameBoard.length; i++) {
				if (gameBoard[i] == "o") {
					$("#row" + Math.floor(i / 3) + "Col" + i % 3).attr('src',
							imageO);
				} else if (gameBoard[i] == "x") {
					$("#row" + Math.floor(i / 3) + "Col" + i % 3).attr('src',
							imageX);
				} else if (gameBoard[i] == " ") {
					$("#row" + Math.floor(i / 3) + "Col" + i % 3).attr('src',
							imageEmpty);
				}
			}
		}
	}

	function goToPrevMove()
	{
		if(currentMove-1>=0)
		{
			currentMove -= 1;
			setupBoard(currentGameMoves[currentMove].split(","));
            if(currentMove<currentGameMoves.length-1)
            {
                $('#btnReplayNext').show();
            }
            
            if(currentMove==0)
            {
                $('#btnReplayPrev').hide();
            }
		}
	}
	
	function goToNextMove()
	{
		if(currentMove+1 < currentGameMoves.length)
		{
			currentMove += 1;
			setupBoard(currentGameMoves[currentMove].split(","));
			if(currentMove==currentGameMoves.length-1)
			{
			    $('#btnReplayNext').hide();
			}
            
            if(currentMove>0)
            {
                $('#btnReplayPrev').show();
            }
		}
	}

	function replayGame(inMoves) {
		$("#replayContainer").toggle();
        $('#btnReplayPrev').hide();
        currentMove=0;
	    currentGameMoves = inMoves.split(";");
	    setupBoard(currentGameMoves[0].split(","));
	}
</script>

<!-- if parameter not specified to page, all uploaded images displayed; else only transformations for specified image are displayed by server side script which writes out page -->
<div class="container">
		<div class="accordion-group">
			<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseAutoScale2">
					Games in Progress
				</a>
			</div>
			 <div id="collapseAutoScale2" class="accordion-body collapse">
				<table class="table table-striped">
				    	<tr>
				    		<th>Game ID</th>
				    		<th>Opponent</th>
				    		<th></th>
				    	</tr>
<%
ArrayList<Entity> gamelist2 = Helper.getGamesInProgress();
int totalGames2 = gamelist2.size();
Entity en2;
String key2;
String gameId2;
String email2;

UserService usrService2 = UserServiceFactory.getUserService();
User currentUser2 = usrService2.getCurrentUser();
User _user1;
User _user2;
User opponent2;

for(int i=0;i<totalGames2;i++){
	en2 = gamelist2.get(i);
	
	key2 = en2.getKey().toString();
	gameId2 = key2.substring(8, key2.length()-1);
	_user1 = ((User)en2.getProperty("user1"));
	_user2 = ((User)en2.getProperty("user2"));
	
	if (currentUser2.equals(_user1)){
		opponent2 = _user2;
	}else{
		opponent2 = _user1;
	}
	
			
%>	
				    	<tr>
	                      <td><%= gameId2 %></td>
	        		      <td><%= opponent2 %></td>
	        		      <td>
	        		      	<form action="/playgame" method="post">
	        		      		<input type="hidden" name="gameId" value=<%= gameId2 %>  >
	        		      		<input type="hidden" name="opponent" value=<%= opponent2 %>  >
	        		      		<input type="hidden" name="opponentAuthDomain" value=<%= opponent2.getAuthDomain() %>  >
	        		      		<input type="hidden" name="userAction" value="returnToGame" >
	        		      		<button class="btn btn-small btn-primary" name="returnToGameBtn" type="submit" >Return to Game</button>
	        		      	</form>
	        		      </td>
                   		</tr>
                   		
                   		<%
                   		}
                   		%>
				  </table>
			</div>
		</div>
		<div class="accordion-group">
			<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseManualSet">
					Games you are invited to
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
			User opponent = ((User)result.getProperty("user1"));
%>	
						
				    	<tr>
				    	  <td><%= gameId %></td>
	        		      <td><%= opponent %></td>
	        		      <td>
	        		      	<form action="/playgame" method="post">
	        		      		<input type="hidden" name="gameId" value=<%= gameId %>  >
	        		      		<input type="hidden" name="opponent" value=<%= opponent %>  >
	        		      		<input type="hidden" name="userAction" value="accept" >
	        		      		<button class="btn btn-small btn-primary" name="acceptBtn" type="submit" >Accept</button>
	        		      	</form>
	        		      </td>
	        		      <td>
	        		      	<form action="/playgame" method="post">
	        		      		<input type="hidden" name="gameId" value=<%= gameId %>  >
	        		      		<input type="hidden" name="opponent" value=<%= opponent %>  >
	        		      		<input type="hidden" name="userAction" value="reject" >
	        		      		<button class="btn btn-small btn-primary" name="rejectBtn" type="submit" >Reject</button>
	        		      	</form>
	        		      </td>
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
				    		<th></th>
				    	</tr>
<%
ArrayList<Entity> gamelist = Helper.getPreviousGames();
int totalGames = gamelist.size();
Entity en;
String key1;
String gameId1;
String email1;

UserService usrService = UserServiceFactory.getUserService();
User currentUser = usrService.getCurrentUser();
User user1;
User user2;
User opponent1;
long winner;
String winnerId = "";

for(int i=0;i<totalGames;i++){
	en = gamelist.get(i);
	
	key1 = en.getKey().toString();
	gameId1 = key1.substring(8, key1.length()-1);
	user1 = ((User)en.getProperty("user1"));
	user2 = ((User)en.getProperty("user2"));
	winner = (Long)en.getProperty("winner");
	ArrayList<String> movesArr = (ArrayList<String>)en.getProperty("boardHistory");
	String moves = "";
	for(String move : movesArr)
	{
		moves+=move+";";
	}
	
	if(moves.length()>0)
	{
		//Eliminate last ;
		moves=moves.substring(0, moves.length()-1);
	}
	
	if (currentUser.equals(user1)){
		opponent1 = user2;
	}else{
		opponent1 = user1;
	}
	
	if (winner == 1){
		winnerId = user1.getEmail();
	}else if (winner == 2){
		winnerId = user2.getEmail();
	}else if (winner == 0){
		winnerId = "(Draw)";
	}
			
%>	
				    	<tr>
	                      <td><%= gameId1 %></td>
	        		      <td><%= opponent1 %></td>
	        		      <td><%= winnerId %></td>
	        		      <td>
	        		      	<form action="/playgame" method="post" style="float:left">
	        		      		<input type="hidden" name="gameId" value="0"  >
	        		      		<input type="hidden" name="opponent" value=<%= opponent1 %>  >
	        		      		<input type="hidden" name="opponentAuthDomain" value=<%= opponent1.getAuthDomain() %>  >
	        		      		<input type="hidden" name="userAction" value="rematch" >
	        		      		<button class="btn btn-small btn-primary" name="rematchBtn" type="submit" >Rematch</button>
	        		      	</form>
	        		      	<div style="float:left; width:5px; height:5px; display:inline-block;"></div>
                            <button class="btn btn-small btn-primary" name="replayBtn" style="float:left" onClick="replayGame('<%= moves %>')">Replay</button>
	        		      </td>
                   		</tr>
                   		
                   		<%
                   		}
                   		%>
				  </table>
			</div>
		</div>
		
			<button class="btn btn-small btn-primary" name="newGameBtn"  onclick="startNewGame()">Start New Game</button>
						
	   <br />
	   <div id="replayContainer">
            <h2>Replay:</h2>
			<ul id="squaresContainer" class="thumbnails"
				style="width: 410px; float: left;">
				<li><a href="#"><img id="row0Col0"
						style="height: 100px; width: 100px; float: left; display: inline;" /></a>
				</li>
				<li><a href="#"><img id="row0Col1"
						style="height: 100px; width: 100px; float: left; display: inline;" /></a>
				</li>
				<li><a href="#"><img id="row0Col2"
						style="height: 100px; width: 100px; float: left; display: inline;" /></a>
				</li>
				<br />
				<li><a href="#"><img id="row1Col0"
						style="height: 100px; width: 100px; float: left; display: inline;" /></a>
				</li>
				<li><a href="#"><img id="row1Col1"
						style="height: 100px; width: 100px; float: left; display: inline;" /></a>
				</li>
				<li><a href="#"><img id="row1Col2"
						style="height: 100px; width: 100px; float: left; display: inline;" /></a>
				</li>
				<br />
				<li><a href="#"><img id="row2Col0"
						style="height: 100px; width: 100px; float: left; display: inline;" /></a>
				</li>
				<li><a href="#"><img id="row2Col1"
						style="height: 100px; width: 100px; float: left; display: inline;" /></a>
				</li>
				<li><a href="#"><img id="row2Col2"
						style="height: 100px; width: 100px; float: left; display: inline;" /></a>
				</li>
			</ul>

			<i id="btnReplayPrev" class="icon-circle-arrow-left icon-4x" style="border: 1px solid black;" onClick="goToPrevMove();"></i>
            <div style="float:left; width:5px; height:5px; display:inline-block;"></div>
			<i id="btnReplayNext" class="icon-circle-arrow-right icon-4x" style="border: 1px solid black;" onClick="goToNextMove()"></i>
            <div style="float:left; width:5px; height:5px; display:inline-block;"></div>
            <i id="btnReplayClose" class="icon-remove-sign icon-4x" style="border: 1px solid black;" onClick="$('#replayContainer').toggle();"></i>
            
		</div>
</div> <!-- /container -->
</body>
</html>

