/**
 *	Cms Global Script	   
 */
$(function(){
	$(".tip_content .tip_title a,.tip_content .right_btn").click(function(){
		$(this).parents(".tip_content").prev(".tip_overlay").hide();
		$(this).parents(".tip_content").hide();
	});
});
alyCms.util = 
{
	/**
	 * 公共方法 验证码
	 */
	validateCaptcha : function(id_captcha,length){
		if(!length)
		{
			length = 4;
		}
		var value = $("#" + id_captcha).val();
		if(value == "" || value == $("#" + id_captcha).attr("backText"))
		{
			showErrorMessage(id_captcha,"抱歉，请输入验证码！");
			$("#" + id_captcha).attr("isError",true);
			return false;
		}
		else if(value.length > 0 && value.length < length)
		{
			showErrorMessage(id_captcha,"请输入四位数的验证码！");
			$("#" + id_captcha).attr("isError",true);
			return false;
		}
		else {
			$("#" + id_captcha).attr("isError",false);
			removeErrorMessage(id_captcha);
			return true;
		}
	},
	checkBoxOnclick : function(obj){
		$this = $(obj);
		var inputHtml = "";
		var value = $this.attr("value") ? $this.attr("value") : "";
		if($this.attr("inputName"))
		{
			inputHtml = "<input type=\"hidden\" isCheckBoxHidden=\"true\" value=\""+value+"\" name=\""+$this.attr("inputName")+"\">";
		}
		if($this.attr("isChecked") == "true")
		{
			$this.attr("class","checkBox_Noselected");
			$this.attr("isChecked","false");
			$this.next("input[type='hidden'][isCheckBoxHidden='true']").remove();
		}
		else
		{
			if($this.attr("mutiple") == "false")
			{
				$("a[type='checkBox'][name='" + $this.attr("name") + "']").each(function(){
					$(this).attr("class","checkBox_Noselected");
					$(this).attr("isChecked","false");
					$(this).next("input[type='hidden']").remove();
				});
			}
			
			$this.attr("class","checkBox_Selected");
			$this.attr("isChecked","true");
			$this.after(inputHtml);
		}
	},
	/**
	 * 公共方法 验证确认密码
	 */
	validateConfirmPassword : function(id_password,id_confirm){
		var password = $("#" + id_password).val();
		var confirmPwd = $("#" + id_confirm).val();;
		if (password == "") {
		    showErrorMessage(id_password,"抱歉，请输入密码后再确认密码！");
		    $("#" + id_password).attr("isError",true);
		    return false;
		} else {
			if (password.length < 6) {
				showErrorMessage(id_password,"抱歉，密码长度不能小于六位！");
			    $("#" + id_password).attr("isError",true);
			    return false;
			} else {
				if (confirmPwd != password) {
					showErrorMessage(id_confirm,"抱歉，你两次输入的密码不一致！");
				    $("#" + id_confirm).attr("isError",true);
				    return false;
				} else {
					$("#" + id_password).attr("isError",false);
					$("#" + id_confirm).attr("isError",false);
					removeErrorMessage(id_confirm);
				    return true;
				}
			}
		}
	},
	/**
	 * 公共方法 验证密码
	 */
	validatePassword : function(id_password){
		var password = $("#" + id_password).val();
		var reg = PATTERN_PASSWORD;
		if(password == "")
		{
			showErrorMessage(id_password,"抱歉，密码不能为空！");
		    $("#" + id_password).attr("isError",true);
		    return false;
		}
		else if (!reg.test(password)) {
			showErrorMessage(id_password,"抱歉，你输入密码不合规范！");
		    $("#" + id_password).attr("isError",true);
		    return false;
		} else if(password.length<6||password.length>16){
			showErrorMessage(id_password,"抱歉，密码长度必须在6到16位之间！");
		    $("#" + id_password).attr("isError",true);
		    return false;
		}else {
			$("#" + id_password).attr("isError",false);
			removeErrorMessage(id_password);
			return true;
		}
	},
	/**
	 * 公共方法 验证邮箱
	 */
	validateEmail : function(email_id){
		var value = $("#" + email_id).val();
		if(value == "" || value == $("#" + email_id).attr("backText"))
		{
			showErrorMessage(email_id,"抱歉，请输入邮箱！");
		    $(this).attr("isError",true);
		    return false;
		}
		else if(value.length < 6 || value.length > 32)
		{
			showErrorMessage(email_id,"抱歉，邮箱长度必须在6到32位之间！");
		    $("#" + email_id).attr("isError",true);
		    return false;
		}
		else if (!alyCms.base.isEmail(value)) {
			showErrorMessage(email_id,"抱歉，你输入的邮箱不合规范！");
		    $("#" + email_id).attr("isError",true);
		    return false;
		} else {
			$("#" + email_id).attr("isError",false);
			return true;
		}
	},
	/**
	 * 公共方法 验证用户名
	 */
	validateUserName : function(username_id){
		var username = $("#" + username_id).val();
		var backText = $("#" + username_id).attr("backText");
		if(!backText)
		{
			backText = "";
		}
		var userNameReg = new RegExp("^[A-Za-z0-9]+$");
	    if(username==null||username==""  || username == backText){
		  showErrorMessage(username_id,"抱歉，用户名不能为空！");
		  $("#" + username_id).attr("isError",true);
		  return false;
		}
	    else if(username.length < 3 || username.length > 14)
    	{
	    	showErrorMessage(username_id,"抱歉，用户名长度必须在3到14位之间！");
			$("#" + username_id).attr("isError",true);
			return false;
    	}
	    else if(alyCms.base.isEmail(username))
	    {
	    	showErrorMessage(username_id,"抱歉，用户名不能为邮箱！");
			$("#" + username_id).attr("isError",true);
			return false;
	    }
	    else if(!userNameReg.test(username))
	    {
	    	showErrorMessage(username_id,"抱歉，用户名只能是数字和字母！");
			$("#" + username_id).attr("isError",true);
			return false;
	    }
	    else
	    {
	    	$("#" + username_id).attr("isError",false);
			return true;
	    }
	}
};
/***********************************  End 	***************************************/