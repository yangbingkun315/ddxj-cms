package net.zn.ddxj.utils.alipay;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;


public class AlipayUtils {

    public static AlipayClient getAlipayClient(){
        return new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, 
                AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, 
                AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE); //获得初始化的AlipayClient
    }

    /**
     * 验证签名 
     * @param request
     * @return
     */
    @SuppressWarnings("all")
    public static boolean rsaCheckV1(HttpServletRequest request){
        boolean verify_result = false;
        try {
            Map<String,String> params = new HashMap<String,String>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);	
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
            }
            verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verify_result;
    }

    /**
     * 支付接口
     * @param out_trade_no 商户订单号
     * @param total_amount 金额
     * @param body 内容
     * @param subject 主题
     * @param request
     * @return
     * @throws Exception 
     */
    public static String aliPrePay(Map<String,Object> data) throws Exception{
        String orderString = null;
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, 
        		AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest payRequest = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//        //描述信息  添加附加数据  
//        model.setPassbackParams(URLEncoder.encode((String)data.get("body").toString()));
        //商品信息
        model.setBody(data.get("body").toString());
        //商品名称
        model.setSubject(data.get("subject").toString());
        //商户订单号
        model.setOutTradeNo(data.get("out_trade_no").toString());
        //交易超时时间
        model.setTimeoutExpress("30m");
        //交易金额
        model.setTotalAmount(data.get("total_amount").toString());
        //禁用支付方式
        model.setDisablePayChannels(data.get("disable_pay_channels").toString());
        //销售产品码
        model.setProductCode(AlipayConfig.PRODUCT_CODE);
        payRequest.setBizModel(model);
        payRequest.setNotifyUrl(AlipayConfig.NOTIFY_URL);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
        	AlipayTradeAppPayResponse payResponse = alipayClient.sdkExecute(payRequest);
            //就是orderString 可以直接给客户端请求，无需再做处理。
            orderString = payResponse.getBody();
            
            //orderString += "&";
            
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return orderString;
    }
    
    /**
     * 退款
     * @param out_trade_no 商户订单号
     * @param trade_no 支付宝订单流水号
     * @param total_amount 金额
     * @param refund_reason 退款原因
     * @return
     */
    public static boolean refund(String out_trade_no,String trade_no,double total_amount,String refund_reason){
        boolean flag = false;
        String str = null;
        try {
            AlipayClient alipayClient = getAlipayClient();
            AlipayTradeRefundRequest apliRequest = new AlipayTradeRefundRequest();
            apliRequest.setBizContent("{" +
            "\"out_trade_no\":\""+out_trade_no+"\"," +
            "\"trade_no\":\""+trade_no+"\"," +
            "\"refund_amount\":"+total_amount+"," +
            "\"refund_reason\":\""+refund_reason+"\"," +
            "\"out_request_no\":\"\"," +
            "\"operator_id\":\"\"," +
            "\"store_id\":\"\"," +
            "\"terminal_id\":\"\"" +
            "  }");
            AlipayTradeRefundResponse aliResponse = alipayClient.execute(apliRequest);//通过alipayClient调用API，获得对应的response类
            if(aliResponse.isSuccess()){
                //调用成功
                flag = true;
            } else {
                //调用失败
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
