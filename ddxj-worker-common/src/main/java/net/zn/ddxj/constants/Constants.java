package net.zn.ddxj.constants;


import net.zn.ddxj.utils.PropertiesUtils;

public class Constants {
	public static final String ORDER_RANDOM = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	public static final String RANDOM_UPPER_WORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String RANDOM_NUMBER = "0123456789";
    public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final Integer SUCCESS_200 = 200;
    public static final Integer ERROR_500 = 500;
    public static final boolean TRUE = true;
    public static final boolean FALSE = false;
    public static final String COMMA= ",";
    public static final String Y= "Y";
    public static final String N= "N";
    public static final String OK= "OK";
    public static final String POST= "POST";
    public static final String GET= "GET";
    public static final String NULL_CHAR= "";
    public static final String YYYY_MM_DD= "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS= "yyyy-MM-dd HH:mm:ss";
    public static final String SUBMIT_HEADER= "header";
    public static final String SUBMIT_FORM= "form";
    public static final String DEFAULT_USER_HEAD= PropertiesUtils.getPropertiesByName("static_url")+"/Fgz_XN_PfpcoLflGWJFY475Dqwcb";//用户默认头像
    public static final String DEFAULT_FOREMAN_HEAD= PropertiesUtils.getPropertiesByName("static_url")+"/Fgz_XN_PfpcoLflGWJFY475Dqwcc";//工头默认头像
    public static final String USER_REGISTER_CODDE= "userRegisterCode:";//注册
    public static final String LOGIN_IDEN_CODE= "loginIdenCode:";//登录
    public static final String CHANGE_PASS_CODE= "changePassCode:";//修改密码
    public static final String CHANGE_PAY_CODE= "changePayCode:";//修改支付密码
    public static final String BOND_BANK_CARD_CODE= "bondBankCardCode:";//绑卡
    public static final String CREDIT_VERIFY_CODE= "creditVerifyCode:";//授信
    public static final String CHANGE_PHONE_CODE= "changePhoneCode:";//修改手机号
    public static final String FORGET_PASSWORD= "forgetPassword:";//忘记密码
    public static final String WEB_SITE_CODE= "webSiteCode:";//网站验证码
    public static final String PAYMENT_VERIFIED_CODE= "paymentVerifiedCode:";//打款验证码
    public static final String WECHAT_TAGS_LIST= "wechatTagsList:";//微信标签
    public static final String LOGIN_USER_INFO= "loginUserInfo:";
    public static final String VALIDATE_MESSAGE_NOTIC= "validateMessageNotic";
    public static final String BANK_VALIDATE= "BANK";
    public static final String USER_MONEY_LOG= "userMoneyLog#";
    public static final String REAL_VALIDATE= "REAL";
    public static final String SENSITIVEKEYWORDS= "SensitiveKeywords";
    public static final String JSAPI= "JSAPI";
    public static final String APP= "APP";
    public static final String IOS= "IOS";
    public static final String ANDROID= "Android";
    public static final String YES= "yes";
    public static final String NO= "no";
    public static final String DDXJ= "点点小匠";
    public static final String UPLOAD_IMAGE_PATH = "/ddxj/image/";
    public static final String SPT = "/";
    public static final String TOKEN_KEY = PropertiesUtils.getPropertiesByName("token_key");
    public static final String WX_APP_ID = PropertiesUtils.getPropertiesByName("wx_app_id");
	public static final String WX_APP_SECRET = PropertiesUtils.getPropertiesByName("wx_app_secret");
	public static final String SUBSCRIBE = "subscribe";//事件类型：subscribe(关注) 公众号订阅
	public static final String SCAN = "SCAN";//公众号已关注
	public static final String UNSUBSCRIBE = "unsubscribe";//公众号取消关注
	public static final String MESSAGE_TEXT = "text"; 	//公众号text消息
	public static final String MESSAGE_IMAGE = "image"; 	//公众号image消息
	public static final String MESSAGE_NEWS = "news"; 	//公众号图文消息
	public static final int MSG_CLICK_ACTION_LINK = 1;// 自定义链接
    public static final int MSG_CLICK_ACTION_SEND_WEIXIN_IMAGES_TEXT = 2;// 下发消息,是专门给第三方平台旗下未微信认证（具体而言，是资质认证未通过）的订阅号准备的事件类型
	//分销设置参数
    public static final String INVITE_WECHAT_KEY = "inviteWechatInfo";
    public static final String INVITE_FRIEND_KEY = "inviteFriendInfo";
//	public static final String INVITE_SHARE_TITLE = "inviteShareTitle";//分享标题
//	public static final String INVITE_SHARE_CONTENT = "inviteShareContent";//分享内容
//	public static final String INVITE_SHARE_IMAGE = "inviteShareImage";//分享图片
    public static final String CLICK = "CLICK";//点击推送
	
