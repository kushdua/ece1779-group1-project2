<!DOCTYPE html>
<html>
<head>
<title>Play TicTacToe</title>
<!-- Sign in template from Bootstrap site modified for ECE1779 AWS project -->
<!-- Bootstrap -->
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="bootstrap/css/bootstrap-responsive.css" rel="stylesheet">

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
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
		<p class= id="error"/>
	</div>
    <ul class="thumbnails" height="300px" width="300px">
		<li>
			<a href="#"><img class="span3" id="row0Col0" onClick="cellClickedHandler(0,0)" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row0Col1" onClick="cellClickedHandler(0,1)" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row0Col2" onClick="cellClickedHandler(0,2)" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row1Col0" onClick="cellClickedHandler(1,0)" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row1Col1" onClick="cellClickedHandler(1,1)" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row1Col2" onClick="cellClickedHandler(1,2)" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row2Col0" onClick="cellClickedHandler(2,0)" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row2Col1" onClick="cellClickedHandler(2,1)" /></a>
		</li>
		<li>
			<a href="#"><img class="span3" id="row2Col2" onClick="cellClickedHandler(2,2)" /></a>
		</li>
    </ul>
</div> <!-- /container -->

<script type="text/javascript">
	<%
		//Grab contents of specified game.
		int gameId = -1;
		try
		{
			gameId = Integer.parseInt(request.getParameter("gameID"));
		}
		catch(NumberFormatException e)
		{
		}
		//TODO AJAX request for game contents (separated by ,) and
		//turn (concatenated to game contents by ;)
		String gameBoardContents = "o, , , ,x,o, ,o,x;1;x";
	%>
	var gameBoardContents = "<%= gameBoardContents %>";
	var split1 = gameBoardContents.split(";");
	var gameBoard = split1[0].split(",");
	var myTurn = true;//(split1[1]==="1") ? true : false;
	var myPiece = split1[2];
	
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
	}
	
	function getGameBoardContents()
	{
		return $.ajax({
		    type: "GET",
		    url: "GameContents?,
		    async: false,
        }).responseText;
	}
	
	
	function cellClickedHandler(row, col)
	{
		if(myTurn===true && gameBoard[row*3+col]===" ")
		{
			//Can add game piece
			if(myPiece ==="o")
			{
				gameBoard[row*3+col]="o";
				//TODO send to JSP on server
				$.ajax({
					url: "http://fiddle.jshell.net/favicon.png",
					beforeSend: function ( xhr ) {
						   xhr.overrideMimeType("text/plain; charset=x-user-defined");
					}
				}).done(function ( data ) {
					
				});
				$("#row"+row+"Col"+col).attr('src',imageO);
			}
			else if(myPiece == "x")
			{
				gameBoard[row*3+col]="x";
				//TODO send to JSP on server
				$("#row"+row+"Col"+col).attr('src',imageX);
			}
			$("#successMessage").text($("#successMessage").text()+"Successfully played move.\n");
			checkGameEnd();
		}
		else
		{
			$("#error").toggleClass("hidden");
			if(myTurn===false)
			{
				$("#error").text($("#error").text()+"It is not your turn yet!\n");
			}
			else if(gameBoard[row][col]!=="")
			{
				$("#error").text($("#error").text()+"Cell is not empty.\n");			
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
			if(gameBoard[row*3]==currPiece && gameBoard[row*3+1]==currPiece && gameBoard[row*3+2]==currPiece)
			{
				$("#successMessage").text($("#successMessage").text()+"Player " + currPiece + " wins.\n");
				setTimeout(new function(){ document.location="view_games.jsp" }, 5000);
			}
			//Check that column
			else if(gameBoard[col]==currPiece && gameBoard[col+3]==currPiece && gameBoard[col+6]==currPiece)
			{
				$("#successMessage").text($("#successMessage").text()+"Player " + currPiece + " wins.\n");
				setTimeout(new function(){ document.location="view_games.jsp" }, 5000);
			}
			//Check diagonal up+right /
			else if(gameBoard[6]==currPiece && gameBoard[3+1]==currPiece && gameBoard[0+2]==currPiece)
			{
				$("#successMessage").text($("#successMessage").text()+"Player " + currPiece + " wins.\n");
				setTimeout(new function(){ document.location="view_games.jsp" }, 5000);
			}
			//Check diagonal up+left  \
			else if(gameBoard[6+2]==currPiece && gameBoard[3+1]==currPiece && gameBoard[0]==currPiece)
			{
				$("#successMessage").text($("#successMessage").text()+"Player " + currPiece + " wins.\n");
				setTimeout(new function(){ document.location="view_games.jsp" }, 5000);
			}
		}
	}
</script>

</body>
</html>

