package main.DZDPquery;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * Created by benywon on 2015/7/17.
 */
public class ResultDeal {
    public boolean IsRequestOk=false;
    public String ErrorMessage="";
    public DianPingResult dianPingResult=new DianPingResult();
    public static org.jsoup.nodes.Document XMLDocument;


    /**
     * 判断这个返回的结果是不是有效
     * @param xmlcontent
     * @return
     */
    public boolean IsResultOk(String xmlcontent)
    {
        ResultDeal.XMLDocument = Jsoup.parse(xmlcontent);//
        Element statustag=XMLDocument.getElementsByTag("status").first();
        if(statustag==null)
        {
            this.IsRequestOk=false;
            return false;
        }
        else
        {
            String status = statustag.text();
            this.dianPingResult.status=status;
            if(status.equals("OK"))
            {
                this.IsRequestOk=true;
                return  true;
            }
            else
            {
                this.IsRequestOk=false;
                Element errorMessageTag=XMLDocument.getElementsByTag("errorMessageTag").first();
                if(errorMessageTag!=null)
                {
                    this.ErrorMessage=errorMessageTag.text();
                }
                return false;
            }
        }
    }
    public static int getcount(String xmlcontent)
    {
        ResultDeal.XMLDocument = Jsoup.parse(xmlcontent);//
        Element number=XMLDocument.getElementsByTag("total_count").first();
        if(number!=null)
        {
            String numstr=number.text();
            return Integer.parseInt(numstr);
        }
        return -1;
    }
}
/**
 * 请求超时，请稍后再试
 10001=Request time out. (请求超时)

 无App Key参数
 10002=Appkey is missing. (无appkey参数)

 App Key参数值无效
 10003=Invalid appkey. (appkey参数值无效)

 无Sign参数
 10004=Sign is missing. (无sign签名参数)

 Sign参数值无效
 10005=Invalid sign. (sign签名参数值无效)

 无当前API访问权限
 10006=This API is not accessible. (无当前API访问权限)

 当日API访问量已达到上限
 10007=You have reached the daily limit. (当日API访问量已达到上限)

 App Key不可用（黑名单）
 10008=Appkey is unavailable. (appkey不可用)

 API地址不存在
 10009=API does not exist. (API地址不存在)

 缺少必选请求参数
 10010=Required parameter is missing. (缺少必选请求参数)

 请求参数值无效
 10011=Parameter value is invalid. (请求参数值无效)

 请求参数无效
 10012=Parameter is invalid. (请求参数无效)

 请求参数组合无效
 10013=Parameters set is invalid. (请求参数组合无效)

 请求IP无效
 10014=IP is invalid. (请求IP无效)

 请求方法错误
 10015=Error method. (请求方法错误)

 禁止访问
 10016=Access forbidden. (禁止访问)

 访问过于频繁
 10017=You have exceeded the allowed frequency. (访问过于频繁)

 无效请求
 10018=Invalid Request.(无效请求)

 未知异常
 100=API service is temporarily unavailable. (API服务器繁忙)

 服务调用超时
 101=API service timeout. (API服务调用超时)

 HTTP Header 错误
 10019=Invalid request-header: {0}. (HTTP Header {0} 无效)

 无效请求(包含非UTF-8编码)
 10020=Request contains invalid UTF-8 characters. (HTTP请求包含非UTF-8编码字符: {0})

 请求参数值的个数超过上限
 10021=Parameter contains more than {1} items: {0}. (请求参数值数量超过{1}上限: {0})

 请求参数值的个数不足
 10022=Parameter contains less than {1} items: {0}. (请求参数值数量不足{1}: {0})

 请求参数值格式错误
 10023=Parameter value format invalid: {0} (请求参数值格式错误: {0})
 */
