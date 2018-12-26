/*$(document).ready(TB_launch); 

function TB_launch() {
$("a.thickbox").click(function(){
  var t = this.title;
  TB_show(t,this.href);
  this.blur();
  return false;
});
}
*/
/**
 * include css file
 */
document.write('<link rel="stylesheet" type="text/css" href="' + alyCms.base.getRootPath() + '/assets/cms/css/common/thickbox.css"/>');

function TB_show(paramObj) { //function called when the user clicks on a thickbox link
	try {
		url = paramObj['url'];
		caption = paramObj['title'];
		if($("#TB_overlay").length == 0)
		{
			$("body").append("<div id='TB_overlay'></div><div id='TB_window'></div>");
			$("#TB_overlay").css("opacity","0.6");
			$("#TB_overlay").css("filter","alpha(opacity=60)");
			$("#TB_overlay").css("-moz-opacity","0.6");
			$("#TB_overlay").css("width","100%");
			$("#TB_overlay").css("height","100%");
		}
		$(window).resize(TB_position);
		if($("#TB_load").length == 0)
		{
			$("body").append("<div id='TB_load'><div id='TB_loadContent'><img src='" + alyCms.base.getRootPath() + "/assets/cms/images/common/loading_1.gif' style='width:200px;height:200px' /></div></div>");
		}
		$("#TB_load").css("z-index",getThickMaxZIndex() + 1);
		$("#TB_overlay").fadeIn(400);
		var urlString = /.jpg|.jpeg|.png|.gif|.html|.htm|.jhtml/g;
		var urlType = url.match(urlString);
		urlType = urlType[0];
		// 监听
		$(document).keyup(function(event){
			if (event.keyCode == 27 && $('#TB_window:visible').length > 0) TB_remove();
		});
		
		if(urlType == '.jpg' || urlType == '.jpeg' || urlType == '.png' || urlType == '.gif'){//code to show images

			var imgPreloader = new Image();
			imgPreloader.onload = function(){

			// Resizing large images added by Christian Montoya
			var de = document.documentElement;
			var x = (self.innerWidth || (de&&de.clientWidth) || document.body.clientWidth) - 50;
			var y = (self.innerHeight || (de&&de.clientHeight) || document.body.clientHeight) - 80;
			if(imgPreloader.width > x) { 
				imgPreloader.height = imgPreloader.height * (x/imgPreloader.width); 
				imgPreloader.width = x; 
				if(imgPreloader.height > y) { 
					imgPreloader.width = imgPreloader.width * (y/imgPreloader.height); 
					imgPreloader.height = y; 
				}
			} 
			else if(imgPreloader.height > y) { 
				imgPreloader.width = imgPreloader.width * (y/imgPreloader.height); 
				imgPreloader.height = y; 
				if(imgPreloader.width > x) { 
					imgPreloader.height = imgPreloader.height * (x/imgPreloader.width); 
					imgPreloader.width = x;
				}
			}
			// End Resizing

			TB_WIDTH = imgPreloader.width + 30;
			TB_HEIGHT = imgPreloader.height + 60;
			$("#TB_window").html("").append("<div id='TB_closeAjaxWindow' onmouseover='moveByMouse();'><div style='float:left;font-size:14px;color:#ff6633;'>&nbsp;&nbsp;<span style=''><b>"+caption+"</b></span></div><div class='box-colse'><a href='#' id='TB_closeWindowButton'>×</a></div></div>");
			$("#TB_window").append("<div><img id='TB_Image' src='"+url+"' width='"+(imgPreloader.width+20)+"' height='"+imgPreloader.height+"' alt='"+caption+"'/></div>"); 
			$("#TB_closeWindowButton").click(TB_remove);
			//$("#TB_Image").click(TB_remove); // close when image clicked added by Christian Montoya
			TB_position();
			$("#TB_load").remove();
			
			var top = $("#TB_window").css("top").replace("px","") * 1;
			var height = $("#TB_window").css("height").replace("px","") * 1 + 100;
			
			$("#TB_window").css("top","-" + height + "px").show();
			$("#TB_window").animate({top:top},400);
			
			$("#TB_window").css("z-index",getThickMaxZIndex() + 1);
			}
			imgPreloader.src = url;
		}
		
		if(urlType == '.htm' || urlType == '.jhtml'){//code to show html pages
			
			//var queryString = url.replace(/^[^\?]+\??/,'');
			//var params = parseQuery( queryString );
			
			TB_WIDTH = (paramObj['width']*1) + 30;
			TB_HEIGHT = (paramObj['height']*1) + 40;
			ajaxContentW = TB_WIDTH;
			ajaxContentH = TB_HEIGHT - 45;
			$("#TB_window").html("").append("<div id='TB_closeAjaxWindow' onmouseover='moveByMouse();'><div class='box-title'>"+caption+"</div><div class='box-colse'><a href='#' id='TB_closeWindowButton'>×</a></div></div><div id='TB_ajaxContent' style='width:"+ajaxContentW+"px;height:"+ajaxContentH+"px;'><input type='hidden' id='doAfterLoginText' value='1'></div>");
			$("#TB_closeWindowButton").click(TB_remove);
			if(url.indexOf("?") > 0)
			{
				url += "&random=" + new Date().getTime();
			}
			else
			{
				url += "?random=" + new Date().getTime();
			}
			$("#TB_overlay").css("height",$(document).height()+"px");
			$("#TB_ajaxContent").load(url, function(){
				TB_position();
				$("#TB_load").remove();
				if($('[data-toggle="tooltip"]').length > 0)
				{
					$('[data-toggle="tooltip"]').tooltip();
				}
//				$("#TB_ajaxContent")
				$("#TB_window").css("z-index",getThickMaxZIndex() + 1);
				
				var top = $("#TB_window").css("top").replace("px","") * 1;
				var height = $("#TB_window").css("height").replace("px","") * 1 + 100;
				var windowHeight = $(window).height();
				if(height > windowHeight)
				{
					$("#TB_window").css("height",windowHeight - 100 + "px");
					$("#TB_ajaxContent").css("height",windowHeight - 150 + "px");
				}
				$("#TB_window").css("top","-" + height + "px").show();
				$("#TB_window").animate({top:top},400);
			});
		}
		 if(urlType == '.html'){//code to show html pages
			TB_WIDTH = (paramObj['width']*1) + 30;
			TB_HEIGHT = (paramObj['height']*1) + 40;
			ajaxContentW = TB_WIDTH;
			ajaxContentH = TB_HEIGHT - 45;
			$("#TB_window").html("").append("<div id='TB_closeAjaxWindow' onmouseover='moveByMouse();'><div class='box-title'>"+caption+"</div><div class='box-colse'><a href='#' id='TB_closeWindowButton'>×</a></div></div><div id='TB_ajaxContent' style='width:"+ajaxContentW+"px;height:"+ajaxContentH+"px;'><input type='hidden' id='doAfterLoginText' value='1'></div>");
			$("#TB_closeWindowButton").click(TB_remove);
			$("#TB_overlay").css("height",$(document).height()+"px");
			
			TB_position();
			$("#TB_load").remove();
			$("#TB_window").css("z-index",getThickMaxZIndex() + 1);
			
			var top = $("#TB_window").css("top").replace("px","") * 1;
			var height = $("#TB_window").css("height").replace("px","") * 1 + 100;
			
			$("#TB_window").css("top","-" + height + "px").show();
			$("#TB_window").animate({top:top},400);
			
			
			$("#TB_ajaxContent").append("<iframe src='"+url+"' id='openIframeUrl'></iframe>");
		}
	} catch(e) {
		alert( e );
	}
}

