package ece1779.appengine;

//import javax.persistence.EntityManager;
//import javax.persistence.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;
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

import net.sf.jsr107cache.*;

public class Helper {
	
	private static Cache cache = null;
	public static final String CACHE_USER_PREV_GAMES_SUFFIX = "_PREVIOUS_GAMES";
	public static final String CACHE_USER_GAMES_IN_PROGRESS_SUFFIX = "_GAMES_IN_PROGRESS";

	//Cannot store PreparedQuery object for invited games implementation
//	public static final String CACHE_USER_INVITED_GAMES_SUFFIX = "_INVITED_GAMES";
	
	//Cannot invalidate list of users for everybody (no keySet, iterator() implementations) once a new user registers
//	public static final String CACHE_USER_LIST_SUFFIX = "_LIST_USERS";
	public static final String CACHE_GAME_CONTENTS_SUFFIX = "_GAME_CONTENTS";
	
	//Coarse lock for previous games (not per player)
	public static final Object cacheLockPreviousGames = new Object();
	
	//Coarse lock for games in progress (not per player)
	public static final Object cacheLockGamesInProgress = new Object();
	
	//Coarse lock for invited games (not per player)
//	public static final Object cacheLockGamesInvited = new Object();
	
	//Fine lock (cannot be coarser) for user list
//	public static final Object cacheLockUserList = new Object();
	
	//Coarse lock for all game contents (not per game)
	public static final Object cacheLockGameContents = new Object();
	
	public static void cacheInitIfNeedBe()
	{
		if(cache==null)
		{
			Map props = new HashMap();
	        props.put(GCacheFactory.EXPIRATION_DELTA, 3600);
	        
			while(cache==null)
			{
		        try {
		            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
		            cache = cacheFactory.createCache(props);
		        } catch (CacheException e) {
		            // ...
		        }
			}
		}
	}
	
	public static Object cacheGetValue(String key)
	{
		if(cache==null)
		{
			cacheInitIfNeedBe();
		}
		
		if(cache != null)
		{
			if(cache.containsKey(key))
			{
				return cache.get(key);
			}
		}
		return null;
	}
	
	public static void cacheSetValue(String key, Object value)
	{
		if(cache==null)
		{
			cacheInitIfNeedBe();
		}
		
		if(cache != null)
		{
			cache.put(key,value);
		}
	}
	
	public static void cacheRemoveValue(String key)
	{
		if(cache==null)
		{
			cacheInitIfNeedBe();
		}
		
		if(cache != null)
		{
			cache.remove(key);
		}
	}
	
//	public static void cacheInvalidateUserLists()
//	{
//		if(cache==null)
//		{
//			cacheInitIfNeedBe();
//		}
//		
//		if(cache!=null)
//		{
//			//TODO: JCache UnsupportedOperationException - need to store userlist as a string or another cache for users and then invalidate
//			//each user's UserList one by one in this cache...
//			synchronized (cacheLockUserList) {
//				HashSet<String> set = (HashSet<String>) cache.entrySet();
//				for(String key : set)
//				{
//					if(key.contains(Helper.CACHE_USER_LIST_SUFFIX))
//					{
//						cacheRemoveValue(key);
//					}
//				}
//			}
//		}
//	}
	
	//public static User currentLoggedInUser = null;
	
	//creating this method since couldn't get the TTTGame.getGames(userid) function to work
	//this function is still work in progress
	public static PreparedQuery getInvitedGames()
    {
		UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();
        
        PreparedQuery pq = null;
//        synchronized (cacheLockGamesInvited) {
//			pq = (PreparedQuery) Helper.cacheGetValue(currentUser.getEmail()+CACHE_USER_INVITED_GAMES_SUFFIX);
//		}
        
        if(pq==null)
        {
	        // Get the Datastore Service
	        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	         
	        Filter userFilter = new FilterPredicate("user2",FilterOperator.EQUAL,currentUser);
	        Filter isAccepted = new FilterPredicate("isAccepted",FilterOperator.EQUAL,false);
	        Filter isRejected = new FilterPredicate("isRejected",FilterOperator.EQUAL,false);
	        
	        Filter fltr = CompositeFilterOperator.and(userFilter ,isAccepted,isRejected );
	        		  
	        // Use class Query to assemble a query
			Query q = new Query("TTTGame").setFilter(fltr);
	        //.setFilter(userFilter);
	
	        // Use PreparedQuery interface to retrieve results
	        pq = datastore.prepare(q);
	        
//	        synchronized (cacheLockGamesInvited) {
//	        	Helper.cacheSetValue(currentUser.getEmail()+CACHE_USER_INVITED_GAMES_SUFFIX, pq);
//			}
        }
        
        return pq;
    }
	
	public static ArrayList<Entity> getPreviousGames()
	{
		return getPreviousGames(10);
	}
	
