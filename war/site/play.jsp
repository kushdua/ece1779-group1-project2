<!DOCTYPE html>
<html>
<head>
<title>Play TicTacToe</title>
<!-- Sign in template from Bootstrap site modified for ECE1779 AWS project -->
<style>
#row0Col0 {border-top: 0px; border-right: 2px solid black; border-bottom: 2px solid black; border-left: 0px;}
#row0Col1 {border-top: 0px; border-right: 2px solid black; border-bottom: 2px solid black; border-left: 2px solid black;}
#row0Col2 {border-top: 0px; border-right: 0px; border-bottom: 2px solid black; border-left: 2px solid black;}
#row1Col0 {border-top: 2px solid black; border-right: 2px solid black; border-bottom: 2px solid black; border-left: 0px;}
#row1Col1 {border-top: 2px solid black; border-right: 2px solid black; border-bottom: 2px solid black; border-left: 2px solid black;}
#row1Col2 {border-top: 2px solid black; border-right: 0px; border-bottom: 2px solid black; border-left: 2px solid black;}
#row2Col0 {border-top: 2px solid black; border-right: 2px solid black; border-bottom: 0px; border-left: 0px;}
#row2Col1 {border-top: 2px solid black; border-right: 2px solid black; border-bottom: 0px; border-left: 2px solid black;}
#row2Col2 {border-top: 2px solid black; border-right: 0px; border-bottom: 0px; border-left: 2px solid black;}
li {margin: 0px;}
</style>
</head>
<body onload="setupPage(); setupBoard();">

<%@ include file="header.jsp" %>

<!-- if parameter not specified to page, all uploaded images displayed; else only transformations for specified image are displayed by server side script which writes out page -->
<div class="container">
    <div id="alertsContainer" style="height:90px;">
		<div id="successContainer" onload="$('#successContainer').hide();" style="display: none;">
		   <i id="successIcon" class="icon-ok"></i><span id="successText" style="padding-left: 5px;"></span>
		</div>
        <div id="errorContainer" onload="$('#errorContainer').hide();" style="display: none;">
           <i id="errorIcon" class="icon-remove"></i><span id="errorText" style="padding-left: 5px;"></span>
        </div>
        <div id="loadingContainer" onload="$('#loadingContainer').hide();" style="display: none;">
           <i id="loadingIcon" class="icon-spin icon-spinner"></i><span id="loadingText" style="padding-left: 5px;"></span>
        </div>
	</div>
  <div id="gameContainer">
    <a href="#" class="close" data-dismiss="thumbnails" style="float:right;" onclick="">&times;</a>
    <ul id="squaresContainer" class="thumbnails" style="width: 410px; float:left;" onload="$('#statusContainer').css('margin-left',$('#squaresContainer').width()+20);">
		<li>
			<a href="#"><img id="row0Col0" onClick="cellClickedHandler(0,0)" style="height:100px; width:100px; float:left; display:inline;" /></a>
		</li>
		<li>
			<a href="#"><img id="row0Col1" onClick="cellClickedHandler(0,1)" style="height:100px; width:100px; float:left; display:inline;" /></a>
		</li>
		<li>
			<a href="#"><img id="row0Col2" onClick="cellClickedHandler(0,2)" style="height:100px; width:100px; float:left; display:inline;" /></a>
		</li>
		<br />
		<li>
			<a href="#"><img id="row1Col0" onClick="cellClickedHandler(1,0)" style="height:100px; width:100px; float:left; display:inline;" /></a>
		</li>
		<li>
			<a href="#"><img id="row1Col1" onClick="cellClickedHandler(1,1)" style="height:100px; width:100px; float:left; display:inline;" /></a>
		</li>
		<li>
			<a href="#"><img id="row1Col2" onClick="cellClickedHandler(1,2)" style="height:100px; width:100px; float:left; display:inline;" /></a>
		</li>
        <br />
		<li>
			<a href="#"><img id="row2Col0" onClick="cellClickedHandler(2,0)" style="height:100px; width:100px; float:left; display:inline;" /></a>
		</li>
		<li>
			<a href="#"><img id="row2Col1" onClick="cellClickedHandler(2,1)" style="height:100px; width:100px; float:left; display:inline;" /></a>
		</li>
		<li>
			<a href="#"><img id="row2Col2" onClick="cellClickedHandler(2,2)" style="height:100px; width:100px; float:left; display:inline;" /></a>
		</li>
    </ul>
    <div id="statusContainer" style="float:left;">
        <span id="turnStatusText" style="font-size: 2em; font-weight: bold;"></span>
    </div>
  </div>
