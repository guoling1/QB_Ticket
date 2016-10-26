<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交易处理结果</title>
<style type="text/css">
a{
	font-size: 12px;
}
legend{
	font-size: 12px;
}
span{
	font-size: 12px;
	padding-left: 15px;
}
p{
	line-height: 30px;
	font-size: 12px;
	padding-left: 50px;
}
.headerTxt{
	padding-left: 50px;
	text-align: left;
	font-size: 18px;
	border-bottom: dotted;
}
</style>
</head>
<body>
<table height="100px" width="100%">
	<tr>
		<th class="headerTxt">合众支付系统交易模拟测试端</th>
	</tr>
</table>
<fieldset>
	<legend>交易处理结果</legend>
	<br/>
	<p>
	<b>返回码：</b>${retCode} <br/>
	<b>返回信息：</b><I>${retMsg}</I><br/>
	<b>验签信息：</b>${signMsg} <br/>
	<b>返回XML：</b><I>${retXml}</I>
	</p>
	<br/>
	<br/>
	<br/>
</fieldset>
</body>
</html>