	//自定义菜单-基本设置
	public static final String USE_SYSTEM_DEFAULT_REPLY = "useSystemDefaultReply";//使用系统默认回复
	public static final String REPLY_TYPE = "replyType";//回复类型
	public static final String REPLAY_WORDS = "replayWords";//回复内容
	public static final String IMAGE_TEXT_LIST = "imageTextList";//选择信息
	public static final String BEFOLLOWED_REPLY_IMG = "befollowedReplyImg";//回复图片
	public static final String BEFOLLOWED_REPLY_MEDIAID = "befollowedReplyMediaId";//图片mediaId
	
	//无关键字-基本设置
	public static final String NO_KEYWORDS_DEFAULT_REPLY = "noKeyWordsDefaultReply";//使用系统默认回复
	public static final String NO_REPLY_TYPE = "noReplyType";//回复类型
	public static final String NO_REPLAY_WORDS = "noReplayWords";//回复内容
	public static final String NO_IMAGE_TEXT_LIST = "noImageTextList";//选择信息
	public static final String NO_BEFOLLOWED_REPLY_IMG = "noBefollowedReplyImg";//回复图片
	public static final String NO_BEFOLLOWED_REPLY_MEDIAID = "noBefollowedReplyMediaId";//图片mediaId
		
	//自定义菜单-接收消息时回复
	public static final String USE_SYSTEM_DEFAULT_REPLY_PIC = "useSystemDefaultReplyPic";//使用系统默认回复
	public static final String REPLY_TYPE_PIC = "replyTypePic";//回复类型
	public static final String REPLAY_WORDS_PIC = "replayWordsPic";//回复内容
	public static final String IMAGE_TEXT_LIST_PIC = "imageTextListPic";//选择信息
	public static final String BEFOLLOWED_REPLY_IMG_PIC = "befollowedReplyImgPic";//回复图片
	public static final String BEFOLLOWED_REPLY_MEDIAID_PIC = "befollowedReplyMediaIdPic";//图片mediaId
	
	//消息中心设置参数
	public static final String MESSAGE_RECRUIT_SUC = "项目审核通过";
	public static final String MESSAGE_RECRUIT_FAIL = "项目审核未通过";
	public static final String MESSAGE_APPLY_SUC = "报名成功";
	public static final String MESSAGE_APPLY_GET = "工人已到达工地";
	public static final String MESSAGE_APPLY_START = "工人已开工";
	public static final String MESSAGE_APPLY_FINISH = "工人已完工";
	public static final String MESSAGE_APPLY_AGREE = "同意工人报名";
	public static final String MESSAGE_APPLY_REFUSE = "拒绝工人报名";
	public static final String MESSAGE_APPLY_NOT_EMPLOYED = "不予录用工人";
	public static final String MESSAGE_APPLY_CANCER = "工人取消报名";
	public static final String MESSAGE_REALNAME_SUC = "实名认证通过";
	public static final String MESSAGE_REALNAME_FAIL = "实名认证未通过";
	public static final String MESSAGE_TRANSFER_STATUS = "交易提醒";
	public static final String MESSAGE_WITHDRAW_SUC = "提现成功";
	public static final String MESSAGE_WITHDRAW_FAIL = "提现失败";
	public static final String MESSAGE_CREDIT_SUC = "授信成功";
	public static final String MESSAGE_CREDIT_FAIL = "授信失败";
	public static final String MESSAGE_CREDIT_RECODE_FAIL = "授信发放审核失败";
	public static final String MESSAGE_USER_REQUEST = "招工邀请";
	public static final String MESSAGE_RECRUIT_RECORD_SUC = "报名提醒";
	public static final String MESSAGE_RECRUIT_RECORD_FAIL = "报名失败";
	public static final String MESSAGE_RECRUIT_RECORD_HIRE_FAIL = "录用失败";
	public static final String MESSAGE_RECRUIT_RECORD_CONFIRM = "到达提醒";
	public static final String MESSAGE_RECRUIT_RECORD_STARTWORK = "开工提醒";
	public static final String MESSAGE_RECRUIT_RECORD_STARTPROMPT = "开工提示";
	public static final String MESSAGE_RECRUIT_RECORD_SAFETY = "安全提示";
	public static final String MESSAGE_RECRUIT_RECORD_SALARY = "工资到账";
	public static final String MESSAGE_RECRUIT_RECORD_EVALUATE = "项目已完工";
	public static final String MESSAGE_RECRUIT_RECORD_CANCEL = "报名取消";
	public static final String MESSAGE_CIRECLE_COMMENT = "您发布的圈子有新评论";
	public static final String MESSAGE_RECRUIT_NEWMESSAGE = "最新招聘信息";
	public static final String USER_NEWMESSAGE = "新用户注册信息";
	public static final String PRODUCTION_MODE = PropertiesUtils.getPropertiesByName("production_mode");
	public static final String YUNPIAN_SMS_PUSH = PropertiesUtils.getPropertiesByName("yunpian_sms_push");
	
