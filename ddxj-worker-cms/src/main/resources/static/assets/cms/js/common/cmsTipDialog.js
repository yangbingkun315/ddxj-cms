function showConfirmDialog(options,confirmCallback,cancelCallback)
{
	if(typeof options == "string")
	{
		options = {message : options};
	}
	var options   = options || {};
	var message = options.message;
	var confirmBtn = options.confirm;
	if(typeof confirmBtn == "undefined")
	{
		confirmBtn = "确定";
	}
	var cancelBtn = options.cancel;
	if(typeof cancelBtn == "undefined")
	{
		cancelBtn = "取消";
	}
	var showRightBtn = options.showRightBtn;
	if(typeof showRightBtn == "undefined")
	{
		showRightBtn = true;
	}
	var iconClass = options.iconClass;
	if(typeof iconClass == "undefined")
	{
		iconClass = "warning";
	}
	
    var html = '<div class="tip_overlay none tip-dialog-container"> \
		    	</div> \
			    <div class="tip_content none tip-dialog-container"> \
			    	<div class="tip_title"> \
			    		<span>提示</span> \
			    		<a>×</a> \
			    	</div> \
			    	<div class="tip_body"> \
			    		<div class="tip_body_text"> \
			    			<span class="tip_body_icon ' + iconClass + '"></span> \
			    			<span class="tip_body_content">' + message + '</span> \
			    		</div> \
			    		<div class="tip_button"> \
			    			<a class="left_btn">' + confirmBtn + '</a>'
			    			if(showRightBtn)
			    			{
			    				html += '<a class="right_btn">' + cancelBtn + '</a>';
			    			}
    html += ' \
			    		</div> \
			    	</div> \
			    </div>';
    
    $(".tip-dialog-container").remove();
    $('body').append(html);
    if($("#TB_window").length > 0)
	{
		var zindex = $("#TB_window").css("z-index");
		if(zindex)
		{
			zindex = zindex * 1 + 1;
			$(".tip-dialog-container").css("z-index",zindex);
		}
	}
    
    $(".tip-dialog-container .left_btn").click(function(){
		$(".tip-dialog-container").hide();
		if(typeof confirmCallback == "function")
		{
			confirmCallback();
		}
	});
    $(".tip_content .tip_title a").click(function(){
    	$(".tip-dialog-container").hide();
    });
    $(".tip-dialog-container .right_btn").click(function(){
    	$(".tip-dialog-container").hide();
    	if(typeof cancelCallback == "function")
		{
    		cancelCallback();
		}
    });
    
    $(".tip-message4").remove();
    
    $(".tip-dialog-container").show();
    
    $(document).keyup(function(event){
		if (event.keyCode == 27)
    	{
			$(".tip-dialog-container").hide();
    	}
	});
}
function showSuccessDialog(options,callback)
{
	if(typeof options == "string")
	{
		options = {message : options};
	}
	options.showRightBtn = false;
	options.iconClass = "success";
	showConfirmDialog(options,callback);
}
function showErrorDialog(options,callback)
{
	if(typeof options == "string")
	{
		options = {message : options};
	}
	options.showRightBtn = false;
	showConfirmDialog(options,callback);
}
function showWarningDialog(options,callback)
{
	if(typeof options == "string")
	{
		options = {message : options};
	}
	options.showRightBtn = false;
	options.iconClass = "warning";
	showConfirmDialog(options,callback);
}