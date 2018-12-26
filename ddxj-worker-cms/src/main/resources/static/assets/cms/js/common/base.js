alyCms.base = 
{
	GetXmlHttpObject : function()
	{
	  var xmlHttp=null;
	  try
	  {
	    // Firefox, Opera 8.0+, Safari
	    xmlHttp=new XMLHttpRequest();
	  }
	  catch (e)
	  {
	      // Internet Explorer
		  try
		  {
		      xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
		  }
		  catch (e)
		  {
		      xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
		  }
	   }
	  return xmlHttp;
	},
	getPageSize : function() 
	{
		var body = document.documentElement;
		var bodyOffsetWidth = 0;
		var bodyOffsetHeight = 0;
		var bodyScrollWidth = 0;
		var bodyScrollHeight = 0;
		var pageDimensions = [ 0, 0 ];
		pageDimensions[0] = body.clientHeight;
		pageDimensions[1] = body.clientWidth;
		bodyOffsetWidth = body.offsetWidth;
		bodyOffsetHeight = body.offsetHeight;
		bodyScrollWidth = body.scrollWidth;
		bodyScrollHeight = body.scrollHeight;
		if (bodyOffsetHeight > pageDimensions[0]) 
		{
			pageDimensions[0] = bodyOffsetHeight;
		}
		if (bodyOffsetWidth > pageDimensions[1]) 
		{
			pageDimensions[1] = bodyOffsetWidth;
		}
		if (bodyScrollHeight > pageDimensions[0]) 
		{
			pageDimensions[0] = bodyScrollHeight;
		}
		if (bodyScrollWidth > pageDimensions[1]) 
		{
			pageDimensions[1] = bodyScrollWidth;
		}
		var pageSize = {width:pageDimensions[1],height:pageDimensions[0]};
		var scrollBar = getScrollBarWidth();
		var scroll = isHaveScroll();
		if(scroll.scrollX)
		{
			pageSize.height = pageSize.height - scrollBar.x;
		}
		if(scroll.scrollY)
		{
			pageSize.width = pageSize.width - scrollBar.y;
		}
		return pageSize;
	},
	warn : function(id){
	   window.scrollTo(0,id.clientHeight);
	   document.getElementById(id).style.border="2px red solid";
	   document.getElementById(id).focus();
	   setTimeout("document.getElementById('"+id+"').style.border='1px #ffcc99 solid'",1000);
	},
	
	setText : function(id){
	   document.getElementById(id).style.border="1px #ffcc99 solid";
	},
	right : function(id, message) {
		document.getElementById(id).innerHTML = "<img src='images/right.png'>" + message;
	},
	wrong : function(id, message) {
		document.getElementById(id).innerHTML = "<img src='images/wrong.gif' style=width:15px;;height:15px;>" + message;
	},
	toUrl : function(url){
	  window.location.href = url;
	},
	//
	getTop : function(e){ 
	var offset=e.offsetTop; 
	if(e.offsetParent!=null) offset+=alyCms.base.getTop(e.offsetParent); 
	return offset; 
	}, 
	getLeft : function(e){ 
	var offset=e.offsetLeft; 
	if(e.offsetParent!=null) offset+=alyCms.base.getLeft(e.offsetParent); 
	return offset; 
	}, 
	//
	getScrollTop : function(){
	    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
	    return scrollTop;
	},
	getScrollLeft : function(){
	    var scrollLeft = document.documentElement.scrollLeft || document.body.scrollLeft;
	    return scrollLeft;
	},
	saveOrUpdateCookie : function(name,value,time){
	  name = encodeURIComponent(name);
	  var cookieEnabled=(typeof navigator.cookieEnabled!='undefined' && navigator.cookieEnabled)?true:false;
	  if(cookieEnabled==false){
	    alert('您的浏览器未开通cookie，执行失败！');
	    return false;
	  }
	  if(!time || isNaN(time))time=365;
	  var expires=new Date();
	  expires.setTime(expires.getTime()+time*24*60*60*1000);
	  var cookieStr=encodeURIComponent(value);
	  if(cookieStr.length>3072){/*?*/}
	  document.cookie=name+'='+cookieStr+';expires='+expires.toGMTString()+';path=/;';
	},
	
	queryCookie : function(name){
	  name = encodeURIComponent(name);
	  var arr=document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
	  if(arr!=null && arr!=false) return decodeURIComponent(arr[2]);
	  return false;
	},
	/**
	*	get the type of explorer
	*/
	getExplorerType : function()
	{
		var rMsie = /.*(msie) ([\w.]+).*/; // ie  
		var rFirefox = /.*(firefox)\/([\w.]+).*/; // firefox  
		var rOpera = /(opera).+version\/([\w.]+)/; // opera  
		var rChrome = /.*(chrome)\/([\w.]+).*/; // chrome  
		var rSafari = /.*version\/([\w.]+).*(safari).*/;// safari  
		var browserInfo = {};  
		
		var userAgent = navigator.userAgent.toLowerCase(); // userAgent
		var match = rMsie.exec(userAgent);
	    if (match != null) {  
	    	browserInfo = { browser : match[1] || "", version : match[2] || "0" };  
	    }  
	    var match = rFirefox.exec(userAgent);  
	    if (match != null) {  
	    	browserInfo = { browser : match[1] || "", version : match[2] || "0" };  
	    }  
	    var match = rOpera.exec(userAgent);  
	    if (match != null) {  
	    	browserInfo = { browser : match[1] || "", version : match[2] || "0" };  
	    }  
	    var match = rChrome.exec(userAgent);  
	    if (match != null) {  
	    	browserInfo = { browser : match[1] || "", version : match[2] || "0" };  
	    }  
	    var match = rSafari.exec(userAgent);  
	    if (match != null) {  
	    	browserInfo = { browser : match[2] || "", version : match[1] || "0" };  
	    }  
	    if (match != null) {  
	    	browserInfo = { browser : "", version : "0" };  
	    } 
	    
	    var browserObj = new Object();
	    if (browserInfo.browser) {  
	    	browserObj.name = browserInfo.browser;  
	    	browserObj.version = browserInfo.version;  
	    	browserObj.language = (navigator.language ? navigator.language  
	                : navigator.userLanguage || "");  
	    } 
	    return browserObj;
	},
	deleteCookie : function(NameOfCookie) {
		var exp = new Date();
	    exp.setTime(exp.getTime() - 1);
	    document.cookie = encodeURIComponent(NameOfCookie) + "=; expires=" + exp.toGMTString();
	},
	checkeURL : function(URL) {
		var str = URL;
		// 在JavaScript中，正则表达式只能使用"/"开头和结束，不能使用双引号
		// 判断URL地址的正则表达式为:http(s)?://([\w-]+\.)+[\w-]+(/[\w- ./?%&=]*)?
		// 下面的代码中应用了转义字符"\"输出一个字符"/"
		var Expression = /http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
		var objExp = new RegExp(Expression);
		return objExp.test(str);
	},
	getRootDomain : function(url) {
		try
		{
			var parse_url = /^(?:([A-Za-z]+):)?(\/{0,3})([0-9.\-A-Za-z]+)(?::(\d+))?(?:\/([^?#]*))?(?:\?([^#]*))?(?:#(.*))?$/;
			var result = parse_url.exec(url);
			return url.substring(0,url.indexOf(result[3])) + result[3]; //host
		}
		catch(e)
		{
			
		}
	},
	removeHTMLTag : function(str) {
	    str = str.replace(/<\/?[^>]*>/g,''); //去除HTML tag
	    str = str.replace(/[ | ]*\n/g,'\n'); //去除行尾空白
	    //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
	    str=str.replace(/&nbsp;/ig,'');//去掉&nbsp;
	    return str;
	},
	getMaxZIndex : function()
	{
		var maxZ = Math.max.apply(null, $.map($('body > *'), function(e, n) {
			if ($(e).css('position') == 'absolute')
			{
				return parseInt($(e).css('z-index')) || 1;
			}
		}));
		return maxZ;
	},
	dragDialog : function(elementId) {
		var d = document;
		var a = window.event;
		var o = document.getElementById(elementId);
		if (!a.pageX)
			a.pageX = a.clientX;
		if (!a.pageY)
			a.pageY = a.clientY;
		var x = a.pageX, y = a.pageY;
		if (o.setCapture)
			o.setCapture();
		else if (window.captureEvents)
			window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
		var backData = {
			x : o.style.top,
			y : o.style.left
		};
		d.onmousemove =function(a) {
			if (!a)
				a = window.event;
			if (!a.pageX)
				a.pageX = a.clientX;
			if (!a.pageY)
				a.pageY = a.clientY;
			var tx = a.pageX - x + parseInt(o.style.left), ty = a.pageY - y
					+ parseInt(o.style.top);
			o.style.left = tx + "px";
			o.style.top = ty + "px";
			x = a.pageX;
			y = a.pageY;
		};
		d.onmouseup= function(a) {
			if (!a)
				a = window.event;
			if (o.releaseCapture)
				o.releaseCapture();
			else if (window.captureEvents)
				window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
			d.onmousemove = null;
			d.onmouseup = null;
			if (!a.pageX)
				a.pageX = a.clientX;
			if (!a.pageY)
				a.pageY = a.clientY;
			if (!document.body.pageWidth)
				document.body.pageWidth = document.body.clientWidth;
			if (!document.body.pageHeight)
				document.body.pageHeight = document.body.clientHeight;
			if (a.pageX < 1 || a.pageY < 1 || a.pageX > document.body.pageWidth
					|| a.pageY > document.body.pageHeight) {
				o.style.left = backData.y;
				o.style.top = backData.x;
			}
		};
	},
	clone : function(obj){
		var objClone;
		if (obj.constructor == Object){
			objClone = new obj.constructor();
		}else{
			objClone = new obj.constructor(obj.valueOf());
		}
		for(var key in obj){
			if ( objClone[key] != obj[key] ){
				if (typeof(obj[key]) == 'object' ){
					objClone[key] = clone(obj[key]);
				}else{
					objClone[key] = obj[key];
				}
			}
		}
		objClone.toString = obj.toString;
		objClone.valueOf = obj.valueOf;
		return objClone;
	},
	bindIframeLoad : function(obj,fun)
	{
		if (obj.attachEvent)
		{
			obj.attachEvent("onload", fun);
		} 
		else 
		{
			obj.onload = fun;
		}
	},
	getFrameHeight : function(obj){  
	    var iframeHeight=0;  
	    if (navigator.userAgent.indexOf("Firefox")>0) 
	    { // Mozilla, Safari, ...  
	        iframeHeight=obj.contentDocument.body.offsetHeight;  
	    } 
	    else if (navigator.userAgent.indexOf("MSIE")>0) 
	    { // IE  
	        iframeHeight=MainFrame.document.body.scrollHeight;//IE这里要用MainFrame，不能用obj，切记  
	    }
	    return iframeHeight;
	},
	getScrollBarWidth : function() {
		var __scrollBarWidth = null;
		var scrollBarHelper = document.createElement("div");
		// if MSIE
		// 如此设置的话，scroll bar的最大宽度不能大于100px（通常不会）。
		scrollBarHelper.style.cssText = "overflow:scroll;width:100px;height:100px;";
		// else OTHER Browsers:
		// scrollBarHelper.style.cssText = "overflow:scroll;";
		document.body.appendChild(scrollBarHelper);
		if (scrollBarHelper) {
			__scrollBarWidth = {
				x : scrollBarHelper.offsetHeight
						- scrollBarHelper.clientHeight,
				y : scrollBarHelper.offsetWidth
						- scrollBarHelper.clientWidth
			};
		}
		document.body.removeChild(scrollBarHelper);
		return __scrollBarWidth;
	},
	isHaveScroll : function(el) {
	    // test targets
	    var elems = el ? [el] : [document.documentElement, document.body];
	    var scrollX = false, scrollY = false;
	    for (var i = 0; i < elems.length; i++) {
	        var o = elems[i];
	        // test horizontal
	        var sl = o.scrollLeft;
	        o.scrollLeft += (sl > 0) ? -1 : 1;
	        o.scrollLeft !== sl && (scrollX = scrollX || true);
	        o.scrollLeft = sl;
	        // test vertical
	        var st = o.scrollTop;
	        o.scrollTop += (st > 0) ? -1 : 1;
	        o.scrollTop !== st && (scrollY = scrollY || true);
	        o.scrollTop = st;
	    }
	    // ret
	    return {
	        scrollX: scrollX,
	        scrollY: scrollY
	    };
	},
	executeFunction : function(fun)
	{
		if(typeof(fun) == "function")
		{
			setTimeout(fun,0);
		}
	},
	getDefineLengthRandom : function(iStart,iLast)
	{    
	    var iLength = iLast-iStart+1;    
	    return Math.floor(Math.random()*iLength+iStart);    
	}, 
	getUrlParam : function(paramName)
	{
	        paramValue = "";
	        isFound = false;
	        if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=")>1)
	        {
	            arrSource = unescape(this.location.search).substring(1,this.location.search.length).split("&");
	            i = 0;
	            while (i < arrSource.length && !isFound)
	            {
	                if (arrSource[i].indexOf("=") > 0)
	                {
	                     if (arrSource[i].split("=")[0].toLowerCase()==paramName.toLowerCase())
	                     {
	                        paramValue = arrSource[i].split("=")[1];
	                        isFound = true;
	                     }
	                }
	                i++;
	            }   
	        }
	   return paramValue;
	},
	isEmail : function(email){
		if(PATTERN_EMAIL.test(email))
		{
			return true;
		}
		return false;
	},
	removeLoading : function(){
		$("#_j_loading_overlay_div").remove();
		$("#_j_loading_overlay_content_div").remove();
	},
	showLoading : function(text){
		_cmsFormPluginLoadingFrameIsCompleted_ = false;
		if($("#_j_loading_overlay_div").length == 0)
		{
			$("body").append('<div id="_j_loading_overlay_div" class="_j_black_overlay"></div>');
			var loading_html = '<div id="_j_loading_overlay_content_div" class="_j_loading_overlay_content">\
									<div>'
										+ text +
									'</div>\
								</div>';
			$("body").append(loading_html);
		}
		$("#_j_loading_overlay_div").height($(document).height());
		$("#_j_loading_overlay_div").width($(document).width());
		$("#_j_loading_overlay_div").css("opacity","0.6");
		$("#_j_loading_overlay_div").css("filter","alpha(opacity=60)");
		$("#_j_loading_overlay_div").css("-moz-opacity","0.6");
		$("#_j_loading_overlay_div").fadeIn(600);
		//$("#_j_loading_overlay_div").css("display","block");
	},
	//js获取项目根路径，如： http://localhost:8083/uimcardprj
	getRootPath : function(){
		var siteResPath = document.getElementById("siteResPath");
		if(siteResPath)
		{
			return siteResPath.value;
		}
		else
		{
			//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
			var curWwwPath=window.document.location.href;
			//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
			var pathName=window.document.location.pathname;
			var pos=curWwwPath.indexOf(pathName);
			//获取主机地址，如： http://localhost:8083
			var localhostPaht=curWwwPath.substring(0,pos);
			//获取带"/"的项目名，如：/uimcardprj
			var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
			return(localhostPaht+projectName);
		}
	},
	isEmptyOrNull : function(item){
		if(item == undefined || !item || item == "" || $.trim(item) == "")
		{
			return true;
		}
		return false;
	}
};
alyCms.user = 
{
		showImageSelector : function(callback){
			var selector = $("#image_selector");
			if(selector.length == 0)
			{
				$("body").append("<div id='image_selector_overlay'></div><div id='image_selector'></div>");
				
				selector = $("#image_selector");
				
				$(document).keyup(function(event){
					if (event.keyCode == 27)
			    	{
						$("#image_selector,#image_selector_overlay").hide();
			    	}
				});
			}
			alertTip("正在加载...",4);
			
			if($("#TB_window").length > 0)
			{
				var zindex = $("#TB_window").css("z-index");
				if(zindex)
				{
					zindex = zindex * 1 + 1;
					$("#image_selector_overlay").css("z-index",zindex);
					selector.css("z-index",zindex);
					$("#tipDiv").css("z-index",zindex + 1);
				}
			}
			else
			{
				$("#tipDiv").css("z-index",1002);
			}
			
			selector.html("");
			$("#image_selector,#image_selector_overlay").show();
			
			showImageSelectorCallback = callback;
			
			selector.load($("#siteContextPath").val() + "/images/selector/index.htm?pageSize=10",function(){
				removeTip();
			});
		},
/**
 *  用户登录
 */
 readyLoginForm : function(){
	$('#login_form').ajaxForm({
		beforeSubmit:alyCms.user.loginFormFinalValidate,
		warningMessage:"正在登录,请稍后...",
		success:function(data){
			$.removeFormOverLay(data);
		},
		closeFormOverLayCallBack:function(data){
			data = JSON.parse(data);
			if(data.response)
			{
				minAlert(data.responseMsg,1);
				hideAllErrorMessage();
					window.location.href=$("#siteContextPath").val() + "/cms/index.htm";
			}
			else
			{
				minAlert(data.responseMsg,3);
			}
		}
	});
	$("form :input").bindInputClearEvent();
	$("#id_username").bindBackTextEvent();
	// validate form data
	$("form :input").blur(function() {
		
		if ($(this).is("#id_username")) {
			var username = $(this).val();
			var isEmail = alyCms.base.isEmail(username);
			if(isEmail)
			{
				if(alyCms.util.validateEmail("id_username"))
				{
					removeErrorMessage("id_username");
				}
			}
			else
			{
				if(alyCms.util.validateUserName("id_username"))
				{
					removeErrorMessage("id_username");
				}
			}
		} 
		 //验证密码
		if ($(this).is("#id_password")) {
			alyCms.util.validatePassword("id_password");
		} 
	});
	// listen enter
	$(document).keypress(function(event) {
	    var key = event.which;
	    if (key == 13) {
	    	$('form').submit();
	    }
	});
},/**
 * 用户登录验证
 */
loginFormFinalValidate : function(){
	$("#login_form :input").trigger('blur');
	var numError = $('#login_form :input[isError=true]').length;
	if(numError){
		minAlert("请填写完整的登录信息！",3);
		return false;
	}
	if ($("#remember_me").is(":checked")) {
		setCookie("rmbUser", "true",7);
		setCookie("8890806337EF73E838519853EFEBC1679C949E81", $("#id_username").val(),7);
		setCookie("2236505FF410E6196EE7626E28585A36E04AC590", $("#id_password").val(),7);
	}
	else {
		saveCookie("rmbUser", "false",-1);
		saveCookie("8890806337EF73E838519853EFEBC1679C949E81", "",-1);
		saveCookie("2236505FF410E6196EE7626E28585A36E04AC590", "",-1);
	}
	removeAllErrorMessage();
	return true;

}
}
/*Array.prototype.remove : function(dx) {  
    if (isNaN(dx) || dx > this.length) {  
        return false;  
    }  
    for (var i = 0, n = 0; i < this.length; i++) {  
        if (this[i] != this[dx]) {  
            this[n++] = this[i];  
        }  
    }  
    this.length -= 1;  
};*/  