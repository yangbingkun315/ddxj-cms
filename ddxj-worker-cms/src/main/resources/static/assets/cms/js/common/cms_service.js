var cms = [];
cms.user = 
{
	initServiceSchedule : function(){
		cms.user.startServiceSchedule();
		// 设置每10秒读取一次
		setInterval(function(){
			cms.user.startServiceSchedule();
		},1000*10);
	},
	noticCount:0,
	startServiceSchedule : function(){
		var siteContextPath = $("#siteContextPath").val();
		$.get($("#siteContextPath").val() + "/validate/message/notic.htm",function(data){
			cms.user.serviceScheduleCallback(data);
		},"json");
	},
	serviceScheduleCallback : function(data){
		var jsonData = JSON.parse(data.data);
		if(jsonData.totalCount != cms.user.noticCount)
		{
			$.post($("#siteContextPath").val() + "/query/notic/list.htm",function(data){
				if(data.response)
				{
					var dataInfo = JSON.parse(data.data);
					if(dataInfo.totalCount > 0)
					{
						cms.user.appendNoticData(dataInfo);
						cms.user.noticCount = jsonData.totalCount;
					}
				}
			},"json");
		}
	},
	 appendNoticData : function(data)
	 {
	 	var parent = $("#cms-message-method");
	 	parent.find(".glyphicon-bell").addClass("active");
	 	if(data.totalCount >= 100)
	 	{
	 		parent.find(".badge").text("99+");
	 	}
	 	else
	 	{
	 		parent.find(".badge").text(data.totalCount);
	 	}
	 	var messageList = data.messageList;
	 	var html="";
	 	for(var c = 0;c<messageList.length;c++)
	 	{
	 		html += '<li><a href="'+alyCms.base.getRootPath()+'/cms/message/list.htm">'+messageList[c].messageTitle+'</a></li>';
	 	}
	 	html+='<li class="not-message-li"><a href="'+alyCms.base.getRootPath()+'/cms/message/list.htm">查看更多 >></a></li>';
	 	parent.find(".message-content ul").html(html);
	 	var openNotic = localStorage.getItem("openNotic");
		if(isNotNull(openNotic) && openNotic == 'false')
		{
			$("#operateNoticId").prop("checked",false);
			return false;
		}
		else
		{
			parent.find(".cms-message-tip").show();
			$("#notic-message-mp3").html('<audio src="'+$("#siteResPath").val()+'/assets/cms/mp3/notic.wav"  preload="preload" id="cmsNoticMp3"></audio>')
			var oAudio = document.getElementById('cmsNoticMp3');
			
			if (oAudio.paused) 
			{
					oAudio.play();
            }
            else 
            {
                oAudio.pause();
            }
			localStorage.setItem("openNotic", true);
		}
	 	
	 }
}