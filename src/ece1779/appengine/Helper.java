package ece1779.appengine;

//import javax.persistence.EntityManager;
//import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.SortDirection;



public class Helper {
	
	//public static User currentLoggedInUser = null;
	
	//creating this method since couldn't get the TTTGame.getGames(userid) function to work
	//this function is still work in progress
	public static PreparedQuery getInvitedGames()
    {

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
        
        return pq;
        
    }
	
	public static ArrayList<Entity> getPreviousGames()
    {

		UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();
        
     // Get the Datastore Service
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        Filter user1Filter = new FilterPredicate("user1",FilterOperator.EQUAL,currentUser);
        Filter user2Filter = new FilterPredicate("user2",FilterOperator.EQUAL,currentUser);
        
        Filter userFilter = CompositeFilterOperator.or(user1Filter, user2Filter);
                
        Filter isAccepted = new FilterPredicate("isAccepted",FilterOperator.EQUAL,true);
        Filter winner = new FilterPredicate("winner",FilterOperator.GREATER_THAN,0);
        
        Filter fltr = CompositeFilterOperator.and(userFilter ,isAccepted, winner );
        		  
        
     // Use class Query to assemble a query
		Query q = new Query("TTTGame").setFilter(fltr).addSort("winner", SortDirection.ASCENDING).addSort(com.google.appengine.api.datastore.Entity.KEY_RESERVED_PROPERTY, SortDirection.DESCENDING);
        //.setFilter(userFilter);

        // Use PreparedQuery interface to retrieve results
        PreparedQuery pq = datastore.prepare(q);
        
        List<Entity> games =  pq.asList(FetchOptions.Builder.withLimit(10));
        ArrayList<Entity> gamelist = new ArrayList<Entity>();
        gamelist.addAll(games);
                
        return gamelist;
        
    }

	public static ArrayList<Entity> getGamesInProgress()
    {

		UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();
        
     // Get the Datastore Service
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        Filter user1Filter = new FilterPredicate("user1",FilterOperator.EQUAL,currentUser);
        Filter user2Filter = new FilterPredicate("user2",FilterOperator.EQUAL,currentUser);
        
        Filter userFilter = CompositeFilterOperator.or(user1Filter, user2Filter);
                
        Filter isAccepted = new FilterPredicate("isAccepted",FilterOperator.EQUAL,true);
        
        Filter fltr = CompositeFilterOperator.and(userFilter ,isAccepted );
        		  
        
     // Use class Query to assemble a query
		Query q = new Query("TTTGame").setFilter(fltr).addSort("winner", SortDirection.ASCENDING).addSort(com.google.appengine.api.datastore.Entity.KEY_RESERVED_PROPERTY, SortDirection.DESCENDING);
        //.setFilter(userFilter);

        // Use PreparedQuery interface to retrieve results
        PreparedQuery pq = datastore.prepare(q);
        
        List<Entity> games =  pq.asList(FetchOptions.Builder.withLimit(10));
        ArrayList<Entity> gamelist = new ArrayList<Entity>();
        gamelist.addAll(games);
                
        return gamelist;
        
    }
}
