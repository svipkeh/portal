<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="common.jfinal.WebRootConfig"%>
<%
    request.setAttribute( "path", request.getContextPath() );
    request.setAttribute( "static_path", request.getContextPath() + WebRootConfig.constant.get( "static.server.location.prefix" ) );
%>