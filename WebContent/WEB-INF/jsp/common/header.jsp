<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<div style="height: 13px; background-color: #2A3F54;"></div>
<nav class="navbar navbar-default">
	<div class="container-fluid">
	    <div class="navbar-header">
	    	<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-main">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
	   		<a href="${path }/bin" class="navbar-brand">
	   			<img alt="" src="${static_path }/bin/img/logo-006.png" style="width: 270px; height: 30px; position: relative; top: -10px;"></img>
	   			<h4 style="color: white; font-weight: bold; position: absolute; top: -0px; left: 132px; ">集中安全运维管理系统</h4>
	   		</a>
	    </div>
	    <div class="navbar-collapse collapse" id="navbar-main">
	      	<ul class="nav navbar-nav" style="font-size: 14px; font-weight: normal;">
	        	${SESSEION_USER_MENU }
			</ul>
			<ul class="nav navbar-nav navbar-right">
	        	<li class="dropdown">
	        		<a class="dropdown-toggle" data-toggle="dropdown" href="#">${SESSEION_USER_NAME_CN } <span class="caret"></span></a>
	          		<ul class="dropdown-menu">
	            		<li><a href="${path}/bin/weihu">自维护</a></li>
	            		<li><a href="${path}/bin/logout">退出系统</a></li>
	         		</ul>
	         	</li>
	      	</ul>
	    </div>
	</div>
</nav>