var myMenuResourceList = [];
jQuery(document).ready(function($){
	// browser window scroll (in pixels) after which the "back to top" link is shown
	var offset = 300,
		//browser window scroll (in pixels) after which the "back to top" link opacity is reduced
		offset_opacity = 1200,
		//duration of the top scrolling animation (in ms)
		scroll_top_duration = 700,
		//grab the "back to top" link
		$back_to_top = $('.cd-top');

	//hide or show the "back to top" link
	$(window).scroll(function(){
		( $(this).scrollTop() > offset ) ? $back_to_top.addClass('cd-is-visible') : $back_to_top.removeClass('cd-is-visible cd-fade-out');
		if( $(this).scrollTop() > offset_opacity ) { 
			$back_to_top.addClass('cd-fade-out');
		}
	});

	//smooth scroll to top
	$back_to_top.on('click', function(event){
		event.preventDefault();
		$('body,html').animate({
			scrollTop: 0 ,
		 	}, scroll_top_duration
		);
	});
    $('.cms-site-search>input').focus(function () {
        return $('#top-bar').addClass('navbox-open');
    });
    $('.cms-site-search>input').blur(function () {
        return $('#top-bar').removeClass('navbox-open');
    });
    return $(document).on('blur', function (e) {
        var $target;
        $target = $(e.target);
        if (!$target.closest('.navbox').length && !$target.closest('#navbox-trigger').length) {
            return $('#top-bar').removeClass('navbox-open');
        }
    });
});
$(function(){
	countRightContentHeight();
	$("#cms-message-method").click(function(){
		var msgTip = $(".cms-message-tip");
		if(msgTip.is(":hidden"))
		{
			msgTip.show();
		}
		else
		{
			msgTip.hide();
		}
	});
	$(".welcome_user").click(function(){
		var userTip = $(".cms-user-operate");
		if(userTip.is(":hidden"))
		{
			userTip.show();
		}
		else
		{
			userTip.hide();
		}
	});
	//站内检索
	var delaySearchTimer = null;
	$(".cmsSiteSearch").keyup(function(e){ 
	   	  var curKey = e.which; 
	  	  if(curKey == 13)
	  	  { 
	  		searchTermMyResource();
 	 	  }
	  	  else
	  	  {
	  		  if(delaySearchTimer)
	  		  {
	  			  clearTimeout(delaySearchTimer);
	  		  }
	  		  delaySearchTimer = setTimeout(function(){
	  			searchTermMyResource();
	  		  },500);
	  	  }
 	  });
	$('[data-toggle="tooltip"]').tooltip();
	 refreshDate();
	 refreshDate();
	 refreshDate();
})
function refreshDate()
{
	$("#cms-screen").text(DateUtils.getStringDate(new Date(),"yyyy-MM-dd HH:mm:ss"))
	// 设置每1秒读取一次
	setTimeout(function(){
		 refreshDate();
	},1000);
}
imagePageCallback = function() {
		var wrapper = $('#galpop-wrapper');
		var info    = $('#galpop-info');
		var count   = wrapper.data('count');
		var index   = wrapper.data('index');
		var current = index + 1;
		var string  = current +'/'+ count;

		info.append('<p>'+ string +'</p>').fadeIn();
	};//图片回调
/**
 * 计算右侧内容块高度
 */
function countRightContentHeight()
{
	var height =$(window).height() - 45;
	var width = $(window).width() - 200;
	$(".cms-content").css("height",height+"px");
	$(".cms-content").animate({bottom:'20px'});
	
	
	
}
function linkLogin()
{
	showConfirmDialog("登陆已失效，请重新登陆",function(){
		alertTip("正在跳转登陆，请稍后...",4);
		setTimeout(function(){
			window.location.href=alyCms.base.getRootPath() + "/cms/login.htm"
		},1000)
	},function(){
		setTimeout(function(){
			window.location.href=alyCms.base.getRootPath() + "/cms/login.htm"
		},1000)
	});
}
function randomLetter(len){
    len = len || 1;
    var $chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    var maxPos = $chars.length;
    var pwd = '';
    for (i = 0; i < len; i++) {
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
    }
    return pwd;
}
/**
 * post ajax
 * @param url
 * @param data
 * @param callback
 * @param dataType
 */
