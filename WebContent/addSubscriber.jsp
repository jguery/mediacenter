<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" media="screen" type="text/css" title="mise_en_page" href="design.css" />
<title>Add a new Subscriber</title>
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
  	 
    String firstname = request.getParameter("firstname"),
			lastname = request.getParameter("lastname"),
			borndate = request.getParameter("borndate"),
			bad = (String)request.getServletContext().getAttribute("badExecution");

   if(bad!=null)
   {
     out.print("<strong>"+bad+"</strong><br/><br/>");
     request.getServletContext().setAttribute("badExecution",null);
   }
%>

<p>Fill this form to add a new subscriber to the library:</p>

<form action="addSubs.do" method="post">
<table>
    <tr>
        <td><label for="firstname">First name</label> :</td>
        <td><input type="text" name="firstname" id="firstname" value="<%= (firstname==null)?"":firstname %>"/></td>
    </tr>
    <tr>
        <td><label for="lastname">Last name</label> :</td>
        <td><input type="text" name="lastname" id="lastname" value="<%= (lastname==null)?"":lastname %>"/></td>
    </tr>
    <tr>
        <td><label for="borndate">Date of birth (mm/dd/yyyy)</label> :</td>
        <td><input type="text" name="borndate" id="borndate" value="<%= (borndate==null)?"":borndate %>"/></td>
    </tr>
</table>
<input type="submit" value="Add Subscriber">
</form>

<% } %>

    </div>
    <h4></h4>
</div>

</body>
</html>