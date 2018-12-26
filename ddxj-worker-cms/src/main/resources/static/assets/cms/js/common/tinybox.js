/**
 * include css file
 */
document.write('<link rel="stylesheet" type="text/css" href="' + alyCms.base.getRootPath() + '/assets/cms/css/common/tinybox.css"/>');

var TINY={};

function T$(i){return document.getElementById(i)}

TINY.box=function(){
	var p,m,b,fn,ic,iu,iw,ih,ia,f=0,close_bt;
	return{
		show:function(c,u,w,h,a,t){
			if(!f){
				// dialog层
				p=document.createElement('div'); p.id='tinybox';
				// cover层
				m=document.createElement('div'); m.id='tinymask';
				// dialog内容层
				b=document.createElement('div'); b.id='tinycontent';
				// 关闭按钮
				close_bt=document.createElement('div'); close_bt.id='tinyclosebt';
				close_bt.innerHTML = "<a></a>";
				
				document.body.appendChild(m); document.body.appendChild(p); p.appendChild(b);
				document.body.appendChild(close_bt);
				//m.onclick=TINY.box.hide;
				window.onresize=TINY.box.resize; f=1
			}
			if(!a&&!u){
				p.style.width=w?w+'px':'auto'; p.style.height=h?h+'px':'auto';
				p.style.backgroundImage='none'; b.innerHTML=c
			}else{
				b.style.display='none'; p.style.width=p.style.height='100px'
			}
			this.mask();
			ic=c; iu=u; iw=w; ih=h; ia=a; this.alpha(m,1,80,3);
			if(t){setTimeout(function(){TINY.box.hide()},1000*t)}
		},
		// 读取内容
		fill:function(c,u,w,h,a){
			if(u){
				p.style.backgroundImage='';
				var x=window.XMLHttpRequest?new XMLHttpRequest():new ActiveXObject('Microsoft.XMLHTTP');
				x.onreadystatechange=function(){
					if(x.readyState==4&&x.status==200){TINY.box.psh(x.responseText,w,h,a)}
				};
				x.open('GET',c,1); x.send(null)
			}else{
				this.psh(c,w,h,a)
			}
		},
		// 将内容填充到div
		psh:function(c,w,h,a){
			if(a){
				if(!w||!h){
					var x=p.style.width, y=p.style.height; b.innerHTML=c;
					p.style.width=w?w+'px':''; p.style.height=h?h+'px':'';
					b.style.display='';
					w=parseInt(b.offsetWidth); h=parseInt(b.offsetHeight);
					b.style.display='none'; p.style.width=x; p.style.height=y;
				}else{
					b.innerHTML=c
				}
				this.size(p,w,h,4)
			}else{
				p.style.backgroundImage='none'
			}
		},
		hide:function(){
			if(getExplorerType().name == "msie")
			{
				TINY.box.alpha(p,-1,0,2);
			}
			else
			{
				TINY.box.alpha(p,-1,0,5);
			}
			close_bt.style.display = "none";
		},
		resize:function(){
			TINY.box.pos(); TINY.box.mask()
		},
		mask:function(){
			var browserObj = getExplorerType();
			if(browserObj.name == "msie" && browserObj.version == "6.0")
			{
				m.style.height=TINY.page.theight()+'px';
				m.style.width=''; m.style.width=TINY.page.twidth()+'px';
			}
			else
			{
				m.style.height = '100%';
				m.style.width = '100%';
			}
		},
		pos:function(){
			var t=(TINY.page.height()/2)-(p.offsetHeight/2); t=t<10?10:t;
			p.style.top=(t+TINY.page.top())+'px';
			p.style.left=(TINY.page.width()/2)-(p.offsetWidth/2)+'px';
		},
		alpha:function(e,d,a,s){
			clearInterval(e.ai);
			if(d==1){
				e.style.opacity=0; e.style.filter='alpha(opacity=0)';
				e.style.display='block'; this.pos()
			}
			if(getExplorerType().name == "msie")
			{
				e.ai=setInterval(function(){TINY.box.twalpha(e,a,d,s)},10)
			}
			else
			{
				e.ai=setInterval(function(){TINY.box.twalpha(e,a,d,s)},20)
			}
		},
		twalpha:function(e,a,d,s){
			var o=Math.round(e.style.opacity*100);
			if(o==a){
				clearInterval(e.ai);
				if(d==-1){
					e.style.display='none';
					e==p?TINY.box.alpha(m,-1,0,3):b.innerHTML=p.style.backgroundImage=''
				}else{
					e==m?this.alpha(p,1,100,5):TINY.box.fill(ic,iu,iw,ih,ia)
				}
			}else{
				var n=o+Math.ceil(Math.abs(a-o)/s)*d;
				e.style.opacity=n/100; e.style.filter='alpha(opacity='+n+')'
			}
		},
		size:function(e,w,h,s){
			e=typeof e=='object'?e:T$(e); clearInterval(e.si);
			var ow=e.offsetWidth, oh=e.offsetHeight,
			wo=ow-parseInt(e.style.width), ho=oh-parseInt(e.style.height);
			var wd=ow-wo>w?-1:1, hd=(oh-ho>h)?-1:1;
			e.si=setInterval(function(){TINY.box.twsize(e,w,wo,wd,h,ho,hd,s)},20)
		},
		twsize:function(e,w,wo,wd,h,ho,hd,s){
			var ow=e.offsetWidth-wo, oh=e.offsetHeight-ho;
			if(ow==w&&oh==h){
				clearInterval(e.si); p.style.backgroundImage='none'; b.style.display='block';
				// 重新定位close按钮
				var closeTop = alyCms.base.getTop(b) - 4;
				var closeLeft = alyCms.base.getLeft(b) + w - 63;
				close_bt.style.top = closeTop + "px";
				close_bt.style.left = closeLeft + "px";
				this.complete();
			}else{
				if(ow!=w){e.style.width=ow+(Math.ceil(Math.abs(w-ow)/s)*wd)+'px'}
				if(oh!=h){e.style.height=oh+(Math.ceil(Math.abs(h-oh)/s)*hd)+'px'}
				this.pos();
			}
		},
		complete:function(){
			b.style.overflowY = "auto";
			b.style.overflowX = "hidden";
			b.style.height = (ih - 30) + "px";
			//p.style.height = TINY.page.height() + "px";alert(TINY.page.height());
			var pWidth = parseInt(p.style.width.replace("px","")) + 10;
			var pTop = parseInt(p.style.top.replace("px","")) - 42;
			//close_bt.style.top = -ih-25 + "px";
			//close_bt.style.left = iw/2 + 15 + "px";
			//close_bt.style.display = "block";
			close_bt.onclick = TINY.box.hide;
			$(document).keyup(function(event){
		        if (event.keyCode == 27)closeTinyBox();
		    });
			close_bt.style.display = "block";
		}
	}
}();

