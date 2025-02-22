package net.zn.ddxj.server.example.api.impl;

import io.swagger.client.ApiException;
import io.swagger.client.api.UploadAndDownloadFilesApi;

import java.io.File;

import net.zn.ddxj.easemob.server.example.api.FileAPI;
import net.zn.ddxj.server.example.comm.EasemobAPI;
import net.zn.ddxj.server.example.comm.OrgInfo;
import net.zn.ddxj.server.example.comm.ResponseHandler;
import net.zn.ddxj.server.example.comm.TokenUtil;


public class EasemobFile implements FileAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private UploadAndDownloadFilesApi api = new UploadAndDownloadFilesApi();
    @Override
    public Object uploadFile(final Object file) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatfilesPost(OrgInfo.ORG_NAME,OrgInfo.APP_NAME,TokenUtil.getAccessToken(),(File)file,true);
             }
        });
    }

    @Override
    public Object downloadFile(final String fileUUID,final  String shareSecret,final Boolean isThumbnail) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
               return api.orgNameAppNameChatfilesUuidGet(OrgInfo.ORG_NAME,OrgInfo.APP_NAME,TokenUtil.getAccessToken(),fileUUID,shareSecret,isThumbnail);
            }
        });
    }
}
