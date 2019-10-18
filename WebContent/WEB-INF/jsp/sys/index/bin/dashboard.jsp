<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp" %>
	<script type="text/javascript">
	
	$(document).ready(function() {
	});
	
	function f_run(appid, accid, resid){
		$.page.post({
			url : "${path}/bin/runInvoke",
			data :  "appid="+appid+"&accid="+accid+"&resid="+resid,
			callback: function(data){
				$("#app").get(0).src = "TRUSTMO://" + data.value;
			}
		});
	}
	</script>
</head>
<body>
	<iframe id="app" width="0" height="0" style="display:none;"></iframe>
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
	<div class="container-fluid">
		<div class="row page-header">
			<div class="col-xs-9">
				<h4><span class="glyphicon glyphicon-user"></span> 欢迎您：${SESSEION_USER_NAME_CN }</h4>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<c:forEach var="data" items="${ress }" varStatus="status">
					<div class="col-xs-2">
						<div class="panel panel-default">
							<div class="panel-body" style="padding-bottom: 10px;">
								<h4><span class="label label-success">${data.name_cn }</span></h4>
					        	<p>${data.ip }</p>
				        		<div class="btn-group" style="float: right;">
							    	<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">运行 <span class="caret"></span></button>
							    	<ul class="dropdown-menu <c:if test="${status.index%6 > 2 }">dropdown-menu-right</c:if>">
							      		<c:forEach var="app" items="${data.apps }">
							      			<c:forEach var="acc" items="${data.accs }">
								      			<li><a href="#" onclick="f_run('${app.id}','${acc.id}','${data.id}')">${app.name_cn }(${acc.name })</a></li>
								      		</c:forEach>
							      		</c:forEach>
							    	</ul>
							  	</div>
							</div>
						</div>
					</div>
				</c:forEach>
				<c:if test="${empty ress}">
					<div style="text-align: center; margin: 100px;">
						<h3>可运维资源列表为空</h3>
						<h5>（您未获得任何资源和账号的授权）</h5>
					</div>
				</c:if>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 text-right">
				<div class="text-right" style="border-top: solid 1px #ddd; padding-top: 10px;"></div>
				运维操作前请先安装  <a href="${path}/bin/clientDownload">运维客户端</a>
			</div>
		</div>
	</div>
</body>
</html>