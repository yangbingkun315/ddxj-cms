package net.zn.ddxj.server.example.api.impl;

import net.zn.ddxj.easemob.server.example.api.SendMessageAPI;
import net.zn.ddxj.server.example.comm.EasemobAPI;
import net.zn.ddxj.server.example.comm.OrgInfo;
import net.zn.ddxj.server.example.comm.ResponseHandler;
import net.zn.ddxj.server.example.comm.TokenUtil;
import io.swagger.client.ApiException;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.model.Msg;

public class EasemobSendMessage implements SendMessageAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private MessagesApi api = new MessagesApi();
    @Override
    public Object sendMessage(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameMessagesPost(OrgInfo.ORG_NAME,OrgInfo.APP_NAME,TokenUtil.getAccessToken(), (Msg) payload);
            }
        });
    }
}
