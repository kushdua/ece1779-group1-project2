package ece1779.appengine;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.appengine.api.users.User;

public class Helper {
	
	//creating this method since couldn't get the TTTGame.getGames(userid) function to work
	//this function is still work in progress
	public static ArrayList<TTTGame> getGames(User userId)
    {
    	ArrayList<TTTGame> games = new ArrayList<TTTGame>();
    	EntityManager em = EMF.get().createEntityManager();
    	
		try
		{
			Query query = null;
            List<TTTGame> results = null;

            // Query for all entities of a kind
            query = em.createQuery("SELECT g from TTTGame g ");
            //query.setParameter("user1", userId);
            //query.setParameter("user2", userId);
            results = (List<TTTGame>) query.getResultList();
            games.addAll(results);
		}
		finally
		{
			em.close();
		}
    	
    	return games;
    }

}
