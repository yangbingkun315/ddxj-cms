package net.zn.ddxj.tool.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.activemq.store.memory.MemoryTransactionStore.AddMessageCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.AppMessage;
import net.zn.ddxj.entity.Category;
import net.zn.ddxj.entity.Circle;
import net.zn.ddxj.entity.CircleComment;
import net.zn.ddxj.entity.InviteRecord;
import net.zn.ddxj.entity.Message;
import net.zn.ddxj.entity.MessageCenter;
import net.zn.ddxj.entity.Notice;
import net.zn.ddxj.entity.RealAuth;
import net.zn.ddxj.entity.Recruit;
import net.zn.ddxj.entity.RecruitCredit;
import net.zn.ddxj.entity.RecruitRecord;
import net.zn.ddxj.entity.SalaryRecord;
import net.zn.ddxj.entity.SendMessageUser;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.UserTransfer;
import net.zn.ddxj.entity.UserWithdraw;
import net.zn.ddxj.entity.WechatSendMessage;
import net.zn.ddxj.mapper.AppMessageMapper;
import net.zn.ddxj.mapper.CircleCommentMapper;
import net.zn.ddxj.mapper.CircleMapper;
import net.zn.ddxj.mapper.InviteRecordMapper;
import net.zn.ddxj.mapper.MessageCenterMapper;
import net.zn.ddxj.mapper.MessageMapper;
import net.zn.ddxj.mapper.NoticeMapper;
import net.zn.ddxj.mapper.RealAuthMapper;
import net.zn.ddxj.mapper.RecruitCategoryMapper;
import net.zn.ddxj.mapper.RecruitCreditMapper;
import net.zn.ddxj.mapper.RecruitMapper;
import net.zn.ddxj.mapper.RecruitRecordMapper;
import net.zn.ddxj.mapper.UserMapper;
import net.zn.ddxj.mapper.UserTransferMapper;
import net.zn.ddxj.mapper.UserWithdrawMapper;
import net.zn.ddxj.tool.AsycService;
import net.zn.ddxj.tool.WechatService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.HttpUtils;
import net.zn.ddxj.utils.OrderNumUtils;
import net.zn.ddxj.utils.ProRandomUtil;
import net.zn.ddxj.utils.PropertiesUtils;
import net.zn.ddxj.utils.RedisUtils;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.utils.wechat.WXException;
import net.zn.ddxj.utils.wechat.WechatUtils;
import net.zn.ddxj.utils.ym.AndroidPushUtils;
import net.zn.ddxj.utils.ym.IOSPushUtils;
import net.zn.ddxj.utils.yunpian.YunPianSMSUtils;

@Component
@Slf4j
public class AsycServiceImpl implements AsycService {

	@Autowired
	public RedisUtils redisUtils;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RecruitMapper recruitMapper;
	@Autowired
	private UserWithdrawMapper userWithdrawMapper;
	@Autowired
	private RecruitCreditMapper recruitCreditMapper;
	@Autowired
	private RecruitRecordMapper recruitRecordMapper;
	@Autowired
	private CircleMapper circleMapper;
	@Autowired
	private CircleCommentMapper circleCommentMapper;
	@Autowired 
	private RealAuthMapper realAuthMapper;
	@Autowired 
	private MessageMapper messageMapper;
	@Autowired 
	private AppMessageMapper appMessageMapper;
	@Autowired
	private WechatService wechatService;
	@Autowired
	private InviteRecordMapper inviteRecordMapper; 
	@Autowired
	private UserTransferMapper userTransferMapper;
	@Autowired
	private MessageCenterMapper messageCenterMapper;
	@Autowired
	private NoticeMapper noticeMapper;
	@Override
	@Async
	public void updateUserInfoAsync(String phone,String ip,String loginChannel) {
		userMapper.updateUserLastInfo(ip,phone,loginChannel);
	}
	
