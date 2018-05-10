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
    		<div class="col-md-6 col-md-offset-3">
    			<div class="form-horizontal">
		    		<div class="form-group">
		    			<label class="col-md-2 control-label">消息头</label>
		    			<div class="col-md-7" style="padding-right: 0px;">
		    				<input class="form-control" type="text" name="title"/>
		    			</div>
		    		</div>
		    		<div class="form-group">
		    			<label class="col-md-2 control-label">消息体</label>
		    			<div class="col-md-7" style="padding-right: 0px;">
		    				<input class="form-control" type="text" name="container"/>
		    			</div>
		    		</div>
		    		<div class="form-group">
		    			<label class="col-md-2 control-label">邮箱</label>
		    			<div class="col-md-7" style="padding-right: 0px;">
		    				<input class="form-control" type="email" name="email" placeholder="请输入邮箱"/>
		    			</div>
		    			<div class="col-md-2" style="padding-left: 0px;">
		    				<input id="sendCode" type="button" class="btn btn-default" value="发送信息"/>
		    			</div>
		    		</div>
		    	</div>
    		</div>
    	</div>
    	
    	
    	<script type="text/javascript">
    		$('#sendCode').click(function(){
    			var title=$('input[name="title"]').val();
    			var container=$('input[name="container"]').val();
    			var email=$('input[name="email"]').val();
    			$.post('sendCode',{title:title,container:container,email:email},function(data){
    				
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
    	</script>
 	</body>
</html>