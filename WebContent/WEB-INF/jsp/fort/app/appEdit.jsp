<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	$(document).ready(function() {
	});
	

	function form_cancel(){
		window.location = '${path}/fort/app/list?keep=1';
	}
	
	function form_save(){
		$.page.post({
			url : "${path}/fort/app/editInvoke",
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
				<h4><span class="glyphicon glyphicon-th"></span> 运维工具--修改</h4>
			</div>
			<div class="col-xs-9 text-right" style="padding-right: 0px;">
				<button type="button" class="btn btn-default btn_loading" onclick="form_cancel()"><span class="glyphicon glyphicon-circle-arrow-left"></span> 返回</button>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<form id="form1" method="post" class="form-horizontal">
							<input type="hidden" name="app.id" value="${app.id }"/>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>工具名称</label>
						    	<div class="col-xs-4"><input type="text" name="app.name_cn" placeholder="用于展示给用户" class="form-control" value="${app.name_cn }"/></div>
						    	<div class="col-xs-4"><label id="validate_app_name_cn" class="control-label validate_label"></label></div>
						  	</div>
<!-- 						  	<div class="form-group"> -->
<!-- 						    	<label class="control-label col-xs-2"><font color="red">*</font>快捷方式名称</label> -->
<%-- 						    	<div class="col-xs-4"><input type="text" name="app.link_name" placeholder="堡垒机上程序的快捷方式名称" class="form-control" value="${app.link_name }"/></div> --%>
<!-- 						    	<div class="col-xs-4"><label id="validate_link_name" class="control-label validate_label"></label></div> -->
<!-- 						  	</div> -->
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>命令行参数</label>
						    	<div class="col-xs-6"><input type="text" name="app.parameter" placeholder="命令行参数" class="form-control" value="${app.parameter }"/></div>
						    	<div class="col-xs-4"><label id="validate_parameter" class="control-label validate_label"></label></div>
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