//helper functions below

function TB_remove() {
	// #TB_load removal added by Christian Montoya; solves bug when overlay is closed before image loads
	$("#TB_overlay").fadeOut(400);
	$("#TB_window").fadeOut(400,function(){$('#TB_window,#TB_load,#TB_overlay').remove();});
	if(removeAllErrorMessage)removeAllErrorMessage();
	if(removeAllRightMessage)removeAllRightMessage();
	return false;
}

function TB_position() {
	var de = document.documentElement;
	var w = self.innerWidth || (de&&de.clientWidth) || document.body.clientWidth;
	var h = self.innerHeight || (de&&de.clientHeight) || document.body.clientHeight;
  
  	if (window.innerHeight && window.scrollMaxY) {	
		yScroll = window.innerHeight + window.scrollMaxY;
	} else if (document.body.scrollHeight > document.body.offsetHeight){ // all but Explorer Mac
		yScroll = document.body.scrollHeight;
	} else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla and Safari
		yScroll = document.body.offsetHeight;
  	}
	var windowTop = (h - TB_HEIGHT)/2;
	if(windowTop < 50)
	{
		windowTop = 50;
	}
	if(self != top)
	{
		windowTop = 80;
	}
	$("#TB_window").css({width:TB_WIDTH+"px",height:TB_HEIGHT+"px",
	left: ((w - TB_WIDTH)/2)+"px", top: windowTop+"px" });
	$("#TB_overlay").css("height",$(document).height()+"px");
}

