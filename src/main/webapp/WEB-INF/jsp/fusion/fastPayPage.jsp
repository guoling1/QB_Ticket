<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>快捷支付</title>
<script type="text/javascript">
</script>
<style type="text/css">
legend {
	font-size: 12px;
}

table {
	font-size: 12px;
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
		<legend>快捷支付</legend>
		<form name="xxx" action="/authenPage/fastPayVerifyCode" method="post">
			<table align="center" width="80%">
				<tr>
					<th>商户号：</th>
					<td><input type="text" name="mercId" /></td>
					<th>手机号：</th>
					<td><input type="text" name="phoneNo" /></td>
					<th>卡号：</th>
					<td><input type="text" name="crdNo" /></td>
					<th>证件类型:</th>
					<td><select name="idTyp">
							<option value="00">身份证</option>
							<option value="1">护照</option>
					</select></td>
				</tr>
				<tr>
					<th>证件号：</th>
					<td><input type="text" name="idNo" /></td>
					<th>资金卡户名：</th>
					<td><input type="text" name="capCrdNm" /></td>
					<th>金额：</th>
					<td><input type="text" name="amount" /></td>
				</tr>
				<tr>
					<td colspan="4" align="center" height="50px"><input
						type="hidden" name="busCnl" value="WWW" />
						&nbsp;&nbsp;&nbsp;&nbsp; <input type="submit" value="提交">
						&nbsp;&nbsp;&nbsp;&nbsp; <input type="button" value="返回"
						onclick="javascript:history.go(-1);"></td>
				</tr>
			</table>
		</form>
	</fieldset>
</body>
</html>