package net.zn.ddxj.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * HttpServletResponse帮助类
 * 
 * @author Jaybert
 * 
 */
public final class ResponseUtils
{
    public static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);
    public static String renderResponseCommend(HttpServletResponse response,String text)
    {
    	renderHtml(response,text);
    	return null;
    }
    public static String renderResponseCommend(HttpServletResponse response,int text)
    {
    	renderHtml(response,text);
    	return null;
    }
    /**
     * 发送文本。使用UTF-8编码。
     * 
     * @param response
     *            HttpServletResponse
     * @param text
     *            发送的字符串
     */
    public static void renderText(HttpServletResponse response, int text)
    {
    	renderText(response,text + "");
    }
    public static void renderText(HttpServletResponse response, String text)
    {
        render(response, "text/plain;charset=UTF-8", text);
    }

    /**
     * 发送json。使用UTF-8编码。
     * 
     * @param response
     *            HttpServletResponse
     * @param text
     *            发送的字符串
     */
    public static void renderJson(HttpServletResponse response, String text)
    {
        render(response, "application/json;charset=UTF-8", text);
    }

    /**
     * 发送xml。使用UTF-8编码。
     * 
     * @param response
     *            HttpServletResponse
     * @param text
     *            发送的字符串
     */
    public static void renderXml(HttpServletResponse response, String text)
    {
        render(response, "text/xml;charset=UTF-8", text);
    }

    public static void renderHtml(HttpServletResponse response, int text)
    {
        renderHtml(response, text + "");
    }
    public static void renderScript(HttpServletResponse response, String text)
    {
    	render(response, "application/javascript;charset=UTF-8", text);
    }
    public static void renderHtml(HttpServletResponse response, String text)
    {
        render(response, "text/html;charset=UTF-8", text);
    }

    /**
     * 发送内容。使用UTF-8编码。
     * 
     * @param response
     * @param contentType
     * @param text
     */
    public static void render(HttpServletResponse response, String contentType, String text)
    {
        response.setContentType(contentType);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try
        {
            response.getWriter().write(text);
        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
        }
    }
    public static void setResponseHeadForIframe(HttpServletResponse response)
    {
    	response.setHeader("P3P","CP=CAO PSA OUR");// 这一步非常重要，要不然IE在iframe下不能存cookie
    }
}
