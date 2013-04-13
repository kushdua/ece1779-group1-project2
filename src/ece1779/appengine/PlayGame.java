package ece1779.appengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class PlayGame extends HttpServlet  {
	public void doPost(HttpServletRequest req,
            HttpServletResponse resp)
            throws IOException {
		
	   UserService userService = UserServiceFactory.getUserService();
       User currentUser = userService.getCurrentUser();
       
       try {
			
			
			int gameId = Integer.parseInt(req.getParameter("gameId"));
			
			String opponent = req.getParameter("opponent");
			
			String userAction = req.getParameter("userAction");
					
			resp.setContentType("text/html");
       	//response.sendRedirect(request.getRequestURI());
			if (userAction.equals("accept")){
				acceptGame(gameId);
				resp.sendRedirect("/site/play.jsp");
			}else if (userAction.equals("reject")){
				rejectGame(gameId);
		       	resp.sendRedirect("/site/view_games.jsp");
			}else if (userAction.equals("rematch")){
		       	resp.sendRedirect("/site/dummyPlay.jsp");
			}
			
			} catch (Exception e) {
				e.printStackTrace();
			} 
		
	}
	
	private void rejectGame(int gameId)
	{
		TTTGame game = TTTGame.getGame(gameId);
		
		game.setRejected(true);
		game.save();
	}
	
	private void acceptGame(int gameId)
	{
		TTTGame game = TTTGame.getGame(gameId);
		
		game.setAccepted(true);
		game.setActive(true);
		game.save();
	}
	
	
	
	
}



