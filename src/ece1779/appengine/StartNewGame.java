package ece1779.appengine;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import ece1779.appengine.UserPrefs;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class StartNewGame extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request,
	              HttpServletResponse response)
    throws IOException, ServletException
    {
		doPost(request, response);
    }

    // Do this because the servlet uses both post and get
    public void doPost(HttpServletRequest request,
    		HttpServletResponse response)
    				throws IOException, ServletException {
        response.setContentType("text/html");
        
        UserService userService = UserServiceFactory.getUserService();
        User user1 = userService.getCurrentUser();
        UserPrefs user1pref=null, user2pref = null;
        
        if(user1!=null)
        {
        	String user2email = null;
        	
        	try
        	{
        		user2email = request.getParameter("user2");
        		user1pref = UserPrefs.getUserPrefs(user1.getEmail());
        		user2pref = UserPrefs.getUserPrefs(user2email);
        	}
        	catch(NumberFormatException nfe)
        	{
        		response.getWriter().println("Invalid username provided.");
        		return;
        	}
        	//Start the game now using TTTGame constructors ... 

        	if(user1pref!=null && user2pref!=null)
        	{
	        	EntityManager em = EMF.get().createEntityManager();
	            try {
	            	TTTGame game = new TTTGame(user1.getUserId());
	            	if(game!=null)
	            	{
		            	game.setUser1(user1);
		            	game.setUser2(user2pref.getUser());
		    	        game.setAccepted(false);
		    	        game.setActive(false);
	    	        	game.setNextTurnUser(user1pref.getUser());
		    	        
		    	        //Assign random starting player, based on ratings
		    	        //If you play a much better player, you will have greater chance of going first
		    	        int user1Range=user2pref.getRating();
		    	        if(user1Range<0)
		    	        {
		    	        	user1Range=50;
		    	        }
		    	        int user2Range=user1Range+user1pref.getRating();
		    	        if(user2Range<0)
		    	        {
		    	        	user2Range=100;
		    	        }
		    	        
		    	        int rand = (int)Math.random()*user2Range;
		    	        if(rand<=user1Range)
		    	        {
		    	        	game.setNextTurnUser(user1pref.getUser());
		    	        }
		    	        else if(rand>user1Range && rand<=user2Range)
		    	        {
		    	        	game.setNextTurnUser(user2pref.getUser());
		    	        }
		    	        
		    	        game.setContentsOfBoard(TTTGame.emptyBoard);
		    	        //Not necessary - 1st move should not be empty board
		    	        //game.addToBoardHistory(TTTGame.emptyBoard);
		                em.persist(game);
		                
		                response.getWriter().print(game.getGameId());
	            	}
	            	else
	            	{
	            		response.getWriter().println("Cannot create game");
	            	}
	
	            } finally {
	                em.close();
	            }
        	}
        	else
        	{
        		response.getWriter().println("Cannot find users.");
        	}
        }
        else
        {
        	response.getWriter().println("You are not logged in.");
        }
    }
}
