<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="subscribersManagement.Subscriber" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" media="screen" type="text/css" title="mise_en_page" href="design.css" />
<title>Library</title>
</head>
<body>

<!-- The menu --> 
<%@ include file="menu.jsp" %>


<div id="corps">
    <h1></h1>
    <div class="interieur_corps">

<% String connected = (String)request.getSession().getAttribute("connected");
    if(connected!=null && connected.equals("admin"))    //Someone is connected as a librarian
    {
%>
    <p>You have successfully been connected as a librarian. 
    You have now full access to all the functions of the library.</p>
    
<%  }
    else if(connected!=null && connected.equals("subscriber"))  //Someone is connected as a subscriber
    {
    	Subscriber subs = (Subscriber)request.getSession().getAttribute("connectedSubs");
%>
    <p>Hello <%=subs.getFirstName()+" "+subs.getLastName() %>,<br/>
    You have successfully been logged on the library.
    You can now book a media online.</p>
<%  } %>

    <p>Welcome to the online WEB interface of your new Library. 
    Feel free to navigate through it and enjoy ;)</p>

    </div>
    <h4></h4>
</div>

</body>
</html>