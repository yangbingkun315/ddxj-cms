//getRoot = "http://app.diandxj.com/worker-manager";//生产环境
//getRootApp = "http://app.diandxj.com/worker-app";//生产环境
//getRootVideo = "http://res.diandxj.com/";//生产环境

getRoot = "http://test.diandxj.com/worker-manager";//测试

//getRoot = "http://localhost:9080/worker-manager";
getRootApp = "http://test.diandxj.com/worker-app";
getRootVideo = "http://res-test.diandxj.com/";
notLogin=["/manager/login.html",]
var Ajax = {
                //ajax模块
                init: function(obj) {
                    //初始化数据
                    var objAdapter = {
                            url: '',
                            method: 'get',
                            data: {},
                            success: function() {},
                            complete: function() {},
                            error: function(s) {
                                alert('status:' + s + 'error!');
                            },
                            xhrFields:{
                				withCredentials:true
                			},
                            async: true
                        }
                        //通过使用JS随机字符串解决IE浏览器第二次默认获取缓存的问题
                    objAdapter.url = obj.url + '?rand=' + Math.random();
                    objAdapter.method = obj.method || objAdapter.method;
                    objAdapter.data = Ajax.params(obj.data) || Ajax.params(objAdapter.data);
                    objAdapter.async = obj.async == false ? obj.async : objAdapter.async;
                    objAdapter.complete = obj.complete || objAdapter.complete;
                    objAdapter.success = obj.success || objAdapter.success;
                    objAdapter.error = obj.error || objAdapter.error;
                    return objAdapter;
                },
                //创建XMLHttpRequest对象
                createXHR: function() {
                    if(window.XMLHttpRequest) { //IE7+、Firefox、Opera、Chrome 和Safari
                        return new XMLHttpRequest();
                    } else if(window.ActiveXObject) { //IE6 及以下
                        var versions = ['MSXML2.XMLHttp', 'Microsoft.XMLHTTP'];
                        for(var i = 0, len = versions.length; i < len; i++) {
                            try {
                                return new ActiveXObject(version[i]);
                                break;
                            } catch(e) {
                                //跳过
                            }
                        }
                    } else {
                        throw new Error('浏览器不支持XHR对象！');
                    }
                },
                params: function(data) {
                    var arr = [];
                    for(var i in data) {
                        //特殊字符传参产生的问题可以使用encodeURIComponent()进行编码处理
                        arr.push(encodeURIComponent(i) + '=' + encodeURIComponent(data[i]));
                    }
                    return arr.join('&');
                },
                callback: function(obj, xhr) {
                    if(xhr.status == 200) { //判断http的交互是否成功，200表示成功
                    	if(isNotNull(xhr.responseText))
                    	{
                    		var json = JSON.parse(xhr.responseText);
                    		if(json.response == false)
                    		{
                    			if(json.responseCode == 500)
                        		{
                        			layer.alert(json.responseMsg,{title: '警告',icon:0}); 
                        			return false;
                        		}
                        		else
                        		{
                        			layer.alert(json.responseMsg,{title: '警告',icon:0}); 
                        			return false;
                        		}
                    		}
                    		
                    	}
                    	var responseJSON = JSON.parse(xhr.responseText);
                    	responseJSON.data = JSON.parse(responseJSON.data);
                        obj.success(responseJSON); //回调传递参数
                    } else {
                    	layer.alert('获取数据错误！错误代号：' + xhr.status + '，错误信息：' + xhr.statusText,{title: '警告',icon:0}); 
                    }
                },
                ajax: function(obj) {
                    if(obj.method === 'post') {
                        Ajax.post(obj);
                    } else {
                        Ajax.get(obj);
                    }
                },
                //post方法
                post: function(obj) {
                    var xhr = Ajax.createXHR(); //创建XHR对象
                    var opt = Ajax.init(obj);
                    opt.method = 'post';
                    if(opt.async === true) { //true表示异步，false表示同步
                        //使用异步调用的时候，需要触发readystatechange 事件
                        xhr.onreadystatechange = function() {
                            if(xhr.readyState == 4) { //判断对象的状态是否交互完成
                                Ajax.callback(opt, xhr); //回调
                            }
                        };
                    }
                    //在使用XHR对象时，必须先调用open()方法，
                    //它接受三个参数：请求类型(get、post)、请求的URL和表示是否异步。
                    xhr.open(opt.method, opt.url, opt.async);
                    //post方式需要自己设置http的请求头，来模仿表单提交。
                    //放在open方法之后，send方法之前。
                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                    xhr.send(opt.data); //post方式将数据放在send()方法里
                    if(opt.async === false) { //同步
                        Ajax.callback(obj, xhr); //回调
                    }
                },
                //get方法
                get: function(obj) {
                    var xhr = Ajax.createXHR(); //创建XHR对象
                    var opt = Ajax.init(obj);
                    if(opt.async === true) { //true表示异步，false表示同步
                        //使用异步调用的时候，需要触发readystatechange 事件
                        xhr.onreadystatechange = function() {
                            if(xhr.readyState == 4) { //判断对象的状态是否交互完成
                                Ajax.callback(obj, xhr); //回调
                            }
                        };
                    }
                    //若是GET请求，则将数据加到url后面
                    opt.url += opt.url.indexOf('?') == -1 ? '?' + opt.data : '&' + opt.data;
                    //在使用XHR对象时，必须先调用open()方法，
                    //它接受三个参数：请求类型(get、post)、请求的URL和表示是否异步。
                    xhr.open(opt.method, opt.url, opt.async);
                    xhr.send(null); //get方式则填null
                    if(opt.async === false) { //同步
                        Ajax.callback(obj, xhr); //回调
                    }
                }
            };
