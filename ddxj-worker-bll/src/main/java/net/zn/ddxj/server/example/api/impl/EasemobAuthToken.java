package net.zn.ddxj.server.example.api.impl;

import net.zn.ddxj.easemob.server.example.api.AuthTokenAPI;
import net.zn.ddxj.server.example.comm.TokenUtil;


public class EasemobAuthToken implements AuthTokenAPI{

	@Override
	public Object getAuthToken(){
		return TokenUtil.getAccessToken();
	}
}