function parseQuery ( query ) {
   var Params = new Object ();
   if ( ! query ) return Params; // return empty object
   var Pairs = query.split(/[;&]/);
   for ( var i = 0; i < Pairs.length; i++ ) {
      var KeyVal = Pairs[i].split('=');
      if ( ! KeyVal || KeyVal.length != 2 ) continue;
      var key = unescape( KeyVal[0] );
      var val = unescape( KeyVal[1] );
      val = val.replace(/\+/g, ' ');
      Params[key] = val;
   }
   return Params;
}
function moveByMouse()
{
	_alyCms_support_drag_("TB_window");
	document.getElementById("TB_closeAjaxWindow").style.cursor="move";
}
function moveByMousePic()
{
	_alyCms_support_drag_("TB_window");
}
//鼠标拖动
var _alyCms_support_drag_isDrag_Flag_ = false;
function _alyCms_support_drag_(elementId)
{
	var _alyCms_support__drag_Event_ = new Function('e',
			'if (!e) e = window.event;return e');
	var x , y;
	document.getElementById("TB_closeAjaxWindow").onmousedown = function(e)
	{
		_alyCms_support_drag_isDrag_Flag_ = true;
		with (document.getElementById("TB_window"))
		{
			style.position = "fixed";
			var temp1 = offsetLeft;
			var temp2 = offsetTop;
			x = _alyCms_support__drag_Event_(e).clientX;
			y = _alyCms_support__drag_Event_(e).clientY;
			document.onmousemove = function(e)
			{
				if(!_alyCms_support_drag_isDrag_Flag_)
				{
					return false;
				}
				with (this)
				{
					style.left = temp1 + _alyCms_support__drag_Event_(e).clientX - x
							+ "px";
					style.top = temp2 + _alyCms_support__drag_Event_(e).clientY - y
							+ "px";
				}
			};
		}
		document.onmouseup = function()
		{
			if(document.getElementById("TB_window") != undefined)
			{
				if(document.getElementById("TB_window").style.left.replace("px","") < 0)
				{
					document.getElementById("TB_window").style.left = "0px";
				}
				if(document.getElementById("TB_window").style.top.replace("px","") < 0)
				{
					document.getElementById("TB_window").style.top = "0px";
				}
				_alyCms_support_drag_isDrag_Flag_ = false;
			}
		};
	};
}
function getTop(e) {
	var offset = e.offsetTop;
	if (e.offsetParent != null)
		offset += getTop(e.offsetParent);
	return offset;
}
function getLeft(e) {
	var offset = e.offsetLeft;
	if (e.offsetParent != null)
		offset += getLeft(e.offsetParent);
	return offset;
}
function getThickMaxZIndex()
{
	var maxZ = Math.max.apply(null, $.map($('body > *'), function(e, n) {
		if ($(e).css('position') == 'absolute')
			return parseInt($(e).css('z-index')) || 1;
	}));
	return maxZ;
}