/**
 * 验证当前url是否需要登陆
 * @returns {Boolean}
 */
function validateCurrentUrlIsLogin()
{
	var hostUrl = window.location.href;
	if(hostUrl.indexOf("template") > -1)
	{
		var hostSplit =hostUrl.split("template");
		var flag = false;
		
		for(var i =0 ;i<notLogin.length;i++)
		{
			var dateUrl = "";
			if(hostSplit[1].indexOf("?") > -1)
			{
				var url = hostSplit[1].split("?")
				dateUrl = url[0];
			}
			else
			{
				dateUrl = hostSplit[1];
			}
			if(notLogin[i] == dateUrl)
			{
				flag = true;
				break ;
			}
		}
		return flag;
	}
}
/**
 * 初始化登陆
 */
function initManager()
{
	if(!validateCurrentUrlIsLogin())//如果当前页面不需要登陆
	{
		var userName = localStorage.getItem("userName");
		if(isNotNull(userName))
		{
			Ajax.post({
		         url: getRoot+'/query/user/validate/login.ddxj',
		         data: {userName:localStorage.getItem("userName")},
		         async: false,
		         success: function(data) {
		             if(data.response)
		             {
		            	 if(!isNotNull(data.data.user))
		            	 {
		            		 window.location.href="../../template/manager/login.html";
		            	 }
		            	 else
		            	 {
		            		 localStorage.setItem("roleId",data.data.user.managerRole.id); 
		            	 }
		               //总页数大于页码总数
		             }
		         }
		     }); 
		}
		else
		{
			window.location.href="../../template/manager/login.html";
		}
	}
}
initManager();

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
 * 时间戳转年龄
 * @param str
 * @returns
 */
function ages(str)   
{   
      if(str==null)return   false;     
      var  birth=   new   Date(str);     
            var today = new Date();   
            var age = today.getFullYear()-birth.getFullYear();
            
            if(today.getMonth()>birth.getMonth()){
            return age;
          }
          if(today.getMonth()==birth.getMonth()){
          if(today.getDate()>=birth.getDate()){
          return age;
          }else{
          return age-1;
          }
          }
          if(today.getMonth()<birth.getMonth()){
          return age-1;
          }
}  

//判断图片大小是否超标
function uploadImageTOQN(event)
{
	var fileObj = event;
	if (fileObj && fileObj.files && fileObj.files[0]) 
	{
		//在这个时刻选择上传图片
		if(fileObj.files[0].size < 8388608)
		{
			return uploadImgUtils(event);
		}
     	else
     	{
			layer.msg("上传文件大小8M以内");
			return false;
		}
	}
}

