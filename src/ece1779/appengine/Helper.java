package ece1779.appengine;

//import javax.persistence.EntityManager;
//import javax.persistence.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;



public class Helper {
	
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

}