TINY.page=function(){
	return{
		top:function(){return document.body.scrollTop||document.documentElement.scrollTop},
		width:function(){return $(window).width();},
		height:function(){return $(window).height();},
		theight:function(){
			var d=document, b=d.body, e=d.documentElement;
			return Math.max(Math.max(b.scrollHeight,e.scrollHeight),Math.max(b.clientHeight,e.clientHeight))
		},
		twidth:function(){
			var d=document, b=d.body, e=d.documentElement;
			return Math.max(Math.max(b.scrollWidth,e.scrollWidth),Math.max(b.clientWidth,e.clientWidth))
		}
	}
}();
function openTinyPage(paramObj)
{
	// type 1  page 0 others e.g image flash ==
	// time 消失的秒数
	TINY.box.show(paramObj['url'],1,paramObj['width'],paramObj['height'],1);
}
function alertTinyBox(paramObj)
{
	TINY.box.show(paramObj['url'],0,0,0,1);
}
function alertTinyImage(paramObj)
{
	TINY.box.show(paramObj['content'],0,0,0,0,paramObj['time']);
}
function closeTinyBox()
{
	$('#tinyclosebt').click();
	if(removeAllErrorMessage)removeAllErrorMessage();
	if(removeAllRightMessage)removeAllRightMessage();
}
/**
T$('click_test1').onclick = function(){TINY.box.show('blank-for-test.html',1,300,150,1)}

var content2 = "<img width='640' height='466' src='http://image.zhangxinxu.com/image/study/s/mm10.jpg' />";
T$('click_test2').onclick = function(){TINY.box.show(content2,0,0,0,1)}

var content3 = "<object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' width='550' height='400'><param name='movie' value='../flash/as3_clock_2.swf' /><param name='quality' value='high' /><param name='wmode' value='opaque' /><embed height='400' width='550'  src='../flash/as3_clock_2.swf' type='application/x-shockwave-flash'></embed></object>";
T$('click_test3').onclick = function(){TINY.box.show(content3,0,0,25,1)}

var content4 = "该浮动层将在3秒钟内消失。";
T$('click_test4').onclick = function(){TINY.box.show(content4,0,0,0,0,3)}
 */
function getExplorerType()
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
}