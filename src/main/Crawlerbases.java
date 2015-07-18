package main;

import java.io.*;
import java.net.*;


/**
 * Created by benywon on 2015/3/25.
 * 这是爬取网页的一些公共程序方法
 */
public class Crawlerbases {

    /**
     * 一个输入流转换为str的方法
     * @param in_st 输入的流
     * @param charset 编码的格式
     * @return 要返回的字符串
     * @throws IOException
     */
    public static String InputStream2String(InputStream in_st,String charset) throws IOException{
        BufferedReader buff = new BufferedReader(new InputStreamReader(in_st, charset));
        StringBuffer res = new StringBuffer();
        String line = "";
        while((line = buff.readLine()) != null){
            res.append(line);
        }

        return res.toString();
    }

    /**
     * 保存网页的程序  filepath是路径 str是输入的字符串
     * @param filepath
     * @param str
     */

    public static void saveHtml(String filepath, String str){

        try {
            OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filepath), "utf-8");
            outs.write(str);
//            System.out.print(str);
            outs.close();
        } catch (IOException e) {
            System.out.println("Error at save html...");
            e.printStackTrace();
        }
    }
    /**
     * 保存网页的程序  filepath是路径 str是输入的字符串
     * @param filepath
     * @param str
     */

    public static void saveHtml(String filepath, String str,String chara){

        try {
            OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filepath), chara);
            outs.write(str);
//            System.out.print(str);
            outs.close();
        } catch (IOException e) {
            System.out.println("Error at save html...");
            e.printStackTrace();
        }
    }
    /**
     * 我们爬虫最核心的 程序
     * @param url_str 要爬取的地址
     * @param character 要爬取网页的编码集
     * @return爬到的数据
     */
    public static String  spiderhtml(String url_str,String character) throws Exception
    {
        URL url = null;
        try {
            url = new URL(url_str);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String charset = character;

         CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));//为了防止cookie重定向
            URLConnection url_con = url.openConnection();
            url_con=SetConnecProp(url_con);
            InputStream htm_in = url_con.getInputStream();

            String htm_str = InputStream2String(htm_in,charset);
            return htm_str;

    }
    public static String  spiderhtml_Job592(String url_str,String character) throws Exception
    {
        URL url = null;

        url = new URL(url_str);


        String charset = character;

        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));//为了防止cookie重定向
        URLConnection url_con = url.openConnection();
        url_con=SetConnecProp_Job592(url_con);
        InputStream htm_in = url_con.getInputStream();

        String htm_str = InputStream2String(htm_in,charset);
        return htm_str;

    }

    public static String  spiderhtml_DaJie(String url_str,String character) throws Exception
    {
        URL url = null;
        try {
            url = new URL(url_str);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String charset = character;

        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));//为了防止cookie重定向
        URLConnection url_con = url.openConnection();
        url_con=SetConnecProp_DaJie(url_con);
        InputStream htm_in = url_con.getInputStream();

        String htm_str = InputStream2String(htm_in,charset);
        return htm_str;

    }

    public static URLConnection SetConnecProp_DaJie(URLConnection url_con)
    {
        int sec_cont = 4;//最多超时两秒
        url_con.setDoOutput(true);
        url_con.setReadTimeout(1000 * sec_cont);
        int random1=(int)(1+Math.random()*(566));
        int random2=(int)(1+Math.random()*(9));
        url_con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/"+random1+".36 (KHTML, like Gecko) Chrome/25.1.1453.110 Safari/537.36 CoolNovo/2.0."+random2+".20");
        url_con.setRequestProperty("Connection", "keep-alive");
        url_con.setRequestProperty("Cookie", "DJ_UVID=MTQyNzYyMDE2MDg1ODA3NjY5; login_email=1138274167%40qq.com; dj_auth_v3=MWbZEn7eJNxDqj7TSxi9O2FYE793XH60gsKgCBzbzDwUsxq0m-RI37P22NNyFnY*; uchome_loginuser=32782678; DJ_RF=empty; DJ_EU=http%3A%2F%2Fwww.dajie.com%2Fcorp%2Fsearch%3Fpage%3D1; app_guide=\"\"; Hm_lvt_38e2415f86d73cdfa1e9c3f4d8560b8e=1431764576,1432015147,1433513162; Hm_lpvt_38e2415f86d73cdfa1e9c3f4d8560b8e=1433513573; USER_ACTION=request^AProfessional^AAUTO^A-^A-; login_email=1138274167%40qq.com");
        url_con.setRequestProperty("Referer", "http://so.dajie.com/job/search?keyword=&f=nav");
        url_con.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
        url_con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        return url_con;
    }
    public static URLConnection SetConnecProp_Job592(URLConnection url_con)
    {
        int sec_cont = 3;
        url_con.setDoOutput(true);
        url_con.setReadTimeout(1000 * sec_cont);
        int random1=(int)(1+Math.random()*(566));
        int random2=(int)(1+Math.random()*(9));
        url_con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/"+random1+".36 (KHTML, like Gecko) Chrome/25.1.1453.110 Safari/537.36 CoolNovo/2.0."+random2+".20");
        url_con.setRequestProperty("Connection", "keep-alive");
        url_con.setRequestProperty("Cookie", "bdshare_firstime=1431774188782; rnapl=111079e8fc2ddf712f174fc4e997fc25; pgv_pvi=8049806336; pgv_si=s2288675840; AJSTAT_ok_pages=6; AJSTAT_ok_times=3");
        url_con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        return url_con;
    }

    public static URLConnection SetConnecProp(URLConnection url_con)
    {
        int sec_cont = 2;
        url_con.setDoOutput(true);
        url_con.setReadTimeout(1000 * sec_cont);
        int random1=(int)(1+Math.random()*(566));
        int random2=(int)(1+Math.random()*(9));
        url_con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/"+random1+".36 (KHTML, like Gecko) Chrome/25.1.1453.110 Safari/537.36 CoolNovo/2.0."+random2+".20");
        url_con.setRequestProperty("Connection", "keep-alive");
        url_con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        return url_con;
    }

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        String url_str = "http://search.51job.com/job/68420404,c.html";
        String charset = "gb2312";
        String filepath = "d:/125.html";
        String out=spiderhtml(url_str,charset);
        System.out.println(out);
    }
//    public static void


}
