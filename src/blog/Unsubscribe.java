package blog;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


@SuppressWarnings("serial")
public class Unsubscribe extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {        

        String email = req.getParameter("email");
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Filter propertyFilter =
			    new FilterPredicate("email", FilterOperator.EQUAL, email);
		Query q = new Query("Subscriber").setFilter(propertyFilter);
        
		List<Entity> results =
			    datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
		for(Entity e : results) {
	        datastore.delete(e.getKey());
		}

        resp.sendRedirect("/");

    }
}
