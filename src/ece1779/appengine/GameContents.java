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
        					Helper.cacheRemoveValue(gameID+Helper.CACHE_GAME_CONTENTS_SUFFIX);
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
		        		        synchronized(Helper.cacheLockUserList)
		        		        {
		        		        	Helper.cacheRemoveValue(Helper.CACHE_KEY_USER_LIST);
		        		        }
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

        					Helper.cacheSetValue(gameID+Helper.CACHE_GAME_CONTENTS_SUFFIX, game);
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
					game=(TTTGame)Helper.cacheGetValue(gameID+Helper.CACHE_GAME_CONTENTS_SUFFIX);
				}
        		
        		if(game==null)
        		{
        			game=TTTGame.getGame(gameID);
        			synchronized (Helper.cacheLockGameContents) {
						Helper.cacheSetValue(gameID+Helper.CACHE_GAME_CONTENTS_SUFFIX, game);
					}
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
    	String[] board = gameBoardContents.split(",");
    	for(int i=0; i<board.length; i++)
    	{
    		if(board[i].compareTo(" ")==0)
    		{
    			return false;
    		}
    	}
    	return true;
    }
}
