<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<html>
<head>
	<title>拒绝访问</title>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp" %>
	<style type="text/css">
		body { padding-top: 180px;}
	</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-xs-4"></div>
			<div class="col-xs-4">
				<div class="panel panel-default">
					<div class="panel-body" style="background-color: #f5f5f5;">
						<h2>权限不足，拒绝访问</h2>
						<div style="text-align: right;">
							<input type="button" class="btn btn-primary" value="返回" onclick="history.go(-1)" />
							<input type="button" class="btn btn-primary" value="关闭" onclick="window.close()" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>