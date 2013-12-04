<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" media="screen" type="text/css" title="mise_en_page" href="design.css" />
<title>Delete a media</title>
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
     
    String key = request.getParameter("key"),
           bad = (String)request.getServletContext().getAttribute("badExecution");

   if(bad!=null)
   {
     out.print("<strong>"+bad+"</strong><br/><br/>");
     request.getServletContext().setAttribute("badExecution",null);
   }
%>

<p>Fill this form to delete a media:</p>

<form action="deleteMedia.do" method="post">
<table>
    <tr>
        <td><label for="key">ISBN/number of exploitation of the media</label> :</td>
        <td><input type="text" name="key" id="key" value="<%= (key==null)?"":key %>"/></td>
    </tr>
</table>
<input type="submit" value="Delete media">
</form>

<% } %>

    </div>
    <h4></h4>
</div>

</body>
</html>