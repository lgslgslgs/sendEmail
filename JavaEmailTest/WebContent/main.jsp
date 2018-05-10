<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
     <meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
	<title></title>
    <!-- 引入 Bootstrap -->
    <link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet"/>
    <script src="<%=request.getContextPath() %>/js/jquery-2.2.4.js"></script>
</head>
<body>
登录成功!<br>
用户名:${requestScope.user.username};<br>
密码:${requestScope.user.password};<br>
性别:${requestScope.user.sex==1?'男':'女'};<br>
<table>
	<tr>
		<th>班级id</th>
		<th>班级编号</th>
	</tr>
	<c:forEach items="${requestScope.cls}" var="cls">
		<tr>
			<td>${cls.cid}</td>
			<td><a href="javaScript:showClass(${cls.cid});">${cls.cname}</a></td>
		</tr>
	</c:forEach>
</table>
<table id="student">
	<tr>
		<th>学生id</th>
		<th>学生姓名</th>
		<th>学生班级</th>
	</tr>
</table>
<script type="text/javascript">
	//点击事件触发时,将对应cid传入,发送异步请求
	function showClass(obj){

		//清除之前表中查询出来的信息
		$("#student>tbody>tr").remove();
		$.post('<%=request.getContextPath() %>/user/queryStudent',{cid:obj},function(data){
			for(var i=0;i<data.length;i++){
				$('#student>tbody').append("<tr>"+
													"<td>"+data[i].sid+"</td>"+
													"<td>"+data[i].sname+"</td>"+
													"<td>"+data[i].cname+"</td>"+
												"</tr>");
			}
		},"json");
	}
	
	
	

	//当点击班级时,发送请求,获取班级中的学生信息
	function loadStudentByClass(cid){
		//异步加载学生信息
		$.post('<%=request.getContextPath() %>/student/loadStudentByClass',{cid:cid},function(data){
			//遍历data数组,取出数据
			for(var i=0;i<data.length;i++){
				$('#student>tbody').append("<tr>"
						+"<td>"+data[i].s_name+"</td>"
						+"<td>"+(data[i].s_sex==1?'男':'女')+"</td>"
						+"<td>"+data[i].s_birth+"</td>"
					+"</tr>");
			}
		});
	}
	
	
	$(function(){
		$.post('<%=request.getContextPath() %>/class/loadAllClass',function(data){
			console.log(data.length);
			for(var i=0;i<data.length;i++){
				$('#class>tbody').append("<tr>"
						+"<td>"+data[i].c_name+"</td>"
						+"<td>"+data[i].c_indate+"</td>"
					+"</tr>");
			}
		},'json');
	})
</script>
</body>
</html>