function doPost(url,data,callback,dataType)
{
	var realData = {};
	var realDataType = "";
	var realCallback = null;
	
	if(typeof data == "function")
	{
		realCallback = data;
		realDataType = callback;
	}
	else if(typeof data == "object")
	{
		realData = data;
		realCallback = callback;
		realDataType = dataType;
	}
	
	$.ajax({
		url : url,
		data : realData,
		type : "POST",
		dataType : realDataType,
		success : function(rs){
			cms_repsonse_callback(rs,realCallback);
		},
		error : function(error){
			cms_repsonse_callback("1036");
		}
	});
}
/**
 * post ajax
 * @param url
 * @param data
 * @param callback
 * @param dataType
 */
function doGet(url,data,callback,dataType)
{
	var realData = {};
	var realDataType = "";
	var realCallback = null;
	
	if(typeof data == "function")
	{
		realCallback = data;
		realDataType = callback;
	}
	else if(typeof data == "object")
	{
		realData = data;
		realCallback = callback;
		realDataType = dataType;
	}
	
	$.ajax({
		url : url,
		data : realData,
		type : "GET",
		dataType : realDataType,
		success : function(rs){
			cms_repsonse_callback(rs,realCallback);
		},
		error : function(error){
			cms_repsonse_callback("1036");
		}
	});
}
/**
 * ajax封装回调方法
 * @param rs
 * @param callback
 * @returns {Boolean}
 */
function cms_repsonse_callback(rs,callback)
{
	if(typeof loading != "undefined" && loading == 1)
	{
		loading = 0;
	}
	
	var alertFn = null;
	if(typeof alertTip != 'undefined')
	{
		alertFn = function(text){
			alertTip(text,2);
		};
	}
	else
	{
		alertFn = function(text){
			alert(text);
		};
	}
	if(rs.responseCode == "1037")
	{
		alertFn("您的操作太快啦，请稍后重试！");
	}
	else if(rs.responseCode == "1029")
	{
			linkLogin();
		 return false;
	}
	else if(rs.responseCode == "500")
	{
		alertFn("加载失败,请重试！",2);
	}
	else
	{
		if(typeof callback == "function")
		{
			callback(rs);
		}
	}
}
/**
 * 显示loading
 */
function showLoading()
{
	alertTip("正在加载中，请稍等片刻 . . . ",4);
}
/**
 * 隐藏loading
 */
function hideLoading()
{
	$("#tipDiv").remove();
}
/**
 * 退出登陆
 */
function loginOut()
{
	showConfirmDialog("您确定要退出吗？",function(){
		alertTip("正在安全退出，请稍后...",4);
		setTimeout(function(){
			window.location.href=alyCms.base.getRootPath() + "/cms/user/logout.htm"
		},500)
	},function(){
		
	});
	
}
/**
 * 单张图片上传
 * @param _self
 */
function oddImageUpload(_self)
{
	if(_self.files.length > 0)
	{
		$(_self).parent("a").next().html('<img  src="'+URL.createObjectURL(_self.files[0])+'" width="100%" height="100%"/>');
		$(_self).parent("a").next().attr("href",URL.createObjectURL($(_self)[0].files[0]));
		$(_self).parent("a").next().css("background","none;")
		$(_self).parent("a").next().galpop();
	}
}
/**
 * 多张图片上传
 * @param _self
 */
function multiImageUpload(_self)
{
	if(_self.files.length > 0)
	{
		var filesArray = _self.files;
		$(_self).parent("a").next().html("");
		$(_self).parent("a").next().css("background","none");
		for(var i =0;i<filesArray.length;i++)
		{
			$(_self).parent("a").next().append('<img href="'+URL.createObjectURL(filesArray[i])+'" src="'+URL.createObjectURL(filesArray[i])+'" class="galpop-multiple" data-galpop-group="multiple" width="100%" height="100%"/>');
			
		}
		$('.galpop-multiple').galpop();
		console.log(_self.files)
	}
}

