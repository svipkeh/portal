<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<div class="modal fade" id="modal_dialog_main" tabindex="-1" role="dialog" aria-labelledby="modal_dialog_label" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
	  		<div class="modal-header">
	    		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	    		<h4 class="modal-title" id="modal_dialog_label"></h4>
	  		</div>
	  		<div class="modal-body" style="padding-bottom: 0px;">
	  			<iframe id="modal_dialog_iframe" name="modal_dialog_iframe" style="height: 462px; width: 100%;" frameborder="0" scrolling="no"></iframe>
	  		</div>
	  		<div class="modal-footer">
	    		<button type="button" class="btn btn-default" id="btn_modal_dialog_ok"></button>
	    		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	  		</div>
		</div>
	</div>
</div>