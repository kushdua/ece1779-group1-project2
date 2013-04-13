package ece1779.appengine;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Transient;

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
    
    @Transient
    private String emptyBoard = " , , , , , , , , ";

    public TTTGame(User user1, User user2) {
    	this.user1=user1;
    	this.user2=user2;
    	this.contentsOfBoard=emptyBoard;
    	this.setActive(false);
    	this.setAccepted(false);
    }
    
    public TTTGame(int gameId)
    {
    	this.user1=null;
    	this.user2=null;
    	this.gameId = gameId;
    	this.contentsOfBoard=emptyBoard;
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
            List<TTTGame> resultsUser1 = null, resultsUser2 = null;

            // Query for all entities of a kind
            query = em.createQuery("SELECT g from TTTGame g WHERE user1=:user1");
            query.setParameter("user1", userId);
            resultsUser1 = (List<TTTGame>) query.getResultList();
            query = em.createQuery("SELECT g from TTTGame g WHERE user2=:user1");
            query.setParameter("user1", userId);
            resultsUser2 = (List<TTTGame>) query.getResultList();
            for(TTTGame g : resultsUser1)
            {
            	if(!games.contains(g)) games.add(g);
            }
            for(TTTGame g : resultsUser2)
            {
            	if(!games.contains(g)) games.add(g);
            }
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

	public void setWinner(User user) {
		if(user.compareTo(user1)==0)
		{
			winner=1;
		}
		else
		{
			winner=2;
		}
	}
	
	public int getWinner()
	{
		return winner;
	}
	
	public User getUser1()
	{
		return user1;
	}
	
	public User getUser2()
	{
		return user2;
	}
	
	public void setNextTurnUser(User user)
	{
		this.nextTurnUser=user;
	}
	
	public User getNextTurnUser()
	{
		return nextTurnUser;
	}
}