//非空判断
function isNotNull(obj)
{
	if(obj != undefined && typeof obj != undefined && obj != null && obj != 'null' && obj != '')
	{
		return true
	}
	return false;
	
}
/**
 * 修改用户信息
 * @param id
 */
function cmsUpdateCmsUser(id)
{
	var url = alyCms.base.getRootPath()+"/cms/user/edit.htm?userId=" + id+"&self=true"
	TB_show({"url":url,"title":"修改资料","width":600,"height":550});
	 $(".cms-user-operate").hide();
	
}
/**
 * 充值密码
 * @param id
 */
function cmsChangeUserPassword(id)
{
	var url = alyCms.base.getRootPath()+"/cms/user/changepwd.htm?userId=" + id
	TB_show({"url":url,"title": "重置密码" ,"width":300,"height":235});
	$(".cms-user-operate").hide();
}
/**
 * 全选
 * @param name
 */
function selectAllOrders(name)
{
	if($("#selectAllOrder").is(":checked"))
	{
		$("input[name='"+name+"']").prop("checked",true); 
	}
	else
	{
		$("input[name='"+name+"']").prop("checked",false); 
	}
}
/**
 * 打开table列表详情
 * @param id
 */
function openCmsDetail(id)
{
	if($("#"+id).is(":visible"))
	{
		$("#" + id).hide();
	}
	else
	{
		$("#"+ id).show();
	}
}
function changeNoticWarn(self)
{
	if($(self).is(":checked"))
	{
		localStorage.setItem("openNotic", true);
	}
	else
	{
		localStorage.setItem("openNotic", false);
	}
}
//浏览器类型及版本
function getBrowserInfo() {
	var agent = navigator.userAgent.toLowerCase();
	var regStr_ie = /msie [\d.]+;/gi;
	var regStr_ff = /firefox\/[\d.]+/gi
	var regStr_chrome = /chrome\/[\d.]+/gi;
	var regStr_saf = /safari\/[\d.]+/gi;
	var isIE = agent.indexOf("compatible") > -1 && agent.indexOf("msie" > -1); //判断是否IE<11浏览器  
	var isEdge = agent.indexOf("edge") > -1 && !isIE; //判断是否IE的Edge浏览器  
	var isIE11 = agent.indexOf('trident') > -1 && agent.indexOf("rv:11.0") > -1;
	if (isIE) {
		var reIE = new RegExp("msie (\\d+\\.\\d+);");
		reIE.test(agent);
		var fIEVersion = parseFloat(RegExp["$1"]);
		if (fIEVersion == 7) {
			return "IE/7";
		} else if (fIEVersion == 8) {
			return "IE/8";
		} else if (fIEVersion == 9) {
			return "IE/9";
		} else if (fIEVersion == 10) {
			return "IE/10";
		} 
	} //isIE end 
	if (isIE11) {
		return "IE/11";
	}
	//firefox
	if (agent.indexOf("firefox") > 0) {
		return agent.match(regStr_ff);
	}
	//Safari
	if (agent.indexOf("safari") > 0 && agent.indexOf("chrome") < 0) {
		return agent.match(regStr_saf);
	}
	//Chrome
	if (agent.indexOf("chrome") > 0) {
		return agent.match(regStr_chrome);
	}
}

function validateBrowser()
{
	//以下是调用上面的函数
	var mb = getBrowserInfo();
	if (mb.indexOf("IE") > -1 || mb[0].indexOf("firefox") >-1) {
		$(".browser_version").removeClass("none");
	}
}
/**
 * 查询我的菜单
 */
