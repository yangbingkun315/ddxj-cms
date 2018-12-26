package net.zn.ddxj.common;

import org.springframework.ui.ModelMap;

import com.github.pagehelper.PageInfo;

public class PageHelperModel {

	public static void responsePageModel(PageInfo<?> pageInfo,ModelMap model)
	{
		model.addAttribute("currentPage",pageInfo.getPageNum());
		model.addAttribute("currentTotalCount",pageInfo.getTotal());
	}
}
