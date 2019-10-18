<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	function form_cancel(){
		history.go(-1);
	}

	function form_save(){
		$.page.post({
			url : "${path}/bin/weihuInvoke",
			data :  $("#form1").serialize(),
			callback: function(data){
				$.dialog.info("保存成功");
				setInterval(form_cancel, 500);
			}
		});
	}
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
	<div class="container-fluid">
		<div class="row page-header">
			<div class="col-xs-3">
				<h4><span class="glyphicon glyphicon-th"></span> 自维护</h4>
			</div>
			<div class="col-xs-9" style="text-align: right; padding-right: 0px;">
				<button type="button" class="btn btn-default btn_loading" onclick="form_cancel()"><span class="glyphicon glyphicon-circle-arrow-left"></span> 返回</button>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<form id="form1" method="post" class="form-horizontal">
							<input type="hidden" name="user.id" value="${user.id }"/>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2">登录名</label>
						    	<div class="col-xs-3"><input type="text" name="user.name" class="form-control" value="${user.name }" readonly="readonly"/></div>
						  	</div>
						  	<div class="form-group">
						  		<label class="control-label col-xs-2"><font color="red">*</font> 原密码 </label>
						    	<div class="col-xs-2"><input type="password" name="password_old" placeholder="当前密码" class="form-control"/></div>
						    	<div class="col-xs-4"><label id="validate_password_old" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						  		<label class="control-label col-xs-2"><font color="red">*</font> 新密码</label>
						    	<div class="col-xs-2"><input type="password" name="user.password" placeholder="新密码" class="form-control"/></div>
						    	<div class="col-xs-4"><label id="validate_user_password" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						  		<label class="control-label col-xs-2"><font color="red">*</font> 确认密码</label>
						    	<div class="col-xs-2"><input type="password" name="password_repeat" placeholder="确认新密码" class="form-control"/></div>
						    	<div class="col-xs-4"><label id="validate_password_repeat" class="control-label validate_label"></label></div>
						  	</div>
			  				<button type="button" class="btn btn-default btn_loading" onclick="form_save();"><span class="glyphicon glyphicon-ok"></span> 保存</button>
			  				<span class="img_loading" style="display: none;"><img src="${static_path}/bin/img/loading.gif" /></span>
			  				<button type="button" class="btn btn-default btn_loading" onclick="form_cancel();"><span class="glyphicon glyphicon-remove"></span> 取消</button>
			  			</form>
		  			</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>