function searchMyResource()
{
	doPost($("#siteContextPath").val() + "/query/my/resource.htm",{},function(data){
		if(data.response)
		{
			if(isNotNull(data.data))
			{
				var dataJson = JSON.parse(data.data);
				var html = "";
				myMenuResourceList = [];
				for(var resource in dataJson.resourceList)
				{
					var resourceObj = dataJson.resourceList[resource];
					if(resourceObj.resourceType == 1)
					{
						myMenuResourceList.push(resourceObj);
					}
				}
				var searchData = {};
				searchData.data = myMenuResourceList;
				$(".navbox .navbox-tiles").html(template.render("my_resource_list", searchData));
			}
		}
	},'json')
}
/**
 * 检索菜单
 */
function searchTermMyResource()
{
	var menuName = $("#cmsSiteSearch").val() ?  $("#cmsSiteSearch").val() : "";
	
	if(menuName == "")
	{
		var searchData = {};
		searchData.data = myMenuResourceList;
		$(".navbox .navbox-tiles").html(template.render("my_resource_list", searchData));
		return ;
	}
	
	if($.trim(menuName) == "")
	{
		return ;
	}
	
	var isSearchByPinyin = false;
	var searchMenuNamePinyin = null;
	if(makePy(menuName)[0] == menuName)
	{
		isSearchByPinyin = true;
		searchMenuNamePinyin = menuName.toUpperCase();
	}
	
	var searchResult = []; 
	for(var category in myMenuResourceList)
	{
		var isFilter = false;
		var name = myMenuResourceList[category].resourceName;
		
		if(isSearchByPinyin)
		{
			var pinyin = makePy(name);
			for(var i in pinyin)
			{
				if(pinyin[i].indexOf(searchMenuNamePinyin) > -1)
				{
					isFilter = true;
				}
			}
		}
		if(name.indexOf(menuName) > -1)
		{
			isFilter = true;
		}
		
		if(isFilter)
		{
			searchResult.push(myMenuResourceList[category]);
		}
	}
	if(searchResult.length == 0)
	{
		doneTip(null,null,"暂无此菜单",2,true);
	}
	else
	{
		$(".navbox .navbox-tiles").html("");
		var searchData = {};
		searchData.data = searchResult;
		$(".navbox .navbox-tiles").html(template.render("my_resource_list", searchData));
	}
}
function removeBanner(data)
{
	$(data).parent().remove();
}
function formatFormData(selector)
{
	var params = new Object();  
    var form = $(selector);
	
    /*组合参数*/                  
    var items = form.find("input[type=hidden],"+  
            "input[type=text],"+  
            "input[type=password],"+  
            "textarea,"+  
            "select,"+  
            "input[type=radio],"+  
            "input[type=checkbox]");  
      
    items.each(function(index){
    	var name = this.name;
    	if(!name)
    	{
    		return true;
    	}
    	if($(this).attr("type") == "radio" || $(this).attr("type") == "checkbox")
    	{
    		if(!$(this).is(":checked"))
    		{
    			return true;
    		}
    	}
    	if($(this).is("select") && $(this).attr("multiple") == "multiple")
    	{
    		var v = $(this).val();
    		for(var i in v)
    		{
    			var n = this.name + "[" + i + "]";
    			params[n] = v[i];  
    		}
    	}
    	else
    	{
    		params[this.name] = $(this).val();  
    	}
    });
    
    $("[contenteditable='true']").each(function(){
    	var name = this.name;
    	if(!name)
    	{
    		return true;
    	}
    	params[this.name] = $(this).html();
    });
    
    return params;
}
function submitCallbackFile(options)
{
	var newOptions = {
			url : options.url ? options.url : '',
			params : options.params ? options.params : '',
			fileName : options.fileName ? options.fileName : '',
			loadingMsg : options.loadingMsg ? options.loadingMsg : '正在下载，请稍后',
			callback : options.callback ? options.callback : ''
	}
	$._form_show_overlay_(newOptions.loadingMsg);
	var oReq = new XMLHttpRequest();
	oReq.timeout = 3600000;
	oReq.open("POST", newOptions.url+"?"+newOptions.params, true);
	oReq.responseType = "blob";
	oReq.onload = function (oEvent) {
		newOptions.callback(oReq)
	    $.removeFormOverLay(null);
	};
	oReq.send();
}
