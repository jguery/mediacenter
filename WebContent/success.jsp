<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" media="screen" type="text/css" title="mise_en_page" href="design.css" />
<title>Successful operation</title>
</head>
<body>
                
<!-- The menu --> 
<%@ include file="menu.jsp" %>

<div id="corps">
    <h1></h1>
    <div class="interieur_corps">

<p>The operation you were performing has been a success.</p>

<%  String success = (String)request.getServletContext().getAttribute("success");

	if(success!=null)
	{
	  out.print("<strong>"+success+"</strong><br/><br/>");
	  request.getServletContext().setAttribute("success",null);
	} 
%>

    </div>
    <h4></h4>
</div>

</body>
</html>