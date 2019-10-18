<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	var tree;
	$(document).ready(function() {
		tree = $("#menu_tree").ui_tree({
			data : '${allMenu}',
			selectedValue : '${menu.pid}',
			onclick : function(node){
				$("#pid").val(node.id);
			}
		});
	});

	function form_cancel(){
		window.location = '${path}/sys/priv/menu/list?keep=1';
	}
	
	function form_save(){
		$.page.post({
			url : "${path}/sys/priv/menu/updateInvoke",
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
				<h4><span class="glyphicon glyphicon-th"></span> 菜单--修改</h4>
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
							<input type="hidden" name="menu.id" value="${menu.id }"/>
							<div class="form-group">
								<label class="control-label col-xs-2"><font color="red">*</font>上级菜单</label>
						    	<div class="col-xs-4">
						    		<div class="panel panel-default">
										<div class="panel-heading">
											<span class="glyphicon glyphicon-signal"></span> 菜单
											<div style="float: right;">
												<a href="#" onclick="tree.expandAll(true)" title="展开"><span class="glyphicon glyphicon-plus"></span></a>
												<a href="#" onclick="tree.expandAll(false)" title="收缩"><span class="glyphicon glyphicon-minus"></span></a>
											</div>
										</div>
										<div class="panel-body" style="max-height:250px; overflow: auto; padding: 0px;">
											<ul id="menu_tree" class="ztree"></ul>
										</div>
									</div>
						    	</div>
						    	<input type="hidden" id="pid" name="menu.pid" value="${menu.pid }"></input>
						  	</div>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>菜单名称</label>
						    	<div class="col-xs-4"><input type="text" name="menu.name" placeholder="菜单名称" class="form-control" value="${menu.name }"/></div>
						    	<div class="col-xs-4"><label id="validate_menu_name" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						  		<label class="control-label col-xs-2">URL</label>
						    	<div class="col-xs-4"><input type="text" name="menu.url" placeholder="菜单的URL地址" class="form-control" value="${menu.url }"/></div>
						    	<div class="col-xs-4"><label id="validate_menu_url" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						  		<label class="control-label col-xs-2"><font color="red">*</font>排序</label>
						    	<div class="col-xs-2"><input type="text" name="menu.index" placeholder="排序，如：1,2,3" class="form-control" value="${menu.index }"/></div>
						    	<div class="col-xs-4"><label id="validate_menu_index" class="control-label validate_label"></label></div>
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