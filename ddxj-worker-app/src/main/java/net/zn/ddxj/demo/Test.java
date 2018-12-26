package net.zn.ddxj.demo;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.pabank.sdk.PABankSDK;

public class Test {

	public static void main(String[] args) throws IOException {
		
		String filepath="conf/config.properties";//注意filepath的内容；
		File file = new File(filepath);
		System.out.println(file.getName());
		
		// 初始化配置
		PABankSDK.init("D:/javaProject/cms/ddxj-worker-app/src/main/java/net/zn/ddxj/conf/config.properties");
		// a.验证开发者
		PABankSDK.getInstance().approveDev();
		// JSON请求报文，系统流水号（CnsmrSeqNo）必输，规范：6位uid(文件传输用户短号)+6位系统日期(YYMMDD)+10位随机数。
		String req = "{\"BussTypeNo\":\"100160\",\"CnsmrSeqNo\":\"H222851807310123456789\",\"CorpAgreementNo\":\"Q000400269\",\"RequestSeqNo\":\"H222851807310123456789\",\"StartDate\":\"20180517\",\"EndDate\":\"20180517\",\"TranStatus\":\"0\"}";
		// b.交易测试，传入参数服务ID，JSON报文流水号字段需传入22位
		Map<String, Object> returnMap=PABankSDK.getInstance().apiInter(req,"TranInfoQuery");		
		// b.交易测试，提供流水号拼接方法，JSON报文流水号字段只需传入10位随机数
		//Map<String, Object> returnMap=PABankSDK.getInstance().newApiInter(req,"TranInfoQuery");
		System.err.println(returnMap);
	}
}
