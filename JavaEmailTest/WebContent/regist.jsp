<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
		<title></title>
	    <!-- 引入 Bootstrap -->
	    <link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet"/>
	    <script src="<%=request.getContextPath() %>/js/jquery-2.2.4.js"></script>
    </head>
    <style>
    	
    </style>
    <body>
    	<div class="container">
	    	<form class="layui-form" action="<%=request.getContextPath() %>/user/regist" method="post">
	    		<div class="col-md-6 col-md-offset-3">
	    			<div class="form-horizontal">
		    			<div class="form-group">
			    			<label class="col-md-2 control-label">用户名</label>
			    			<div class="col-md-10">
			    				<input class="form-control" required type="text" name="username" placeholder="请输入用户名" />
			    			</div>
			    		</div>
			    		<div class="form-group">
			    			<label class="col-md-2 control-label">密码</label>
			    			<div class="col-md-10">
			    				<input class="form-control" required type="password" name="password" placeholder="请输入密码" />
			    			</div>
			    		</div>
			    		<div class="form-group">
			    			<label class="col-md-2 control-label">确认密码</label>
			    			<div class="col-md-10">
			    				<input class="form-control" required type="password" name="re_password" placeholder="请再次输入密码" />
			    			</div>
			    		</div>
			    		<div class="form-group">
			    			<label class="col-md-2 control-label">邮箱</label>
			    			<div class="col-md-7" style="padding-right: 0px;">
			    				<input class="form-control" required type="text" name="email" placeholder="请输入邮箱"/>
			    			</div>
			    			<div class="col-md-2" style="padding-left: 0px;">
			    				<input id="sendCode"  type="button" class="btn btn-default" value="发送验证码"/>
			    			</div>
			    		</div>
			    		<div class="form-group">
			    			<label class="col-md-2 control-label">验证码</label>
			    			<div class="col-md-4">
			    				<input class="form-control" required type="text" name="code" placeholder="请输入验证码" />
			    			</div>
			    		</div>
			    		<div class="form-group">
			    				<div class="col-md-10 col-md-offset-1">
			    					<input type="submit" id="regist" class="btn btn-danger form-control" value="注册"/>
			    				</div>
			    		</div>
			    		<div class="form-group">
			    				<label id="error" class="col-md-offset-5 control-label" style="color: red; text-align: center;">
			    				${requestScope.error}
			    				</label>
			    		</div>
			    	</div>
	    		</div>
    		</form>
    	</div>
    	
    	
    	<script type="text/javascript">
    		$('#sendCode').click(function(){
    			setTime();
    			var email=$('input[name="email"]').val();
    			$.post('<%=request.getContextPath()%>/user/sendCode',{email:email},function(data){
    				console.log($('#error').text(data));
    			},'text');
    		});
    		
    		//倒计时
    		function setTime(){
    			$('#sendCode').prop('disabled',true);
    			var time=10;
    			$('#sendCode').val((time--)+"秒后重新获取");
    			var id=setInterval(function(){
    				$('#sendCode').val((time--)+"秒后重新获取");
    				if(time==0){
    					clearInterval(id);
    					$('#sendCode').val("获取验证码");
    					$('#sendCode').prop('disabled',false);
    				}
    			},1000);
    		}
    		
    		//点击注册,发送请求
    		$('#regist').click(function(){
       			var username=$('input[name="username"]').val();
       			var password=$('input[name="password"]').val();
       			var re_password=$('input[name="re_password"]').val();
       			var email=$('input[name="email"]').val();
       			var code=$('input[name="code"]').val();
       			$.post('<%=request.getContextPath()%>/user/regist',{username:username,password:password,re_password:re_password,email:email,code:code},function(data){
       				$('#error').innerText=data;
       			},'text');
    		});
    		
    	</script>
 	</body>
</html>