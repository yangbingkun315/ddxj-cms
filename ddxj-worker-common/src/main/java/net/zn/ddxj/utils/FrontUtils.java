package net.zn.ddxj.utils;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zn.ddxj.constants.CmsConstants;

import org.springframework.ui.ModelMap;


/**
 * 前台工具类
 * 
 * @author Jaybert
 * 
 */
public class FrontUtils
{
    public static String findFrontTpl(HttpServletRequest request, HttpServletResponse response, ModelMap model, String path)
    {
        prepareFrontData(request, model);
        return  path;
    }

    public static String redirect(String path)
    {
        return "redirect:" + path;
    }
    public static String forward(String path)
    {
        return "forward:" + path;
    }

    public static void prepareFrontData(HttpServletRequest request, ModelMap model)
    {
    }
}
