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
    private float winPercentage;

    public UserPrefs(String userId) {
        this.userId = userId;
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

    public static UserPrefs getPrefsForUser(User user) {
        UserPrefs userPrefs = null;

        EntityManager em = EMF.get().createEntityManager();
        try {
            userPrefs = em.find(UserPrefs.class, user.getUserId());
            if (userPrefs == null) {
                userPrefs = new UserPrefs(user.getUserId());
                userPrefs.setUser(user);
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

	public float getWinPercentage() {
		return winPercentage;
	}

	public void setWinPercentage(float winPercentage) {
		this.winPercentage = winPercentage;
	}
}
