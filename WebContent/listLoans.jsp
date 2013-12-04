<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="main.Library" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" media="screen" type="text/css" title="mise_en_page" href="design.css" />
<title>List the current loans of the library</title>
</head>
<body>
                
<!-- The menu --> 
<%@ include file="menu.jsp" %>

<div id="corps">
    <h1></h1>
    <div class="interieur_corps">

<% 
    String connected = (String)request.getSession().getAttribute("connected");
    if(connected == null)
      response.sendRedirect("connexion.jsp");
    else if(connected.equals("subscriber"))
        out.print("<strong>Only librarians can access this functionality.</strong><br/><br/>");
    else {
  
	   Library lib = (Library)request.getServletContext().getAttribute("library");
	   out.print(lib.listLoansHTML()); 

   } %>

    </div>
    <h4></h4>
</div>

</body>
</html>