<!DOCTYPE html>
<html>
<head>
<title>Play TicTacToe</title>
<!-- Sign in template from Bootstrap site modified for ECE1779 AWS project -->
</head>
<body onload="setupBoard()">

<%@ include file="header.jsp" %>

<!-- if parameter not specified to page, all uploaded images displayed; else only transformations for specified image are displayed by server side script which writes out page -->
<div class="container">
	<div class="alert alert-success hidden" id="successContainer">
		<a class="close" data-dismiss="alert">×</a>
		<p id="successMessage" />
	</div>
	<div class="alert alert-error hidden" id="errorContainer">
		<a class="close" data-dismiss="alert">×</a>
		<p class= id="error" />
	</div>
    <ul class="thumbnails">
		<li>
			<a href="#"><img class="span3" id="row0Col0" onClick="cellClickedHandler(0,0)" style="height:100px; width:100px; border:1px solid #021a40;" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row0Col1" onClick="cellClickedHandler(0,1)" style="height:100px; width:100px border:1px solid #021a40;" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row0Col2" onClick="cellClickedHandler(0,2)" style="height:100px; width:100px border:1px solid #021a40;" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row1Col0" onClick="cellClickedHandler(1,0)" style="height:100px; width:100px border:1px solid #021a40;" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row1Col1" onClick="cellClickedHandler(1,1)" style="height:100px; width:100px border:1px solid #021a40;" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row1Col2" onClick="cellClickedHandler(1,2)" style="height:100px; width:100px border:1px solid #021a40;" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row2Col0" onClick="cellClickedHandler(2,0)" style="height:100px; width:100px border:1px solid #021a40;" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row2Col1" onClick="cellClickedHandler(2,1)" style="height:100px; width:100px border:1px solid #021a40;" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row2Col2" onClick="cellClickedHandler(2,2)" style="height:100px; width:100px border:1px solid #021a40;" /></a>
		</li>
    </ul>
</div> <!-- /container -->

