<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>

  <head>
  	<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  </head>

 

  <body>
 
   	<h1 align="center">Awesome Blog</h1>
 

	<%

	    UserService userService = UserServiceFactory.getUserService();

	    User user = userService.getCurrentUser();

	    if (user != null) {

	      pageContext.setAttribute("user", user);

	%>

	<p>Hello, ${fn:escapeXml(user.nickname)}! (You can

	<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out by clicking here.</a>)</p>
	<p>You can make a <a href="/message.jsp">post by clicking here.</a></p>

	<%

	    } else {

	%>

	<p>Hello!

	<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>

	to post on the blog.</p>

	<%

	    }

	%>

	 

	<%

	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	    Key guestbookKey = KeyFactory.createKey("Guestbook", "default");

	    // Run an ancestor query to ensure we see the most up-to-date

	    // view of the Greetings belonging to the selected Guestbook.

	    Query query = new Query("Post", guestbookKey).addSort("date", Query.SortDirection.DESCENDING);

	    List<Entity> greetings = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));

	    if (greetings.isEmpty()) {

	        %>

	        <p>The blog has no messages right now.</p>

	        <%

	    } else {

	        %>

	        <p>Awesome Messages:</p>

	        <%

	        for (Entity greeting : greetings) {
	            pageContext.setAttribute("greeting_title",

                        greeting.getProperty("title"));	        	

	            pageContext.setAttribute("greeting_content",

	                    greeting.getProperty("content"));
	            
	            pageContext.setAttribute("greeting_date",

	                    greeting.getProperty("date"));
	            

	            if (greeting.getProperty("user") == null) {

	                %>

	                <p>An anonymous person wrote on ${fn:escapeXml(greeting_date)}:</p>

	                <%

	            } else {

	                pageContext.setAttribute("greeting_user", greeting.getProperty("user"));

	                %>

	                <p>${fn:escapeXml(greeting_user.nickname)} wrote on ${fn:escapeXml(greeting_date)}:</p>

	                <%

	            }

	            %>
	            <blockquote id="posttitle">${fn:escapeXml(greeting_title)}</blockquote></div>
	            <blockquote>${fn:escapeXml(greeting_content)}</blockquote>

	            <%
	        }
	    }
	%>

 

    

  </body>

</html>