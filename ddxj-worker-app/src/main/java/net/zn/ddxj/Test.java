package net.zn.ddxj;

import io.swagger.client.model.Msg;
import io.swagger.client.model.Nickname;
import io.swagger.client.model.UserName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zn.ddxj.server.example.api.impl.EasemobIMUsers;
import net.zn.ddxj.server.example.api.impl.EasemobSendMessage;
import net.zn.ddxj.utils.wechat.WXException;
import net.zn.ddxj.utils.wechat.WechatUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Test {
	
	public static void main(String[] args) 
	{
/*		EasemobIMUsers api = new EasemobIMUsers();
		//检查用户是否存在
		Object un = api.getIMUserByUserName("17621328341");
		System.out.println(un);
		
		//注册新用户 可批量注册
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("username","17621328341");
		data.put("password","17621328341");
		data.put("nickname","何俊辉");
		list.add(data);
		Map<String,Object> data1 = new HashMap<String, Object>();
		data1.put("username","13127663126");
		data1.put("password","13127663126");
		data1.put("nickname","范存鑫");
		list.add(data1);
		try {
			net.sf.json.JSONObject obj = WechatUtils.addEasemobUser(list, "Bearer YWMtCQKrIrDSEeiimIW8r1GlVQAAAAAAAAAAAAAAAAAAAAG9DXRwryAR6L8DUw0dIFonAgMAAAFlqFg0dwBPGgCn822mBP67j3J-nqt4anruemBht7AwNf-lK931_jPheQ");
			System.out.println(obj);
		} catch (WXException e) {}
		
		//更改推送名称
		Nickname name = new Nickname().nickname("挠开宇");
		Object userNode = api.modifyIMUserNickNameWithAdminToken("13127663126", name, "Bearer YWMtCQKrIrDSEeiimIW8r1GlVQAAAAAAAAAAAAAAAAAAAAG9DXRwryAR6L8DUw0dIFonAgMAAAFlqFg0dwBPGgCn822mBP67j3J-nqt4anruemBht7AwNf-lK931_jPheQ");
		System.out.println(userNode);
		
		//发送消息
		EasemobSendMessage mes = new EasemobSendMessage();
		Msg msg = new Msg();
		msg.targetType("users");
		UserName n = new UserName();
		n.add("18621703245");
		msg.setTarget(n);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode o = mapper.createObjectNode();
		o.put("type", "txt");
		o.put("msg", "刘备");
		msg.msg(o);
		msg.from("13127663126");
		Object re = mes.sendMessage(msg);
		System.out.println(re);*/
		System.out.println("s");
	}
}


