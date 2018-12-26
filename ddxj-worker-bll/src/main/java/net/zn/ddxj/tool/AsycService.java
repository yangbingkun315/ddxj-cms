package net.zn.ddxj.tool;

import java.util.List;

import net.zn.ddxj.entity.AppMessage;
import net.zn.ddxj.entity.SalaryRecord;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.UserTransfer;
import net.zn.ddxj.vo.RequestVo;

/**
 * 线程统一处理类
 * @author ddxj
 *
 */
public interface AsycService 
{
	void addManagerMessage(Integer messageType,String messageContent,String messageTitle,String messageParameter);//后台插入消息
	//void addUserMessage(AppMessage appMessage);
	void updateUserInfoAsync(String phone,String ip,String loginChannel);//登录时修改登录信息
	void updateUserInfoAsyncByUnionid(String unionid,String ip,String loginChannel);//登录时修改登录信息
    void updateUserAppUserToken(String phone,String appUserToken);//登录时更新用户APPUSERTOKEN
    void updateUserAppUserTokenByUnionid(String unionid,String appUserToken);//登录时更新用户APPUSERTOKEN
    void updateUserWechatInfo(String phone,String wechatInfo,String loginChannel);//登录时更新用户授权信息
    void changeUserWechatInfo(String phone,String wechatInfo);//登录时改变用户授权信息
    void pushRecruitValidataStatus(Integer validateStatus,Integer recruitId);//推送审核招聘信息
    void pushRealName(Integer userId,Integer realAuthId);//推送实名认证信息 -- 杨丙坤
    void pushTransferAccounts(Integer fromUserId,Integer toUserId,UserTransfer userTransfer);//推送转账信息 -- 杨丙坤
    void pushUserWithdrawStatus(Integer userId,Integer userWithdrawId);//推送提现审核状态  -- 杨丙坤
    void pushUserCreditStatus(Integer userId,Integer recruitCreditId);//推送授信审核状态 -- 杨丙坤
    void pushRecruitRequest(Integer toUserId,Integer recruitId);//推送招聘邀请 -- 饶开宇
    void pushCircleGive(Integer fromUserId,Integer circleId);//推送动态点赞 -- 饶开宇
    void pushCircleComment(Integer circleCommentId);//推送圈子动态评论信息 -- 饶开宇
    void pushRecruitBatchByCategory(Integer recruitId);//批量推送招聘工种
    void pushRecruitComment(Integer fromUserId,Integer toUserId,String RecruitTitle);//推送招聘活动完工评论
    void pushPayMoney(Integer userId,Integer recruitId,SalaryRecord salaryRecord,UserTransfer userTransfer);//发放工资推送
    void pushWorkerOrForeManStatus(Integer userId,Integer recruitId);//推送工人工头状态信息 -- 饶开宇
    void addUserMessage(Integer userId,String title,String content,Integer messageTypeId,String param,Integer typeId);//添加消息中心消息信息
    void sendAppTemplateFromCircle(Integer circleId);//后台删除违规圈子推送
    void appCustomBatchMassMessage(List<User> userList,Integer logId);//APP自定义批量推送
    /**
     * 添加用户奖励金
     * @param openId
     * @param phone
     */
    void addUserBonus(Integer userId,String openId,String phone,Integer registerChannel);
    void pushBaiduLink();//主动推送百度爬虫，让百度收录
}
