<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" media="screen" type="text/css" title="mise_en_page" href="design.css" />
<title>Look for a media</title>
</head>
<body>
                
<!-- The menu --> 
<%@ include file="menu.jsp" %>

<div id="corps">
    <h1></h1>
    <div class="interieur_corps">

<%  String title = request.getParameter("title"),
           bad = (String)request.getServletContext().getAttribute("badExecution");

    if(bad!=null)
    {
      out.print("<strong>"+bad+"</strong><br/><br/>");
      request.getServletContext().setAttribute("badExecution",null);
    }
    
    String success = (String)request.getServletContext().getAttribute("success");

    if(success!=null)
    {
      out.print("<p>Result of the search \""+title+"\":</p>");
      out.print(success+"<br/><br/>");
      request.getServletContext().setAttribute("success",null);
    } 
%>

<p>Fill in the following fields to find a media:</p>

<form action="lookForMedia.do" method="post">
<table>
    <tr>
        <td><label for="title">Title (or at least pieces) of the media</label> :</td>
        <td><input type="text" name="title" id="title" value="<%= (title==null)?"":title %>"/></td>
    </tr>
</table>
<input type="submit" value="Search">
</form>


    </div>
    <h4></h4>
</div>

</body>
</html>