	@Override
	@Async
	public void updateUserAppUserToken(String phone, String appUserToken) {
		// TODO Auto-generated method stub
		userMapper.updateUserAppUserToken(phone, appUserToken);
	}
	/**
	 * 项目审核推送
	 */
	@Override
	@Async
	public void pushRecruitValidataStatus(Integer validateStatus, Integer recruitId) {
		
		Recruit recruit = recruitMapper.selectByPrimaryKey(recruitId);
		
		User toUser = userMapper.queryUserDetail(recruit.getUserId());
		
		if(!CmsUtils.isNullOrEmpty(toUser.getLastDevice()) && !Constants.JSAPI.equals(toUser.getLastDevice()))
		{
			if(!CmsUtils.isNullOrEmpty(toUser.getAppUserToken()))
			{
				String msg = "恭喜您【"+recruit.getRecruitTitle() + "】已通过审核,赶快去邀请工人吧";
				if(validateStatus == 2)//审核失败
				{
					msg = "非常抱歉【"+recruit.getRecruitTitle() + "】未通过审核,继续努力哦";
				}
				if(Constants.IOS.equals(toUser.getLastDevice()))
				{
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", toUser.getAppUserToken());
					param.put("content",msg );// 当content-available=1时(静默推送),可选; 否则必填。// 可为JSON类型和字符串类型
					JSONObject customized = new JSONObject();
					customized.put("pushType", "recruitStatus");//推送类型 recruitStatus 表示审核状态推送
					customized.put("userId", toUser.getId());
					customized.put("recruitId", recruit.getId());
					customized.put("messageTypeId", 2);
					param.put("customized", customized);
					try 
					{
						IOSPushUtils.sendIOSSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(Constants.ANDROID.equals(toUser.getLastDevice()))
				{
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", toUser.getAppUserToken());
					param.put("title", Constants.DDXJ);
					param.put("text",msg);// 当content-available=1时(静默推送),可选; 否则必填。// 可为JSON类型和字符串类型
					param.put("img",recruit.getCoverImage());
					Map<String, Object> data = new HashMap<>();
					data.put("messageTypeId", 2);
					data.put("pushType", "recruitStatus");//推送类型 recruitStatus 表示审核状态推送
					param.put("data", JsonUtil.map2jsonToObject(data));
					try 
					{
						AndroidPushUtils.sendAndroidSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				log.info("#####项目审核推送成功###");
			}
		}
		Notice notice = new Notice();
		notice.setNoticeContent(toUser.getRealName().charAt(0)+"师傅已经成功发布了一个新项目");
		notice.setNoticeType(2);
		notice.setStartTime(new Date());
		//结束时间是开始时间推迟24小时
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 1);
		notice.setEndTime(calendar.getTime());
		notice.setCreateTime(new Date());
		notice.setUpdateTime(new Date());
		StringBuffer paramBuffer = new StringBuffer();
		if(validateStatus == 3)
		{
			paramBuffer.append("{").append("\"recruitId\":").append(recruit.getId()).append("}");
			String param = paramBuffer.toString();
			String message = "恭喜您【"+recruit.getRecruitTitle() + "】已通过审核,赶快去邀请工人吧";
			addUserMessage(toUser.getId(), Constants.MESSAGE_RECRUIT_SUC, message, 2, param, 2);
			noticeMapper.insertSelective(notice);
		}
		if(validateStatus == 2)
		{
			paramBuffer.append("{").append("\"recruitId\":").append(recruit.getId()).append(",").append("\"recruit\":").append(JsonUtil.bean2jsonToString(recruit)).append("}");
			String param = paramBuffer.toString();
			String message = "非常抱歉【"+recruit.getRecruitTitle() + "】未通过审核,继续努力哦";
			addUserMessage(toUser.getId(), Constants.MESSAGE_RECRUIT_FAIL, message, 2, param, 3);
		}
		else//使用手机号推送
		{
			
		}
		
		if(!CmsUtils.isNullOrEmpty(toUser.getOpenid()))
		{
			//发送微信模板消息推送
			try {
				Map<String,Object> template = new HashMap<String,Object>();
				template.put("touser", toUser.getOpenid());
				template.put("template_id", "UN7Ck04wQL18SmjiHVDZNt-HzbLWQ_7yfLQeAQLy_Ac");
				//template.put("url", "www.baidu.com");
				template.put("topcolor", "#44b549");
				
				Map<String,Object> data = new HashMap<String,Object>();
				
				Map<String,String> data1 = new HashMap<String,String>();
				String msg = "恭喜您,您发布的项目已通过审核,赶快去邀请工人吧";
				if(validateStatus == 2)//审核失败
				{
					msg = "非常抱歉,您发布项目未通过审核,继续努力哦";
				}
				data1.put("value", msg);
				data.put("first", data1);
				
				Map<String,String> data2 = new HashMap<String,String>();
				data2.put("value", recruit.getRecruitTitle());
				data.put("keyword1", data2);
				
				Map<String,String> data3 = new HashMap<String,String>();
				String msgt = "已通过";
				if(validateStatus == 2)//审核失败
				{
					msgt = "未通过";
				}
				data3.put("value", msgt);
				data.put("keyword2", data3);
				
				Map<String,String> data4 = new HashMap<String,String>();
				data4.put("value", DateUtils.getStringDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				data.put("keyword3", data4);
				
				Map<String,String> remark = new HashMap<String,String>();
				remark.put("value", "感谢您的使用");
				data.put("remark", remark);
				
				template.put("data",data);
				
				WechatUtils.sendWechatTemplateMassage(wechatService.queryWechatToken(), JsonUtil.map2jsonToString(template));
			} catch (WXException e) {}
		}
	}
	/**
	 * 实名认证推送
	 */
	@Override
	@Async
	public void pushRealName(Integer userId, Integer realAuthId) {
		RealAuth auth = realAuthMapper.selectByPrimaryKey(realAuthId);
		Integer authStatus=auth.getRealStatus();
		User userDetail = userMapper.queryUserDetail(userId);
		if(!CmsUtils.isNullOrEmpty(userDetail.getLastDevice()) && !Constants.JSAPI.equals(userDetail.getLastDevice()))
		{
			if(!CmsUtils.isNullOrEmpty(userDetail.getAppUserToken()))
			{
				String msg = userDetail.getRealName() + ",您已通过实名认证,赶快去报名吧!";
				if(authStatus==3)//审核成功
				{
					if(userDetail.getRole()==2)//工头
					{
						msg = userDetail.getRealName() + ",您已通过实名认证,赶快去发布招聘吧!";
					}
				}
				else if(authStatus==2)//审核失败
				{
					msg = userDetail.getRealName() + "非常抱歉,您的实名认证未通过,请重新进行提交资料!";
				}
				
				if(Constants.IOS.equals(userDetail.getLastDevice()))
				{
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", userDetail.getAppUserToken());
					param.put("content", msg);
					JSONObject customized = new JSONObject();
					customized.put("pushType", "authStatus");
					customized.put("userId", userId);
					customized.put("realAuthId", auth.getId());
					customized.put("messageTypeId", 2);
					param.put("customized", customized);
					try 
					{
						IOSPushUtils.sendIOSSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(Constants.ANDROID.equals(userDetail.getLastDevice()))
				{
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", userDetail.getAppUserToken());
					param.put("title", Constants.DDXJ);
					param.put("text", msg);
					Map<String, Object> data = new HashMap<>();
					data.put("messageTypeId", 2);
					param.put("data", JsonUtil.map2jsonToObject(data));
					try 
					{
						AndroidPushUtils.sendAndroidSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				log.info("#####实名认证推送成功###");
			}
		}
		
		//实名认证跳转需要的参数
		StringBuffer paramBuffer = new StringBuffer();
		if(authStatus == 3)
		{
			String message = userDetail.getRealName() + ",您已通过实名认证,赶快去发布招聘吧!";
			if(userDetail.getRole() == 1)
			{
				message = userDetail.getRealName() + ",您已通过实名认证,赶快去报名吧!";
				String param = paramBuffer.toString();
				addUserMessage(userId, Constants.MESSAGE_REALNAME_SUC, message, 2, param, 7);
			}else
			{
				paramBuffer.append("{").append("\"userId\":").append(userId).append("}");
				String param = paramBuffer.toString();
				addUserMessage(userId, Constants.MESSAGE_REALNAME_SUC, message, 2, param, 7);
			}
		}
		if(authStatus == 2)
		{
			String message = userDetail.getRealName() + "非常抱歉,您的实名认证未通过,请重新进行提交资料!";
			paramBuffer.append("{").append("\"userId\":").append(userId).append(",").append("\"realAuth\":").append(JsonUtil.bean2jsonToString(auth)).append("}");
			String param = paramBuffer.toString();
			addUserMessage(userId, Constants.MESSAGE_REALNAME_FAIL, message, 2, param, 8);
		}
		else
		{
			
		}
	}
	/**
	 * 转账推送
	 */
	@Override
	@Async
	public void pushTransferAccounts(Integer fromUserId, Integer toUserId, UserTransfer userTransfer) {
		// TODO Auto-generated method stub
		User toUser = userMapper.queryUserDetail(toUserId);
		User fromUser = userMapper.queryUserDetail(fromUserId);
		if(!CmsUtils.isNullOrEmpty(toUser.getLastDevice()) && !Constants.JSAPI.equals(toUser.getLastDevice()))
		{
			String msg = fromUser.getRealName() +"给您转账" +userTransfer.getMoney() +"元,请注意查收！";
			if(!CmsUtils.isNullOrEmpty(toUser.getAppUserToken()))
			{
				if(Constants.IOS.equals(toUser.getLastDevice()))
				{
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", toUser.getAppUserToken());
					param.put("content", msg);	
					JSONObject customized = new JSONObject();
					customized.put("pushType", "transferType");
					customized.put("fromUserId", fromUserId);
					customized.put("toUserId", toUserId);
					customized.put("money", userTransfer.getMoney());
					customized.put("userTransfer",toUser.getRealName());
					customized.put("messageTypeId", 4);
					param.put("customized", customized);
					try 
					{
						IOSPushUtils.sendIOSSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(Constants.ANDROID.equals(toUser.getLastDevice()))
				{
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", toUser.getAppUserToken());
					param.put("title",Constants.DDXJ);					
					param.put("text", msg);					
					Map<String, Object> data = new HashMap<>();
					data.put("messageTypeId", 4);
					param.put("data", JsonUtil.map2jsonToObject(data));
					try 
					{
						AndroidPushUtils.sendAndroidSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					YunPianSMSUtils.sendPayTemplate(toUser.getPhone(), String.valueOf(userTransfer.getMoney()));//发短信
				}
				log.info("#####转账推送成功###");
			}
			else
			{
				YunPianSMSUtils.sendPayTemplate(toUser.getPhone(), String.valueOf(userTransfer.getMoney()));//发短信
			}
		}
		else
		{
			YunPianSMSUtils.sendPayTemplate(toUser.getPhone(), String.valueOf(userTransfer.getMoney()));//发短信
		}
		StringBuffer paramBuffer = new StringBuffer();
		paramBuffer.append("{").append("\"userTransferId\":").append(userTransfer.getId()).append(",").append("\"transferType\":").append(userTransfer.getTransferType()).append("}");
		String param = paramBuffer.toString();
		String message = fromUser.getRealName() +"给您转账" +userTransfer.getMoney() +"元,请注意查收！";
		addUserMessage(toUserId, Constants.MESSAGE_TRANSFER_STATUS, message, 4, param, 10);
	}
	/**
	 * 提现推送
	 */
	@Override
	@Async
	public void pushUserWithdrawStatus(Integer userId, Integer userWithdrawId) {
		// TODO Auto-generated method stub
		UserWithdraw userWithdraw=userWithdrawMapper.selectByPrimaryKey(userWithdrawId);
		UserTransfer userTransfer = userTransferMapper.selectByWithdrawId(userWithdrawId);
		Integer withdrawStatus = userWithdraw.getWithdrawStatus();
		User userDetail = userMapper.queryUserDetail(userId);
		String bankNo = userWithdraw.getBankOn();
		if(!CmsUtils.isNullOrEmpty(userDetail.getLastDevice()) && !Constants.JSAPI.equals(userDetail.getLastDevice()))
		{
			if(!CmsUtils.isNullOrEmpty(userDetail.getAppUserToken()))
			{
				String msg = null;
				if(withdrawStatus == 9)
				{
					msg = "恭喜您,您提现的"+userWithdraw.getMoney()+"元人民币,已转尾号"+bankNo.substring(bankNo.length() - 4, bankNo.length())+"的银行卡,请注意查收！";
				}
				
				if(withdrawStatus == 2 || withdrawStatus == 4 || withdrawStatus == 6 || withdrawStatus == 8)//提现失败
				{
					msg = "经工作人员核实,您的账户信息显示异常,请联系客服:400-800-8739";
				}
				if(!CmsUtils.isNullOrEmpty(msg))
				{
					if(Constants.IOS.equals(userDetail.getLastDevice()))
					{
						Map<String,Object> param = new HashMap<String,Object>();
						param.put("deviceToken", userDetail.getAppUserToken());
						param.put("content",msg);
						JSONObject customized = new JSONObject();
						customized.put("pushType", "withdrawStatus");
						customized.put("userId", userId);
						customized.put("userWithdraw",userWithdraw.getUser().getRealName() );
						customized.put("messageTypeId", 4);
						param.put("customized", customized);
						try 
						{
							IOSPushUtils.sendIOSSingleMsg(param);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if(Constants.ANDROID.equals(userDetail.getLastDevice()))
					{
						Map<String,Object> param = new HashMap<String,Object>();
						param.put("deviceToken", userDetail.getAppUserToken());
						param.put("title", Constants.DDXJ);
						param.put("text",msg);
						Map<String, Object> data = new HashMap<>();
						data.put("messageTypeId", 4);
						param.put("data", JsonUtil.map2jsonToObject(data));
						try 
						{
							AndroidPushUtils.sendAndroidSingleMsg(param);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					log.info("#####申请提现推送成功###");
				}
			}
		}
		StringBuffer paramBuffer = new StringBuffer();
		paramBuffer.append("{").append("\"userTransferId\":").append(userTransfer.getId()).append(",").append("\"transferType\":").append(userTransfer.getTransferType()).append("}");
		String param = paramBuffer.toString();
		if(withdrawStatus == 9)
		{
			String message = "恭喜您,您提现的"+userWithdraw.getMoney()+"元人民币,已转尾号"+bankNo.substring(bankNo.length() - 4, bankNo.length())+"的银行卡,请注意查收！";
			addUserMessage(userId, Constants.MESSAGE_WITHDRAW_SUC, message, 4, param, 11);
		}
		if(withdrawStatus == 2 || withdrawStatus == 4 || withdrawStatus == 6 || withdrawStatus == 8)
		{
			String message = "经工作人员核实,您的账户信息显示异常,为了保证您的资金安全,我们拒绝了本次提现申请。稍后,工作人员会与您联系,您也可以主动联系客服:400-800-8739";
			addUserMessage(userId, Constants.MESSAGE_WITHDRAW_FAIL, message, 4, param, 12);
		}
		else
		{
			
		}
	}
	/**
	 * 授信推送
	 */
	@Override
	@Async
	public void pushUserCreditStatus(Integer userId, Integer recruitCreditId) {
		// TODO Auto-generated method stub
		RecruitCredit recruitCredit = recruitCreditMapper.selectByPrimaryKey(recruitCreditId);
		Integer creditStatus = recruitCredit.getCreditStatus();
		User userDetail = userMapper.queryUserDetail(userId);
		if(!CmsUtils.isNullOrEmpty(userDetail.getLastDevice()) && !Constants.JSAPI.equals(userDetail.getLastDevice()))
		{
			if(!CmsUtils.isNullOrEmpty(userDetail.getAppUserToken()))
			{
				String msg = "恭喜您,【"+ recruitCredit.getRecruit().getRecruitTitle()+ "】已通过授信";
				if(creditStatus==3)
				{
					msg = "非常抱歉,您的【"+recruitCredit.getRecruit().getRecruitTitle()+ "】未通过授信,请重新提交资料";
				}
				if(Constants.IOS.equals(userDetail.getLastDevice()))
				{
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", userDetail.getAppUserToken());
					param.put("content", msg);
					JSONObject customized = new JSONObject();
					customized.put("pushType", "recruitCredit");
					customized.put("userId", userId);
					customized.put("recruitCredit",recruitCredit.getCredit().getCreditName() );
					customized.put("messageTypeId", 4);
					param.put("customized", customized);
					try 
					{
						IOSPushUtils.sendIOSSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(Constants.ANDROID.equals(userDetail.getLastDevice()))
				{
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", userDetail.getAppUserToken());
					param.put("title", Constants.DDXJ);
					param.put("text", msg);
					Map<String, Object> data = new HashMap<>();
					data.put("messageTypeId", 4);
					param.put("data", JsonUtil.map2jsonToObject(data));
					try 
					{
						AndroidPushUtils.sendAndroidSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				log.info("#####授信推送成功###");
			}
		}
		StringBuffer paramBuffer = new StringBuffer();
		paramBuffer.append("{").append("\"recruitId\":").append(recruitCredit.getRecruitId()).append("}");
		String param = paramBuffer.toString();
		if(creditStatus == 2)
		{
			String message = "恭喜您,【"+ recruitCredit.getRecruit().getRecruitTitle()+ "】已通过授信";
			addUserMessage(userId, Constants.MESSAGE_CREDIT_SUC, message, 4, param, 13);
		}
		if(creditStatus == 3)
		{
			String message = "非常抱歉,您的【"+recruitCredit.getRecruit().getRecruitTitle()+ "】未通过授信,请重新提交资料";
			addUserMessage(userId, Constants.MESSAGE_CREDIT_FAIL, message, 4, param, 14);
		}
		else
		{
			
		}
	}

	/**
	 * 推送招聘邀请
	 */
	@Override
	public void pushRecruitRequest(Integer toUserId, Integer recruitId) {
		Recruit recruit = recruitMapper.selectByPrimaryKey(recruitId);
		User user = userMapper.selectByPrimaryKey(toUserId);
		if(!CmsUtils.isNullOrEmpty(user.getLastDevice()) && !Constants.JSAPI.equals(user.getLastDevice()))
		{
			if(!CmsUtils.isNullOrEmpty(user.getAppUserToken()))
			{
				if(Constants.IOS.equals(user.getLastDevice()))
				{
					//IOS推送
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", user.getAppUserToken());
					param.put("content", recruit.getRecruitAddress()+"工头的【"+recruit.getRecruitTitle()+"】邀请您加入,待遇从优,快来加入吧！");
					JSONObject customized = new JSONObject();
					customized.put("pushType", "inviteStatus");//推送类型 inviteStatus 表示招聘邀请推送
					customized.put("userId", toUserId);
					customized.put("messageTypeId", 2);
					param.put("customized", customized);
					try 
					{
						IOSPushUtils.sendIOSSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(Constants.ANDROID.equals(user.getLastDevice()))
				{
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", user.getAppUserToken());
					param.put("title", Constants.DDXJ);
					param.put("text", recruit.getRecruitAddress()+"工头的【"+recruit.getRecruitTitle()+"】邀请您加入,待遇从优,快来加入吧！");// 当content-available=1时(静默推送),可选; 否则必填。// 可为JSON类型和字符串类型
					param.put("img",recruit.getCoverImage());
					Map<String, Object> data = new HashMap<>();
					data.put("messageTypeId", 2);
					param.put("data", JsonUtil.map2jsonToObject(data));
					try 
					{
						AndroidPushUtils.sendAndroidSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				log.info("#####推送招聘邀请推送成功###");
		}
	}
		StringBuffer paramBuffer = new StringBuffer();
		paramBuffer.append("{").append("\"recruitId\":").append(recruitId).append("}");
		String param = paramBuffer.toString();
		addUserMessage(user.getId(), Constants.MESSAGE_USER_REQUEST, recruit.getRecruitAddress()+"工头的【"+recruit.getRecruitTitle()+"】邀请您加入,待遇从优,快来加入吧！", 2, param, 15);
		//营销短信发送
		List<Category> recruitCategoryList = recruit.getCategoryList();
		YunPianSMSUtils.sendInviteActivityTemplate(user.getPhone(), recruit.getRecruitAddress(), recruitCategoryList.get(0).getCategoryName());
	}
	
	/**
	 * 推送发放工资
	 */
	@Override
	public void pushPayMoney(Integer userId,Integer recruitId,SalaryRecord salaryRecord,UserTransfer userTransfer) {
		Recruit recruit = recruitMapper.selectByPrimaryKey(recruitId);
		Integer workerId = userId;//工人ID
		Integer foreWorkerId = recruit.getUserId();//工头ID
		User worker = userMapper.selectByPrimaryKey(workerId);
		User foreWorker = userMapper.selectByPrimaryKey(foreWorkerId);
		int typeId = 25;//代表信息中心跳转到资金详情页面
		StringBuffer paramBuffer = new StringBuffer();
		paramBuffer.append("{").append("\"userTransferId\":").append(userTransfer.getId()).append(",").append("\"transferType\":").append(userTransfer.getTransferType()).append("}");
		String Messageparam = paramBuffer.toString();
		String content = null;
		//添加工人端公告
		Notice notice = new Notice();
		notice.setNoticeContent(worker.getRealName().charAt(0)+"师傅已经成功获得工资:"+salaryRecord.getMoney()+"元");
		notice.setNoticeType(1);
		notice.setStartTime(new Date());
		//结束时间是开始时间推迟24小时
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 1);
		notice.setEndTime(calendar.getTime());
		notice.setCreateTime(new Date());
		notice.setUpdateTime(new Date());
		//余额支付
		if(salaryRecord.getTransferWay() == 2)
		{
			//添加至工人消息中心
			content = "您参加的【"+recruit.getRecruitTitle()+"】已成功结算工资"+salaryRecord.getMoney()+"元,请注意查收!";
			addUserMessage(workerId, Constants.MESSAGE_RECRUIT_RECORD_SALARY, content, 4, Messageparam, typeId);
			noticeMapper.insertSelective(notice);
		}
		//授信支付
		if(salaryRecord.getTransferWay() == 1)
		{
			//审核成功
			if(salaryRecord.getAuditStatus() == 2)
			{
				//添加至工人消息中心
				content = "您参加的【"+recruit.getRecruitTitle()+"】已成功结算工资"+salaryRecord.getMoney()+"元,请注意查收!";
				addUserMessage(workerId, Constants.MESSAGE_RECRUIT_RECORD_SALARY, content, 4, Messageparam, typeId);
				noticeMapper.insertSelective(notice);
			}
			//审核失败
			if(salaryRecord.getAuditStatus() == 3)
			{
				content = "非常抱歉,【"+recruit.getRecruitTitle()+"】授信余额发放给"+ worker.getRealName() + "的工资审核失败,请重新发放!";
				typeId = 0;
				addUserMessage(foreWorkerId, Constants.MESSAGE_CREDIT_RECODE_FAIL, content, 4, Messageparam, typeId);
				//工头推送
				if(!CmsUtils.isNullOrEmpty(foreWorker.getLastDevice()) && !Constants.JSAPI.equals(foreWorker.getLastDevice()))
				{
					if(!CmsUtils.isNullOrEmpty(foreWorker.getAppUserToken()))
					{
						if(Constants.IOS.equals(foreWorker.getLastDevice()))
						{
							Map<String,Object> param = new HashMap<String,Object>();
							param.put("deviceToken", foreWorker.getAppUserToken());
							param.put("content", content);
							JSONObject customized = new JSONObject();
							customized.put("messageTypeId", 4);//跳转到消息中心交易页面标识
							param.put("customized", customized);
							try 
							{
								IOSPushUtils.sendIOSSingleMsg(param);
							}
							catch (Exception e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(Constants.ANDROID.equals(foreWorker.getLastDevice()))
						{
							Map<String,Object> param = new HashMap<String,Object>();
							param.put("deviceToken", foreWorker.getAppUserToken());
							param.put("title", Constants.DDXJ);
							param.put("text", content);
							Map<String, Object> data = new HashMap<>();
							data.put("messageTypeId", 4);
							param.put("data", JsonUtil.map2jsonToObject(data));
							try 
							{
								AndroidPushUtils.sendAndroidSingleMsg(param);
							}
							catch (Exception e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						log.info("####工头授信余额推送####");
					}
				}
			}
		}
		if(salaryRecord.getTransferWay() == 2 || (salaryRecord.getTransferWay() == 1 && salaryRecord.getAuditStatus() == 2))
		{
			//工人推送
			if(!CmsUtils.isNullOrEmpty(worker.getLastDevice()) && !Constants.JSAPI.equals(worker.getLastDevice()))
			{
				if(!CmsUtils.isNullOrEmpty(worker.getAppUserToken()))
				{
					if(Constants.IOS.equals(worker.getLastDevice()))
					{
						Map<String,Object> param = new HashMap<String,Object>();
						param.put("deviceToken", worker.getAppUserToken());
						param.put("content", content);
						JSONObject customized = new JSONObject();
						customized.put("messageTypeId", 4);//跳转到消息中心交易页面标识
						param.put("customized", customized);
						try 
						{
							IOSPushUtils.sendIOSSingleMsg(param);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(Constants.ANDROID.equals(worker.getLastDevice()))
					{
						Map<String,Object> param = new HashMap<String,Object>();
						param.put("deviceToken", worker.getAppUserToken());
						param.put("title", Constants.DDXJ);
						param.put("text", content);
						Map<String, Object> data = new HashMap<>();
						data.put("messageTypeId", 4);
						param.put("data", JsonUtil.map2jsonToObject(data));
						try 
						{
							AndroidPushUtils.sendAndroidSingleMsg(param);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					log.info("#############推送工人工资到账成功################");
				}
			}
		}
	}

	/**
	 * 推送动态点赞
	 */
	@Override
	public void pushCircleGive(Integer fromUserId, Integer circleId) {
		User fromUser = userMapper.selectByPrimaryKey(fromUserId);
		Circle circle = circleMapper.selectByPrimaryKey(circleId);
		User toUser = userMapper.selectByPrimaryKey(circle.getUserId());
		if(!CmsUtils.isNullOrEmpty(toUser.getLastDevice()) && !Constants.JSAPI.equals(toUser.getLastDevice()))
		{
			if(!CmsUtils.isNullOrEmpty(toUser.getAppUserToken()))
			{
				if(Constants.IOS.equals(toUser.getLastDevice()))
				{
					//IOS推送
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", fromUser.getAppUserToken());
					param.put("content", fromUser.getRealName()+"工友给您点赞");
					JSONObject customized = new JSONObject();
					customized.put("pushType", "thumbStatus");//推送类型 thumbStatus 表示推送动态点赞
					customized.put("userId", toUser.getId());
					param.put("customized", customized);
					try 
					{
						IOSPushUtils.sendIOSSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(Constants.ANDROID.equals(toUser.getLastDevice()))
				{
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", toUser.getAppUserToken());
					param.put("title", Constants.DDXJ);
					param.put("text", fromUser.getRealName()+"工友给您点赞了");// 当content-available=1时(静默推送),可选; 否则必填。// 可为JSON类型和字符串类型
					//param.put("img",recruit.getCoverImage());
					Map<String, Object> data = new HashMap<>();
					data.put("messageTypeId", 4);
					param.put("data", JsonUtil.map2jsonToObject(data));
					try 
					{
						AndroidPushUtils.sendAndroidSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				log.info("#####推送动态点赞成功###");
			}
		}
	}
	
	/**
	 * 推送圈子动态评论信息
	 */
	@Override
	public void pushCircleComment(Integer circleCommentId) {
		CircleComment circleComment = circleCommentMapper.selectByPrimaryKey(circleCommentId);
		User toUser = userMapper.selectByPrimaryKey(circleComment.getToUserId());
		User fromUser = userMapper.selectByPrimaryKey(circleComment.getFromUserId());
		if(!CmsUtils.isNullOrEmpty(toUser.getLastDevice()) && !Constants.JSAPI.equals(toUser.getLastDevice()))
		{
			if(!CmsUtils.isNullOrEmpty(toUser.getAppUserToken()))
			{
				if(Constants.IOS.equals(toUser.getLastDevice()))
				{
					//IOS推送
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", toUser.getAppUserToken());
					String content = fromUser.getRealName()+"评论了:"+circleComment.getContent();
					param.put("content", content);
					JSONObject customized = new JSONObject();
					customized.put("pushType", "commentStatus");//推送类型 commentStatus 表示推送动态动态评论信息
					customized.put("userId", toUser.getId());
					customized.put("messageTypeId", 3);
					param.put("customized", customized);
					try 
					{
						IOSPushUtils.sendIOSSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(Constants.ANDROID.equals(toUser.getLastDevice()))
				{
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", toUser.getAppUserToken());
					param.put("title", Constants.DDXJ);
					String content = fromUser.getRealName()+"评论了:"+circleComment.getContent();
					param.put("text", content);// 当content-available=1时(静默推送),可选; 否则必填。// 可为JSON类型和字符串类型
					//param.put("img",recruit.getCoverImage());
					Map<String, Object> data = new HashMap<>();
					data.put("messageTypeId", 3);
					param.put("data", JsonUtil.map2jsonToObject(data));
					try 
					{
						AndroidPushUtils.sendAndroidSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				log.info("#####推送动态评论信息###");
				
			}
		}
	}
	
	/**
	 * 推送招聘活动完工评论
	 */
	@Override
	public void pushRecruitComment(Integer fromUserId,Integer toUserId,String RecruitTitle) {
		
		User toUser = userMapper.selectByPrimaryKey(toUserId);
		User fromUser = userMapper.selectByPrimaryKey(fromUserId);
		if(fromUser.getRole() == Constants.Number.TWO_INT)
		{
			//工头给工人评论
			if(!CmsUtils.isNullOrEmpty(toUser.getLastDevice()) && !Constants.JSAPI.equals(toUser.getLastDevice()))
			{
				if(!CmsUtils.isNullOrEmpty(toUser.getAppUserToken()))
				{
					if(Constants.IOS.equals(toUser.getLastDevice()))
					{
						//IOS推送
						Map<String,Object> param = new HashMap<String,Object>();
						param.put("deviceToken", toUser.getAppUserToken());
						String content = "【 "+RecruitTitle+" 】的工头评价了您的工作";
						param.put("content", content);
						JSONObject customized = new JSONObject();
						customized.put("pushType", "commentStatus");//推送类型 commentStatus 表示推送动态评论信息
						customized.put("userId", toUser.getId());
						customized.put("messageTypeId", 3);
						param.put("customized", customized);
						try 
						{
							IOSPushUtils.sendIOSSingleMsg(param);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if(Constants.ANDROID.equals(toUser.getLastDevice()))
					{
						Map<String,Object> param = new HashMap<String,Object>();
						param.put("deviceToken", toUser.getAppUserToken());
						param.put("title", Constants.DDXJ);
						String content = "【 "+RecruitTitle+" 】的工头评价了您的工作";
						param.put("text", content);// 当content-available=1时(静默推送),可选; 否则必填。// 可为JSON类型和字符串类型
						//param.put("img",recruit.getCoverImage());
						Map<String, Object> data = new HashMap<>();
						data.put("messageTypeId", 3);
						param.put("data", JsonUtil.map2jsonToObject(data));
						try 
						{
							AndroidPushUtils.sendAndroidSingleMsg(param);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					log.info("#####推送动态评论信息###");
				}
			}
		}
		if(fromUser.getRole() == Constants.Number.ONE_INT)
		{
			//工人给工头评论
			if(!CmsUtils.isNullOrEmpty(toUser.getLastDevice()) && !Constants.JSAPI.equals(toUser.getLastDevice()))
			{
				if(!CmsUtils.isNullOrEmpty(toUser.getAppUserToken()))
				{
					if(Constants.IOS.equals(toUser.getLastDevice()))
					{
						//IOS推送
						Map<String,Object> param = new HashMap<String,Object>();
						param.put("deviceToken", toUser.getAppUserToken());
						String content = "【 "+RecruitTitle+" 】的工人评价了您的项目";
						param.put("content", content);
						JSONObject customized = new JSONObject();
						customized.put("pushType", "commentStatus");//推送类型 commentStatus 表示推送动态评论信息
						customized.put("userId", toUser.getId());
						customized.put("messageTypeId", 3);
						param.put("customized", customized);
						try 
						{
							IOSPushUtils.sendIOSSingleMsg(param);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if(Constants.ANDROID.equals(toUser.getLastDevice()))
					{
						Map<String,Object> param = new HashMap<String,Object>();
						param.put("deviceToken", toUser.getAppUserToken());
						param.put("title", Constants.DDXJ);
						String content = "【 "+RecruitTitle+" 】的工人评价了您的项目";
						param.put("text", content);// 当content-available=1时(静默推送),可选; 否则必填。// 可为JSON类型和字符串类型
						Map<String, Object> data = new HashMap<>();
						data.put("messageTypeId", 3);
						param.put("data", JsonUtil.map2jsonToObject(data));
						try 
						{
							AndroidPushUtils.sendAndroidSingleMsg(param);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					log.info("#####推送动态评论信息###");
				}
			}
		}
	}
	
	@Override
	public void pushWorkerOrForeManStatus(Integer userId, Integer recruitId) {
		Recruit recruit = recruitMapper.selectByPrimaryKey(recruitId);
		RecruitRecord recruitRecord = recruitRecordMapper.queryAllWorkerDeliver(recruitId, userId);
		Integer recruitRecordId = recruitRecord.getId(); 
		Integer workerId = userId;//工人ID
		Integer foreWorkerId = recruit.getUserId();//工头ID
		Integer enlistStatus = recruitRecord.getEnlistStatus();//工人报名状态
		Integer workerStatus = recruitRecord.getWorkerStatus();//工人开工状态
		Integer foreWorkerStatus = recruitRecord.getForemanStatus();//工头开工状态
		User worker = userMapper.selectByPrimaryKey(workerId);
		User foreWorker = userMapper.selectByPrimaryKey(foreWorkerId);
		String content = null;
		String messageParam = null;
		String messageTitle = null;
		Integer typeId = null;
		int workerCount = 0;
		int foreCount = 0;
		StringBuffer paramBuffer = new StringBuffer();
		paramBuffer.append("{").append("\"recruitRecordId\":").append(recruitRecordId).append(",").append("\"userId\":").append(workerId).append(",").append("\"role\":").append(worker.getRole()).append(",").append("\"recruitId\":").append(recruitId).append("}");
		messageParam = paramBuffer.toString();
		if(recruitRecord.getFlag() == 1)
		{
			//工人添加消息
			if(enlistStatus == 1 && workerStatus == 0 && foreWorkerStatus == 0)
			{
				messageTitle = Constants.MESSAGE_RECRUIT_RECORD_SUC;
				content = "您已成功报名【"+recruit.getRecruitTitle()+"】,请耐心等待工头同意";
				typeId = 16;
				workerCount++;
			}else if(enlistStatus == 3)
			{
				messageTitle = Constants.MESSAGE_RECRUIT_RECORD_FAIL;
				content = "您报名的【"+recruit.getRecruitTitle()+"】被工头残忍拒绝报名,不要灰心,再接再厉";
				typeId = 0;
				workerCount++;
			}else if(enlistStatus == 2 && workerStatus == 4)
			{
				messageTitle = Constants.MESSAGE_RECRUIT_RECORD_HIRE_FAIL;
				content = "您报名的【"+recruit.getRecruitTitle()+"】被工头残忍拒绝录用,不要灰心,再接再厉";
				typeId = 0;
				workerCount++;
			}else if(enlistStatus == 2 && foreWorkerStatus == 0 && workerStatus == 1)
			{
				messageTitle = Constants.MESSAGE_RECRUIT_RECORD_CONFIRM;
				content = "您报名的【"+recruit.getRecruitTitle()+"】已成功确认到达,请等待工头确认";
				typeId = 17;
				workerCount++;
			}else if(enlistStatus == 2 && foreWorkerStatus == 0 && workerStatus == 0)
			{
				messageTitle = Constants.MESSAGE_RECRUIT_RECORD_STARTWORK;
				content = "您报名的【"+recruit.getRecruitTitle()+"】已被工头同意,请注意开工时间前往工地";
				typeId = 18;
				workerCount++;
			}else if(enlistStatus == 2 && foreWorkerStatus == 1 && workerStatus == 5)
			{
				messageTitle = Constants.MESSAGE_RECRUIT_RECORD_STARTPROMPT;
				content = "您报名的【"+recruit.getRecruitTitle()+"】工头已经确认开工,请点击到达工地开工";
				typeId = 19;
				workerCount++;
			}else if(enlistStatus == 2 && foreWorkerStatus == 2 && workerStatus == 2 && recruitRecord.getBalanceStatus() == 0)
			{
				messageTitle = Constants.MESSAGE_RECRUIT_RECORD_SAFETY;
				content = "您报名的【"+recruit.getRecruitTitle()+"】已成功开工,请注意施工安全";
				typeId = 20;
				workerCount++;
			}
/*			else if(enlistStatus == 2 && foreWorkerStatus == 2 && workerStatus == 2 && recruitRecord.getBalanceStatus() == 1)
			{
				messageTitle = Constants.MESSAGE_RECRUIT_RECORD_SALARY;
				content = "您参加的【"+recruit.getRecruitTitle()+"】已成功结算工资";
				typeId = 0;
				workerCount++;
			}*/
			else if(enlistStatus == 2 && foreWorkerStatus == 3 && workerStatus == 3 && recruitRecord.getWorkerCommentStatus() == 0)
			{
				messageTitle = Constants.MESSAGE_RECRUIT_RECORD_EVALUATE;
				content = "您参加的【"+recruit.getRecruitTitle()+"】已成功结束,快去评价工头吧";
				typeId = 24;
				workerCount++;
			}
			if(workerCount > 0)
			{
				addUserMessage(userId, messageTitle, content, 2, messageParam, typeId);
			}
			//给工头添加消息
			if(enlistStatus == 1 && workerStatus == 0 && foreWorkerStatus == 0)
			{
				messageTitle = Constants.MESSAGE_APPLY_SUC;
				content = worker.getRealName()+"已成功报名【"+recruit.getRecruitTitle()+"】,正在等待您的同意";
				//发送营销短信
				if(Boolean.valueOf(Constants.YUNPIAN_SMS_PUSH))
				{
					YunPianSMSUtils.sendApplyActivityTemplate(foreWorker.getPhone(), String.valueOf(foreWorker.getRealName().charAt(0)), "("+recruit.getRecruitTitle()+")");
				}
				typeId = 4;
				foreCount++;
			}else if(enlistStatus == 2 && workerStatus == 1 && foreWorkerStatus == 0)
			{
				messageTitle = Constants.MESSAGE_APPLY_GET;
				content = worker.getRealName()+"已到达【"+recruit.getRecruitTitle()+"】工地,请及时确认到达";
				typeId = 5;
				foreCount++;
			}else if(enlistStatus == 2 && foreWorkerStatus == 2 && workerStatus == 2 && recruitRecord.getBalanceStatus() == 0)
			{
				messageTitle = Constants.MESSAGE_APPLY_START;
				content = "您的【"+recruit.getRecruitTitle()+"】已成功开工,请注意施工安全";
				typeId = 22;
				foreCount++;
			}else if(enlistStatus == 2 && foreWorkerStatus == 3 && workerStatus == 3 && recruitRecord.getForemanCommentStatus() == 0)
			{
				messageTitle = Constants.MESSAGE_APPLY_FINISH;
				content = "您的【"+recruit.getRecruitTitle()+"】已成功结束,快去评价工人吧";
				typeId = 6;
				foreCount++;
			}else if(enlistStatus == 2 && foreWorkerStatus == 0 && workerStatus == 0)
			{
				messageTitle = Constants.MESSAGE_APPLY_AGREE;
				content = "您的【"+recruit.getRecruitTitle()+"】已同意"+ worker.getRealName() + "报名";
				typeId = 0;
				foreCount++;
			}else if(enlistStatus == 3)
			{
				messageTitle = Constants.MESSAGE_APPLY_REFUSE;
				content = "您的【"+recruit.getRecruitTitle()+"】已拒绝"+ worker.getRealName() + "报名";
				typeId = 0;
				foreCount++;
			}else if(enlistStatus == 2 && workerStatus == 4)
			{
				messageTitle = Constants.MESSAGE_APPLY_NOT_EMPLOYED;
				content = "您的【"+recruit.getRecruitTitle()+"】不予录用"+ worker.getRealName();
				typeId = 0;
				foreCount++;
			}
			if(foreCount > 0)
			{
				addUserMessage(foreWorkerId, messageTitle, content, 2, messageParam, typeId);
			}
		}else if(recruitRecord.getFlag() == 2)
		{
			messageTitle = Constants.MESSAGE_RECRUIT_RECORD_CANCEL;
			content = "您报名的【"+recruit.getRecruitTitle()+"】已成功取消";
			typeId = 0;
			addUserMessage(userId, messageTitle, content, 2, messageParam, typeId);
			messageTitle = Constants.MESSAGE_APPLY_CANCER;
			content = worker.getRealName()+"已取消报名【"+recruit.getRecruitTitle()+"】";
			addUserMessage(foreWorkerId, messageTitle, content, 2, messageParam, typeId);
		}
		if(!CmsUtils.isNullOrEmpty(worker.getLastDevice()) && !Constants.JSAPI.equals(worker.getLastDevice()))
		{
			//给工人推送
			if(!CmsUtils.isNullOrEmpty(worker.getLastDevice()))
			{
				if(Constants.IOS.equals(worker.getLastDevice()))
				{
					//IOS推送
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", worker.getAppUserToken());
					JSONObject customized = new JSONObject();
					customized.put("pushType", "WorkerStatus");
					customized.put("userId", userId);
					customized.put("messageTypeId", 2);
					param.put("customized", customized);
					if(recruitRecord.getFlag() == 1)
					{
						if(enlistStatus == 1 && workerStatus == 0 && foreWorkerStatus == 0)
						{
							content = "您已成功报名【"+recruit.getRecruitTitle()+"】,请耐心等待工头同意";
							param.put("content", content);
						}
						else if(enlistStatus == 3)
						{
							content = "您报名的【"+recruit.getRecruitTitle()+"】被工头残忍拒绝报名,不要灰心,再接再厉";
							param.put("content", content);
						}
						else if(enlistStatus == 2 && workerStatus == 4)
						{
							content = "您报名的【"+recruit.getRecruitTitle()+"】被工头残忍拒绝录用,不要灰心,再接再厉";
							param.put("content", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 0 && workerStatus == 1)
						{
							content = "您报名的【"+recruit.getRecruitTitle()+"】已成功确认到达,请等待工头确认";
							param.put("content", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 0 && workerStatus == 0)
						{
							content = "您报名的【"+recruit.getRecruitTitle()+"】已被工头同意,请注意开工时间前往工地";
							param.put("content", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 1 && workerStatus == 5)
						{
							content = "您报名的【"+recruit.getRecruitTitle()+"】工头已经确认开工,请点击到达工地开工";
							param.put("content", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 2 && workerStatus == 2 && recruitRecord.getBalanceStatus() == 0)
						{
							content = "您报名的【"+recruit.getRecruitTitle()+"】已成功开工,请注意施工安全";
							param.put("content", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 3 && workerStatus == 3 && recruitRecord.getWorkerCommentStatus() == 0)
						{
							content = "您参加的【"+recruit.getRecruitTitle()+"】已成功结束项目,快去评价工头吧";
							param.put("content", content);
						}
					}else if(recruitRecord.getFlag() == 2)
					{
						content = "您报名的【"+recruit.getRecruitTitle()+"】已成功取消";
						param.put("content", content);
					}
					try 
					{
						IOSPushUtils.sendIOSSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(Constants.ANDROID.equals(worker.getLastDevice()))
				{
					//Andriod推送
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", worker.getAppUserToken());
					param.put("title", Constants.DDXJ);
					param.put("img",recruit.getCoverImage());
					Map<String,Object> data = new HashMap<String,Object>();
					data.put("pushType", "WorkerStatus");
					data.put("messageTypeId", 2);
					data.put("userId", userId);
					param.put("data", JsonUtil.map2jsonToObject(data));
					if(recruitRecord.getFlag() == 1)
					{
						if(enlistStatus == 1 && workerStatus == 0 && foreWorkerStatus == 0)
						{
							content = "您已成功报名【"+recruit.getRecruitTitle()+"】,请耐心等待工头同意";
							param.put("text", content);
						}
						else if(enlistStatus == 3)
						{
							content = "您报名的【"+recruit.getRecruitTitle()+"】被工头残忍拒绝报名,不要灰心,再接再厉";
							param.put("text", content);
						}
						else if(enlistStatus == 2 && workerStatus == 4)
						{
							content = "您报名的【"+recruit.getRecruitTitle()+"】被工头残忍拒绝录用,不要灰心,再接再厉";
							param.put("text", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 0 && workerStatus == 1)
						{
							content = "您报名的【"+recruit.getRecruitTitle()+"】已成功确认到达,请等待工头确认";
							param.put("text", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 0 && workerStatus == 0)
						{
							content = "您报名的【"+recruit.getRecruitTitle()+"】已被工头同意,请注意开工时间前往工地";
							param.put("text", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 1 && workerStatus == 5)
						{
							content = "您报名的【"+recruit.getRecruitTitle()+"】工头已经确认开工,请点击到达工地开工";
							param.put("text", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 2 && workerStatus == 2 && recruitRecord.getBalanceStatus() == 0)
						{
							content = "您报名的【"+recruit.getRecruitTitle()+"】已成功开工,请注意施工安全";
							param.put("text", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 3 && workerStatus == 3 && recruitRecord.getWorkerCommentStatus() == 0)
						{
							content = "您参加的【"+recruit.getRecruitTitle()+"】已成功结束项目,快去评价工头吧";
							param.put("text", content);
						}
				}else if(recruitRecord.getFlag() == 2)
				{
					content = "您报名的【"+recruit.getRecruitTitle()+"】已成功取消";
					param.put("text", content);
				}
				try 
				{
					AndroidPushUtils.sendAndroidSingleMsg(param);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(!CmsUtils.isNullOrEmpty(foreWorker.getLastDevice()) && !Constants.JSAPI.equals(foreWorker.getLastDevice()))
		{
			//给工头推送
			if(!CmsUtils.isNullOrEmpty(foreWorker.getLastDevice()))
			{
				if(Constants.IOS.equals(foreWorker.getLastDevice()))
				{
					//IOS推送
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", foreWorker.getAppUserToken());
					JSONObject customized = new JSONObject();
					customized.put("pushType", "ForeManStatus");
					customized.put("userId", foreWorkerId);
					customized.put("messageTypeId", 2);
					param.put("customized", customized);
					if(recruitRecord.getFlag() == 1)
					{
						if(enlistStatus == 1 && workerStatus == 0 && foreWorkerStatus == 0)
						{
							content = worker.getRealName()+"已成功报名【"+recruit.getRecruitTitle()+"】,正在等待您的同意";
							param.put("content", content);
						}
						else if(enlistStatus == 2 && workerStatus == 1 && foreWorkerStatus == 0)
						{
							content = worker.getRealName()+"已到达【"+recruit.getRecruitTitle()+"】工地,请及时确认到达";
							param.put("content", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 2 && workerStatus == 2 && recruitRecord.getBalanceStatus() == 0)
						{
							content = "您的【"+recruit.getRecruitTitle()+"】已成功开工,请注意施工安全";
							param.put("content", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 3 && workerStatus == 3 && recruitRecord.getForemanCommentStatus() == 0)
						{
							content = "您的【"+recruit.getRecruitTitle()+"】已成功结束,快去评价工人吧";
							param.put("content", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 0 && workerStatus == 0)
						{
							content = "您的【"+recruit.getRecruitTitle()+"】已同意"+ worker.getRealName() + "报名";
							param.put("content", content);
						}else if(enlistStatus == 3)
						{
							content = "您的【"+recruit.getRecruitTitle()+"】已拒绝"+ worker.getRealName() + "报名";
							param.put("content", content);
						}else if(enlistStatus == 2 && workerStatus == 4)
						{
							content = "您的【"+recruit.getRecruitTitle()+"】不予录用"+ worker.getRealName();
							param.put("content", content);
						}
					}
					else if(recruitRecord.getFlag() == 2)
					{
						content = worker.getRealName()+"已取消报名【"+recruit.getRecruitTitle()+"】";
						param.put("content", content);
					}
					try 
					{
						IOSPushUtils.sendIOSSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(Constants.ANDROID.equals(foreWorker.getLastDevice()))
				{
					//Andriod推送
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", foreWorker.getAppUserToken());
					param.put("title", Constants.DDXJ);
					param.put("img",recruit.getCoverImage());
					Map<String,Object> data = new HashMap<String,Object>();
					data.put("pushType", "WorkerStatus");
					data.put("messageTypeId", 2);
					data.put("userId", userId);
					param.put("data", JsonUtil.map2jsonToObject(data));
					if(recruitRecord.getFlag() == 1)
					{
						if(enlistStatus == 1 && workerStatus == 0 && foreWorkerStatus == 0)
						{
							content = worker.getRealName()+"已成功报名【"+recruit.getRecruitTitle()+"】,正在等待您的同意";
							param.put("text", content);
						}
						else if(enlistStatus == 2 && workerStatus == 1 && foreWorkerStatus == 0)
						{
							content = worker.getRealName()+"已到达【"+recruit.getRecruitTitle()+"】工地,请及时确认到达";
							param.put("text", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 2 && workerStatus == 2 && recruitRecord.getBalanceStatus() == 0)
						{
							content = "您的【"+recruit.getRecruitTitle()+"】已成功开工,请注意施工安全";
							param.put("text", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 3 && workerStatus == 3 && recruitRecord.getForemanCommentStatus() == 0)
						{
							content = "您的【"+recruit.getRecruitTitle()+"】已成功结束,快去评价工人吧";
							param.put("text", content);
						}
						else if(enlistStatus == 2 && foreWorkerStatus == 0 && workerStatus == 0)
						{
							content = "您的【"+recruit.getRecruitTitle()+"】已同意"+ worker.getRealName() + "报名";
							param.put("text", content);
						}else if(enlistStatus == 3)
						{
							content = "您的【"+recruit.getRecruitTitle()+"】已拒绝"+ worker.getRealName() + "报名";
							param.put("text", content);
						}else if(enlistStatus == 2 && workerStatus == 4)
						{
							content = "您的【"+recruit.getRecruitTitle()+"】不予录用"+ worker.getRealName();
							param.put("text", content);
						}
					}
					else if(recruitRecord.getFlag() == 2)
					{
						content = worker.getRealName()+"已取消报名【"+recruit.getRecruitTitle()+"】";
						param.put("text", content);
					}
					try 
					{
						AndroidPushUtils.sendAndroidSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
	/**
	 * 发布批量推送对应工种
	 */
	@Override
	@Async
	public void pushRecruitBatchByCategory(Integer recruitId) {
		Recruit recruit = recruitMapper.selectByPrimaryKey(recruitId);
		List<Category> categoryList = recruit.getCategoryList();
		String pushIos = "";//IOS的token
		String pushAndroid = "";//安卓的TOKEN
		int iosCount = 0;//ios数量
		int androidCount = 0;//安卓推送数量
		List<String> allPhone = new ArrayList<String>();
		JSONObject customized = new JSONObject();
		customized.put("messageTypeId", 2);
		String messageTitle = Constants.MESSAGE_RECRUIT_NEWMESSAGE;
		int typeId = 23;//消息中心的消息标识
		StringBuffer paramBuffer = new StringBuffer();
		paramBuffer.append("{").append("\"recruitId\":").append(recruit.getId()).append("}");
		String messageParam = paramBuffer.toString();
		if(!CmsUtils.isNullOrEmpty(categoryList))
		{
			List<String> jsapiPhone = new ArrayList<String>();//最后一次用公众号登陆的用户
			String address = recruit.getRecruitProvince()+recruit.getRecruitCity()+recruit.getRecruitArea();
			for (int i = 0; i < categoryList.size(); i++) 
			{
				String content = address+"需要"+categoryList.get(i).getCategoryName()+recruit.getRecruitPerson()+"名,赶紧来报名吧！";
				List<User> userList = userMapper.userListByCategoryId(categoryList.get(i).getId());//获取所有该工种的工人
				if(!CmsUtils.isNullOrEmpty(userList))
				{
					for (int j = 0; j < userList.size(); j++) 
					{
						//消息中心添加消息
						addUserMessage(userList.get(j).getId(), messageTitle, content, 2, messageParam, typeId);
						if(Constants.JSAPI.equals(userList.get(j).getLastDevice()) || CmsUtils.isNullOrEmpty(userList.get(j).getLastDevice()))//微信公众号
						{
							jsapiPhone.add(userList.get(j).getPhone());//工人手机号码
						}
						else if(Constants.IOS.equals(userList.get(j).getLastDevice()))//苹果
						{
							if(!CmsUtils.isNullOrEmpty(userList.get(j).getAppUserToken()))
							{
								if(iosCount == 500)//批量推送最多500个token
								{
									Map<String,Object> param = new HashMap<String,Object>();
									if(",".equals(pushIos.substring(pushIos.length()-1,pushIos.length())))
									{
										pushIos = pushIos.substring(0,pushIos.length());
									}
									param.put("deviceToken", pushIos);
									param.put("title", "最新招聘信息 >>");
									param.put("body", address+"需要"+categoryList.get(i).getCategoryName()+recruit.getRecruitPerson()+ "名,赶紧来报名吧！");
									param.put("customized", customized);
									try 
									{
										IOSPushUtils.sendIOSBatchMsg(param);//调用推送
									}
									catch (Exception e) 
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									log.info("######IOS端 项目发布推送"+iosCount+"次######");
									pushIos = "";//初始化数据
									iosCount = 0;//初始化数量
								}
								pushIos += userList.get(j).getAppUserToken()+",";
								iosCount++;
							}
							else
							{
								jsapiPhone.add(userList.get(j).getPhone());//工人手机号码
							}
						}
						else if(Constants.ANDROID.equals(userList.get(j).getLastDevice()))//安卓
						{
							if(!CmsUtils.isNullOrEmpty(userList.get(j).getAppUserToken()))
							{
								if(androidCount == 500)//批量推送最多500个token
								{

									Map<String,Object> param = new HashMap<String,Object>();
									if(",".equals(pushAndroid.substring(pushAndroid.length()-1,pushAndroid.length())))
									{
										pushAndroid = pushAndroid.substring(0,pushAndroid.length());
									}
									param.put("deviceToken", pushAndroid);
									param.put("title", "最新招聘信息 >>");
									param.put("ticker", address+"需要"+categoryList.get(i).getCategoryName()+recruit.getRecruitPerson()+"名,赶紧来报名吧！");
									param.put("text", "工友们,赶紧来报名吧！");
									param.put("customized", customized);
									Map<String, Object> data = new HashMap<>();
									data.put("messageTypeId", 2);
									param.put("data", JsonUtil.map2jsonToObject(data));
									try 
									{
										AndroidPushUtils.sendAndroidSingleMsg(param);//调用推送
									} 
									catch (Exception e) 
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									log.info("######Android端 项目发布推送"+iosCount+"次######");
									pushAndroid = "";//初始化数据
									androidCount = 0;//初始化数量
								}
								pushAndroid += userList.get(j).getAppUserToken()+",";
								androidCount++;
							}
							else
							{
								jsapiPhone.add(userList.get(j).getPhone());//工人手机号码
							}
						}
						
						allPhone.add(userList.get(j).getPhone());
						
					}
				}
				if(!CmsUtils.isNullOrEmpty(pushAndroid))//安卓
				{
					if(",".equals(pushAndroid.substring(pushAndroid.length()-1,pushAndroid.length())))
					{
						pushAndroid = pushAndroid.substring(0,pushAndroid.length());
					}
					Map<String,Object> param = new HashMap<String,Object>();
					if(",".equals(pushAndroid.substring(pushAndroid.length()-1,pushAndroid.length())))
					{
						pushAndroid = pushAndroid.substring(0,pushAndroid.length());
					}
					param.put("deviceToken", pushAndroid);
					param.put("title", "最新招聘信息 >>");
					param.put("ticker", address+"需要"+categoryList.get(i).getCategoryName()+recruit.getRecruitPerson()+"名,赶紧来报名吧！");
					param.put("text", "工友们,赶紧来报名吧！");
					param.put("customized", customized);
					Map<String, Object> data = new HashMap<>();
					data.put("messageTypeId", 2);
					param.put("data", JsonUtil.map2jsonToObject(data));
					try 
					{
						AndroidPushUtils.sendAndroidSingleMsg(param);//调用推送
					} 
					catch (Exception e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.info("######Android端 项目发布推送"+iosCount+"次######");
				}
				if(!CmsUtils.isNullOrEmpty(pushIos))//ios
				{
					if(",".equals(pushIos.substring(pushIos.length()-1,pushIos.length())))
					{
						pushIos = pushIos.substring(0,pushIos.length());
					}
					
					Map<String,Object> param = new HashMap<String,Object>();
					if(",".equals(pushIos.substring(pushIos.length()-1,pushIos.length())))
					{
						pushIos = pushIos.substring(0,pushIos.length());
					}
					param.put("deviceToken", pushIos);
					param.put("title", "最新招聘信息 >>");
					param.put("body", address+"需要"+categoryList.get(i).getCategoryName()+recruit.getRecruitPerson()+"名,赶紧来报名吧！");
					param.put("customized", customized);
					try 
					{
						IOSPushUtils.sendIOSBatchMsg(param);//调用推送
					}
					catch (Exception e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.info("######IOS端 项目发布推送"+iosCount+"次######");
				}
				
//				if(!CmsUtils.isNullOrEmpty(jsapiPhone) && jsapiPhone.size() > 0)//接不到APP推送,发短信给工人
//				{
//					//暂时留着
//				}
				
				if(allPhone.size() > 0)
				{
					
					address=null;
					if(recruit.getRecruitProvince().equals(recruit.getRecruitCity()))
					{
						 address = recruit.getRecruitProvince()+recruit.getRecruitArea();

					}
					else
					{
						address= recruit.getRecruitProvince()+recruit.getRecruitCity()+recruit.getRecruitArea();
					}
					if(Boolean.valueOf(Constants.YUNPIAN_SMS_PUSH))
					{
						//YunPianSMSUtils.sendApplyTemplate(pushPhone,  address,categoryList.get(i).getCategoryName());
						for (String pushPhone : allPhone) 
						{
							YunPianSMSUtils.sendApplyTemplate(pushPhone,  address,categoryList.get(i).getCategoryName());
						}
					}
					
				}
			}
		}
		
	}
	@Override
	@Async
	public void addManagerMessage(Integer messageType,String messageContent,String messageTitle,String messageParameter)
	{
		Message message = new Message();
		message.setMessageTitle(messageTitle);
		message.setMessageContent(messageContent);
		message.setMessageType(messageType);
		message.setMessageParameter(messageParameter);
		message.setMessageRead(1);
		message.setCreateTime(new Date());
		messageMapper.insertSelective(message);
		redisUtils.set(Constants.VALIDATE_MESSAGE_NOTIC, !CmsUtils.isNullOrEmpty(redisUtils.get(Constants.VALIDATE_MESSAGE_NOTIC))?Integer.parseInt(redisUtils.get(Constants.VALIDATE_MESSAGE_NOTIC).toString()) + 1:1);
		log.info("####成功通知点点小匠后台管理系统###");
	}

	@Override
	@Async
	public void updateUserWechatInfo(String phone, String wechatInfo,String loginChannel) {
		User user = userMapper.queryUserByPhone(phone);
		if(!CmsUtils.isNullOrEmpty(user))
		{
			if(!CmsUtils.isNullOrEmpty(wechatInfo))
			{
				JSONObject wechatInfoJson = (JSONObject)JSONObject.parse(wechatInfo);
				if("JSAPI".equals(loginChannel))
				{
					user.setOpenid(wechatInfoJson.getString("openid"));
				}
				user.setWxName(wechatInfoJson.getString("nickname"));
				user.setUnionid(wechatInfoJson.getString("unionid"));
				userMapper.updateByPrimaryKey(user);
			}
		}
	}

	@Override
	@Async
	public void changeUserWechatInfo(String phone, String wechatInfo) {
		if(!CmsUtils.isNullOrEmpty(wechatInfo))
		{
			JSONObject wechatInfoJson = (JSONObject)JSONObject.parse(wechatInfo);
			User user = userMapper.queryUserByunionid(wechatInfoJson.getString("unionid"));
			if(!CmsUtils.isNullOrEmpty(user))
			{
				if(!phone.equals(user.getPhone()))
				{
					userMapper.changeUserWechatInfo(user.getId());
				}
			}
		}
		
	}

	@Override
	@Async
	public void addUserBonus(Integer userId,String openId,String phone,Integer registerChannel) {
		log.info("--------异步加钱启动,参数：userid:{},openId:{},phone:{},registerChannel:{}",userId,openId,phone,registerChannel);
		//给新用户加奖金
		List<UserTransfer> transfer = userTransferMapper.queryCapitalChangeRecord(userId);
		if(CmsUtils.isNullOrEmpty(transfer)){
			this.addTransferRecord(userId,new BigDecimal(20.00),"注册奖励金");
			this.updateRemainderBnousMoney(new BigDecimal(20.00), userId,1);
			YunPianSMSUtils.sendNewActivityTemplate(phone);
		}
		//公众号注册
		if(registerChannel==1){
			//查找此用户是否是用过二维码邀请用户 状态为 1.已关注
			InviteRecord inviteRecord = inviteRecordMapper.selectByOpenId(openId,1);
			if(!CmsUtils.isNullOrEmpty(inviteRecord)){
				//随机奖励 1~5元奖励金
				BigDecimal inviterBonus = ProRandomUtil.probabilityRandom();
				BigDecimal bonus = this.updateRemainderBnousMoney(inviterBonus,inviteRecord.getInviterId(),2);
				//更新邀请状态 为已注册
				inviteRecord.setStatus(3);
				inviteRecord.setUpdateTime(new Date());
				inviteRecord.setInviterBonus(bonus);
				inviteRecord.setInviteesBonus(new BigDecimal(20.00));
				inviteRecord.setInviteesPhone(phone);
				inviteRecordMapper.updateByPrimaryKeySelective(inviteRecord);
			}
		}
		//新用户奖励推送
		User user = userMapper.selectByPrimaryKey(userId);
		addUserMessage(userId, Constants.USER_NEWMESSAGE, "您好，您已获得20元现金奖励，请到我的钱包里查看吧！", 4, "", 0);
		if(!CmsUtils.isNullOrEmpty(user.getLastDevice()) && !Constants.JSAPI.equals(user.getLastDevice()))
		{
			if(!CmsUtils.isNullOrEmpty(user.getLastDevice()))
			{
				if(Constants.IOS.equals(user.getLastDevice()))
				{
					//IOS推送
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", user.getAppUserToken());
					JSONObject customized = new JSONObject();
					customized.put("userId", userId);
					customized.put("messageTypeId", 4);
					param.put("customized", customized);
					//推送内容
					param.put("content", "您好，您已获得20元现金奖励，请到我的钱包里查看吧！");
					try
					{
						IOSPushUtils.sendIOSSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(Constants.ANDROID.equals(user.getLastDevice()))
				{
					//Andriod推送
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", user.getAppUserToken());
					param.put("title", Constants.DDXJ);
					param.put("text","您好，您已获得20元现金奖励，请到我的钱包里查看吧！");// 当content-available=1时(静默推送),可选; 否则必填。// 可为JSON类型和字符串类型
					Map<String, Object> data = new HashMap<>();
					data.put("messageTypeId", 4);
					param.put("data", JsonUtil.map2jsonToObject(data));
					try 
					{
						AndroidPushUtils.sendAndroidSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private BigDecimal updateRemainderBnousMoney(BigDecimal bonus, Integer userId,int flag){
		if(flag == 1){
			userMapper.updateRemainderBnousMoney(bonus, userId);
		}else{
			int count = inviteRecordMapper.queryInviteRecordCount(userId);
			if(count==9||count==4){
				bonus = new BigDecimal("10.00");
			}
			BigDecimal money = inviteRecordMapper.queryInviteBonusCount(userId);
			if(CmsUtils.isNullOrEmpty(money)){
				money = new BigDecimal("0.00");
			}
			BigDecimal limit = new BigDecimal("500");
			if(limit.compareTo(money)>0){
				if(money.add(bonus).compareTo(limit)>=0){
					bonus = limit.subtract(money);
				}
				//给邀请的老用户加奖金
				this.addTransferRecord(userId,bonus,"邀请注册奖励金");
				userMapper.updateRemainderBnousMoney(bonus, userId);
				return bonus;
			}
		}
		return new BigDecimal(0.00);
	}
	
	private void addTransferRecord(Integer userId, BigDecimal bnous, String desc) {
		// 收款人的信息
		User toUser = userMapper.queryUserDetail(userId);
		// 收款人的金额
		BigDecimal toUserMoney = toUser.getRemainderMoney();

		UserTransfer userTransfer = new UserTransfer();
		userTransfer.setFromUserId(1);
		userTransfer.setToUserId(userId);
		userTransfer.setMoney(bnous);
		userTransfer.setTransferType(5);
		userTransfer.setCreateTime(new Date());
		userTransfer.setUpdateTime(new Date());
		userTransfer.setTransferDesc(desc);
		userTransfer.setTransferWay(2);
		userTransfer.setToOverplusBalance(toUserMoney.add(bnous));
		userTransfer.setOrderNo(OrderNumUtils.getOredrNum());
		userTransferMapper.insertSelective(userTransfer);
	}

	@Override
	public void sendAppTemplateFromCircle(Integer circleId)
	{
		Circle circle = circleMapper.selectByPrimaryKey(circleId);
		User user = userMapper.selectByPrimaryKey(circle.getUserId());
		Integer userId = user.getId();
		if(!CmsUtils.isNullOrEmpty(user.getLastDevice()) && !Constants.JSAPI.equals(user.getLastDevice()))
		{
			//给工人推送
			if(!CmsUtils.isNullOrEmpty(user.getLastDevice()))
			{
				if(Constants.IOS.equals(user.getLastDevice()))
				{
					//IOS推送
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", user.getAppUserToken());
					JSONObject customized = new JSONObject();
					customized.put("pushType", "WorkerStatus");
					customized.put("userId", userId);
					param.put("customized", customized);
					//推送内容
					param.put("text", "您发布的圈子(" + circle.getContent() + ")涉嫌违规已被管理员删除！");
					try
					{
						IOSPushUtils.sendIOSSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(Constants.ANDROID.equals(user.getLastDevice()))
				{
					//Andriod推送
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("deviceToken", user.getAppUserToken());
					param.put("title", Constants.DDXJ);
					JSONObject customized = new JSONObject();
					customized.put("pushType", "WorkerStatus");
					customized.put("userId", userId);
					param.put("customized", customized);
					//推送内容
					param.put("text", "您发布的圈子(" + circle.getContent() + ")涉嫌违规已被管理员删除！");
					try 
					{
						AndroidPushUtils.sendAndroidSingleMsg(param);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void addUserMessage(Integer userId,String title,String content,Integer messageTypeId,String param,Integer typeId){
		MessageCenter messageCenter = new MessageCenter();
		if(!CmsUtils.isNullOrEmpty(param))
		{
			messageCenter.setParam(param);
		}
		messageCenter.setIsRead(1);
		messageCenter.setTitle(title);
		messageCenter.setUserId(userId);
		messageCenter.setContent(content);
		messageCenter.setMessageTypeId(messageTypeId);
		messageCenter.setTypeId(typeId);
		messageCenter.setCreateTime(new Date());
		messageCenter.setUpdateTime(new Date());
		messageCenter.setFlag(1);
		messageCenterMapper.insertSelective(messageCenter);
	}

	@Override
	public void pushBaiduLink() {
		String url = "http://data.zz.baidu.com/urls?site=www.diandxj.com&token=IzAY90Wzvbdfjv7p";//网站的服务器连接
        String[] param = {
                "http://www.diandxj.com/index.html",
                "http://www.diandxj.com/product.html",
                "http://www.diandxj.com/company.html",
                "http://www.diandxj.com/salary-service.html"
        };
        String json = HttpUtils.baiduPost(url, param);//执行推送方法
       if(!CmsUtils.isNullOrEmpty(json))
       {
    	   JSONObject parse = (JSONObject) JSONObject.parse(json);
    	   Integer remain = parse.getInteger("remain");
    	   log.info("###baidu蜘蛛收入中,主动推送成功,推送剩余条数:"+remain+"###");
       }
	}
	
	/**
     * APP自定义群发
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws UnsupportedEncodingException 
     * @throws ClientException 
     */
	@Override
	@Async
	public void appCustomBatchMassMessage(List<User> userList,Integer logId) 
	{
		WechatSendMessage msg = wechatService.queryWechatSendMessageById(logId);
		
		String pushIos = "";//IOS的token
		String pushAndroid = "";//安卓的TOKEN
		int iosCount = 0;//ios数量
		int androidCount = 0;//安卓推送数量
		if(!CmsUtils.isNullOrEmpty(userList))
		{
			if(!CmsUtils.isNullOrEmpty(userList))
			{
				for (int i = 0; i < userList.size(); i++) 
				{
					//添加推送用户
					SendMessageUser user = new SendMessageUser();
					user.setUserId(userList.get(i).getId());
					user.setMessageId(msg.getId());
					user.setSendType(1);
					user.setSendTime(new Date());
					user.setCreateTime(new Date());
					user.setUpdateTime(new Date());
					
					//加入消息中心
					MessageCenter center = new MessageCenter();
					center.setMessageTypeId(1);
					center.setTitle(msg.getMassTitle());
					center.setContent(msg.getMassContent());
					center.setIsRead(1);
					center.setTypeId(1);
					center.setUserId(userList.get(i).getId());
					
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("userId", userList.get(i).getId());
			    	if(!CmsUtils.isNullOrEmpty(msg.getMassLink()))
			    	{
			    		data.put("url",msg.getMassLink());
			    	}
					center.setParam(JsonUtil.map2jsonToString(data));
					center.setCreateTime(new Date());
					center.setUpdateTime(new Date());
					
					if(msg.getSendType() == 3)
					{
						if(msg.getMassPlatform() == 1 || msg.getMassPlatform() == 3)//推送IOS
						{
							if(Constants.IOS.equals(userList.get(i).getLastDevice()))//苹果
							{
								if(!CmsUtils.isNullOrEmpty(userList.get(i).getAppUserToken()))
								{
									if(iosCount == 500)//批量推送最多500个token
									{
										Map<String,Object> param = new HashMap<String,Object>();
										if(",".equals(pushIos.substring(pushIos.length()-1,pushIos.length())))
										{
											pushIos = pushIos.substring(0,pushIos.length());
										}
										param.put("deviceToken", pushIos);
										param.put("title", msg.getMassTitle());
										param.put("subtitle", msg.getMassContent());
										param.put("body", msg.getMassContent());
										if(!CmsUtils.isNullOrEmpty(msg.getTimingTime()))
										{
											param.put("timingTime", DateUtils.getStringDate(msg.getTimingTime(), "yyyy-MM-dd HH:mm:ss"));
										}
										
										try 
										{
											int suc = IOSPushUtils.sendIOSBatchMsg(param);//调用推送
											if(suc == 2)
											{
												user.setSendType(2);
											}
										}
										catch (Exception e) 
										{
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										log.info("######IOS端 项目发布推送"+iosCount+"次######");
										pushIos = "";//初始化数据
										iosCount = 0;//初始化数量
									}
									pushIos += userList.get(i).getAppUserToken()+",";
									iosCount++;
								}
							}
						}
						
						if(msg.getMassPlatform() == 2 || msg.getMassPlatform() == 3)//推送安卓
						{
							if(Constants.ANDROID.equals(userList.get(i).getLastDevice()))//安卓
							{
								if(!CmsUtils.isNullOrEmpty(userList.get(i).getAppUserToken()))
								{
									if(androidCount == 500)//批量推送最多500个token
									{
										Map<String,Object> param = new HashMap<String,Object>();
										if(",".equals(pushAndroid.substring(pushAndroid.length()-1,pushAndroid.length())))
										{
											pushAndroid = pushAndroid.substring(0,pushAndroid.length());
										}
										param.put("deviceToken", pushAndroid);
										param.put("title", msg.getMassTitle());
										param.put("ticker", msg.getMassContent());
										param.put("text", msg.getMassContent());
										if(!CmsUtils.isNullOrEmpty(msg.getTimingTime()))
										{
											param.put("timingTime", DateUtils.getStringDate(msg.getTimingTime(), "yyyy-MM-dd HH:mm:ss"));
										}
										try
										{
											int suc = AndroidPushUtils.sendAndroidBatchMsg(param);//调用推送
											if(suc == 2)
											{
												user.setSendType(2);
											}
										}
										catch (Exception e) 
										{
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										log.info("######Android端 项目发布推送"+iosCount+"次######");
										pushAndroid = "";//初始化数据
										androidCount = 0;//初始化数量
									}
									pushAndroid += userList.get(i).getAppUserToken()+",";
									androidCount++;
								}
							}
						}
					}
					wechatService.addSendMessageUser(user);
					messageCenterMapper.insertSelective(center);
				}
			}
			
			if(msg.getMassPlatform() == 1 || msg.getMassPlatform() == 3)//推送IOS
			{
				if(!CmsUtils.isNullOrEmpty(pushIos))//ios
				{
					if(",".equals(pushIos.substring(pushIos.length()-1,pushIos.length())))
					{
						pushIos = pushIos.substring(0,pushIos.length());
					}
					
					Map<String,Object> param = new HashMap<String,Object>();
					if(",".equals(pushIos.substring(pushIos.length()-1,pushIos.length())))
					{
						pushIos = pushIos.substring(0,pushIos.length());
					}
					param.put("deviceToken", pushIos);
					param.put("title", msg.getMassTitle());
					param.put("body", msg.getMassContent());
					if(!CmsUtils.isNullOrEmpty(msg.getTimingTime()))
					{
						param.put("timingTime", DateUtils.getStringDate(msg.getTimingTime(), "yyyy-MM-dd HH:mm:ss"));
					}
					try 
					{
						IOSPushUtils.sendIOSBatchMsg(param);//调用推送
					}
					catch (Exception e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.info("######IOS端 项目发布推送"+iosCount+"次######");
				}
			}
			
			if(msg.getMassPlatform() == 2 || msg.getMassPlatform() == 3)//推送安卓
			{
				if(!CmsUtils.isNullOrEmpty(pushAndroid))//安卓
				{
					if(",".equals(pushAndroid.substring(pushAndroid.length()-1,pushAndroid.length())))
					{
						pushAndroid = pushAndroid.substring(0,pushAndroid.length());
					}
					Map<String,Object> param = new HashMap<String,Object>();
					if(",".equals(pushAndroid.substring(pushAndroid.length()-1,pushAndroid.length())))
					{
						pushAndroid = pushAndroid.substring(0,pushAndroid.length());
					}
					param.put("deviceToken", pushAndroid);
					param.put("title", msg.getMassTitle());
					param.put("ticker", msg.getMassContent());
					param.put("text", msg.getMassContent());
					if(!CmsUtils.isNullOrEmpty(msg.getTimingTime()))
					{
						param.put("timingTime", DateUtils.getStringDate(msg.getTimingTime(), "yyyy-MM-dd HH:mm:ss"));
					}
					try 
					{
						AndroidPushUtils.sendAndroidBatchMsg(param);//调用推送
					} 
					catch (Exception e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.info("######Android端 项目发布推送"+iosCount+"次######");
				}
			}
		}
	}

	@Override
	@Async
	public void updateUserAppUserTokenByUnionid(String unionid,
			String appUserToken) {
		userMapper.updateUserAppUserTokenByUnionid(unionid, appUserToken);
		
	}

	@Override
	public void updateUserInfoAsyncByUnionid(String unionid, String ip,
			String loginChannel) {
		// TODO Auto-generated method stub
		userMapper.updateUserInfoAsyncByUnionid(ip, unionid, loginChannel);
	}
}
