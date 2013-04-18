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
        UserPrefs user2pref = null;
        
        if(user1!=null)
        {
        	String user2email = null;
        	
        	try
        	{
        		user2email = request.getParameter("user2");
        		user2pref = UserPrefs.getUserPrefs(user2email);
        	}
        	catch(NumberFormatException nfe)
        	{
        		response.getWriter().println("Invalid username provided.");
        		return;
        	}
        	//Start the game now using TTTGame constructors ... 

        	EntityManager em = EMF.get().createEntityManager();
            try {

            	TTTGame game = new TTTGame(user1.getUserId());
            	//TTTGame game = new TTTGame();
            	game.setUser1(user1);
            	game.setUser2(user2pref.getUser());
    	        game.setAccepted(false);
    	        game.setActive(false);
    	        game.setNextTurnUser(user2pref.getUser());
    	        game.setContentsOfBoard(" , , , , , , , , ");
    	        game.addToBoardHistory(" , , , , , , , , ");
                em.persist(game);

            } finally {
                em.close();
            }
        }
        else
        {
        	response.getWriter().println("You are not logged in.");
        }
    }
}
