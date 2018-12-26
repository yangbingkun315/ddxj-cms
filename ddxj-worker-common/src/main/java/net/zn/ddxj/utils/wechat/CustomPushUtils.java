package net.zn.ddxj.utils.wechat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.constants.Constants;
@Slf4j
public class CustomPushUtils {
	/**
	 * @将xml转为WxPayData对象并返回对象内部的数据
	 * @param string
	 *            待转换的xml串
	 * @return 经转换得到的Dictionary
	 * @throws WxPayException
	 * @throws ParserConfigurationException
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 * @throws SAXException
	 */
	public static Map<String, Object> FromXml(String xml) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException{
		Map<String,Object> m_values = new HashMap<String,Object>();
		if (xml == null || xml.isEmpty()) {
			log.error("xml为空!");
		}

		DocumentBuilderFactory documentBuildFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder doccumentBuilder = documentBuildFactory
				.newDocumentBuilder();
		Document document = doccumentBuilder.parse(new ByteArrayInputStream(xml
				.getBytes("UTF-8")));
		Node xmlNode = document.getFirstChild();// 获取到根节点<xml>
		NodeList nodes = xmlNode.getChildNodes();
		for (int i = 0, length = nodes.getLength(); i < length; i++) {
			Node xn = nodes.item(i);
			if (xn instanceof Element) {
				Element xe = (Element) xn;
				m_values.put(xe.getNodeName(), xe.getTextContent());// 获取xml的键值对到WxPayData内部的数据中
			}
		}
		return m_values;
	}
	
	
	public static String customAssembleText(Map<String,Object> fromXml,String content) throws WxPayException{
		String openId = fromXml.get("FromUserName").toString();
		String toUserName = fromXml.get("ToUserName").toString();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("ToUserName", openId);
		resultMap.put("FromUserName", toUserName);
		resultMap.put("CreateTime", ((int) System.currentTimeMillis()));
		resultMap.put("MsgType", Constants.MESSAGE_TEXT);
		resultMap.put("Content", content);
		return WechatUtils.ToXml(resultMap);
	}

}
