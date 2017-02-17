package blog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;

@SuppressWarnings("serial")
public class DailyUpdate extends HttpServlet {
	@SuppressWarnings("deprecation")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Logger.getLogger("log").info("sending emails");
		Query sq = new Query("Subscriber");
	    List<Entity> subscribers =
				    datastore.prepare(sq).asList(FetchOptions.Builder.withDefaults());
	    
	    Date d = new Date();
	    d.setDate(d.getDay()-1);
	    Filter dateFilter =
			    new FilterPredicate("date", FilterOperator.GREATER_THAN, d);
		Query pq = new Query("Post").setFilter(dateFilter);
	    List<Entity> posts =
			    datastore.prepare(pq).asList(FetchOptions.Builder.withDefaults());
	    
	    if(posts.size() != 0) {
		    Properties props = new Properties();
		    Session session = Session.getDefaultInstance(props, null);
		    for(Entity subscribe: subscribers) {
		    	String email = (String) subscribe.getProperty("email");
				Logger.getLogger("log").info("sending to " + email);

		    	String message = "";
			    try {
			      MimeMessage msg = new MimeMessage(session);

			      msg.setSubject("Awesome Blog Daily Update");
			      msg.setFrom(new InternetAddress("markcarter25@utexas.edu", "Awsome Blog Admin"));
			      msg.addRecipient(Message.RecipientType.TO,
			                       new InternetAddress(email, "Awesome Blogger"));
			      message += "Here are posts you have missed in the past 24 hours!\n\n";

			      for(Entity post: posts) {
			    	  String title = (String) post.getProperty("title");
			    	  String content = (String) post.getProperty("content");
			    	  Date date = (Date) post.getProperty("date");
			    	  User user = (User) post.getProperty("user");
			    	  
			    	  message += user.getEmail() + " posted on " + date.toString() + ":\n";
			    	  message += "\t"+title+"\n\n";
			    	  message += "\t"+content+"\n\n";
			      }
			      
			      msg.setText(message);
			      Transport.send(msg);
				  Logger.getLogger("log").info(message + " sent");

			    } catch (Exception e) {
				  Logger.getLogger("log").info("failed to send an email");
			    }
	    
	    	}
	    }
	}
}
