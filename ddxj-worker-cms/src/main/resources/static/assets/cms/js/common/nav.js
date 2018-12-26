$(function(){
	 if(localStorage.getItem("fixedLeftMinMenu") == 'true')
	{
		$(".nav-left").addClass("nav-mini");
		$(".cms-content,.notic-operate").css("margin-left","60px");
    	$(".cms-logo").addClass("cms-logo-mini");
    	$(".nav-item").removeClass("nav-show");
    	var changeLeftMenu = $(".nav-left .nav-item").find("#"+$(".cms-content").attr("id"));
    	$(changeLeftMenu).parent("ul").parent("li").addClass("nav-show-min");
    	$(changeLeftMenu).find("a").addClass("active");
    	$(".menu-left-content").removeClass("active");
	}
	else
	{
		$(".nav-left").removeClass("nav-mini");
		$(".cms-logo").removeClass("cms-logo-mini");
        $(".cms-content,.notic-operate").css("margin-left","180px");
        $(".menu-left-content").css("height",$(window).height() - 60 - 160 + "px");
    	$(".menu-left-content").addClass("active");
	}
    // nav收缩展开
    $('.nav-item>a').on('click',function(){
        if (!$('.nav-left').hasClass('nav-mini')) {
            if ($(this).next().css('display') == "none") {
                //展开未展开
                $('.nav-item').children('ul').slideUp(300);
                $(this).next('ul').slideDown(300);
                $(this).parent('li').addClass('nav-show').siblings('li').removeClass('nav-show');
            }else{
                //收缩已展开
                $(this).next('ul').slideUp(300);
                $('.nav-item.nav-show').removeClass('nav-show');
            }
        }
    });
    //nav-mini切换
    $('#mini').on('click',function(){
        if (!$('.nav-left').hasClass('nav-mini')) {
        	localStorage.setItem("fixedLeftMinMenu", true);
        	$(".cms-content,.notic-operate").css("margin-left","60px");
        	$(".cms-logo").addClass("cms-logo-mini");
            $('.nav-item.nav-show').removeClass('nav-show');
            $('.nav-item').children('ul').removeAttr('style');
            $('.nav-left').addClass('nav-mini');
            $(".menu-left-content").removeClass("active");
        }else{
            $('.nav-left').removeClass('nav-mini');
            $(".cms-logo").removeClass("cms-logo-mini");
            $(".cms-content,.notic-operate").css("margin-left","180px");
            localStorage.setItem("fixedLeftMinMenu", false);
            $(".menu-left-content").css("height",$(window).height() - 60 - 160 + "px");
            $(".menu-left-content").addClass("active");
            
            changeLeftMenuActive();
        }
    });
    changeLeftMenuActive();
    /**
     * 改变左侧菜单，并且选中初始化
     */
    function changeLeftMenuActive()
    {
    	if (!$('.nav-left').hasClass('nav-mini')) {
	    	var changeLeftMenu = $(".nav-left .nav-item").find("#"+$(".cms-content").attr("id"));
	    	$(changeLeftMenu).parent("ul").parent("li").addClass("nav-show");
	    	$(changeLeftMenu).find("a").addClass("active");
	    	 $(".menu-left-content").css("height",$(window).height() - 60 - 160 + "px");
	    	$(".menu-left-content").addClass("active");
    	}
    }
});