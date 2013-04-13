package ece1779.appengine;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import com.google.appengine.api.users.User;

@Entity(name = "UserPrefs")
public class UserPrefs {
	/*
	 username
( user object)
nickname
GamesWon
GamesDrawn
GamesLost
IsLoggedIn
SuccessMsg
ErrorMsg
	 */
    @Id
    private String userId;

    @Basic
    private String errorMessages;
    
    @Basic
    private String successMessages;
    
    @Basic
    private User user;

    @Basic
    private boolean loggedIn;

    @Basic
    private int rating;
    
    @Basic
    private int GamesWon;
    
    @Basic
    private int GamesDrawn;
    
    @Basic
    private int GamesLost;
      

    public UserPrefs(String userId) {
        this.userId = userId;
        this.errorMessages = "";
        this.successMessages = "";
        this.user=null;
        this.loggedIn=false;
        this.rating=100;
        this.GamesWon = 0;
        this.GamesDrawn = 0;
        this.GamesLost = 0;
    }
    

    public String getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public int getGamesWon(){
    	return this.GamesWon;
    }

    public int getGamesLost(){
    	return this.GamesLost;
    }
    
    public int getGamesDrawn(){
    	return this.GamesDrawn;
    }
    
    public void incrementGamesWon(){
    	this.GamesWon += 1;
    }
    
    public void incrementGamesLost(){
    	this.GamesLost += 1;
    }
    
    public void incrementGamesDrawn(){
    	this.GamesDrawn += 1;
    }
        
    
    public static UserPrefs getPrefsForUser(User user, boolean loggedIn) {
        UserPrefs userPrefs = null;

        EntityManager em = EMF.get().createEntityManager();
        try {
            userPrefs = em.find(UserPrefs.class, user.getUserId());
            if (userPrefs == null) {
                userPrefs = new UserPrefs(user.getUserId());
                userPrefs.setUser(user);
                userPrefs.setLoggedIn(loggedIn);
                em.persist(userPrefs);
            }
        } finally {
            em.close();
        }

        return userPrefs;
    }

    public void save() {
        EntityManager em = EMF.get().createEntityManager();
        try {
            em.merge(this);
        } finally {
            em.close();
        }
    }

	public String getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(String errorMessages) {
		this.errorMessages = errorMessages;
	}

	public String getSuccessMessages() {
		return successMessages;
	}

	public void setSuccessMessages(String successMessages) {
		this.successMessages = successMessages;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
}
