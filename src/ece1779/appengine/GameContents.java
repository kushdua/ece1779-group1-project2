package ece1779.appengine;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class GameContents extends HttpServlet {
    public void doGet(HttpServletRequest request,
	              HttpServletResponse response)
    throws IOException, ServletException
    {
		response.getWriter().println("GET not supported.");
    }

    // Do this beca1use the servlet uses both post and get
    public void doPost(HttpServletRequest request,
    		HttpServletResponse response)
    				throws IOException, ServletException {
        response.setContentType("text/html");
        
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        if(user!=null)
        {
        	String gameID = request.getParameter("gameID");
        	
        	//Game ID properly passed in at this point.
        	String gameBoardContents = request.getParameter("gameBoardContents");
        	String gameDone = request.getParameter("gameDone");
        	if(gameBoardContents!=null && gameBoardContents.length()>0)
        	{
        		//Set
        		TTTGame game = TTTGame.getGame(gameID);
        		String[] split = gameBoardContents.split(";");
        		if(split.length==3 || split.length==2)
        		{
        			if(game!=null)
        			{
        				synchronized(Helper.cacheLockGameContents)
        				{
        					Helper.cacheRemoveValue(game.getUser1().getEmail()+"_"+gameID+Helper.CACHE_GAME_CONTENTS_SUFFIX);
        					Helper.cacheRemoveValue(game.getUser2().getEmail()+"_"+gameID+Helper.CACHE_GAME_CONTENTS_SUFFIX);
		        			game.setContentsOfBoard(split[0]);
		        			game.addToBoardHistory(split[0]);
		        			boolean gameIsTied = isGameTied(split[0]);
		        			if(gameIsTied || (gameDone!=null && gameDone.length()>0 && gameDone.compareTo("true")==0))
		        			{
		        				game.setActive(false);
		        				game.setNextTurnUser(null);
		        				
		        				if(!gameIsTied)
		        				{
		        					game.setWinner(user);
		        				}
		        				else
		        				{
		        					game.setWinner(null);
		        				}
		        				
		        				UserPrefs userDSInfo = UserPrefs.getUserPrefs(user.getEmail());
		        				if(userDSInfo!=null)
		        				{
		        					userDSInfo.setRating(userDSInfo.getRating()+3);
		        					
		        					if(!gameIsTied)
		        					{
		        						userDSInfo.incrementGamesWon();
		        					}
		        					else
		        					{
		        						userDSInfo.incrementGamesDrawn();
		        					}
		        					
		        					userDSInfo.save();
		        				}
	
		        				UserPrefs user2DSInfo = null;
		        				if(game.getUser1().compareTo(user)==0)
		        				{
		        					user2DSInfo=UserPrefs.getUserPrefs(game.getUser2().getEmail());
		        					if(user2DSInfo!=null)
		        					{
			        					if(!gameIsTied)
			        					{
			        						user2DSInfo.incrementGamesLost();
			        					}
			        					else
			        					{
			        						user2DSInfo.incrementGamesDrawn();
			        					}
		        					}
		        				}
		        				else if(game.getUser2().compareTo(user)==0)
		        				{
		        					user2DSInfo=UserPrefs.getUserPrefs(game.getUser1().getEmail());
		        					if(user2DSInfo!=null)
		        					{
			        					if(!gameIsTied)
			        					{
			        						user2DSInfo.incrementGamesLost();
			        					}
			        					else
			        					{
			        						user2DSInfo.incrementGamesDrawn();
			        					}
		        					}
		        				}
		        				
		        				if(user2DSInfo!=null)
		        				{
		        					if(!gameIsTied)
		        					{
		        						user2DSInfo.setRating(userDSInfo.getRating()-3);
		        					}
		        					user2DSInfo.save();
		        				}
		        				
		        				//Invalidate cache for affected lists
		        		        synchronized(Helper.cacheLockPreviousGames)
		        		        {
		        		        	Helper.cacheRemoveValue(user.getEmail()+Helper.CACHE_USER_PREV_GAMES_SUFFIX);
		        		        	if(user2DSInfo!=null)
		        		        	{
		        		        		Helper.cacheRemoveValue(user2DSInfo.getUserEmail()+Helper.CACHE_USER_PREV_GAMES_SUFFIX);
		        		        	}
		        		        }

		        		        synchronized(Helper.cacheLockGamesInProgress)
		        		        {
		        		        	Helper.cacheRemoveValue(user.getEmail()+Helper.CACHE_USER_GAMES_IN_PROGRESS_SUFFIX);
		        		        	if(user2DSInfo!=null)
		        		        	{
		        		        		Helper.cacheRemoveValue(user2DSInfo.getUserEmail()+Helper.CACHE_USER_GAMES_IN_PROGRESS_SUFFIX);
		        		        	}
		        		        }

		        		        //Invalidate user list because of updated ratings
//		        		        synchronized(Helper.cacheLockUserList)
//		        		        {
//		        		        	Helper.cacheInvalidateUserLists();
//		        		        }
		        			}
		        			else
		        			{
		        				//Not game end
			        			if(game.getUser1().compareTo(user)==0)
			        			{
			        				game.setNextTurnUser(game.getUser2());
			        			}
			        			else
			        			{
			        				game.setNextTurnUser(game.getUser1());
			        			}
		        			}
		        			
		        			game.save();
		        			System.out.println("Saved get from SET on GameContents");

		        			if(game.getNextTurnUser()!=null)
		        			{
	        					Helper.cacheSetValue(game.getUser1().getEmail()+"_"+gameID+Helper.CACHE_GAME_CONTENTS_SUFFIX, game.getContentsOfBoard()+
	        							(game.getNextTurnUser().compareTo(game.getUser1())==0 ? ";1":";0")+";x");
	        					
	        					Helper.cacheSetValue(game.getUser2().getEmail()+"_"+gameID+Helper.CACHE_GAME_CONTENTS_SUFFIX, game.getContentsOfBoard()+
        							(game.getNextTurnUser().compareTo(game.getUser2())==0 ? ";1":";0")+";o");
		        			}
		        			else
		        			{
		        				Helper.cacheRemoveValue(game.getUser1().getEmail()+"_"+gameID+Helper.CACHE_GAME_CONTENTS_SUFFIX);
		        				Helper.cacheRemoveValue(game.getUser2().getEmail()+"_"+gameID+Helper.CACHE_GAME_CONTENTS_SUFFIX);
		        			}
        				}
        			}
        			//Let requester know SET (gameboard update) was a success
        			response.getWriter().println("Success");
        		}
        		else
        		{
        			response.getWriter().println("Invalid gameBoardContents string passed in ("+gameBoardContents+"). Format should be <comma delimited string of board contents>;<user turn - 1 or 2>;<sending user's board piece - optional>");
        		}
        	}
        	else if(gameID!=null && gameID.length()>0)
        	{
        		//Get
        		TTTGame game = null;
        		synchronized (Helper.cacheLockGameContents) {
        			String ans=(String)Helper.cacheGetValue(user.getEmail()+"_"+gameID+Helper.CACHE_GAME_CONTENTS_SUFFIX);
        			if(ans!=null && ans.length()>0)
        			{
						response.getWriter().print(ans);
						return;
        			}
				}
        		
        		game=TTTGame.getGame(gameID);
        		if(game!=null)
        		{
	        		if(game.getNextTurnUser()!=null)
	        		{
		        		synchronized (Helper.cacheLockGameContents) {
		        			Helper.cacheSetValue(user.getEmail()+"_"+gameID+Helper.CACHE_GAME_CONTENTS_SUFFIX, game.getContentsOfBoard()+
								(game.getNextTurnUser().compareTo(user)==0 ? ";1":";0")+
								(game.getUser1().compareTo(user)==0 ? ";x":";o"));
		        		}
	        		}
	        		else
	        		{
	    				Helper.cacheRemoveValue(game.getUser1().getEmail()+"_"+gameID+Helper.CACHE_GAME_CONTENTS_SUFFIX);
	    				Helper.cacheRemoveValue(game.getUser2().getEmail()+"_"+gameID+Helper.CACHE_GAME_CONTENTS_SUFFIX);
	        		}
	        		
	        		String answer="";
	        		if(game!=null)
	        		{
		        		answer+=game.getContentsOfBoard();
		        		if(game.getNextTurnUser()!=null)
		        		{
		        			answer+=(game.getNextTurnUser().compareTo(user)==0 ? ";1":";0");
		        		}
		        		else
		        		{
		        			answer+=";0";
		        		}
		        		answer+=(game.getUser1().compareTo(user)==0 ? ";x":";o");
	        		}
	        		response.getWriter().print(answer);
        		}
        		else
        		{
        			response.getWriter().print("Cannot find game.");
        		}
        	}
        	else
        	{
        		response.getWriter().println("Game ID cannot be null.");
        	}
        }
        else
        {
        	response.getWriter().println("You are not logged in.");
        }
    }
    
    public boolean isGameTied(String gameBoardContents)
    {
    	boolean won = false;
    	String[] gameBoard = gameBoardContents.split(",");
    	boolean hasEmpty=false;
    	for(int i=0; i<gameBoard.length; i++)
    	{
			int row=i/3;
			int col=i%3;
			String currPiece=gameBoard[row*3+col];
			if(currPiece.compareTo(" ")!=0 && gameBoard[row*3].compareTo(currPiece)==0 && gameBoard[row*3+1].compareTo(currPiece)==0 && gameBoard[row*3+2].compareTo(currPiece)==0)
			{
				won=true;
			}
			//Check that column
			else if(currPiece.compareTo(" ")!=0 && gameBoard[col].compareTo(currPiece)==0 && gameBoard[col+3].compareTo(currPiece)==0 && gameBoard[col+6].compareTo(currPiece)==0)
			{
				won=true;
			}
			//Check diagonal up+right /
			else if(currPiece.compareTo(" ")!=0 && gameBoard[6].compareTo(currPiece)==0 && gameBoard[3+1].compareTo(currPiece)==0 && gameBoard[0+2].compareTo(currPiece)==0)
			{
				won=true;
			}
			//Check diagonal up+left  \
			else if(currPiece.compareTo(" ")!=0 && gameBoard[6+2].compareTo(currPiece)==0 && gameBoard[3+1].compareTo(currPiece)==0 && gameBoard[0].compareTo(currPiece)==0)
			{
				won=true;
			}
    		if(gameBoard[i].compareTo(" ")==0)
    		{
    			hasEmpty=true;
    		}
    	}
    	
    	if(won)
    	{
    		return false;
    	}
    	else
    	{
    		if(!hasEmpty)
    		{
    			return true;
    		}
    	}
    	return false;
    }
}
