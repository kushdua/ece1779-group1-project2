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

public class AuthenticateUpdate extends HttpServlet {
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
        String userID = "";
        userID = request.getParameter("userID");
        String action = request.getParameter("action");
        if(action.compareTo("login")==0)
        {
        	UserPrefs user = UserPrefs.getUserPrefs(userID);
        	if(user!=null)
        	{
        		user.setLoggedIn(true);
        		user.save();
        	}
        }
        else if(action.compareTo("logout")==0)
        {
        	UserPrefs user = UserPrefs.getUserPrefs(userID);
        	if(user!=null)
        	{
        		user.setLoggedIn(false);
        		user.save();
        	}
        }
    }
}