//获取token
function uploadImgUtils(event) 
{
	var image;
	var files = [];
    files.push(event.files[0].name);
    Ajax.post({
		url: getRootApp+'/member/query/qiniu/token.ddxj',
		data: {},
		async: false,
		success: function(data) {
			if(data.response == true)
			{
	        	var token = data.data.token;
	       		var f = event.files[0];
	       		image = qiniuUpload(f, token);
			}
		}
	}); 
    return image;
}

//图片上传到七牛
function qiniuUpload(f,token)
{
	var Qiniu_UploadUrl = "http://up.qiniu.com";
    
    var xhr = new XMLHttpRequest();
	xhr.open('POST', Qiniu_UploadUrl, false);
    var formData, startDate;
    formData = new FormData();
    formData.append('token', token);
    formData.append('file', f);
    var taking,picture;
    xhr.upload.addEventListener("progress", function (evt) 
	{
		if (evt.lengthComputable) 
		{
			var nowDate = new Date().getTime();
	        taking = nowDate - startDate;
	        var x = (evt.loaded) / 1024;
	        var y = taking / 1000;
	        var uploadSpeed = (x / y);
	        var formatSpeed;
	        formatSpeed = (uploadSpeed / 1024).toFixed(2) + "Mb\/s";
		}
    }, false);
    xhr.onload = function (response) 
    {
		if (xhr.readyState == 4 && xhr.status == 200 && xhr.responseText != "") 
		{
	        var blkRet = JSON.parse(xhr.responseText);
	        layer.msg('上传成功');
	        picture = "http://res.diandxj.com/" + blkRet.key;
      	}
		else if (xhr.status != 200 && xhr.responseText) 
		{
			layer.msg('上传失败');
		}
    };
    startDate = new Date().getTime();
    $("#progressbar").show();
    xhr.send(formData);
    return picture;
}

//判断图片大小是否超标
function uploadVideoTOQN(event)
{
	var fileObj = event;
	if (fileObj && fileObj.files && fileObj.files[0]) 
	{
		//在这个时刻选择上传图片
		if(fileObj.files[0].size < 104857600)
		{
			return uploadVideoUtils(event);
		}
		else
		{
			layer.msg("上传文件大小100M以内");
			return false;
		}
	}
}

//获取token
function uploadVideoUtils(event)
{
	var image;
	var files = [];
    files.push(event.files[0].name);
    Ajax.post({
		url: getRootApp+'/member/query/qiniu/token.ddxj',
		data: {},
		async: false,
		success: function(data) {
			if(data.response == true)
			{
	        	var token = data.data.token;
	       		var f = event.files[0];
	       		image = qiniuUploadVideo(f, token);
			}
		}
	}); 
    return image;
}

//视频上传到七牛
function qiniuUploadVideo(f,token)
{
	var Qiniu_UploadUrl = "http://up.qiniu.com";
    
    var xhr = new XMLHttpRequest();
	xhr.open('POST', Qiniu_UploadUrl, false);
    var formData, startDate;
    formData = new FormData();
    formData.append('token', token);
    formData.append('file', f);
    var taking,picture;
    xhr.upload.addEventListener("progress", function (evt) 
	{
		if (evt.lengthComputable) 
		{
			var nowDate = new Date().getTime();
	        taking = nowDate - startDate;
	        var x = (evt.loaded) / 1024;
	        var y = taking / 1000;
	        var uploadSpeed = (x / y);
	        var formatSpeed;
	        formatSpeed = (uploadSpeed / 1024).toFixed(2) + "Mb\/s";
		}
    }, false);
    xhr.onload = function (response) 
    {
		if (xhr.readyState == 4 && xhr.status == 200 && xhr.responseText != "") 
		{
	        var blkRet = JSON.parse(xhr.responseText);
	        layer.msg('上传成功');
	        picture = getRootVideo + blkRet.key;
      	}
		else if (xhr.status != 200 && xhr.responseText) 
		{
			layer.msg('上传失败');
		}
    };
    startDate = new Date().getTime();
    $("#progressbar").show();
    xhr.send(formData);
    return picture;
}

//获取地址栏参数
function urlParam(name)
{
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r!=null) return unescape(r[2]); return '';
}

//根据时间戳获取时间格式
function timeInMillisToformat(shijianchuo)
{
	//shijianchuo是整数，否则要parseInt转换
	var time = new Date(shijianchuo);
	var y = time.getFullYear();
	var m = time.getMonth()+1;
	var d = time.getDate();
	var h = time.getHours();
	var mm = time.getMinutes();
	var s = time.getSeconds();
	return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
}

