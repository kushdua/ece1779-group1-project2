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
			
			
			String gameId = req.getParameter("gameId");
			
			String opponent = req.getParameter("opponent");
			
			
			String userAction = req.getParameter("userAction");
					
			resp.setContentType("text/html");
       	//response.sendRedirect(request.getRequestURI());
			if (userAction.equals("accept") || userAction.equals("returnToGame")){
				acceptOrReturnToGame(gameId,userAction );
				
				// for this action, should really redirect to play.jsp , so the following code
				// should be uncommented 
				//resp.sendRedirect("/site/play.jsp");
				resp.sendRedirect("/site/view_games.jsp");
				
			}else if (userAction.equals("reject")){
				rejectGame(gameId);
		       	resp.sendRedirect("/site/view_games.jsp");
			}else if (userAction.equals("rematch")){
				String opponentAuthDomain = req.getParameter("opponentAuthDomain");
				if (opponentAuthDomain.isEmpty()){
					opponentAuthDomain = "gmail.com";
				}				
				User user2 = new User(opponent, opponentAuthDomain);
				rematch(currentUser, user2 );
		       	resp.sendRedirect("/site/play.jsp");
			}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	
	private void rejectGame(String gameId)
	{
		TTTGame game = TTTGame.getGame(gameId);
		game.setRejected(true);
		game.save();
	}
	
	private void acceptOrReturnToGame(String gameId, String userAction)
	{		
		TTTGame game = TTTGame.getGame(gameId);
		
		if (userAction.equals("accept")){
			game.setAccepted(true);
			game.setActive(true);
			game.save();
		}
	}
	
	private void rematch(User user1, User user2)
	{
		TTTGame game = new TTTGame(user1, user2);
		game.save();
	}
	
	
	
	
}



