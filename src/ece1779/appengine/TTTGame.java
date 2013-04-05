package ece1779.appengine;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;

import com.google.appengine.api.users.User;

@Entity(name = "TTTGame")
public class TTTGame {
    @Id
    private int gameId;
    
    //User 1 (initiated game)
    @Basic
    private User user1;
    
    //User 2 (accepted game)
    @Basic
    private User user2;

    @Basic
    private boolean isActive;
    
    @Basic
    private boolean isAccepted;

    // | X |  |  |
    // |   |  |  |
    // |   |  |  |
    @Basic
    private String contentsOfBoard;
    
    //Who's turn it is
    @Basic
    private User nextTurnUser;
    
    //Who was the winner - int with values 1 or 2 for user 1 or 2 respectively
    @Basic
    private int winner;

    public TTTGame(User user1, User user2) {
    	this.user1=user1;
    	this.user2=user2;
    	this.setActive(false);
    	this.setAccepted(false);
    }
    
    public TTTGame(int gameId)
    {
    	this.user1=null;
    	this.user2=null;
    	this.gameId = gameId;
    	this.setActive(false);
    	this.setAccepted(false);
    }

    public int getGameId() {
        return gameId;
    }
    
    /**
     * Get list of games for a user.
     * @param userId	User ID for which to return list of games
     * @return			Array of game IDs
     */
    public static ArrayList<TTTGame> getGames(int userId)
    {
    	ArrayList<TTTGame> games = new ArrayList<TTTGame>();
    	EntityManager em = EMF.get().createEntityManager();
    	
		try
		{
			Query query = null;
            List<TTTGame> results = null;

            // Query for all entities of a kind
            query = em.createQuery("SELECT g from TTTGame g WHERE user1=:user1 OR user2=:user2");
            query.setParameter("user1", userId);
            query.setParameter("user2", userId);
            results = (List<TTTGame>) query.getResultList();
            games.addAll(results);
		}
		finally
		{
			em.close();
		}
    	
    	return games;
    }

    public static TTTGame getGame(int gameId) {
        TTTGame game = null;

        EntityManager em = EMF.get().createEntityManager();
        try {
            game = em.find(TTTGame.class, gameId);
            if (game == null) {
                game = new TTTGame(gameId);
                em.persist(game);
            }
        } finally {
            em.close();
        }

        return game;
    }

    public void save() {
        EntityManager em = EMF.get().createEntityManager();
        try {
            em.merge(this);
        } finally {
            em.close();
        }
    }

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isAccepted() {
		return isAccepted;
	}

	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public String getContentsOfBoard() {
		return contentsOfBoard;
	}

	public void setContentsOfBoard(String contentsOfBoard) {
		this.contentsOfBoard = contentsOfBoard;
	}
}
