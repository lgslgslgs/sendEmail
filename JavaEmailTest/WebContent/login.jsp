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
	    	<form class="layui-form" action="<%=request.getContextPath() %>/user/login" method="post">
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
			    				<div class="col-md-10 col-md-offset-1">
			    					<input type="submit" id="regist" class="btn btn-danger form-control" value="登录"/>
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
    	</script>
 	</body>
</html>