//时间整列  14:9 改为   14:09
function add0(m){return m<10?'0'+m:m }

//文件转图片base64流
function imageToBase64(file,reader,callback)
{
    var AllowImgFileSize = 5242880; //上传图片最大值(单位字节)（ 2 M = 2097152 B ）超过2M上传失败
    var imgUrlBase64;
    if(file) 
    {
        //将文件以Data URL形式读入页面  
        imgUrlBase64 = reader.readAsDataURL(file);
     	if (AllowImgFileSize != 0 && AllowImgFileSize < reader.result.length) 
     	{
            layui.use(['layer'], function()
			{
				layer.msg("上传失败，请上传不大于2M的图片！");
				return ;
			})
        }
     	else
		{
	        reader.onload = callback;
		}
	}      
}
function imageChange(event,urlId)
{
	var reader = new FileReader();
	compress(event, function(base64Img){
		console.log(base64Img);
		Ajax.post({
			url: getRoot+'/file/images/upload.ddxj',
			data: {"base64":base64Img},
			async: true,
			success: function(data) 
			{
				if(data.response) 
				{
					$("#" + urlId).attr("src",data.data.url);
				}
				else
				{
					layui.use(['layer'], function()
					{
						layer.msg(data.responseMsg);
						return false;
					})
				}
			}
		})
		
		
	});
}

//图片压缩方法
function compress(event, callback)
{
	if(typeof (FileReader) === 'undefined') 
	{
		//调用上传方式  不压缩
		layer.msg("当前浏览器内核不支持base64图标压缩！");
		return ;
	} 
	else 
	{
		try
		{
			var file = event.files[0];
			if(!/image\/\w+/.test(file.type))
			{
				layer.msg("请确保文件为图像类型！");
				return false;
			} 
			var reader = new FileReader();
			reader.onload = function (e) 
			{
				var image = $('<img/>');
				image.load(function () 
				{
					var canvas = document.createElement('canvas');
					canvas.width = this.width;
					canvas.height = this.height;
					var context = canvas.getContext('2d');
				    context.clearRect(0, 0, this.width, this.height);
					context.drawImage(this, 0, 0, this.width, this.height);
					var data = canvas.toDataURL('image/jpeg');
				 	//压缩完成执行回调
			     	callback(data);
				});
				image.attr('src', e.target.result);
			};
			reader.readAsDataURL(file);
		} catch(e) {
			//调用上传方式  不压缩
			layer.msg("上传失败！");
			return ;
		}
	}
}

function imageChanges(event,urlId)
{
	var reader = new FileReader();
    reader.readAsDataURL(event.files[0]);
    reader.onload = function(e) {
      var event = this.result;
      console.log(this.result);
      var _ir = ImageResizer({
        resizeMode: "auto",
        dataSource: event,
        dataSourceType: "base64",
        maxWidth: 1200 ,//允许的最大宽度,
        maxHeight: 600, //允许的最大高度。
        onTmpImgGenerate: function(img) {
        },
        success: function(resizeImgBase64, canvas) {
        	Ajax.post({
        		url: getRoot+'/file/images/upload.ddxj',
    			data: {"base64":resizeImgBase64},
    			async: true,
    			success: function(data) 
    			{
    				if(data.response) 
    				{
    					$("#" + urlId).attr("src",data.data.url);
    				}
    				else
    				{
    					layui.use(['layer'], function()
    					{
    						layer.msg(data.responseMsg);
    						return false;
    					})
    				}
    			}
        	})
        }
      });

    }
}

function changeBtnGroup(obj)
{
	$(obj).next().toggle();
	var self = $(obj).next();
	$(self).find("li").on('click' , function(){
		$(obj).next().hide();
	});
}
function multiGraphShow(images)
{
	var callback = function() {
		var wrapper = $('#galpop-wrapper');
		var info    = $('#galpop-info');
		var count   = wrapper.data('count');
		var index   = wrapper.data('index');
		var current = index + 1;
		var string  = current +'/'+ count;
		info.append('<span >'+ string +'</span>').fadeIn();
	};
	var settings = {
		callback:callback
	};
	$.fn.galpop('openBox',settings,images,0);
}