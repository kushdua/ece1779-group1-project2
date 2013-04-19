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
				acceptOrReturnToGame(currentUser.getEmail(), gameId,userAction );
				resp.sendRedirect("/site/play.jsp?gameID="+gameId);
			}else if (userAction.equals("reject")){
				rejectGame(currentUser.getEmail(), gameId);
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
	
	private void rejectGame(String userEmail, String gameId)
	{
		TTTGame game = TTTGame.getGame(gameId);
		game.setRejected(true);
		game.save();
		
//		synchronized (Helper.cacheLockGamesInvited) {
//			Helper.cacheRemoveValue(userEmail+Helper.CACHE_USER_INVITED_GAMES_SUFFIX);
//		}
	}
	
	private void acceptOrReturnToGame(String userEmail, String gameId, String userAction)
	{		
		TTTGame game = TTTGame.getGame(gameId);
		
		if (userAction.equals("accept")){
			game.setAccepted(true);
			game.setActive(true);
			game.save();
			
//			synchronized (Helper.cacheLockGamesInvited) {
//				Helper.cacheRemoveValue(userEmail+Helper.CACHE_USER_INVITED_GAMES_SUFFIX);
//			}
			
			//Invalidate my + other user's active games list
			synchronized (Helper.cacheLockGamesInProgress) {
				Helper.cacheRemoveValue(game.getUser1().getEmail()+Helper.CACHE_USER_GAMES_IN_PROGRESS_SUFFIX);
				Helper.cacheRemoveValue(game.getUser2().getEmail()+Helper.CACHE_USER_GAMES_IN_PROGRESS_SUFFIX);
			}
		}
	}
	
	private void rematch(User user1, User user2)
	{
		TTTGame game = new TTTGame(user1, user2);
		
		game.setNextTurnUser(user1);
		
		game.save();
		
//		synchronized (Helper.cacheLockGamesInvited) {
//			Helper.cacheRemoveValue(user1.getEmail()+Helper.CACHE_USER_INVITED_GAMES_SUFFIX);
//			Helper.cacheRemoveValue(user2.getEmail()+Helper.CACHE_USER_INVITED_GAMES_SUFFIX);
//		}
	}
	
	
	
	
}