	public static ArrayList<Entity> getPreviousGames(int limit)
    {
		UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();
        ArrayList<Entity> gamelist = null;
        synchronized(cacheLockPreviousGames)
        {
        	gamelist=(ArrayList<Entity>) cacheGetValue(currentUser.getEmail()+Helper.CACHE_USER_PREV_GAMES_SUFFIX);
        }
        
        if(gamelist==null)
        {
        	gamelist = new ArrayList<Entity>();

        	// Get the Datastore Service
        	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        	Filter user1Filter = new FilterPredicate("user1",FilterOperator.EQUAL,currentUser);
        	Filter user2Filter = new FilterPredicate("user2",FilterOperator.EQUAL,currentUser);

        	Filter userFilter = CompositeFilterOperator.or(user1Filter, user2Filter);

        	Filter isAccepted = new FilterPredicate("isAccepted",FilterOperator.EQUAL,true);
        	Filter isCompleted = new FilterPredicate("isActive",FilterOperator.EQUAL,false);
        	//Include ties too
        	Filter winner = new FilterPredicate("winner",FilterOperator.GREATER_THAN_OR_EQUAL,-1);

        	Filter fltr = CompositeFilterOperator.and(userFilter ,isAccepted, isCompleted, winner );

        	// Use class Query to assemble a query
        	Query q = new Query("TTTGame").setFilter(fltr).addSort("winner", SortDirection.ASCENDING).addSort(com.google.appengine.api.datastore.Entity.KEY_RESERVED_PROPERTY, SortDirection.DESCENDING);
        	//.setFilter(userFilter);

        	// Use PreparedQuery interface to retrieve results
        	PreparedQuery pq = datastore.prepare(q);

        	List<Entity> games =  pq.asList(FetchOptions.Builder.withLimit(limit));
        	gamelist.addAll(games);

            synchronized(cacheLockPreviousGames)
            {
            	cacheSetValue(currentUser.getEmail() + Helper.CACHE_USER_PREV_GAMES_SUFFIX, gamelist);
            }
        }
        return gamelist;
    }
	
	
	 public static ArrayList<Entity> getUsers()
	 {
		 
		 UserService userService = UserServiceFactory.getUserService();
		 User currentUser = userService.getCurrentUser();
		 ArrayList<Entity> userlist = null;

//		 synchronized(cacheLockUserList)
//		 {
//			 userlist=(ArrayList<Entity>) cacheGetValue(currentUser.getEmail()+Helper.CACHE_USER_LIST_SUFFIX);
//		 }
	        
		 if(userlist==null)
		 {
			 userlist = new ArrayList<Entity>();

			 // Get the Datastore Service
			 DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			 Filter fltr = new FilterPredicate("user",FilterOperator.NOT_EQUAL,currentUser);

			 // Use class Query to assemble a query
			 Query q = new Query("UserPrefs").setFilter(fltr);

			 PreparedQuery pq = datastore.prepare(q);

			 List<Entity> users =  pq.asList(FetchOptions.Builder.withLimit(1000));
			 userlist.addAll(users);
			 
//			 synchronized(cacheLockUserList)
//			 {
//				 cacheSetValue(currentUser.getEmail()+Helper.CACHE_USER_LIST_SUFFIX, userlist);
//			 }
		 }

		 return userlist;
	 }

	public static ArrayList<Entity> getGamesInProgress()
    {

		UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();
        ArrayList<Entity> gamelist = null;

		synchronized(cacheLockGamesInProgress)
		{
			gamelist=(ArrayList<Entity>) cacheGetValue(currentUser.getEmail()+Helper.CACHE_USER_GAMES_IN_PROGRESS_SUFFIX);
		}
        
        if(gamelist==null)
        {
        	gamelist = new ArrayList<Entity>();

        	// Get the Datastore Service
        	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        	Filter user1Filter = new FilterPredicate("user1",FilterOperator.EQUAL,currentUser);
        	Filter user2Filter = new FilterPredicate("user2",FilterOperator.EQUAL,currentUser);

        	Filter userFilter = CompositeFilterOperator.or(user1Filter, user2Filter);

        	Filter isAccepted = new FilterPredicate("isAccepted",FilterOperator.EQUAL,true);
        	Filter isActive = new FilterPredicate("isActive",FilterOperator.EQUAL,true);

        	Filter fltr = CompositeFilterOperator.and(userFilter ,isAccepted, isActive );

        	// Use class Query to assemble a query
        	Query q = new Query("TTTGame").setFilter(fltr).addSort(com.google.appengine.api.datastore.Entity.KEY_RESERVED_PROPERTY, SortDirection.DESCENDING);
        	//.setFilter(userFilter);

        	// Use PreparedQuery interface to retrieve results
        	PreparedQuery pq = datastore.prepare(q);

        	List<Entity> games =  pq.asList(FetchOptions.Builder.withLimit(1000));
        	gamelist.addAll(games);

    		synchronized(cacheLockGamesInProgress)
    		{
    			cacheSetValue(currentUser.getEmail()+Helper.CACHE_USER_GAMES_IN_PROGRESS_SUFFIX, gamelist);
    		}
        }
                
        return gamelist;
    }
}
