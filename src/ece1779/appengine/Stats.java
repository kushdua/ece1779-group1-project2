                                      
package ece1779.appengine;

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

public class Stats {
	
	public static PreparedQuery query()
    {

        UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();
        
     // Get the Datastore Service
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
         
      Filter stats = new FilterPredicate("user",FilterOperator.EQUAL,currentUser);
           
     // Use class Query to assemble a query
                Query q = new Query("UserPrefs").setFilter(stats);
     
        // Use PreparedQuery interface to retrieve results
        PreparedQuery pq = datastore.prepare(q);
        
        return pq;
        
    }

/*	
	public static List<Entity> query()
    {

        UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();
        
     // Get the Datastore Service
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
         
      Filter stats = new FilterPredicate("user",FilterOperator.EQUAL,currentUser);
           
     // Use class Query to assemble a query
                Query q = new Query("UserPrefs").setFilter(stats);
     
        // Use PreparedQuery interface to retrieve results
        PreparedQuery pq = datastore.prepare(q1);
        List<Entity> games =  pq.asList(FetchOptions.Builder.withLimit(50));
        
        return games;
        
    }
*/
	
}