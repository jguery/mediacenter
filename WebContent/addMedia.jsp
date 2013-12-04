<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" media="screen" type="text/css" title="mise_en_page" href="design.css" />
<title>Add a new Media</title>
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
     
    String makers = request.getParameter("makers"),
    		title = request.getParameter("title"),
            type = request.getParameter("type"),
            date = request.getParameter("date"),
            key = request.getParameter("key"),
            bad = (String)request.getServletContext().getAttribute("badExecution");

   if(bad!=null)
   {
     out.print("<strong>"+bad+"</strong><br/><br/>");
     request.getServletContext().setAttribute("badExecution",null);
   }
%>

<p>Fill this form to add a new media to the library:</p>

<form action="addMedia.do" method="post">
<table>
    <tr>
        <td><label for="type">Type of the media</label> :</td>
        <td>
            <select name="type" id="type">
                <option value="DVD" <%= (type!=null && type.equals("DVD"))?"selected":"" %> >DVD</option>
                <option value="Book"<%= (type!=null && type.equals("Book"))?"selected":"" %> >Book</option>
            </select>
        </td>
    </tr>
    <tr>
        <td><label for="title">Title</label> :</td>
        <td><input type="text" name="title" id="title" value="<%= (title==null)?"":title %>"/></td>
    </tr>
    <tr>
        <td><label for="makers">Directors/authors (seperate the names by comas)</label> :</td>
        <td><input type="text" name="makers" id="makers" value="<%= (makers==null)?"":makers %>"/></td>
    </tr>
    <tr>
        <td><label for="date">Date of edition/out date (mm/dd/yyyy)</label> :</td>
        <td><input type="text" name="date" id="date" value="<%= (date==null)?"":date %>"/></td>
    </tr>
    <tr>
        <td><label for="key">ISBN/Number of exploitation</label> :</td>
        <td><input type="text" name="key" id="key" value="<%= (key==null)?"":key %>"/></td>
    </tr>
    <tr>
        <td><label for="exemp">Number of exemplaries</label> :</td>
        <td><input type="number" min=1 name="exemp" id="exemp"/></td>
    </tr>
</table>
<input type="submit" value="Add Media">
</form>

<% } %>

    </div>
    <h4></h4>
</div>

</body>
</html>