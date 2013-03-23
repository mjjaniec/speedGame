<%@ page language="java"%>
<html>
<head>
    <title>Add number program in JSP</title>
</head>

<body>
<%
    out.print(request.getParameter("time"));
    int a=5;
    int b=2;

    int result=a+b;

    out.print("Additon of a and b :"+ System.getProperties());

%>
</body>
</html>