	 public static final class Number
	{
		public static final String ONE_STRING = "1";
		public static final String TWO_STRING = "2";
		public static final String THREE_STRING = "3";
		public static final String FOUR_STRING = "4";
		public static final String FIVE_STRING = "5";
		public static final String SIX_STRING = "6";
		public static final String SEVEN_STRING = "7";
		public static final String EIGHT_STRING = "8";
		public static final String NINE_STRING = "9";
		public static final String ZERO_STRING = "0";
		public static final String TEN_STRING = "10";
		public static final String FIFTY_STRING = "50";
		public static final String ZERO_TO_ONE = "0-1";
		public static final String ONT_TO_THREE = "1-3";
		public static final String THREE_TO_FIVE = "3-5";
		public static final String FIVE_TO_TEN = "5-10";
		public static final String EIGHTEEN_TO_THIRTY = "18-30";
		public static final String THIRTY_TO_FORTY = "30-40";
		public static final String FORTY_TO_FIFTY = "40-50";
		public static final String ONE_TWO_STRING = "1,2";
		public static final long MONTH_SECOND = 2678400;//一个月秒数
		public static final long WEEK_SECOND = 604800;//一周秒数
		public static final long THIRTY_SECOND = 1800;// 验证码30分钟有效时间
		public static final long SIXTY_SECOND = 60;
		public static final int REDUCE_ONE_INT = -1;
		public static final int ONE_INT = 1;
		public static final int TWO_INT = 2;
		public static final int THREE_INT = 3;
		public static final int FOUR_INT = 4;
		public static final int FIVE_INT = 5;
		public static final int SIX_INT = 6;
		public static final int SEVEN_INT = 7;
		public static final int EIGHT_INT = 8;
		public static final int NINE_INT = 9;
		public static final int ZERO_INT = 0;
		public static final int TEN_INT = 10;
		public static final String WORKER_SINGLE_MONEY  = "20000.00";
		public static final String WORKER_SINGLE_AMOUNT  = "30000.00";
		public static final String BOSS_SINGLE_MONEY  = "100000.00";
		public static final String BOSS_SINGLE_AMOUNT  = "500000.00";
		
	}
	 public static final class Prompt
	 {
		 public static final String QUERY_SUCCESSFUL = "查询成功";
		 public static final String QUERY_FAILURE = "查询失败";
		 public static final String QUERY_NORECORD = "暂无记录";
		 public static final String ADD_SUCCESSFUL = "添加成功";
		 public static final String ADD_FAILURE = "添加失败";
		 public static final String UPDATE_SUCCESSFUL = "修改成功";
		 public static final String UPDATE_FAILURE = "修改失败";
		 public static final String DELETE_SUCCESSFUL = "删除成功";
		 public static final String DELETE_FAILURE = "删除失败";
		 public static final String SEND_SUCCESSFUL = "发送成功";
		 public static final String SEND_FAILURE = "发送失败";
		 public static final String VALIDATE_FAILURE = "验证失败";
		 public static final String UOLOAD_SUCCESSFUL = "上传成功";
		 public static final String SYSTEM_FAILURE = "系统异常";
		 public static final String PARAMETER_FAILURE = "参数不匹配";
		 public static final String PASSWORD_FAILURE = "密码不正确,请输入正确的密码!";
		 public static final String USER_NOT_PASSWORD_LOGIN = "该用户未设置密码请选择其他登陆方式";
		 public static final String USER_NOT_REGISTER_LOGIN = "该用户尚未注册,请选择其他登录方式";
		 public static final String IDENCODE_NULL = "验证码不能为空";
		 public static final String PHONE_EXISTENCE = "该手机号已存在";
		 public static final String REGISTER_SUCCESSFUL = "注册成功";
		 public static final String IDENCODE_FAILURE = "用户验证码错误,请输入正确的验证码";
		 public static final String IDENCODE_INVALID = "验证码已失效，请重新获取";
		 public static final String LOGIN_SUCCESSFUL = "登陆成功";
		 public static final String OPERATION_FREQUENTLY = "操作频繁，请稍后再试";
		 public static final String VALIDATE_SUCCESSFUL = "验证成功";
		 public static final String VALIDATE_CODE_ERROR = "验证码错误";
		 public static final String NOT_USER = "该用户不存在";
		 public static final String OLD_PASSWORD_FAILURE = "原密码错误";
		 public static final String NOT_SETTING_LOGIN_PASSWORD = "未设置登录密码密码,请先设置登录密码";
		 public static final String IMAGE_ILLEGALITY = "图片涉及到暴露、血腥、色情，请更换图片";
		 public static final String IMAGE_SIZE_NOT_EXCEED_5M = "图片大小不能超过5M";
		 public static final String IMAGE_NOT_NULL = "图片不能为空";
		 public static final String TEXT_ILLEGALITY = "文字中包含敏感词";
		 public static final String TRANSFER_TYPE_NOT_NULL = "转账类型不能为空";
		 public static final String APPLY_SUBMIT_WAIT = "申请已提交，请耐心等待";
		 public static final String NO_APPLICATION = "您还没有申请成为工头";
		 public static final String PAY_SUCCESSFUL = "支付成功";
		 public static final String NOT_CREDIT = "您暂未授信，请先前去授信";
		 public static final String CREDIT_BALANCE_INSUFFICIENT = "您的授信余额不足";
		 public static final String REALNAME_NOT_MATCHING = "您输入的姓名不匹配";
		 public static final String NOT_SETTING_PAY_PASSWORD = "未设置支付密码,请先设置支付密码";
		 public static final String PAY_PASSWORD_ERROR = "支付密码错误";
		 public static final String USER_INFO_NOT_NULL = "信息未填写完,不能找回密码";
		 public static final String NOT_VERSION_UPDATE = "暂无版本更新";
		 public static final String CONTACT_CUSTOMER_UPDATE_REALNAME = "请联系客服修改您的真实姓名";
		 public static final String CONTACT_CUSTOMER_UPDATE_SEX = "请联系客服修改您的性别";
		 public static final String CONTACT_CUSTOMER_UPDATE_AGE = "请联系客服修改您的年龄";
		 public static final String CONTACT_CUSTOMER_UPDATE_PLACE = "请联系客服修改您的籍贯";
		 public static final String CONTACT_CUSTOMER_UPDATE_CATEGORY = "请联系客服修改您的工种";
		 public static final String CONTACT_CUSTOMER_UPDATE_CONTRACT = "请联系客服修改您的承包方式";
		 public static final String NOT_BOND_CARD = "没有绑卡";
		 public static final String CASH_WITHDRAWAL_SUCCESSFUL = "提现成功";
		 public static final String GET_WECHAT_USER_INFORMATION_SUCCESSFUL = "获取微信用户信息成功";
		 public static final String GET_WECHAT_USER_INFORMATION_FAILURE = "获取微信用户信息失败";
		 public static final String WECHAT_USER__SUBSCRIBE = "该用户已关注微信公众号";
		 public static final String WECHAT_USER__UNSUBSCRIBE = "该用户未关注微信公众号";
		 public static final String QUERY_INFORMATION_SUCCESSFUL = "获取信息成功";
		 public static final String QUERY_INFORMATION_FAILURE = "获取信息失败";
		 public static final String USER_ID_NULL = "用户ID不能为空";
		 public static final String AMOUNT_ERROR = "金额有误，请重新输入";
		 public static final String MAX_AMOUNT_ERROR = "金额不能大于50万";
		 public static final String PAY_STATUS_NULL = "支付方式不能为空";
		 public static final String NUMBER_EXISTENCE = "该卡号已存在";
		 public static final String AUTHENTICATION_FAILURE = "实名认证失败";
		 public static final String BINDING_TOOMANYTIMES = "今日绑定银行卡次数过多，请明日在绑定";
		 public static final String INFORMATION_MISMATCH = "银行卡信息不匹配";
		 public static final String NOTFOUND_REPLACE_BANKCARD = "验证无该银行数据，请更换银行卡";
		 public static final String UNIDENTIFIED_REPLACE_BANKCARD = "无法识别此银行卡，请更换银行卡";
		 public static final String COMPLETE_INFORMATION = "请填写完整的信息";
		 public static final String WONTSUPPORT_BANK = "目前不支持该银行，请更换其他银行卡";
		 public static final String TSUPPORT_SAVINGSBANK = "目前只支持储蓄卡，请更换其他银行卡";
		 public static final String INPUTCORRECTLY_BANKNUMBER = "请输入正确的银行卡号";
		 public static final String NAME_ISNOTNULL = "系统错误,真实姓名不能为空";
		 public static final String IDNUMBER_ISNOTNULL = "系统错误,身份证号不能为空";
		 public static final String IDPOSITIVE_ISNOTNULL = "系统错误,身份证正面不能为空";
		 public static final String IDOTHERSIDE_ISNOTNULL = "系统错误,身份证反面不能为空";
		 public static final String HOLDID_ISNOTNULL = "系统错误,手持身份证不能为空";
		 public static final String ALREADY_AUTHENTICATION = "该用户已经实名认证";
		 public static final String ALREADY_AUTHENTICATION_REPLACE = "身份证已实名认证过,请更换身份证";
		 public static final String NAME_FORMATMISTAKEN = "姓名格式有误，请重新填写";
		 public static final String ID_FORMATMISTAKEN = "身份证填写有误，请重新填写";
		 public static final String AUTHENTICATION_TOOMANYTIMES = "今日实名认证次数过多，请明日在绑定";
		 public static final String IDCORE_MAINTAIN = "身份证中心维护，请稍后重试";
		 public static final String NOTHING_IDNUMBER = "无此身份证号码";
		 public static final String INFORMATION_FILLINGINERROR = "信息填写有误，请检查后再提交";
		 public static final String RECRUITPEOPLE_ISFULL = "此招聘人数已满，换一个吧";
		 public static final String HAVEINHANDPROJECT_CANNOTPARTICIPATE = "您目前有在进行中的项目，无法参与其他项目";
		 public static final String ALREADYSTART_CANNOTCANCEL = "已经开工，取消报名失败";
		 public static final String PEOPLENUMBER_TOOMUCH = "工人报名数过多,报名失败";
		 public static final String ONTHEJOB_FAILURETOSIGNUP = "工人已经在职，报名失败";
		 public static final String ONTHEJOB_FAIL = "报名失败";
		 public static final String WORKERHAVEINHANDPROJECT_CANNOTPARTICIPATE = "此工人有在进行中的项目，无法参与其他项目";
		 public static final String WORKER_UNCONFIRMED = "工人未确认";
		 public static final String USER_NOTCREDIT = "该用户未授信";
		 public static final String USER_ALREADYINVITATION = "此用户已被邀请过";
		 public static final String AUTHENTICATION_PLEASEWAIT= "实名认证中,请耐心等待";
		 public static final String AUTHENTICATIONFAILURE_PLEASEAGAIN = "实名认证失败,请重新进行实名认证";
		 public static final String UNCERTIFIED_CANNOTPUBLISH = "未进行实名认证,不能发布招聘";
		 public static final String PHONE_NULL = "手机号码不能为空";
		 
		 public static final String TRANSFER_NUM = "单日转账次数不得超过";
		 public static final String TRANSFER_BOUND_AMOUNT = "单日转账额度不得超过";
		 public static final String SINGLE_TRANSFER_AMOUNT = "单笔转账金额不得超过";
		 /**
		  * 工人
		  */
		 public static final String WORKER_NUM="3次";//次数
		 public static final String WORKER_SINGLE_AMOUNT="2万";//单笔金额
		 public static final String WORKER_ONE_DAY_AMOUNT="3万";//单日金额
		 /**
		  * 工头
		  */
		 public static final String GANGER_NUM="5次";//次数
		 public static final String GANGER_SINGLE_AMOUNT="10万";//单笔金额
		 public static final String GANGER_ONE_DAY_AMOUNT="50万"; //单日金额
		 
		 public static final String WITHDRAW_NUM = "单日提现次数不得超过";
		 public static final String WITHDRAW_BOUND_AMOUNT = "单日提现额度不得超过";
		 public static final String SINGLE_WITHDRAW_AMOUNT = "单笔提现金额不得超过";
	 }
}
