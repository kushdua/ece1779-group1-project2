package ece1779.appengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
//import javax.persistence.Query;
import javax.servlet.http.*; 

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.*;
public class PopulateTestData extends HttpServlet {
		 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		public void doGet(HttpServletRequest req,
	             HttpServletResponse resp)
	throws IOException {
			
			UserService userService = UserServiceFactory.getUserService();
            User currentUser = userService.getCurrentUser();
            
         // Get the Datastore Service
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
             
            Filter userFilter = new FilterPredicate("user2",FilterOperator.EQUAL,currentUser);
            Filter isAccepted = new FilterPredicate("isAccepted",FilterOperator.EQUAL,false);
            
            Filter fltr = CompositeFilterOperator.and(userFilter ,isAccepted );
            		  
            
         // Use class Query to assemble a query
			Query q = new Query("TTTGame").setFilter(fltr);
            //.setFilter(userFilter);

            // Use PreparedQuery interface to retrieve results
            PreparedQuery pq = datastore.prepare(q);
            
           // List<Entity> results = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
			
            
            
            /*
            ArrayList<TTTGame> games = new ArrayList<TTTGame>();
        	EntityManager em = EMF.get().createEntityManager();
        	
    		try
    		{
    			
            	
    			Query query = null;
                List<TTTGame> results = null;

                // Query for all entities of a kind
                query = em.createQuery("SELECT gameId,user1 from TTTGame g");
                //query.setParameter("user1", userId);
                //query.setParameter("user2", userId);
                results = (List<TTTGame>) query.getResultList();
                games.addAll(results);
    		}
    		finally
    		{
    			em.close();
    		}
            */
            
            /*
			ArrayList<User> users = new ArrayList<User>();
			EntityManager em = EMF.get().createEntityManager();
			
			boolean loadData = false;
			
						
			if (loadData){
			try
			{
				Query query = null;
	            List<User> results = null;

	            // Query for all entities of a kind
	            query = em.createQuery("SELECT user from UserPrefs u");
	            results = (List<User>) query.getResultList();
	            
	            //results.
	            users.addAll(results);
	            
	            
	            User user2;
	            users.remove(user1);
	            
	            int numOfUsers = users.size();
	            for (int i=0; i<numOfUsers; i++){
	            	user2 =users.get(i);
	            	//to simulate that all other users have invited the current user to a game
	            	TTTGame game = new TTTGame(user2,user1);
	            	game.save();
	            }
	            
	            
			}
			finally
			{
				em.close();
			}
			
			}
			*/
			
			/*
			//ArrayList<TTTGame> games = new ArrayList<TTTGame>();
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
			*/
	    	
	    	
			
			/*
			int numOfUsers = 5;

			String userid = "";
        	for (int i=1; i<= numOfUsers; i++){
        		userid = "test" + i + "@example.com";
        		
	        EntityManager em = EMF.get().createEntityManager();
	        UserPrefs userPrefs;
	        //User user = new User("test1@example.com");
	        
	        try {	
	        	
	        	
	        	//String userid = "test1@example.com";
	           
	        	userPrefs = new UserPrefs(userid);
	                //userPrefs.setUser(userid);
	                //userPrefs.setLoggedIn(false);
	            	
	                em.persist(userPrefs);
	        	
	            
	        } finally {
	            em.close();
	        }
	        
	        }
        	*/

	
	resp.setContentType("text/html");
	PrintWriter out = resp.getWriter();
	for (Entity result : pq.asIterable()) {
		out.println("<p>"+ ((User)result.getProperty("user1")).getEmail() +"</p>");
	}
	out.println("<p>The test data have been loaded</p>");
}
		
		public void doPost(HttpServletRequest req,
	             HttpServletResponse resp)
	throws IOException {
			doGet(req,resp);
		}
		
	

}