<script type="text/javascript">
	<%
		//Grab contents of specified game.
		String gameId = request.getParameter("gameID");
		
		if(gameId == null || gameId.length()==0)
		{
			response.sendRedirect("/view_games.jsp");
		}
		
		//TODO AJAX request for game contents (separated by ,) and
		//turn (concatenated to game contents by ;)
		String gameBoardContents = "o, , , ,x,o, ,o,x;1;x";
	%>
    var gameID = "<%= gameId %>";
    var myTurn = false;
	var gameBoardContents = "";
	getGameBoardContents();
	var split1 = gameBoardContents.split(";");
	var gameBoard = split1[0].split(",");
	myTurn = (split1[1]=="1") ? true : false;
	var myPiece = split1[2];
	var gameDone = false;
	var winner = -1;
	
	var imageO = "assets/o.png";
	var imageX = "assets/x.png";
	var imageEmpty = "assets/empty.png";
	
	function setupBoard()
	{
		for(var i=0; i<gameBoard.length; i++)
		{
			if(gameBoard[i]=="o")
			{
				$("#row"+Math.floor(i/3)+"Col"+i%3).attr('src',imageO);
			}
			else if(gameBoard[i]=="x")
			{
				$("#row"+Math.floor(i/3)+"Col"+i%3).attr('src',imageX);
			}
			else if(gameBoard[i]==" ")
			{
				$("#row"+Math.floor(i/3)+"Col"+i%3).attr('src',imageEmpty);
			}
		}
 		setTimeout(timerUpdateGameboard, 5000);
	}
	
	function timerUpdateGameboard()
	{
		if(!gameDone)
        {
            getGameBoardContents();
            split1 = gameBoardContents.split(";");
            gameBoard = split1[0].split(",");
            myTurn = (split1[1]=="1") ? true : false;
            myPiece = split1[2];
            setupBoard();
            checkGameEnd();
        }
	}
	
	function sendGameBoardContents()
	{
		/* $.ajax({
		    type: "GET",
		    url: "GameContents?gameID="+gameID+"&gameBoardContents="+getGameBoardString()+"&gameDone="+gameDone,
		    async: false,
        }); */
		$.post("/GameContents", { gameID: gameID, gameBoardContents: getGameBoardString(), gameDone: gameDone } );
	}
    
    function getGameBoardContents()
    {
        /* return $.ajax({
            type: "GET",
            url: "GameContents?gameID="+gameID,
            async: false,
        }).responseText; */
        if(myTurn==false)
        {
	        var returnedData = "";
	        $.post("/GameContents", { gameID: gameID } ).done(function(data){gameBoardContents = data;});
        }
    }
	
	function getGameBoardString()
	{
	    var answer="";
	    for(var i=0; i<gameBoard.length; i++)
    	{
	    	answer+=gameBoard[i];
	        if(i<gameBoard.length-1)
	        {
	        	answer+=",";
	        }
    	}
	    answer+=(gameDone?";1":(myTurn?";1":";2"));
	    answer+=myPiece;
	    return answer;
	}
	
	function cellClickedHandler(row, col)
	{
		if(myTurn===true && gameBoard[row*3+col]===" ")
		{
			//Can add game piece
			if(myPiece ==="o")
			{
				gameBoard[row*3+col]="o";
				//send to JSP on server
				sendGameBoardContents();
				myTurn=false;
				$("#row"+row+"Col"+col).attr('src',imageO);
			}
			else if(myPiece == "x")
			{
				gameBoard[row*3+col]="x";
				//send to JSP on server
                sendGameBoardContents();
				myTurn=false;
				$("#row"+row+"Col"+col).attr('src',imageX);
			}
			$("#successContainer").toggleClass("hidden");
			$("#successMessage").text($("#successMessage").text()+" Successfully played move.\n");
			checkGameEnd();
		}
		else
		{
			$("#errorContainer").toggleClass("hidden");
			if(myTurn===false)
			{
				$("#error").text($("#error").text()+" It is not your turn yet!\n");
			}
			else if(gameBoard[row][col]!=="")
			{
				$("#error").text($("#error").text()+" Cell is not empty.\n");			
			}
		}
	}
	
	function checkGameEnd()
	{
		var minBound = 0;
		var maxBound = gameBoard.length;
		var currPiece="";
		var row=-1;
		var col=-1;
		for(var i=minBound; i<maxBound; i++)
		{
			row=Math.floor(i/3);
			col=i%3;
			currPiece=gameBoard[row*3+col];
			
			//Check that row
			if(currPiece!=" " && gameBoard[row*3]==currPiece && gameBoard[row*3+1]==currPiece && gameBoard[row*3+2]==currPiece)
			{
				$("#successMessage").text($("#successMessage").text()+"Player " + currPiece + " wins.\n");
				gameDone=true;
				sendGameBoardContents();
				setTimeout(new function(){ document.location="view_games.jsp" }, 5000);
			}
			//Check that column
			else if(currPiece!=" " && gameBoard[col]==currPiece && gameBoard[col+3]==currPiece && gameBoard[col+6]==currPiece)
			{
				$("#successMessage").text($("#successMessage").text()+"Player " + currPiece + " wins.\n");
                gameDone=true;
                sendGameBoardContents();
				setTimeout(new function(){ document.location="view_games.jsp" }, 5000);
			}
			//Check diagonal up+right /
			else if(currPiece!=" " && gameBoard[6]==currPiece && gameBoard[3+1]==currPiece && gameBoard[0+2]==currPiece)
			{
				$("#successMessage").text($("#successMessage").text()+"Player " + currPiece + " wins.\n");
                gameDone=true;
                sendGameBoardContents();
				setTimeout(new function(){ document.location="view_games.jsp" }, 5000);
			}
			//Check diagonal up+left  \
			else if(currPiece!=" " && gameBoard[6+2]==currPiece && gameBoard[3+1]==currPiece && gameBoard[0]==currPiece)
			{
				$("#successMessage").text($("#successMessage").text()+"Player " + currPiece + " wins.\n");
                gameDone=true;
                sendGameBoardContents();
				setTimeout(new function(){ document.location="view_games.jsp" }, 5000);
			}
		}
	}
</script>

</body>
</html>

