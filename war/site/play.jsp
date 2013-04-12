<!DOCTYPE html>
<html>
<head>
<title>Play TicTacToe</title>
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

<script type="text/javascript">
	<%
		//Grab contents of specified game.
		int gameId = Integer.parseInt(request.getParameter("gameID"));
		//TODO AJAX request for game contents (separated by ,) and
		//turn (concatenated to game contents by ;)
		String gameBoardContents = "x, , , ,x,o, ,o,x;1,o";
	%>
	var gameBoardContents = "<%= gameBoardContents %>";
	var split1 = gameBoardContents.split(";");
	var gameBoard = split1[0].split(",");
	var myTurn = (split[1]==="1") ? true : false;
	var myPiece = split[2];
	
	var imageO = "<image src=\"assets/o.png\" />";
	var imageX = "<image src=\"assets/x.png\" />";
	var imageEmpty = "<image src=\"assets/empty.png\" />";
	
	for(var i=0; i<gameBoard.length; i++)
	{
		if(gameBoard[i]=="o")
		{
			$("#row"+i/3+"Col"+i%3).src = imageX;
		}
		else if(gameBoard[i]=="x")
		{
			$("#row"+i/3+"Col"+i%3).src = imageO;
		}
		else if(gameBoard[i]==" ")
		{
			$("#row"+i/3+"Col"+i%3).src = imageEmpty;
		}
	}
	
	function cellClickedHandler(row, col)
	{
		if(myTurn===true && gameBoard[row*3+col]==="")
		{
			//Can add game piece
			if(myPiece ==="o")
			{
				gameBoard[row*3+col]="o";
				//TODO send to JSP on server
				$("#row"+row+"Col"+col).src=imageO;
			}
			else if(myPiece == "x")
			{
				gameBoard[row*3+col]="x";
				//TODO send to JSP on server
				$("#row"+row+"Col"+col).src=imageX;
			}
			$("#successMessage").text($("#successMessage").text()+"Successfully played move.\n");
			checkGameEnd();
		}
		else
		{
			$("#error").class -= "hidden";
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
			row=i/3;
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
    <ul class="thumbnails">
		<li class="span4" id="row1Col1" onClick="cellClickedHandler(0,0)"></li>
		<li class="span4" id="row1Col2" onClick="cellClickedHandler(0,1)"></li>
		<li class="span4" id="row1Col3" onClick="cellClickedHandler(0,2)"></li>
		<li class="span4" id="row2Col1" onClick="cellClickedHandler(1,0)"></li>
		<li class="span4" id="row2Col2" onClick="cellClickedHandler(1,1)"></li>
		<li class="span4" id="row2Col3" onClick="cellClickedHandler(1,2)"></li>
		<li class="span4" id="row3Col1" onClick="cellClickedHandler(2,0)"></li>
		<li class="span4" id="row3Col2" onClick="cellClickedHandler(2,1)"></li>
		<li class="span4" id="row3Col3" onClick="cellClickedHandler(2,2)"></li>
    </ul>
</div> <!-- /container -->

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
</body>
</html>