</div> <!-- /container -->

<script type="text/javascript">
	<%
		//Grab contents of specified game.
		String gameId = request.getParameter("gameID");
		
		if(gameId == null || gameId.length()==0)
		{
			response.sendRedirect("/site/view_games.jsp");
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
	var savedAfterPlayGameBoard = null;
	myTurn = (split1[1]=="1") ? true : false;
	var myPiece = split1[2];
	var gameDone = false;
	var winner = -1;
	var canCheckNewBoard=true;
	var dataBeingSent = "";
	var setGetTimer=false;
	
	var imageO = "assets/o.png";
	var imageX = "assets/x.png";
	var imageEmpty = "assets/empty.png";
	
/* 	//preload images
	function preload(arrayOfImages) {
	    $(arrayOfImages).each(function(){
	        $('<img/>')[0].src = this;
	        // Alternatively you could use:
	        // (new Image()).src = this;
	    });
	}
	
	preload([
	         imageO,
	         imageX,
	         imageEmpty
	     ]); */
	     
	$('<img src="'+imageO+'" />');
	$('<img src="'+imageX+'" />');
	$('<img src="'+imageEmpty+'" />');
	
	function setupPage()
	{
		$('#statusContainer').css('margin-left','20px');
		$('#statusContainer').css('padding-top', '5px');
	}
	
	//setup board and gameplay functions below
	function setupBoard()
	{
		//console.log("updating board with contents " + gameBoard);
		if(gameBoard!=null && gameBoard.length > 0)
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
			if(myTurn==false)
			{
			    setTimeout(timerUpdateGameboard, 5000);
			    setGetTimer=true;
			}
		}
	}
	
	function timerUpdateGameboard()
	{
		if(setGetTimer==true)
		{
			setGetTimer=true;
		}
		//console.log("timer update: gameDone="+gameDone + " , myTurn="+myTurn);
		if(!gameDone && canCheckNewBoard)
        {
	        //console.log("getting game board contents");
            getGameBoardContents();
            //updateGameData();
        }
	}
	
	function updateGameData()
	{
        //console.log("timer update with gbc " + gameBoardContents);
        split1 = gameBoardContents.split(";");
        gameBoard = split1[0].split(",");
        myTurn = (split1[1]=="1") ? true : false;
        //console.log("timer update with myTurn = " + myTurn);
        $("#turnStatusText").text("It is " + (myTurn ? "your":"your opponent's") + " turn.");
        myPiece = split1[2];
        //console.log("setting up board");
        setupBoard();
        //console.log("checking end");
        checkGameEnd();
	}
    
    function hideSuccess()
    {
        $("#successContainer").hide(500);
        $("#successIcon").hide(500);
        $("#successText").text("");
    }
	
	function showSuccess(message)
	{
        $("#successContainer").show(500);
        $("#successIcon").show(500);
		$("#successText").text(message);
        if(!gameDone)
        {
        	  setTimeout(hideSuccess,2000);
        }
	}
    
    function hideError()
    {
        $("#errorContainer").hide(500);
        $("#errorIcon").hide(500);
        $("#errorText").text("");
    }
    
    function showError(message)
    {
        $("#errorContainer").show(500);
        $("#errorIcon").show(500);
        $("#errorText").text(message);
        setTimeout(hideError,2000);
    }
    
    function showLoading(message)
    {
        $("#loadingContainer").show(500);
        $("#loadingIcon").show(500);
        $("#loadingText").text(message);
    }
    
    function hideLoading()
    {
    	$("#loadingContainer").hide(500);
        $("#loadingIcon").hide(500);
    	$("#loadingText").text("");
    }
	
	function sendGameBoardContents(redirectURL)
	{
		/* $.ajax({
		    type: "GET",
		    url: "GameContents?gameID="+gameID+"&gameBoardContents="+getGameBoardString()+"&gameDone="+gameDone,
		    async: false,
        }); */

        if(myTurn)
        {
	        var gbc=getGameBoardString();
	        canCheckNewBoard=false;
	        
	        if(dataBeingSent!="" && dataBeingSent!=gbc)
	        {
	            console.log("data being sent NOT NULL = " + dataBeingSent);
	            showLoading("Please wait while we send your move to our servers...");
		        while(dataBeingSent!="")
		        {
		        }
	            showLoading("Synchronizing game state with server...");
	        }
	        else
	       	{
	            showLoading("Synchronizing game state with server...");
	       	}
	        
	        console.log("data being sent = " + dataBeingSent);
	        if(dataBeingSent=="")
	        {
	              if(myTurn==true)
	              {
	                  myTurn=false;
	              }
                  dataBeingSent=getGameBoardString();
	              console.log("sending data for gameID " + gameID + " of contents " + getGameBoardString() + " and gameDone = " + gameDone);
	        	  $.post("/GameContents", { gameID: gameID, gameBoardContents: dataBeingSent, gameDone: gameDone } ).done(
					function(data)
					{
				        //console.log("got game board set contents response of " + data);
				        updateGameBoardString();
				        canCheckNewBoard=true;
				        dataBeingSent="";
                        updateGameData();
				        setTimeout(hideLoading,2000);

				        //if(redirectURL!=null)
				        //{
				        //	  setTimeout(new function(){ document.location=redirectURL }, 10000);
				        //}
					});
	        }

        }
	}
	
	function updateGameBoardString()
	{
		gameBoardContents = gameBoard + (myTurn ? ";1":";0") + ";" + myPiece;
	}
	
	function isDataNewerOrSameThanSaved(serverData, savedData)
	{
		var newerOrSame=true;
		for(var i=0; i<savedData.length; i++)
		{
		    if(savedData[i]!=serverData[i] && savedData[i]!=" ")
	    	{
		    	newerOrSame=false;
	    	}
		}
		return newerOrSame;
	}
    
    function getGameBoardContents()
    {
        /* return $.ajax({
            type: "GET",
            url: "GameContents?gameID="+gameID,
            async: false,
        }).responseText; */
        //console.log("getting game board contents");
        if(myTurn==false && !gameDone)
        {
        	showLoading("Synchronizing game state with server...");
	        $.post("/GameContents", { gameID: gameID } ).done(
	        		function(data){
	        			if(canCheckNewBoard==true){
	        		        //console.log("got game board contents of" + data);
	        				//console.log("GBC="+gameBoardContents + ", SAPGB="+savedAfterPlayGameBoard + ", data="+data+"\n");
	        				//If savedAfterPlayGameBoard is null we didn't play recently (ensure consistency on player screen)
	        				if(gameBoardContents == "" || savedAfterPlayGameBoard==null || isDataNewerOrSameThanSaved(data.split(";")[0].split(","), savedAfterPlayGameBoard))
	        				{	//data.split(";")[0]==savedAfterPlayGameBoard.join(",")){
	        					savedAfterPlayGameBoard=null;
	        					gameBoardContents = data;
	        				    //console.log("Updated game board contents!");
	                            updateGameData();
	        					setTimeout(hideLoading,2000);
	        				}
	        			}
	        		});
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
	    answer+=(gameDone?";1":(myTurn?";1":";0"));
	    answer+=";"+myPiece;
	    return answer;
	}
	
	function updateGameBoardContentsString()
	{
		gameBoardContents=gameBoard + (myTurn ? ";1":";0") + ";"+myPiece;
	}
	
	function cellClickedHandler(row, col)
	{
		if(myTurn==true && gameBoard[row*3+col]==" ")
		{
			//Can add game piece
			if(myPiece ==="o")
			{
				gameBoard[row*3+col]="o";
				gameBoardContents = getGameBoardString();
                savedAfterPlayGameBoard=gameBoard;
                //console.log("before sending game board contents; sapgb = " + savedAfterPlayGameBoard + ", gb = " + gameBoard.join(",") + ", gbc = " + gameBoardContents);
				$("#row"+row+"Col"+col).attr('src',imageO);
			}
			else if(myPiece == "x")
			{
				gameBoard[row*3+col]="x";
                gameBoardContents = getGameBoardString();
                savedAfterPlayGameBoard=gameBoard;
                //console.log("before sending game board contents; sapgb = " + savedAfterPlayGameBoard);
				$("#row"+row+"Col"+col).attr('src',imageX);
			}
			showSuccess("Successfully played move.");
			checkGameEnd();
			if(!gameDone)
			{
		        //console.log("sending board contents from cell clicked handler");
			 sendGameBoardContents(null);
			}
		}
		else
		{
			if(myTurn===false)
			{
				showError("It is not your turn yet!");
			}
			else if(gameBoard[row][col]!==" ")
			{
				showError("Cell is not empty.");			
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
		var hasEmpty=false;
		for(var i=minBound; i<maxBound; i++)
		{
			row=Math.floor(i/3);
			col=i%3;
			currPiece=gameBoard[row*3+col];
			
			if(currPiece==" ")
			{
				hasEmpty=true;
			}
			
			//Check that row
			if(currPiece!=" " && gameBoard[row*3]==currPiece && gameBoard[row*3+1]==currPiece && gameBoard[row*3+2]==currPiece)
			{
				if(myTurn)
				{
                    gameDone=true;
					showSuccess("Player " + currPiece + " wins.");
					sendGameBoardContents("view_games.jsp");
					//console.log("Game ended.");
				}
				else
				{
                    gameDone=true;
					showSuccess("Player " + currPiece + " wins.");
                    //setTimeout(new function(){ document.location="view_games.jsp" }, 5000);
				}
			}
			//Check that column
			else if(currPiece!=" " && gameBoard[col]==currPiece && gameBoard[col+3]==currPiece && gameBoard[col+6]==currPiece)
			{
				if(myTurn)
				{
                    gameDone=true;
					showSuccess("Player " + currPiece + " wins.");
	                sendGameBoardContents("view_games.jsp");
	                //console.log("Game ended.");
                }
                else
                {
                    gameDone=true;
                    showSuccess("Player " + currPiece + " wins.");
                    //setTimeout(new function(){ document.location="view_games.jsp" }, 5000);
                }
			}
			//Check diagonal up+right /
			else if(currPiece!=" " && gameBoard[6]==currPiece && gameBoard[3+1]==currPiece && gameBoard[0+2]==currPiece)
			{
				if(myTurn)
				{
                    gameDone=true;
					showSuccess("Player " + currPiece + " wins.");
	                sendGameBoardContents("view_games.jsp");
	                //console.log("Game ended.");
                }
                else
                {
                    gameDone=true;
                    showSuccess("Player " + currPiece + " wins.");
                    //setTimeout(new function(){ document.location="view_games.jsp" }, 5000);
                }
			}
			//Check diagonal up+left  \
			else if(currPiece!=" " && gameBoard[6+2]==currPiece && gameBoard[3+1]==currPiece && gameBoard[0]==currPiece)
			{
				if(myTurn)
				{
                    gameDone=true;
					showSuccess("Player " + currPiece + " wins.");
	                sendGameBoardContents("view_games.jsp");
	                //console.log("Game ended.");
                }
                else
                {
                    gameDone=true;
                    showSuccess("Player " + currPiece + " wins.");
                    //setTimeout(new function(){ document.location="view_games.jsp" }, 5000);
                }
			}
		}
		
		if(hasEmpty == false && !gameDone)
		{
            showSuccess("Game is a tie.");
            gameDone=true;
            if(myTurn)
            {
                sendGameBoardContents("view_games.jsp");
            }
            else
            {
            	//setTimeout(new function(){ document.location="view_games.jsp" }, 5000);
            }
            //console.log("Game ended.");
            //setTimeout(new function(){ document.location="view_games.jsp" }, 5000);
		}
	}
</script>

</body>
</html>

