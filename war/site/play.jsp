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
		String gameBoardContents = "x,o,x,o,x,o,x,o,x;1,x";
	%>
	var gameBoardContents = "<%= gameBoardContents %>";
	var split1 = gameBoardContents.split(";");
	var gameBoard = split1[0].split(",");
	var myTurn = (split[1]==="1") ? true : false;
	var myPiece = split[2];
	
	var imageO = "<image src=\"\" />";
	var imageX = "<image src=\"\" />";
	
	void cellClickedHandler(row, col)
	{
		if(myTurn===true && gameBoard[row][col]==="")
		{
			//Can add game piece
			if(myPiece ==="o")
			{
				$("#row"+row+"Col"+col).src=imageO;
			}
			else
			{
				$("#row"+row+"Col"+col).src=imageX;
			}
		}
		else
		{
			$("#error").class -= "hidden";
			if(myTurn===false)
			{
				$("#error").src += "It is not your turn yet!";
			}
			else if(gameBoard[row][col]!=="")
			{
				$("#error").src += "Cell is not empty.";			
			}
		}
	}
	
</script>

<!-- if parameter not specified to page, all uploaded images displayed; else only transformations for specified image are displayed by server side script which writes out page -->
<div class="container">
	<div class="alert alert-success hidden" id="turn">
		<a class="close" data-dismiss="alert">×</a>
		<p id="successMessage" />
	</div>
	<p class="alert alert-error hidden" id="error"/>
    <ul class="thumbnails">
		<li class="span4" id="row1Col1" onClick="cellClickedHandler(1,1)"></li>
		<li class="span4" id="row1Col2"></li>
		<li class="span4" id="row1Col3"></li>
		<li class="span4" id="row2Col1"></li>
		<li class="span4" id="row2Col2"></li>
		<li class="span4" id="row2Col3"></li>
		<li class="span4" id="row3Col1"></li>
		<li class="span4" id="row3Col2"></li>
		<li class="span4" id="row3Col3"></li>
    </ul>
</div> <!-- /container -->

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
</body>
</html>

