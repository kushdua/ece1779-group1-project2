package ece1779.appengine;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

public class GameContents extends HttpServlet {
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
        User user = userService.getCurrentUser();
        
        if(user!=null)
        {
        	int gameID = -1;
        	try
        	{
        		gameID = Integer.parseInt(request.getParameter("gameID"));
        	}
        	catch(NumberFormatException nfe)
        	{
        		response.getWriter().println("Invalid game ID provided ("+request.getParameter("gameID")+").");
        	}
        	
        	if(gameID!=-1)
        	{
        		TTTGame game = TTTGame.getGame(gameID);
        		String answer="";
        		answer+=game.getContentsOfBoard();
        		answer+=(game.getNextTurnUser().compareTo(user)==0 ? ";1":";0");
        		answer+=(game.getUser1().compareTo(user)==0 ? ";x":";o");
        	}
        }
        else
        {
        	response.getWriter().println("You are not logged in.");
        }
    }
}
