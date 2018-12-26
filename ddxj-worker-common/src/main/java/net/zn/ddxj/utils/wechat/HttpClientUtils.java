package net.zn.ddxj.utils.wechat;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Map;

/**
 * Created by zhouquan on 2016/3/29.
 */
public class HttpClientUtils {


    public static String sendGet(String url, Map<String, String> param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString;
            if (param == null) {
                urlNameString = url;
            } else {
                urlNameString = url + "?";
                for (String key : param.keySet()) {
                    urlNameString += key + "=" + param.get(key) + "&";
                }
                urlNameString = urlNameString.substring(0, urlNameString.length() - 1);
            }

            URL realUrl = new URL(urlNameString);
            // �򿪺�URL֮�������
            URLConnection connection = realUrl.openConnection();
            // ����ͨ�õ���������
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����ʵ�ʵ�����
            connection.connect();
            // ��ȡ������Ӧͷ�ֶ�
//            Map<String, List<String>> map = connection.getHeaderFields();
            // �������е���Ӧͷ�ֶ�
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // ���� BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("����GET��������쳣��" + e);
            e.printStackTrace();
        }
        // ʹ��finally�����ر�������
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ��ָ�� URL ����POST����������
     *
     * @param url   ��������� URL
     * @param param ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
     * @return ������Զ����Դ����Ӧ���
     */
    public static String sendPost(String url, Map<String, String> param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // �򿪺�URL֮�������
            URLConnection conn = realUrl.openConnection();
            // ����ͨ�õ���������
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����POST�������������������
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ��ȡURLConnection�����Ӧ�������
            out = new PrintWriter(conn.getOutputStream());
            // �����������
            String urlNameString = "";

            for (String key : param.keySet()) {
                urlNameString += key + "=" + param.get(key) + "&";
            }
            urlNameString = urlNameString.substring(0, urlNameString.length() - 1);
            out.print(urlNameString);
            // flush������Ļ���
            out.flush();
            // ����BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("���� POST ��������쳣��" + e);
            e.printStackTrace();
        }
        //ʹ��finally�����ر��������������
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String sendXmlPost(String urlStr, String request) {
        String result = "";
        try {
            URL realUrl = new URL(urlStr);
            // �򿪺�URL֮�������
            URLConnection conn = realUrl.openConnection();
            // ����ͨ�õ���������
            conn.setRequestProperty("Pragma:", "no-cache");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "text/xml");
            // ����POST�������������������
            conn.setDoOutput(true);
            // ��ȡURLConnection�����Ӧ�������
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            out.write(request);
            // flush������Ļ���
            out.flush();
            out.close();
            // ����BufferedReader����������ȡURL����Ӧ
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getLocalImg(String urlStr, Map<String, String> param, String topath) {
        InputStream in = null;
        FileOutputStream outputStream = null;
        String newname = null;
        try {
            String urlNameString;
            if (param == null) {
                urlNameString = urlStr;
            } else {
                urlNameString = urlStr + "?";
                for (String key : param.keySet()) {
                    urlNameString += key + "=" + param.get(key) + "&";
                }
                urlNameString = urlNameString.substring(0, urlNameString.length() - 1);
            }

            URL realUrl = new URL(urlNameString);
            // �򿪺�URL֮�������
            URLConnection connection = realUrl.openConnection();
            // ����ͨ�õ���������
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����ʵ�ʵ�����
            connection.connect();
            // ��ȡ������Ӧͷ�ֶ�
//            Map<String, List<String>> map = connection.getHeaderFields();
            // �������е���Ӧͷ�ֶ�
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // ���� BufferedReader����������ȡURL����Ӧ
            in = connection.getInputStream();
            newname = topath + new Date().getTime() + ".jpg";
            File f = new File(topath);
            if(!f.exists()){
                f.mkdirs();
            }
            outputStream = new FileOutputStream(new File(newname));
            int len = 0;
            byte[] buffer = new byte[1024 * 1024];
            //ʹ��һ����������buffer������ݶ�ȡ����
            while ((len = in.read(buffer)) != -1) {
                //���������buffer��д�����ݣ��м����������ĸ�λ�ÿ�ʼ����len�����ȡ�ĳ���
                outputStream.write(buffer, 0, len);
            }

        } catch (Exception e) {
            System.out.println("����GET��������쳣��" + e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return newname;
    }


//    public static void main(String[] args) {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("access_token","qkcDH214femJ_48SCS-a-r3hchL6hfGDkE1fnHup3dHzHwl-uVBr2INCNazn9h0GeiqaEDJ-J2xkrQVd_cCo6CK18-eaIfXKdsgVvFfAVKzULtuvW_vc6YmF5MI-3mK3HXLjAJARHV");
//        params.put("type", "jsapi");
////        params.put("grant_type", "client_credential");
////        params.put("appid", Config.instance().getAppid());
////        params.put("secret", Config.instance().getAppSecret());
////        String resxml = sendGet("https://api.weixin.qq.com/cgi-bin/token", params);
//        String resxml = sendGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket", params);
//        System.out.println(resxml);
//
//
////        String s = sendXmlPost("http://localhost:8080/NB_Ocean/weixinjscallbakc", "<xml><return_code><![CDATA[��ë]]></return_code><return_msg><![CDATA[pay error]]></return_msg></xml>");
////        System.out.println(s);
//    }
}
