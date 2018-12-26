/**
 * include css file
 */
(function($) {
"use strict";

/**
 * Feature detection
 */
var feature = {};
feature.fileapi = $("<input type='file'/>").get(0).files !== undefined;
feature.formdata = window.FormData !== undefined;
$.fn.validateLength = function(){
	var index = 0 , length = this.length;
	for ( ; index < length; index++ )
	{
		var $thisDom = $(this[index]);
		var min = $thisDom.attr(FORM_FIELD_MINLENGTH);
		var max = $thisDom.attr(FORM_FIELD_MAXLENGTH);
		var required = $thisDom.attr(FORM_FIELD_REQUIRED);
		var value = $thisDom.val().length;
		if($thisDom.val() == $thisDom.attr("backText"))
		{
			value = 0;
		}
		var id = $thisDom.attr("id");
		var isError = false;
		var text = null;
		if(required == "true" && (value == 0  || $thisDom.val() == $thisDom.attr("backText")))
		{
			isError = true;
			text = $thisDom.attr(FORM_FIELD_BLANK_TEXT);
			if(!text)text = "抱歉，此项为必填项！";
		}
		else if(min & max && (value < min || value > max))
		{
			isError = true;
			text = $thisDom.attr(FORM_FIELD_LENGTH_TEXT);
			if(!text)text = "抱歉，此项长度必须在" + min + "到" + max + "位之间";
		}
		else if(max && value > max)
		{
			isError = true;
			text = $thisDom.attr(FORM_FIELD_LENGTH_TEXT);
			if(!text)text = "抱歉，此项最大长度为" + max;
		}
		else if(min && value < min)
		{
			isError = true;
			text = $thisDom.attr(FORM_FIELD_LENGTH_TEXT);
			if(!text)text = "抱歉，此项最小长度为" + min;
		}
		$thisDom.attr("isError",isError);
		if(isError)
		{
			showErrorMessage(id,text);
			return false;
		}
		else
		{
			return true;
		}
	}
};
$.fn.validate = function(param) {
	var index = 0 , length = this.length;
	for ( ; index < length; index++ )
	{
		var $thisForm = $(this[index]);
		if($thisForm.get(0).tagName.toLowerCase() == "form")
		{
			$thisForm.find("input").trigger('blur');
			$thisForm.find("textarea").trigger('blur');
			var numError = $thisForm.find("input[isError=true]").length + $thisForm.find("textarea[isError=true]").length;
			if(numError)
			{
				var text = $(this).attr(FORM_FIELD_FORM_ERROR_TEXT);
				if(!text)text = "请填写完整的信息！";
				alertTip(text,3);
				if(param && param.scrollTo != false)
				{
					window.scrollTo(0,$("div[divType='errorMessage']").eq(0).offset().top);
				}
				return false;
			}
			return true;
		}
		else
		{
			$(this).trigger('blur');
			return $(this).attr("isError") != "true";
		}
	}
};
/**
 * form validation
 * attr : require,maxLength,minLength,equalTo,fieldType:email/username or other,formErrorText
 * 
 */
$.fn.configValidate = function() {
	var index = 0 , length = this.length;
	for ( ; index < length; index++ )
	{
		var $thisForm = $(this[index]);
		if($thisForm.get(0).tagName.toLowerCase() == "form")
		{
			// validate input text
			$thisForm.find("input[type='text'][notConfig!='true']").each(function(){
				// bind back text
				/*if(!$(this).is("input[type='password']"))
				{
					$(this).bindBackTextEvent();
				}*/
				$(this).blur(function(){
					if(!$(this).is(":visible"))
					{
						$(this).attr("isError",false);
						return true;
					}
					if($(this).validateLength())
					{
						var fieldType = $(this).attr(FORM_FIELD_FIELDTYPE);
						// 如果是邮箱
						if(fieldType == "email" && $(this).val() != "")
						{
							alyCms.util.validateEmail($(this).attr("id"));
						}
						else if(fieldType == "username" && $(this).val() != "")
						{
							alyCms.util.validateUserName($(this).attr("id"));
						}
						else if((fieldType == "number" || fieldType == "decimal") && $(this).val() != "")
						{
							if($(this).val() == "" || $(this).val() == $(this).attr("backText"))
							{
								return true;// 相当于continue
							}
							$(this).val($.trim($(this).val()));
							var reg = null;
							var errorTipMsg = $(this).attr(FORM_FIELD_TYPE_ERROR_TEXT);
							if(fieldType == "number")
							{
								reg = new RegExp("^-?[0-9]*$");
								if(!errorTipMsg)errorTipMsg = "请输入整数数字！";
							}
							else if(fieldType == "decimal")
							{
								reg = new RegExp(/^-?\d+(\.\d{1,2})?$/);
								if(!errorTipMsg)errorTipMsg = "请输入数字,限两位小数！";
							}
						    if(!reg.test($(this).val())){
						    	showErrorMessage($(this).attr("id"),errorTipMsg);
						    	$(this).attr("isError",true);
						    }
						    else
						    {
						    	var validateMinMax = true;
						    	if($(this).attr(FORM_FIELD_MINVALUE))
							    {
							    	if($(this).val()*1 < $(this).attr(FORM_FIELD_MINVALUE)*1)
							    	{
							    		showErrorMessage($(this).attr("id"),"此项的最小值为"+ $(this).attr(FORM_FIELD_MINVALUE) +"！");
							    		$(this).attr("isError",true);
							    		validateMinMax = false;
							    	}
							    }
						    	
						    	if(validateMinMax == true)
						    	{
						    		if($(this).attr(FORM_FIELD_MAXVALUE))
						    		{
						    			if($(this).val()*1 > $(this).attr(FORM_FIELD_MAXVALUE)*1)
						    			{
						    				showErrorMessage($(this).attr("id"),"此项的最大值为"+ $(this).attr(FORM_FIELD_MAXVALUE) +"！");
						    				$(this).attr("isError",true);
						    				validateMinMax = false;
						    			}
						    		}
						    	}
					    		if(validateMinMax)
					    		{
					    			 var text = $(this).attr(FORM_FIELD_RIGHT_MESSAGE);
									 if(!text)text = "恭喜，你输入的格式正确！";
									 if("true" == $(this).attr(FORM_FIELD_SHOW_RIGHT_MSG))
									 {
									 	showRightMessage($(this).attr("id"),text);
									 }
									 removeErrorMessage($(this).attr("id"));
									 $(this).attr("isError",false);
					    		}
						    }
						}
						else
						{
							var text = $(this).attr(FORM_FIELD_RIGHT_MESSAGE);
							if(!text)text = "恭喜，你输入的格式正确！";
							if("true" == $(this).attr(FORM_FIELD_SHOW_RIGHT_MSG))
							{
								showRightMessage($(this).attr("id"),text);
							}
							removeErrorMessage($(this).attr("id"));
							$(this).attr("isError",false);
						}
					}
				});
			});
			/*// validate input password
			$thisForm.find("input[type='password'][notConfig!='true']").each(function(){
				$(this).blur(function(){
					if(!$(this).is(":visible"))
					{
						$(this).attr("isError",false);
						return true;
					}
					if($(this).validateLength() && alyCms.util.validatePassword($(this).attr("id")))
					{
						if($(this).attr(FORM_FIELD_EQUALTO))
						{
							alyCms.util.validateConfirmPassword($(this).attr("id"),$(this).attr(FORM_FIELD_EQUALTO));
						}
					}
				});
			});*/
			// validate input checkbox
			$thisForm.find("input[type='checkbox'][notConfig!='true']").each(function(){
				$(this).blur(function(){
					if(!$(this).is(":visible"))
					{
						$(this).attr("isError",false);
						return true;
					}
					
				});
			});
			// validate input radio
			$thisForm.find("input[type='radio'][notConfig!='true']").each(function(){
				$(this).blur(function(){
					if(!$(this).is(":visible"))
					{
						$(this).attr("isError",false);
						return true;
					}
					if($(this).attr(FORM_FIELD_REQUIRED) == "true")
					{
						var value = $('input[name="'+$(this).attr("name")+'"][type="radio"]:checked').val();
						if(!value)
						{
							var text = $(this).attr(FORM_FIELD_BLANK_TEXT);
							if(!text)text = "抱歉，此项为必选项";
							showErrorMessage($(this).attr("id"),text);
							$(this).attr("isError",false);
						}
					}
				});
			});
			// validate texteara
			$thisForm.find("textarea[notConfig!='true']").each(function(){
				$(this).blur(function(){
					if(!$(this).is(":visible"))
					{
						$(this).attr("isError",false);
						return true;
					}
					if($(this).validateLength())
					{
						var text = $(this).attr(FORM_FIELD_RIGHT_MESSAGE);
						if(!text)text = "恭喜，你输入的格式正确！";
						if("true" == $(this).attr(FORM_FIELD_SHOW_RIGHT_MSG))
						{
							showRightMessage($(this).attr("id"),text);
						}
						removeErrorMessage($(this).attr("id"));
						$(this).attr("isError",false);
					}
				});
			});
			// validate select
			$thisForm.find("select[notConfig!='true']").each(function(){
				$(this).blur(function(){
					if(!$(this).is(":visible"))
					{
						$(this).attr("isError",false);
						return true;
					}
					
				});
			});
			// validate input file
			$thisForm.find("input[type='file'][notConfig!='true']").each(function(){
				$(this).blur(function(){
					if(!$(this).is(":visible"))
					{
						$(this).attr("isError",false);
						return true;
					}
					if($(this).validateLength())
					{
						var text = $(this).attr(FORM_FIELD_RIGHT_MESSAGE);
						if(!text)text = "恭喜，你输入的格式正确！";
						if("true" == $(this).attr(FORM_FIELD_SHOW_RIGHT_MSG))
						{
							showRightMessage($(this).attr("id"),text);
						}
						removeErrorMessage($(this).attr("id"));
						$(this).attr("isError",false);
					}
				});
			});
		}
	}
};
/**
 * ajaxSubmit() provides a mechanism for immediately submitting
 * an HTML form using AJAX.
 */
$.fn.submitData = function(options) {
	this.ajaxSubmit(options);
};
$.fn.cmsSubmit = function(options)
{
	if(!options)options = {};
	if (!this.length) 
	{
       log('ajaxSubmit: skipping submit process - no element selected');
       return this;
    }
	if (typeof options == 'function') {
        options = { success: options };
    }
	$._configCmsForm_(this,options);
	if(!(options.cmsForm && options.cmsForm === true))
	{
		if(options.closeFormOverLayCallBack)
		{
			_close_FormOverLay_CallBack_Function_ = options.closeFormOverLayCallBack;
		}
		
		var form = $(this);
		// 为了解决IE下处理性能差
		setTimeout(function(){
			form.submit();
		},100);
	}
};
$.fn.cmsForm = function(options) {
	if(!options)options = {};
	if (typeof options == 'function') {
        options = { success: options };
    }
	options.cmsForm = true;
	this.cmsSubmit(options);
};
$.fn.bindBackTextEvent = function(text){
	var index = 0 , length = this.length;
	for ( ; index < length; index++ )
	{
		if(!text)
		{
			text = $(this).attr(FORM_FIELD_REPLACE_HOLDER);
		}
		if(!text)
		{
			continue ;
		}
		var $thisInput = $(this[index]);
		if($._isTextInputForJqueryPopUpMessage_($thisInput))
		{
			$thisInput.attr("backText",text);
			var thisInputOnBlur = function(thisObj){
				if($(thisObj).val() == "")
				{
					// bakup font color
					if($(thisObj).attr("color"))
					{
						$(thisObj).attr("bakFontColor",$(thisObj).attr("color"));
					}
					else
					{
						$(thisObj).attr("bakFontColor","");
					}
					$(thisObj).val(text);
					$(thisObj).css("color","#CCC");
				}
			};
			$thisInput.focus(function(){
				$(this).css("color",$(this).attr("bakFontColor") + "");
				if($(this).val() == text)
				{
					$(this).val("");
				}
			});
			$thisInput.blur(function(){
				thisInputOnBlur($thisInput);
				
			});
			thisInputOnBlur($thisInput);
		}
	}	
};
$.fn.bindInputClearEvent = function(options) {
	var index = 0 , length = this.length;
	for ( ; index < length; index++ )
	{
		var $thisInput = $(this[index]);
		if($._isTextInputForJqueryPopUpMessage_($thisInput))
		{
			$thisInput.keyup(function(){
				if($(this).val() != "")
				{
					var clearBarId = "#clearBar_" + $(this).attr("id");
					if($(clearBarId).length == 0)
					{
						var html = '<span id="clearBar_' + $(this).attr("id") + '" fartherId = "' + $(this).attr("id") + '" style="position:absolute;display:none;cursor:pointer;" title="点击清除" class="_j_delete_bt_"></span>';
						$('body').append(html);
						$(clearBarId).click(function(){
							var fartherId = $(this).attr("fartherId");
							$("#" + fartherId).val("");
							$(this).remove();
							$("#" + fartherId).focus();
						});
						var paddingTop = $(this).css("padding-top");
						if($._isTextInputForJqueryPopUpMessage_($(this)))
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
						var top = $(this).offset().top + ($(this).height() / 2)  + parseInt(paddingTop) - 8;
						var left = $(this).offset().left + $(this).width() - 20;
						$(clearBarId).css("top",top + "px");
						$(clearBarId).css("left",left + "px");
						$(clearBarId).fadeIn(500);
					}
				}
				else
				{
					$("#clearBar_" + $(this).attr("id")).remove();
				}
			});
		}
	}
};
$.fn.ajaxSubmit = function(options) {
    /*jshint scripturl:true */

    // fast fail if nothing selected (http://dev.jquery.com/ticket/2752)
    if (!this.length) {
        log('ajaxSubmit: skipping submit process - no element selected');
        return this;
    }
    
    var method, action, url, $form = this;

    if (typeof options == 'function') {
        options = { success: options };
    }

    method = this.attr('method');
    action = this.attr('action');
    url = (typeof action === 'string') ? $.trim(action) : '';
    url = url || window.location.href || '';
    if (url) {
        // clean url (don't include hash vaue)
        url = (url.match(/^([^#]+)/)||[])[1];
    }

    options = $.extend(true, {
        url:  url,
        success: $.ajaxSettings.success,
        type: method || 'GET',
        iframeSrc: /^https/i.test(window.location.href || '') ? 'javascript:false' : 'about:blank'
    }, options);
    if(options.closeFormOverLayCallBack)
	{
		_close_FormOverLay_CallBack_Function_ = options.closeFormOverLayCallBack;
	}
    // hook for manipulating the form data before it is extracted;
    // convenient for use with rich editors like tinyMCE or FCKEditor
    var veto = {};
    this.trigger('form-pre-serialize', [this, options, veto]);
    if (veto.veto) {
        log('ajaxSubmit: submit vetoed via form-pre-serialize trigger');
        return this;
    }
    // 清除value=placeholder的值
    this.find("input[type='text']").each(function(){
		if($(this).val() == $(this).attr("placeholder"))
		{
			$(this).val("");
		}
	});
    
    // provide opportunity to alter form data before it is serialized
    if (options.beforeSerialize && options.beforeSerialize(this, options) === false) {
        log('ajaxSubmit: submit aborted via beforeSerialize callback');
        return this;
    }

    var traditional = options.traditional;
    if ( traditional === undefined ) {
        traditional = $.ajaxSettings.traditional;
    }
    
    var elements = [];
    var qx, a = this.formToArray(options.semantic, elements);
    if (options.data) {
        options.extraData = options.data;
        qx = $.param(options.data, traditional);
    }

    // give pre-submit callback an opportunity to abort the submit
    if (options.beforeSubmit && options.beforeSubmit(a, this, options) === false) {
        log('ajaxSubmit: submit aborted via beforeSubmit callback');
        return this;
    }

    // fire vetoable 'validate' event
    this.trigger('form-submit-validate', [a, this, options, veto]);
    if (veto.veto) {
        log('ajaxSubmit: submit vetoed via form-submit-validate trigger');
        return this;
    }

    var q = $.param(a, traditional);
    if (qx) {
        q = ( q ? (q + '&' + qx) : qx );
    }    
    if (options.type.toUpperCase() == 'GET') {
        options.url += (options.url.indexOf('?') >= 0 ? '&' : '?') + q;
        options.data = null;  // data is null for 'get'
    }
    else {
        options.data = q; // data is the query string for 'post'
    }

    var callbacks = [];
    if (options.resetForm) {
        callbacks.push(function() { $form.resetForm(); });
    }
    if (options.clearForm) {
        callbacks.push(function() { $form.clearForm(options.includeHidden); });
    }

    // perform a load on the target only if dataType is not provided
    if (!options.dataType && options.target) {
        var oldSuccess = options.success || function(){};
        callbacks.push(function(data) {
            var fn = options.replaceTarget ? 'replaceWith' : 'html';
            $(options.target)[fn](data).each(oldSuccess, arguments);
        });
    }
    else if (options.success) {
        callbacks.push(options.success);
    }

    options.success = function(data, status, xhr) { // jQuery 1.4+ passes xhr as 3rd arg
        var context = options.context || this ;    // jQuery 1.4+ supports scope context 
        for (var i=0, max=callbacks.length; i < max; i++) {
            callbacks[i].apply(context, [data, status, xhr || $form, $form]);
        }
    };
    
    if(typeof options.error != "function")
    {
    	options.error = function(data){
    		if(data.status == 500)
			{
				$.removeFormOverLay("1036");
			}
    	};
    }

    // are there files to upload?
    var fileInputs = $('input:file:enabled[value]', this); // [value] (issue #113)
    var hasFileInputs = fileInputs.length > 0;
    var mp = 'multipart/form-data';
    var multipart = ($form.attr('enctype') == mp || $form.attr('encoding') == mp);

    var fileAPI = feature.fileapi && feature.formdata;
    log("fileAPI :" + fileAPI);
    var shouldUseFrame = (hasFileInputs || multipart) && !fileAPI;

    // start post data
    $._form_show_overlay_(options.warningMessage ? options.warningMessage : "正在加载,请稍后...");
	// add ajax submit flag
    if(options.data)
    {
    	options.data += "&ajaxHttpRequest=true";
    }
    
    // options.iframe allows user to force iframe mode
    // 06-NOV-09: now defaulting to iframe mode if file input is detected
    if (options.iframe !== false && (options.iframe || shouldUseFrame)) {
        // hack to fix Safari hang (thanks to Tim Molendijk for this)
        // see:  http://groups.google.com/group/jquery-dev/browse_thread/thread/36395b7ab510dd5d
        if (options.closeKeepAlive) {
            $.get(options.closeKeepAlive, function() {
                fileUploadIframe(a);
            });
        }
          else {
            fileUploadIframe(a);
          }
    }
    else if ((hasFileInputs || multipart) && fileAPI) {
        fileUploadXhr(a);
    }
    else {
        $.ajax(options);
    }

    // clear element array
    for (var k=0; k < elements.length; k++)
        elements[k] = null;

    // fire 'notify' event
    this.trigger('form-submit-notify', [this, options]);
    return this;

    // utility fn for deep serialization
    function deepSerialize(extraData){
        var serialized = $.param(extraData).split('&');
        var len = serialized.length;
        var result = {};
        var i, part;
        for (i=0; i < len; i++) {
            part = serialized[i].split('=');
            result[decodeURIComponent(part[0])] = decodeURIComponent(part[1]);
        }
        return result;
    }

     // XMLHttpRequest Level 2 file uploads (big hat tip to francois2metz)
    function fileUploadXhr(a) {
        var formdata = new FormData();

        for (var i=0; i < a.length; i++) {
            formdata.append(a[i].name, a[i].value);
        }

        if (options.extraData) {
            var serializedData = deepSerialize(options.extraData);
            for (var p in serializedData)
                if (serializedData.hasOwnProperty(p))
                    formdata.append(p, serializedData[p]);
        }

        options.data = null;

        var s = $.extend(true, {}, $.ajaxSettings, options, {
            contentType: false,
            processData: false,
            cache: false,
            type: 'POST'
        });
        
        if (options.uploadProgress) {
            // workaround because jqXHR does not expose upload property
            s.xhr = function() {
                var xhr = jQuery.ajaxSettings.xhr();
                if (xhr.upload) {
                    xhr.upload.onprogress = function(event) {
                        var percent = 0;
                        var position = event.loaded || event.position; /*event.position is deprecated*/
                        var total = event.total;
                        if (event.lengthComputable) {
                            percent = Math.ceil(position / total * 100);
                        }
                        options.uploadProgress(event, position, total, percent);
                    };
                }
                return xhr;
            };
        }

        s.data = null;
            var beforeSend = s.beforeSend;
            s.beforeSend = function(xhr, o) {
                o.data = formdata;
                if(beforeSend)
                    beforeSend.call(this, xhr, o);
        };
        $.ajax(s);
    }

    // private function for handling file uploads (hat tip to YAHOO!)
    function fileUploadIframe(a) {
        var form = $form[0], el, i, s, g, id, $io, io, xhr, sub, n, timedOut, timeoutHandle;
        var useProp = !!$.fn.prop;

        if ($(':input[name=submit],:input[id=submit]', form).length) {
            // if there is an input with a name or id of 'submit' then we won't be
            // able to invoke the submit fn on the form (at least not x-browser)
            alert('Error: Form elements must not have name or id of "submit".');
            return;
        }
        
        if (a) {
            // ensure that every serialized input is still enabled
            for (i=0; i < elements.length; i++) {
                el = $(elements[i]);
                if ( useProp )
                    el.prop('disabled', false);
                else
                    el.removeAttr('disabled');
            }
        }

        s = $.extend(true, {}, $.ajaxSettings, options);
        s.context = s.context || s;
        id = 'jqFormIO' + (new Date().getTime());
        if (s.iframeTarget) {
            $io = $(s.iframeTarget);
            n = $io.attr('name');
            if (!n)
                 $io.attr('name', id);
            else
                id = n;
        }
        else {
            $io = $('<iframe name="' + id + '" src="'+ s.iframeSrc +'" />');
            $io.css({ position: 'absolute', top: '-1000px', left: '-1000px' });
        }
        io = $io[0];


        xhr = { // mock object
            aborted: 0,
            responseText: null,
            responseXML: null,
            status: 0,
            statusText: 'n/a',
            getAllResponseHeaders: function() {},
            getResponseHeader: function() {},
            setRequestHeader: function() {},
            abort: function(status) {
                var e = (status === 'timeout' ? 'timeout' : 'aborted');
                log('aborting upload... ' + e);
                this.aborted = 1;
                // #214
                if (io.contentWindow.document.execCommand) {
                    try { // #214
                        io.contentWindow.document.execCommand('Stop');
                    } catch(ignore) {}
                }
                $io.attr('src', s.iframeSrc); // abort op in progress
                xhr.error = e;
                if (s.error)
                    s.error.call(s.context, xhr, e, status);
                if (g)
                    $.event.trigger("ajaxError", [xhr, s, e]);
                if (s.complete)
                    s.complete.call(s.context, xhr, e);
            }
        };

        g = s.global;
        // trigger ajax global events so that activity/block indicators work like normal
        if (g && 0 === $.active++) {
            $.event.trigger("ajaxStart");
        }
        if (g) {
            $.event.trigger("ajaxSend", [xhr, s]);
        }

        if (s.beforeSend && s.beforeSend.call(s.context, xhr, s) === false) {
            if (s.global) {
                $.active--;
            }
            return;
        }
        if (xhr.aborted) {
            return;
        }

        // add submitting element to data if we know it
        sub = form.clk;
        if (sub) {
            n = sub.name;
            if (n && !sub.disabled) {
                s.extraData = s.extraData || {};
                s.extraData[n] = sub.value;
                if (sub.type == "image") {
                    s.extraData[n+'.x'] = form.clk_x;
                    s.extraData[n+'.y'] = form.clk_y;
                }
            }
        }
        
        var CLIENT_TIMEOUT_ABORT = 1;
        var SERVER_ABORT = 2;

        function getDoc(frame) {
            var doc = frame.contentWindow ? frame.contentWindow.document : frame.contentDocument ? frame.contentDocument : frame.document;
            return doc;
        }
        
        // Rails CSRF hack (thanks to Yvan Barthelemy)
        var csrf_token = $('meta[name=csrf-token]').attr('content');
        var csrf_param = $('meta[name=csrf-param]').attr('content');
        if (csrf_param && csrf_token) {
            s.extraData = s.extraData || {};
            s.extraData[csrf_param] = csrf_token;
        }

        // take a breath so that pending repaints get some cpu time before the upload starts
        function doSubmit() {
            // make sure form attrs are set
            var t = $form.attr('target'), a = $form.attr('action');

            // update form attrs in IE friendly way
            form.setAttribute('target',id);
            if (!method) {
                form.setAttribute('method', 'POST');
            }
            if (a != s.url) {
                form.setAttribute('action', s.url);
            }

            // ie borks in some cases when setting encoding
            if (! s.skipEncodingOverride && (!method || /post/i.test(method))) {
                $form.attr({
                    encoding: 'multipart/form-data',
                    enctype:  'multipart/form-data'
                });
            }

            // support timout
            if (s.timeout) {
                timeoutHandle = setTimeout(function() { timedOut = true; cb(CLIENT_TIMEOUT_ABORT); }, s.timeout);
            }
            
            // look for server aborts
            function checkState() {
                try {
                    var state = getDoc(io).readyState;
                    log('state = ' + state);
                    if (state && state.toLowerCase() == 'uninitialized')
                        setTimeout(checkState,50);
                }
                catch(e) {
                    log('Server abort: ' , e, ' (', e.name, ')');
                    cb(SERVER_ABORT);
                    if (timeoutHandle)
                        clearTimeout(timeoutHandle);
                    timeoutHandle = undefined;
                }
            }

            // add "extra" data to form if provided in options
            var extraInputs = [];
            try {
                if (s.extraData) {
                    for (var n in s.extraData) {
                        if (s.extraData.hasOwnProperty(n)) {
                           // if using the $.param format that allows for multiple values with the same name
                           if($.isPlainObject(s.extraData[n]) && s.extraData[n].hasOwnProperty('name') && s.extraData[n].hasOwnProperty('value')) {
                               extraInputs.push(
                               $('<input type="hidden" name="'+s.extraData[n].name+'">').attr('value',s.extraData[n].value)
                                   .appendTo(form)[0]);
                           } else {
                               extraInputs.push(
                               $('<input type="hidden" name="'+n+'">').attr('value',s.extraData[n])
                                   .appendTo(form)[0]);
                           }
                        }
                    }
                }

                if (!s.iframeTarget) {
                    // add iframe to doc and submit the form
                    $io.appendTo('body');
                    if (io.attachEvent)
                        io.attachEvent('onload', cb);
                    else
                        io.addEventListener('load', cb, false);
                }
                setTimeout(checkState,15);
                form.submit();
            }
            finally {
                // reset attrs and remove "extra" input elements
                form.setAttribute('action',a);
                if(t) {
                    form.setAttribute('target', t);
                } else {
                    $form.removeAttr('target');
                }
                $(extraInputs).remove();
            }
        }

        if (s.forceSync) {
            doSubmit();
        }
        else {
            setTimeout(doSubmit, 10); // this lets dom updates render
        }

        var data, doc, domCheckCount = 50, callbackProcessed;

        function cb(e) {
            if (xhr.aborted || callbackProcessed) {
                return;
            }
            try {
                doc = getDoc(io);
            }
            catch(ex) {
                log('cannot access response document: ', ex);
                e = SERVER_ABORT;
            }
            if (e === CLIENT_TIMEOUT_ABORT && xhr) {
                xhr.abort('timeout');
                return;
            }
            else if (e == SERVER_ABORT && xhr) {
                xhr.abort('server abort');
                return;
            }

            if (!doc || doc.location.href == s.iframeSrc) {
                // response not received yet
                if (!timedOut)
                    return;
            }
            if (io.detachEvent)
                io.detachEvent('onload', cb);
            else    
                io.removeEventListener('load', cb, false);

            var status = 'success', errMsg;
            try {
                if (timedOut) {
                    throw 'timeout';
                }

                var isXml = s.dataType == 'xml' || doc.XMLDocument || $.isXMLDoc(doc);
                log('isXml='+isXml);
                if (!isXml && window.opera && (doc.body === null || !doc.body.innerHTML)) {
                    if (--domCheckCount) {
                        // in some browsers (Opera) the iframe DOM is not always traversable when
                        // the onload callback fires, so we loop a bit to accommodate
                        log('requeing onLoad callback, DOM not available');
                        setTimeout(cb, 250);
                        return;
                    }
                    // let this fall through because server response could be an empty document
                    //log('Could not access iframe DOM after mutiple tries.');
                    //throw 'DOMException: not available';
                }

                //log('response detected');
                var docRoot = doc.body ? doc.body : doc.documentElement;
                xhr.responseText = docRoot ? docRoot.innerHTML : null;
                xhr.responseXML = doc.XMLDocument ? doc.XMLDocument : doc;
                if (isXml)
                    s.dataType = 'xml';
                xhr.getResponseHeader = function(header){
                    var headers = {'content-type': s.dataType};
                    return headers[header];
                };
                // support for XHR 'status' & 'statusText' emulation :
                if (docRoot) {
                    xhr.status = Number( docRoot.getAttribute('status') ) || xhr.status;
                    xhr.statusText = docRoot.getAttribute('statusText') || xhr.statusText;
                }

                var dt = (s.dataType || '').toLowerCase();
                var scr = /(json|script|text)/.test(dt);
                if (scr || s.textarea) {
                    // see if user embedded response in textarea
                    var ta = doc.getElementsByTagName('textarea')[0];
                    if (ta) {
                        xhr.responseText = ta.value;
                        // support for XHR 'status' & 'statusText' emulation :
                        xhr.status = Number( ta.getAttribute('status') ) || xhr.status;
                        xhr.statusText = ta.getAttribute('statusText') || xhr.statusText;
                    }
                    else if (scr) {
                        // account for browsers injecting pre around json response
                        var pre = doc.getElementsByTagName('pre')[0];
                        var b = doc.getElementsByTagName('body')[0];
                        if (pre) {
                            xhr.responseText = pre.textContent ? pre.textContent : pre.innerText;
                        }
                        else if (b) {
                            xhr.responseText = b.textContent ? b.textContent : b.innerText;
                        }
                    }
                }
                else if (dt == 'xml' && !xhr.responseXML && xhr.responseText) {
                    xhr.responseXML = toXml(xhr.responseText);
                }

                try {
                    data = httpData(xhr, dt, s);
                }
                catch (e) {
                    status = 'parsererror';
                    xhr.error = errMsg = (e || status);
                }
            }
            catch (e) {
                log('error caught: ',e);
                status = 'error';
                xhr.error = errMsg = (e || status);
            }

            if (xhr.aborted) {
                log('upload aborted');
                status = null;
            }

            if (xhr.status) { // we've set xhr.status
                status = (xhr.status >= 200 && xhr.status < 300 || xhr.status === 304) ? 'success' : 'error';
            }

            // ordering of these callbacks/triggers is odd, but that's how $.ajax does it
            if (status === 'success') {
                if (s.success)
                    s.success.call(s.context, data, 'success', xhr);
                if (g)
                    $.event.trigger("ajaxSuccess", [xhr, s]);
            }
            else if (status) {
                if (errMsg === undefined)
                    errMsg = xhr.statusText;
                if (s.error)
                    s.error.call(s.context, xhr, status, errMsg);
                if (g)
                    $.event.trigger("ajaxError", [xhr, s, errMsg]);
            }

            if (g)
                $.event.trigger("ajaxComplete", [xhr, s]);

            if (g && ! --$.active) {
                $.event.trigger("ajaxStop");
            }

            if (s.complete)
                s.complete.call(s.context, xhr, status);

            callbackProcessed = true;
            if (s.timeout)
                clearTimeout(timeoutHandle);

            // clean up
            setTimeout(function() {
                if (!s.iframeTarget)
                    $io.remove();
                xhr.responseXML = null;
            }, 100);
        }

        var toXml = $.parseXML || function(s, doc) { // use parseXML if available (jQuery 1.5+)
            if (window.ActiveXObject) {
                doc = new ActiveXObject('Microsoft.XMLDOM');
                doc.async = 'false';
                doc.loadXML(s);
            }
            else {
                doc = (new DOMParser()).parseFromString(s, 'text/xml');
            }
            return (doc && doc.documentElement && doc.documentElement.nodeName != 'parsererror') ? doc : null;
        };
        var parseJSON = $.parseJSON || function(s) {
            /*jslint evil:true */
            return window['eval']('(' + s + ')');
        };

        var httpData = function( xhr, type, s ) { // mostly lifted from jq1.4.4

            var ct = xhr.getResponseHeader('content-type') || '',
                xml = type === 'xml' || !type && ct.indexOf('xml') >= 0,
                data = xml ? xhr.responseXML : xhr.responseText;

            if (xml && data.documentElement.nodeName === 'parsererror') {
                if ($.error)
                    $.error('parsererror');
            }
            if (s && s.dataFilter) {
                data = s.dataFilter(data, type);
            }
            if (typeof data === 'string') {
                if (type === 'json' || !type && ct.indexOf('json') >= 0) {
                    data = parseJSON(data);
                } else if (type === "script" || !type && ct.indexOf("javascript") >= 0) {
                    $.globalEval(data);
                }
            }
            return data;
        };
    }
};

/**
 * ajaxForm() provides a mechanism for fully automating form submission.
 *
 * The advantages of using this method instead of ajaxSubmit() are:
 *
 * 1: This method will include coordinates for <input type="image" /> elements (if the element
 *    is used to submit the form).
 * 2. This method will include the submit element's name/value data (for the element that was
 *    used to submit the form).
 * 3. This method binds the submit() method to the form for you.
 *
 * The options argument for ajaxForm works exactly as it does for ajaxSubmit.  ajaxForm merely
 * passes the options argument along after properly binding events for submit elements and
 * the form itself.
 */
$.fn.ajaxForm = function(options) {
    options = options || {};
    options.delegation = options.delegation && $.isFunction($.fn.on);
    
    // in jQuery 1.3+ we can fix mistakes with the ready state
    if (!options.delegation && this.length === 0) {
        var o = { s: this.selector, c: this.context };
        if (!$.isReady && o.s) {
            log('DOM not ready, queuing ajaxForm');
            $(function() {
                $(o.s,o.c).ajaxForm(options);
            });
            return this;
        }
        // is your DOM ready?  http://docs.jquery.com/Tutorials:Introducing_$(document).ready()
        log('terminating; zero elements found by selector' + ($.isReady ? '' : ' (DOM not ready)'));
        return this;
    }

    if ( options.delegation ) {
        $(document)
            .off('submit.form-plugin', this.selector, doAjaxSubmit)
            .off('click.form-plugin', this.selector, captureSubmittingElement)
            .on('submit.form-plugin', this.selector, options, doAjaxSubmit)
            .on('click.form-plugin', this.selector, options, captureSubmittingElement);
        return this;
    }

    return this.ajaxFormUnbind()
        .bind('submit.form-plugin', options, doAjaxSubmit)
        .bind('click.form-plugin', options, captureSubmittingElement);
};

// private event handlers    
function doAjaxSubmit(e) {
    /*jshint validthis:true */
    var options = e.data;
    if (!e.isDefaultPrevented()) { // if event has been canceled, don't proceed
        e.preventDefault();
        $(this).ajaxSubmit(options);
    }
}
    
function captureSubmittingElement(e) {
    /*jshint validthis:true */
    var target = e.target;
    var $el = $(target);
    if (!($el.is(":submit,input:image"))) {
        // is this a child element of the submit el?  (ex: a span within a button)
        var t = $el.closest(':submit');
        if (t.length === 0) {
            return;
        }
        target = t[0];
    }
    var form = this;
    form.clk = target;
    if (target.type == 'image') {
        if (e.offsetX !== undefined) {
            form.clk_x = e.offsetX;
            form.clk_y = e.offsetY;
        } else if (typeof $.fn.offset == 'function') {
            var offset = $el.offset();
            form.clk_x = e.pageX - offset.left;
            form.clk_y = e.pageY - offset.top;
        } else {
            form.clk_x = e.pageX - target.offsetLeft;
            form.clk_y = e.pageY - target.offsetTop;
        }
    }
    // clear form vars
    setTimeout(function() { form.clk = form.clk_x = form.clk_y = null; }, 100);
}


// ajaxFormUnbind unbinds the event handlers that were bound by ajaxForm
$.fn.ajaxFormUnbind = function() {
    return this.unbind('submit.form-plugin click.form-plugin');
};

/**
 * formToArray() gathers form element data into an array of objects that can
 * be passed to any of the following ajax functions: $.get, $.post, or load.
 * Each object in the array has both a 'name' and 'value' property.  An example of
 * an array for a simple login form might be:
 *
 * [ { name: 'username', value: 'jresig' }, { name: 'password', value: 'secret' } ]
 *
 * It is this array that is passed to pre-submit callback functions provided to the
 * ajaxSubmit() and ajaxForm() methods.
 */
$.fn.formToArray = function(semantic, elements) {
    var a = [];
    if (this.length === 0) {
        return a;
    }

    var form = this[0];
    var els = semantic ? form.getElementsByTagName('*') : form.elements;
    if (!els) {
        return a;
    }

    var i,j,n,v,el,max,jmax;
    for(i=0, max=els.length; i < max; i++) {
        el = els[i];
        n = el.name;
        if (!n) {
            continue;
        }

        if (semantic && form.clk && el.type == "image") {
            // handle image inputs on the fly when semantic == true
            if(!el.disabled && form.clk == el) {
                a.push({name: n, value: $(el).val(), type: el.type });
                a.push({name: n+'.x', value: form.clk_x}, {name: n+'.y', value: form.clk_y});
            }
            continue;
        }

        v = $.fieldValue(el, true);
        if (v && v.constructor == Array) {
            if (elements) 
                elements.push(el);
            for(j=0, jmax=v.length; j < jmax; j++) {
                a.push({name: n, value: v[j]});
            }
        }
        else if (feature.fileapi && el.type == 'file' && !el.disabled) {
            if (elements) 
                elements.push(el);
            var files = el.files;
            if (files.length) {
                for (j=0; j < files.length; j++) {
                    a.push({name: n, value: files[j], type: el.type});
                }
            }
            else {
                // #180
                a.push({ name: n, value: '', type: el.type });
            }
        }
        else if (v !== null && typeof v != 'undefined') {
            if (elements) 
                elements.push(el);
            a.push({name: n, value: v, type: el.type, required: el.required});
        }
    }

    if (!semantic && form.clk) {
        // input type=='image' are not found in elements array! handle it here
        var $input = $(form.clk), input = $input[0];
        n = input.name;
        if (n && !input.disabled && input.type == 'image') {
            a.push({name: n, value: $input.val()});
            a.push({name: n+'.x', value: form.clk_x}, {name: n+'.y', value: form.clk_y});
        }
    }
    return a;
};

/**
 * Serializes form data into a 'submittable' string. This method will return a string
 * in the format: name1=value1&amp;name2=value2
 */
$.fn.formSerialize = function(semantic) {
    //hand off to jQuery.param for proper encoding
    return $.param(this.formToArray(semantic));
};

/**
 * Serializes all field elements in the jQuery object into a query string.
 * This method will return a string in the format: name1=value1&amp;name2=value2
 */
$.fn.fieldSerialize = function(successful) {
    var a = [];
    this.each(function() {
        var n = this.name;
        if (!n) {
            return;
        }
        var v = $.fieldValue(this, successful);
        if (v && v.constructor == Array) {
            for (var i=0,max=v.length; i < max; i++) {
                a.push({name: n, value: v[i]});
            }
        }
        else if (v !== null && typeof v != 'undefined') {
            a.push({name: this.name, value: v});
        }
    });
    //hand off to jQuery.param for proper encoding
    return $.param(a);
};

/**
 * Returns the value(s) of the element in the matched set.  For example, consider the following form:
 *
 *  <form><fieldset>
 *      <input name="A" type="text" />
 *      <input name="A" type="text" />
 *      <input name="B" type="checkbox" value="B1" />
 *      <input name="B" type="checkbox" value="B2"/>
 *      <input name="C" type="radio" value="C1" />
 *      <input name="C" type="radio" value="C2" />
 *  </fieldset></form>
 *
 *  var v = $(':text').fieldValue();
 *  // if no values are entered into the text inputs
 *  v == ['','']
 *  // if values entered into the text inputs are 'foo' and 'bar'
 *  v == ['foo','bar']
 *
 *  var v = $(':checkbox').fieldValue();
 *  // if neither checkbox is checked
 *  v === undefined
 *  // if both checkboxes are checked
 *  v == ['B1', 'B2']
 *
 *  var v = $(':radio').fieldValue();
 *  // if neither radio is checked
 *  v === undefined
 *  // if first radio is checked
 *  v == ['C1']
 *
 * The successful argument controls whether or not the field element must be 'successful'
 * (per http://www.w3.org/TR/html4/interact/forms.html#successful-controls).
 * The default value of the successful argument is true.  If this value is false the value(s)
 * for each element is returned.
 *
 * Note: This method *always* returns an array.  If no valid value can be determined the
 *    array will be empty, otherwise it will contain one or more values.
 */
$.fn.fieldValue = function(successful) {
    for (var val=[], i=0, max=this.length; i < max; i++) {
        var el = this[i];
        var v = $.fieldValue(el, successful);
        if (v === null || typeof v == 'undefined' || (v.constructor == Array && !v.length)) {
            continue;
        }
        if (v.constructor == Array)
            $.merge(val, v);
        else
            val.push(v);
    }
    return val;
};

/**
 * Returns the value of the field element.
 */
$.fieldValue = function(el, successful) {
    var n = el.name, t = el.type, tag = el.tagName.toLowerCase();
    if (successful === undefined) {
        successful = true;
    }

    if (successful && (!n || el.disabled || t == 'reset' || t == 'button' ||
        (t == 'checkbox' || t == 'radio') && !el.checked ||
        (t == 'submit' || t == 'image') && el.form && el.form.clk != el ||
        tag == 'select' && el.selectedIndex == -1)) {
            return null;
    }

    if (tag == 'select') {
        var index = el.selectedIndex;
        if (index < 0) {
            return null;
        }
        var a = [], ops = el.options;
        var one = (t == 'select-one');
        var max = (one ? index+1 : ops.length);
        for(var i=(one ? index : 0); i < max; i++) {
            var op = ops[i];
            if (op.selected) {
                var v = op.value;
                if (!v) { // extra pain for IE...
                    v = (op.attributes && op.attributes['value'] && !(op.attributes['value'].specified)) ? op.text : op.value;
                }
                if (one) {
                    return v;
                }
                a.push(v);
            }
        }
        return a;
    }
    return $(el).val();
};

/**
 * Clears the form data.  Takes the following actions on the form's input fields:
 *  - input text fields will have their 'value' property set to the empty string
 *  - select elements will have their 'selectedIndex' property set to -1
 *  - checkbox and radio inputs will have their 'checked' property set to false
 *  - inputs of type submit, button, reset, and hidden will *not* be effected
 *  - button elements will *not* be effected
 */
$.fn.clearForm = function(includeHidden) {
    return this.each(function() {
        $('input,select,textarea', this).clearFields(includeHidden);
    });
};

/**
 * Clears the selected form elements.
 */
$.fn.clearFields = $.fn.clearInputs = function(includeHidden) {
    var re = /^(?:color|date|datetime|email|month|number|password|range|search|tel|text|time|url|week)$/i; // 'hidden' is not in this list
    return this.each(function() {
        var t = this.type, tag = this.tagName.toLowerCase();
        if (re.test(t) || tag == 'textarea') {
            this.value = '';
        }
        else if (t == 'checkbox' || t == 'radio') {
            this.checked = false;
        }
        else if (tag == 'select') {
            this.selectedIndex = -1;
        }
        else if (includeHidden) {
            // includeHidden can be the value true, or it can be a selector string
            // indicating a special test; for example:
            //  $('#myForm').clearForm('.special:hidden')
            // the above would clean hidden inputs that have the class of 'special'
            if ( (includeHidden === true && /hidden/.test(t)) ||
                 (typeof includeHidden == 'string' && $(this).is(includeHidden)) )
                this.value = '';
        }
    });
};

/**
 * Resets the form data.  Causes all form elements to be reset to their original value.
 */
$.fn.resetForm = function() {
    return this.each(function() {
        // guard against an input with the name of 'reset'
        // note that IE reports the reset function as an 'object'
        if (typeof this.reset == 'function' || (typeof this.reset == 'object' && !this.reset.nodeType)) {
            this.reset();
        }
    });
};

/**
 * Enables or disables any matching elements.
 */
$.fn.enable = function(b) {
    if (b === undefined) {
        b = true;
    }
    return this.each(function() {
        this.disabled = !b;
    });
};

/**
 * Checks/unchecks any matching checkboxes or radio buttons and
 * selects/deselects and matching option elements.
 */
$.fn.selected = function(select) {
    if (select === undefined) {
        select = true;
    }
    return this.each(function() {
        var t = this.type;
        if (t == 'checkbox' || t == 'radio') {
            this.checked = select;
        }
        else if (this.tagName.toLowerCase() == 'option') {
            var $sel = $(this).parent('select');
            if (select && $sel[0] && $sel[0].type == 'select-one') {
                // deselect all other options
                $sel.find('option').selected(false);
            }
            this.selected = select;
        }
    });
};

// expose debug var
$.fn.ajaxSubmit.debug = false;

// helper fn for console logging
function log() {
    if (!$.fn.ajaxSubmit.debug) 
        return;
    var msg = '[jquery.form] ' + Array.prototype.join.call(arguments,'');
    if (window.console && window.console.log) {
        window.console.log(msg);
    }
    else if (window.opera && window.opera.postError) {
        window.opera.postError(msg);
    }
}
// remove content and overlay
$.removeFormOverLay = function(paramObj)
{
	if(_cmsFormPluginLoadingFrameIsCompleted_ === false)
	{
		setTimeout(function(){
			$.removeFormOverLay(paramObj);
		},100);
	}
	else
	{
		$("#_id_overlay_response_msg").fadeOut(250);
		$("#_id_overlay_content_div").fadeOut(250);
		setTimeout(function(){
			$("#_id_overlay_div").fadeOut(250);
			try
			{
				hideLoading();
			}
			catch(e){}
			setTimeout(function(){
				if(_close_FormOverLay_CallBack_Function_)
				{
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
					// 全局返回
					if(paramObj.responseCode == "1036")
					{
						 linkLogin();
					}
					else if(paramObj.responseCode == "500")
					{
						minAlert("加载失败，请稍后再试",3)
					}
					else
					{
						_close_FormOverLay_CallBack_Function_(paramObj);
					}
				}
			},250);
		},250);
	}
};
// remove loading info
$.removeFormOverLayContent = function()
{
	$("#_id_overlay_response_msg").fadeOut(250);
};
$.resizeFormResult = function(width,height)
{
	$("#_id_overlay_response_msg").animate({"width":(width + "px"),"height":(height + "px"),"margin-left":(-width/2 + "px"),"margin-top":(-height/2 + "px")},300);
};
// show quick form result
$.showFormResult = function(data)
{
	$.showFormResponseMessage({width:"850",height:"336",data:data});
};
// show response info
$.showFormResponseMessage = function(paramObj)
{
	if(_cmsFormPluginLoadingFrameIsCompleted_ === false)
	{
		setTimeout(function(){
			$.showFormResponseMessage(paramObj);
		},100);
	}
	else
	{
		$("#_id_overlay_content_div").hide();
		$("#_id_overlay_response_msg").css({
			"left" : "50%",
			"width" : "140px",
			"height" : "100px",
			"margin-left" : "-70px", 
			"margin-top" : "-60px"
		});
		$("#_id_overlay_response_msg").show();
		$._form_showResponseMessage(paramObj);
	}
	
};
//private function
$._form_showResponseMessage = function(paramObj)
{

	if($("#_id_overlay_response_msg").length == 0)
	{
		var loading_html = '<div id="_id_overlay_response_msg" class="_j_overlay_response_msg"></div>';
		$("body").append(loading_html);
	}
	var $responseDiv = $("#_id_overlay_response_msg");
	$responseDiv.html("");
	if(paramObj.width)
	{
		paramObj.width = paramObj.width.replace("px","");
	}
	else
	{
		paramObj.width = "200";
	}
	if(paramObj.height)
	{
		paramObj.height = paramObj.height.replace("px","");
	}
	else
	{
		paramObj.height = "200";
	}
	$responseDiv.animate({"width":(paramObj.width + "px"),"height":(paramObj.height + "px"),"margin-left":(-paramObj.width/2 + "px"),"margin-top":(-paramObj.height/2 + "px")},500,function(){
		$responseDiv.html(paramObj.data);
	});
};
$._form_show_overlay_ = function(text)
{
	if(text == "no")
	{
		return ;
	}
	else if(text == "loading")
	{
		showLoading();
		return ;
	}
	_cmsFormPluginLoadingFrameIsCompleted_ = false;
	if($("#_id_overlay_div").length == 0)
	{
		$("body").append('<div id="_id_overlay_div" class="_j_black_overlay"></div>');
		var loading_html = '<div id="_id_overlay_content_div" class="_j_overlay_content">\
								<div>'
									+ text +
								'</div>\
							</div>';
		$("body").append(loading_html);
	}
	$("#_id_overlay_content_div").children("div").text(text);
	$("#_id_overlay_div").css("z-index",alyCms.base.getMaxZIndex() + 100);
	$("#_id_overlay_content_div").css("z-index",alyCms.base.getMaxZIndex() + 100);
	$("#_id_overlay_div").height($(document).height());
	$("#_id_overlay_div").width($(document).width()+100);
	$("#_id_overlay_div").css("opacity","0.6");
	$("#_id_overlay_div").css("filter","alpha(opacity=60)");
	$("#_id_overlay_div").css("-moz-opacity","0.6");
	$("#_id_overlay_div").fadeIn(250);
	//$("#_id_overlay_div").css("display","block");
	setTimeout(function(){
		$("#_id_overlay_content_div").fadeIn(400,function(){
			_cmsFormPluginLoadingFrameIsCompleted_ = true;
		});
	},250);
};
$._configCmsForm_ = function($form,options)
{

	if($("#cmsProcessFrame").length == 0)
	{
		var html = '<iframe id="cmsProcessFrame" name="cmsProcessFrame" style="display:none;"></iframe>';
		$("body").append(html);
	}
	var successCallBack = function(data){
		if(options.success && _cmsConfigIsFirstLoading_ === true && $.support.msie)
		{
			_cmsConfigIsFirstLoading_ = false;
		}
		else
		{
			options.success.apply($form,[data]);
		}
	};
	// bind loading func
	var cmsProcessFrame = document.getElementById("cmsProcessFrame");
	if (cmsProcessFrame.attachEvent)
	{
		cmsProcessFrame.attachEvent("onload", function(){
			successCallBack($("#cmsProcessFrame").contents().find("body").html());
	    });
	} 
	else 
	{
		cmsProcessFrame.onload = function(){
			successCallBack($("#cmsProcessFrame").contents().find("body").html());
	    };
	}
	$form.bind('submit',function(){
		if (options.beforeSubmit && options.beforeSubmit() === false) {
	        return false;
	    }
		if(options.closeFormOverLayCallBack)
		{
			_close_FormOverLay_CallBack_Function_ = options.closeFormOverLayCallBack;
		}
		$._form_show_overlay_((options && options.warningMessage) ? options.warningMessage : "正在加载,请稍后...");
		return true;
	});
	$form.attr('target','cmsProcessFrame');
	// add ajax submit flag
	$form.append('<input type="hidden" name="ajaxHttpRequest" value="true" />');
};
$._isTextInputForJqueryPopUpMessage_ = function(obj)
{
	if($(obj).get(0).tagName.toLowerCase() == "input")
	{
		if($(obj).attr("type").toLowerCase() == "text" || $(obj).attr("type").toLowerCase() == "password")
		{
			return true;
		}
	}
	return false;
};
})(jQuery);
var _cmsConfigIsFirstLoading_ = true;
var _cmsFormPluginLoadingFrameIsCompleted_ = true;
var _close_FormOverLay_CallBack_Function_ = null;