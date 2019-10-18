<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	var tree;
	$(document).ready(function() {
		init_function_tree();
	});
	
	function init_function_tree(){
		tree = $("#function_tree").ui_tree({
			url : '${path}/sys/priv/role/functionTreeInvoke',
			checkbox : true
		});
		var checkedNodes = eval('${privileges}');
		if(checkedNodes){
			for(var j=0; j<checkedNodes.length; j++){
				var temp = checkedNodes[j];
				var matcheds = tree.getNodesByParam("id", temp.res_id, null);
				for(var i=0; i<matcheds.length; i++){
					var currentNode = matcheds[i];
					if(currentNode.type == temp.res_type){
						tree.checkNode(currentNode, true, false);
						break;
					}
				}
			}
		}
	}

	function form_cancel(){
		window.location = '${path}/sys/priv/role/list?keep=1';
	}

	function get_checked_nodes(){
		var ckd_nodes = [];
		var nodes = tree.getCheckedNodes();
		for(var i=0; i<nodes.length; i++){
			ckd_nodes.push([nodes[i].id, nodes[i].type, nodes[i].name]);
		}
		return ckd_nodes;
	}
	
	function form_save(){
		$.page.post({
			url : "${path}/sys/priv/role/updateInvoke",
			data :  $("#form1").serialize() + "&selected_nodes="+$.toJSON(get_checked_nodes()),
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
				<h4><span class="glyphicon glyphicon-th"></span> 角色--修改</h4>
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
							<input type="hidden" name="role.id" value="${role.id }"/>
							<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>角色名称</label>
						    	<div class="col-xs-4"><input type="text" name="role.name" placeholder="角色名称" class="form-control" value="${role.name }"/></div>
						    	<div class="col-xs-4"><label id="validate_role_name" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>权限 </label>
						    	<div class="col-xs-4">
						    		<div class="panel panel-default">
										<div class="panel-heading">
											<span class="glyphicon glyphicon-signal"></span> 菜单和功能
											<div style="float: right;">
												<a href="#" onclick="tree.expandAll(true)" title="展开"><span class="glyphicon glyphicon-plus"></span></a>
												<a href="#" onclick="tree.expandAll(false)" title="收缩"><span class="glyphicon glyphicon-minus"></span></a>
											</div>
										</div>
										<div class="panel-body" style="max-height:250px; overflow: auto; padding: 0px;">
											<ul id="function_tree" class="ztree"></ul>
										</div>
									</div>
						    	</div>
						    	<div class="col-xs-4"><label id="validate_role_priv" class="control-label validate_label"></label></div>
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