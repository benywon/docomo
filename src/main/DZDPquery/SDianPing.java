package main.DZDPquery;

import main.Crawlerbases;
import main.Filebases;
import main.Myconfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by benywon on 2015/7/17.
 */
public class SDianPing {
    private final static String STR_APPKEY = "574378838";
    private final static String STR_SECRET = "398bf6f8fa45446ab3b819d591a847be";
    public static final String apiUrl = "http://api.dianping.com/v1/business/find_businesses";


    Map<String, String> paramMap = new HashMap<String, String>();
    public String getresult(String question)
    {
        paramMap.put("keyword", question);
        paramMap.put("city", "北京");
//        paramMap.put("latitude","39.915119");
//        paramMap.put("longitude","116.403963");
//        paramMap.put("radius","5000");
        paramMap.put("format", "xml");
        paramMap.put("limit", "40");
        String str_content="";
        try {
            str_content = get_dianping_data(paramMap,apiUrl);
            System.out.println(str_content);
            String xmlfilepath=Myconfig.Getconfiginfo("tempxmlfile");
            Filebases.Write2File(str_content,xmlfilepath,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str_content;
    }
    public static String get_dianping_data(Map<String, String> paramMap,String URL)  {


        StringBuilder stringBuilder = new StringBuilder();

        // 对参数名进行字典排序
        String[] keyArray = paramMap.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);
        // 拼接有序的参数名-值串
        stringBuilder.append(STR_APPKEY);
        for (String key : keyArray)
        {
            stringBuilder.append(key).append(paramMap.get(key));
        }
        String codes = stringBuilder.append(STR_SECRET).toString();
        String sign = org.apache.commons.codec.digest.DigestUtils.shaHex(codes).toUpperCase();

        // 添加签名
        stringBuilder = new StringBuilder();
        stringBuilder.append("appkey=").append(STR_APPKEY).append("&sign=").append(sign);
        try {
            for (Map.Entry<String, String> entry : paramMap.entrySet())
            {
                stringBuilder.append('&').append(entry.getKey()).append('=').append( URLEncoder.encode(entry.getValue(), "UTF-8") );
            }

        } catch (UnsupportedEncodingException e) {}

        String queryString = stringBuilder.toString();
        // System.out.println(queryString);

        String str_url = URL + "?" + queryString;

//        String str_content = CHttpDownloader.getPageContent_UTF8(str_url);
        String str_content= null;
        try {
            str_content = Crawlerbases.spiderhtml(str_url, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str_content;

    }

    public static void main(String[] args) {
        SDianPing sDianPing=new SDianPing();
        String tt=sDianPing.getresult("南锣鼓巷");
        DianPingResult dd=new DianPingResult();
        dd.dealxml(tt);
        System.out.println("result");

    }
}
