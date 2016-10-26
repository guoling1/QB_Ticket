<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>快捷支付</title>
<style type="text/css">
legend{
	font-size: 12px;
}
table{
	font-size: 12px;
}
</style>
</head>
<body>
<%String retCode = (String)request.getAttribute("retCode"); %>
<%String retMsg= (String)request.getAttribute("retMsg"); %>
<%String retXml = (String)request.getAttribute("retXml"); %>
	<span>结果：</span><span><%=retCode %></span><br/>
	<span>返回码：</span><span><%=retMsg %></span><br/>
	<span>返回xml:</span><span><%=retXml %></span>
</body>
</html>