package main.DealQuestion;

import main.Crawlerbases;

/**
 * Created by benywon on 2015/7/19.
 */
public class BaiduMap {
    public static final String Baiduak="CGL2GfpsGcD4bpHT6vU8slf4";
    public static String apiurl="http://api.map.baidu.com/geocoder/v2/?ak=CGL2GfpsGcD4bpHT6vU8slf4&callback=renderReverse&location=纬度,经度&output=xml&pois=0";
    public static void main(String[] args) throws Exception {
        String url=apiurl.replaceAll("经度","116.403963").replaceAll("纬度","39.915119");
        String content =Crawlerbases.spiderhtml(url,"utf-8");
        System.out.println(content);
    }
}
