<html>	
	<head>
  		<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  		<title>Post A Message</title>	    
  	</head>
  	
  	<h1 align="center">Post a message</h1>
  	 <br></br>
  	<div id = "container">
	<form action="/message" method="post">
	  <div>Title:<input type="text" name= "title" required></div>
	  <br></br>
      <div>Message:<textarea name="content" rows="3" cols="60" required></textarea></div>
      <br></br>
      <div><input type="submit" value="Post Greeting" /></div>
    </form>
    </div>
</html>