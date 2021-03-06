package ece1779.appengine;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Transient;

import com.google.appengine.api.users.User;

@Entity(name = "TTTGame")
public class TTTGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String gameId;
    
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
    
	@Basic
    private boolean isRejected;

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
    
    @Basic
    private ArrayList<String> boardHistory;
    
    @Transient
    public static String emptyBoard = " , , , , , , , , ";

    //Empty game with two users
    public TTTGame(User user1, User user2) {
    	this.user1=user1;
    	this.user2=user2;
    	this.gameId=String.valueOf(Long.parseLong(user1.getUserId().substring(user1.getUserId().length()-6))+(System.currentTimeMillis()/1000));
    	this.contentsOfBoard=emptyBoard;
    	this.setActive(false);
    	this.setAccepted(false);
    	this.setRejected(false);
    	this.boardHistory=new ArrayList<String>();
    }
    
    //Empty game with provided game ID
    public TTTGame(String gameId)
    {
    	this.user1=null;
    	this.user2=null;
    	this.gameId=String.valueOf(Long.parseLong(gameId)+(System.currentTimeMillis()/1000));
    	this.contentsOfBoard=emptyBoard;
    	this.setActive(false);
    	this.setAccepted(false);
    	this.boardHistory=new ArrayList<String>();
    }
    
    //Empty game with auto generated game ID
    public TTTGame()
    {
    	this.user1=null;
    	this.user2=null;
    	this.gameId=String.valueOf((System.currentTimeMillis()/1000));
    	this.contentsOfBoard=emptyBoard;
    	this.setActive(false);
    	this.setAccepted(false);
    	this.boardHistory=new ArrayList<String>();
    }

    public String getGameId() {
        return gameId;
    }

    public static TTTGame getGame(String gameId) {
        TTTGame game = null;

        EntityManager em = EMF.get().createEntityManager();
        try {
            game = em.find(TTTGame.class, gameId);
//            if (game == null) {
//                game = new TTTGame(gameId);
//                em.persist(game);
//            }
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

	public boolean isRejected() {
		return isRejected;
	}

	public void setRejected(boolean isRejected) {
		this.isRejected = isRejected;
	}
	
	public String getContentsOfBoard() {
		return contentsOfBoard;
	}

	public void setContentsOfBoard(String contentsOfBoard) {
		this.contentsOfBoard = contentsOfBoard;
	}

	public void setWinner(User user) {
		if(user!=null)
		{
			if(user.compareTo(user1)==0)
			{
				winner=1;
			}
			else
			{
				winner=2;
			}
		}
		else
		{
			winner=-1;
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
	
	public void setUser1(User user)
	{
		this.user1=user;
	}
	
	public void setUser2(User user)
	{
		this.user2=user;
	}
	
	public void setNextTurnUser(User user)
	{
		this.nextTurnUser=user;
	}
	
	public User getNextTurnUser()
	{
		return nextTurnUser;
	}
	
	public ArrayList<String> getBoardHistory()
	{
		return boardHistory;
	}
	
	public void addToBoardHistory(String currentBoard)
	{
		boardHistory.add(currentBoard);
		System.out.println("Added " + currentBoard + " to game " + getGameId() + " history.");
		save();
	}
}
