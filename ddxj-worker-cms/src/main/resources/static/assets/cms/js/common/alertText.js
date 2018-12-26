/**
 * include css file
 */
document.write('<link rel="stylesheet" type="text/css" href="' + alyCms.base.getRootPath() + '/assets/cms/css/common/alertText.css"/>');
function hideTextFrame() {
	if (document.getElementById("checkBoxDiv")) {
		document.body.removeChild(document.getElementById("checkBoxDiv"));
	}
}
function alertText(top, left, text, width, height1, height2, checkDone,isCloseAfter) {
    if (document.getElementById("checkBoxDiv") != null) {
		hideTextFrame();
	}
	var html = "<div style='width:" + (width - 10) + "px; height:" + height2 + "px;' class='smallBlock1'>";
	html += text;
	html += "</div>";
	html += "<center>";
	html += "<div style='height:20px; width: " + (width - 10) + "px;' class='smallBlock2'>";
	if(isCloseAfter == false)
	{
		html += "<input type='button' value='\u786e\u5b9a' class='checkButton' onclick='" + checkDone + ";'>&nbsp;&nbsp;";
	}
	else
	{
		html += "<input type='button' value='\u786e\u5b9a' class='checkButton' onclick='" + checkDone + ";hideTextFrame();'>&nbsp;&nbsp;";
	}
	html += "<input type='button' value='\u53d6\u6d88' class='checkButton' onclick='hideTextFrame()'>";
	html += "</div>";
	html += "</center>";
	var checkDiv = document.createElement("div");
	checkDiv.style.height = height1 + "px";
	checkDiv.style.width = width + "px";
	checkDiv.style.top = top + "px";
	checkDiv.style.left = left + "px";
	checkDiv.className = "bigBorder";
	checkDiv.id = "checkBoxDiv";
	document.body.appendChild(checkDiv);
	document.getElementById("checkBoxDiv").innerHTML = html;
}
function alertTip(str,type)
{
	if(type == 1)
	{
		if(typeof showSuccessDialog == "function")
		{
			showSuccessDialog(str);
		}
		else
		{
			doneTip(null,null,str,type,true);
		}
	}
	else if(type == 2)
	{
		if(typeof showWarningDialog == "function")
		{
			showWarningDialog(str);
		}
		else
		{
			doneTip(null,null,str,type,true);
		}
	}
	else if(type == 3)
	{
		if(typeof showErrorDialog == "function")
		{
			showErrorDialog(str);
		}
		else
		{
			doneTip(null,null,str,type,true);
		}
	}
	else
	{
		doneTip(null,null,str,type,true);
	}
}
function minAlert(str,type)
{
	doneTip(null,null,str,type,true);
}
function doneTip(top, left, text, type,isCenter) {
	if (document.getElementById("tipDiv") != null) {
		document.body.removeChild(document.getElementById("tipDiv"));
	}
	if (type == 1) {//操作成功
		var html = "<div class='alertTextCommonTipBg' style='width: 45px; height: 53px;  no-repeat; background-position: -5px 0px; float: left;'></div>";
	} else {
		if (type == 2) {//操作警告
			var html = "<div class='alertTextCommonTipBg' style='width: 45px; height: 53px;  no-repeat; background-position: -5px -54px; float: left;'></div>";
		} else {
			if (type == 3) {//错误警告
				var html = "<div class='alertTextCommonTipBg' style='width: 45px; height: 53px;  no-repeat; background-position: -5px -108px; float: left;'></div>";
			} else {
				if (type == 4) {//正在提交请求
					var html = "<div class='alertTextCommonTipBg' style='width: 10px; height: 53px;  no-repeat; background-position: -5px 0px; float: left;'></div>";
					html += "<div class='alertTextCommonTipBg' style='width: 40px; height: 53px;  repeat-x; background-position: 0px -161px; float: left;'>";
					html += "<img src='" + alyCms.base.getRootPath() + "/assets/cms/images/common/loading.gif' style='position:relative;top:9px;left:5px;'>";
					html += "</div>";
				}
			}
		}
	}
	html += "<div class='alertTextCommonTipBg' style='width: auto; height: 53px;  repeat-x; background-position: -5px -161px; float: left;'>";
	html += "<span style='font-size: 16px; font-weight: bold; line-height: 53px;'>&nbsp;" + text + "&nbsp;&nbsp;</span>";
	html += "</div>";
	html += "<div class='alertTextCommonTipBg' style='width: 5px; height: 53px;  repeat-x; background-position: 0px 0px; float: left;'>";
	html += "</div>";
	var tipDiv = document.createElement("div");
	tipDiv.style.position = "fixed";
	if(isCenter == true)
	{
		var de = document.documentElement;
		var w = self.innerWidth || (de&&de.clientWidth) || document.body.clientWidth;
		var h = self.innerHeight || (de&&de.clientHeight) || document.body.clientHeight;
		
		tipDiv.style.top = ((h - 50)/2) + "px";
		if(self != top)
		{
			tipDiv.style.top = "300px";
		}
		var tipTextWidth = text.length * 15 + 45;
		tipDiv.style.left = (w - tipTextWidth)/2 + "px";
	}
	else
	{
		tipDiv.style.top = top + "px";
		tipDiv.style.left = left + "px";
	}
	tipDiv.id = "tipDiv";
	tipDiv.className = "tip-message" + type;
	document.body.appendChild(tipDiv);
	$("#tipDiv").css("z-index",999);
	document.getElementById("tipDiv").innerHTML = html;
	if (type == 4) {
	} else {
		setTimeout(function(){
			document.body.removeChild(tipDiv);
		}, 4000);
	}
}
//显示动态信息
function createTipDiv(top, left, text) {
    
	var html = "<div class='alertTextCommonTipBg' style='width: 10px; height: 53px;  no-repeat; background-position: -5px 0px; float: left;'></div>";
	html += "<div class='alertTextCommonTipBg' style='width: 40px; height: 53px;  repeat-x; background-position: 0px -161px; float: left;'>";
	html += "<img <img src='" + alyCms.base.getRootPath() + "/assets/cms/images/common/loading.gif' style='position:relative;top:9px;left:5px;'>";
	html += "</div>";
	
	html += "<div class='alertTextCommonTipBg' style='width: auto; height: 53px;  repeat-x; background-position: -5px -161px; float: left;'>";
	html += "<span style='font-size: 16px; font-weight: bold; line-height: 53px;' id='tipMoveText'>&nbsp;" + text + "&nbsp;&nbsp;</span>";
	html += "</div>";
	html += "<div class='alertTextCommonTipBg' style='width: 5px; height: 53px;  repeat-x; background-position: 0px 0px; float: left;'>";
	html += "</div>";
	var tipDiv = document.createElement("div");
	tipDiv.style.position = "absolute";
	tipDiv.style.top = top + "px";
	tipDiv.style.left = left + "px";
	tipDiv.id = "tipDiv";
	document.body.appendChild(tipDiv);
	document.getElementById("tipDiv").innerHTML = html;
}
function removeTip()
{
	if (document.getElementById("tipDiv") != null) {
		document.body.removeChild(document.getElementById("tipDiv"));
	}
}
function _isTextInputForPopUpMessage_(domId)
{
	if($("#" + domId).get(0).tagName.toLowerCase() == "input")
	{
		if($("#" + domId).attr("type").toLowerCase() == "text" || $("#" + domId).attr("type").toLowerCase() == "password")
		{
			return true;
		}
	}
	return false;
}
function showRightMessage(domId,message)
{
	if(!domId)return;
	var isCompletedShow = $("#" + domId).attr("isCompletedShow") || true;
	if(!isCompletedShow)
	{
		setTimeout(function(){
			showRightMessage(domId,message);
		},100);
		return false;
	}
	$("#" + domId).attr("isCompletedShow",false);
	_createRightMessageContentDiv("right_" + domId);
	var $error = $("#right_" + domId);
	$error.find(".poup_rightMessage_Left").html('<div class="succ_msg_tips"></div>' + message);
	if($("#" + domId).parents("#TB_ajaxContent").length > 0)
	{
		$error.css("position","fixed");
	}
	else
	{
		$error.css("position","absolute");
	}
	var paddingTop = $("#" + domId).css("padding-top");
	if(_isTextInputForPopUpMessage_(domId))
	{
		if(paddingTop)
		{
			paddingTop = paddingTop.replace("px","");
		}
		else
		{
			paddingTop = 0;
		}
	}
	else
	{
		paddingTop = 0;
	}
	var top = _getTop4AlertText(domId) + ($("#" + domId).height() / 2) + parseInt(paddingTop) - 18 + $(window).scrollTop();
	var left = _getLeft4AlertText(domId) + $("#" + domId).width() + 20;
	$error.css("top",top + "px");
	$error.css("left",left + "px");
	$error.css("cursor","pointer");
	$error.css("width",(65 + message.length * 13) + "px");
	$error.attr("title","点击关闭");
	//设置最大的z-index
	$error.css("z-index",getMaxZIndex4ContentZoom()*1 + 10);
	$error.click(function(){
		removeRightMessage(domId);
	});
	$error.fadeIn(300,function(){
		$("#" + domId).attr("isCompletedShow",true);
		$error.css("display","inline");
		removeErrorMessage(domId);
	});
}
function showErrorMessage(domId,message)
{
	if(!domId)return;
	var isCompletedShow = $("#" + domId).attr("isCompletedShow") || true;
	if(!isCompletedShow)
	{
		setTimeout(function(){
			showErrorMessage(domId,message);
		},100);
		return false;
	}
	$("#" + domId).attr("isCompletedShow",false);
	_createErrorMessageContentDiv("error_" + domId);
	var $error = $("#error_" + domId);
	$error.find("span[errorText='true']").html(message);
	if($("#" + domId).parents("#TB_ajaxContent").length > 0)
	{
		$error.css("position","fixed");
	}
	else
	{
		$error.css("position","absolute");
	}
	var domType = $("#" + domId).attr("type");
	if(domType == "radio" || domType == "checkbox")
	{
		$error.css("top",(_getTop4AlertText(domId) - 40) + "px");
		$error.css("left",(_getLeft4AlertText(domId)) + "px");
	}
	else
	{
		$error.css("top",(_getTop4AlertText(domId) - 30) + "px");
		$error.css("left",(_getLeft4AlertText(domId) + 50) + "px");
	}
	$error.css("cursor","pointer");
	//设置最大的z-index
	$error.css("z-index",getMaxZIndex4ContentZoom()*1+1);
	$error.attr("title","点击关闭");
	$error.click(function(){
		removeErrorMessage(domId);
	});
	$error.fadeIn(300,function(){
		isCompletedShow = $("#" + domId).attr("isCompletedShow",true);
		removeRightMessage(domId);
		// add waring border
		if(_isTextInputForPopUpMessage_(domId))
		{
			$("#" + domId).addClass("error_input_border");
		}
	});
}
function removeErrorMessage(domId,flag)
{
	if($("#error_" + domId).length == 0)
	{
		return ;
	}
	if(flag == "hide")
	{
		$("#error_" + domId).hide();
	}
	else
	{
		$("#error_" + domId).fadeOut(400);
	}
	if(_isTextInputForPopUpMessage_(domId))
	{
		$("#" + domId).removeClass("error_input_border");
	}
}
function hideErrorMessage(domId)
{
	removeErrorMessage(domId,"hide");
}
function hideAllErrorMessage()
{
	removeAllErrorMessage("hide");
}
function removeAllErrorMessage(flag)
{
	$("div[divType='errorMessage']").each(function(){
		if(flag == "hide")
		{
			$(this).fadeOut(400);
		}
		else
		{
			$(this).hide();
		}
		if(_isTextInputForPopUpMessage_($(this).attr("id")))
		{
			$(this).removeClass("error_input_border");
		}
	});
}
function hideRightMessage(domId)
{
	removeRightMessage(domId,"hide");
}
function removeRightMessage(domId,flag)
{
	if(flag == "hide")
	{
		$("#right_" + domId).hide();
	}
	else
	{
		$("#right_" + domId).fadeOut(400);
	}
}
function hideAllRightMessage()
{
	removeAllRightMessage("hide");
}
function removeAllRightMessage(flag)
{
	if(flag == "hide")
	{
		$("div[divType='rightMessage']").hide();
	}
	else
	{
		$("div[divType='rightMessage']").fadeOut(400);
	}
}
function _getTop4AlertText(id)
{ 
	var e = document.getElementById(id);
	return _getTop4AlertTextByObject(e);
}
function _getTop4AlertTextByObject(e)
{
	if(e == null || typeof e == "undefined")
	{
		return 0;
	}
	var offset=e.offsetTop; 
	if(e.offsetParent!=null) offset+=_getTop4AlertTextByObject(e.offsetParent); 
	return offset; 
}
function _getLeft4AlertText(id)
{ 
	var e = document.getElementById(id);
	return _getLeft4AlertTextByObject(e);
}
function _getLeft4AlertTextByObject(e)
{
	if(e == null || typeof e == "undefined")
	{
		return 0;
	}
	var offset=e.offsetLeft; 
	if(e.offsetParent!=null) offset+=_getLeft4AlertTextByObject(e.offsetParent); 
	return offset; 
}
function _createRightMessageContentDiv(id)
{
	if($("#" + id).length == 0)
	{
		var html = '<div divType="rightMessage" id="' + id + '" class="alertText_Right_Msg_tips"><div class="poup_rightMessage_Left"></div><div class="poup_rightMessage_Right"></div></div>';
		$("body").append(html);
	}
}
function _createErrorMessageContentDiv(id)
{
	/*if($("#" + id).length == 0)
	{
		var html = '<div divType="errorMessage" style="z-index:1001;" id="' + id + '" class="none"><div class="poup_errorLeft"></div><div class="poup_errorRight"></div></div>';
		$("body").append(html);
	}*/
	if($("#" + id).length == 0)
	{
		var html = '<div divType="errorMessage" style="z-index:1001;" id="' + id + '" class="error_mgs_littlepop none">\
						<i class="error_mgs_empty"></i>\
						<span class="error_mgs_popmessage">\
							  <span class="error_mgs_icontwo"></span>\
							  <span errorText="true">&nbsp;</span>\
						</span>\
						<i class="error_mgs_little_corner"></i>\
					</div>';
		$("body").append(html);
	}
}
function getMaxZIndex4ContentZoom()
{
	var maxZ = Math.max.apply(null, $.map($('body > *'), function(e, n) {
		if ($(e).css('position') == 'absolute')
		{
			return parseInt($(e).css('z-index')) || 1;
		}
	}));
	return maxZ;
}