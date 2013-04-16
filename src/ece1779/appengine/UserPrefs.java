package ece1779.appengine;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userEmail;

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
      

    public UserPrefs(String userEmail) {
        this.userEmail = userEmail;
        this.errorMessages = "";
        this.successMessages = "";
        this.user=null;
        this.loggedIn=false;
        this.rating=100;
        this.GamesWon = 0;
        this.GamesDrawn = 0;
        this.GamesLost = 0;
    }

    public String getUserEmail() {
        return userEmail;
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
        
    public static UserPrefs getUserPrefs(String userEmail)
    {
    	EntityManager em = EMF.get().createEntityManager();
    	UserPrefs userPrefs = null;
    	try
    	{
    		userPrefs = em.find(UserPrefs.class, userEmail);
    	}
    	finally
    	{
    		em.close();
    	}
    	return userPrefs;
    }
    
    public static UserPrefs getPrefsForUser(User user, boolean loggedIn) {
        UserPrefs userPrefs = null;

        EntityManager em = EMF.get().createEntityManager();
        try {
            userPrefs = em.find(UserPrefs.class, user.getEmail());
            if (userPrefs == null) {
                userPrefs = new UserPrefs(user.getEmail());
                userPrefs.setUser(user);
                userPrefs.setLoggedIn(loggedIn);
                em.persist(userPrefs);
            }
        } finally {
            em.close();
        }
        
        em = EMF.get().createEntityManager();
        try {
	        TTTGame game = new TTTGame(user.getUserId());
	        game.setUser1(user);
	        User user2=new User("test2@example.com", "gmail.com");
	        game.setUser2(user2);
	        game.setAccepted(true);
	        game.setActive(true);
	        game.setContentsOfBoard("x,,,,,,,,");
	        game.addToBoardHistory("x,,,,,,,,,");
	        em.persist(game);
        }
        finally
        {
        	em.close();
        }

        return userPrefs;
    }
    /*
    public static ArrayList<Entity> getUsers()
    {
    	
		UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();
        
     // Get the Datastore Service
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        Filter fltr = new FilterPredicate("user1",FilterOperator.NOT_EQUAL,currentUser);
        
        // Use class Query to assemble a query
     		Query q = new Query("TTTGame").setFilter(fltr);

        /* OLD WAY
    	EntityManager em = EMF.get().createEntityManager();
        ArrayList<String>resultsList = new ArrayList<String>();
    	try
		{
	        Query query = em.createQuery("SELECT * from UserPrefs");// WHERE i.user1 != :user1 AND i.user2 != :user2");
//	        query.setParameter("user1", exceptUser);
//	        query.setParameter("user2", exceptUser);
	        List<UserPrefs> users = (List<UserPrefs>) query.getResultList();
	        for(UserPrefs u : users)
	        {
	        	if(u.getUser().compareTo(exceptUser)!=0) resultsList.add(u.userId+","+u.getUser().getEmail());
	        }
		}
    	finally
		{
			em.close();
		}
     		
     	// Use PreparedQuery interface to retrieve results
            PreparedQuery pq = datastore.prepare(q);
            
        List<com.google.appengine.api.datastore.Entity> games =  pq.asList(FetchOptions.Builder.withLimit(1000));
        ArrayList<Entity> gamelist = new ArrayList<Entity>();
        gamelist.addAll(games);
		return gamelist;
    }*/

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
