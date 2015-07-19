package main.BaiduMap;

import main.Crawlerbases;

/**
 * Created by benywon on 2015/7/19.
 */
public class BaiduMap {
    public static final String Baiduak="CGL2GfpsGcD4bpHT6vU8slf4";
    public static String apiurl="http://api.map.baidu.com/geocoder/v2/?ak=CGL2GfpsGcD4bpHT6vU8slf4&callback=renderReverse&location=纬度,经度&output=xml&pois=0";
    public static final String suggestapi="http://api.map.baidu.com/place/v2/suggestion?query=天安门&region=131&output=xml&ak=CGL2GfpsGcD4bpHT6vU8slf4";
//    public static final String positionapi="http://api.map.baidu.com/place/v2/search?q=问句&location=纬度,经度&output=xml&ak=CGL2GfpsGcD4bpHT6vU8slf4&scope=2&radius=10000";
    public static final String positionapi="http://api.map.baidu.com/place/v2/search?";
    public static final String detailapiurl="http://api.map.baidu.com/place/v2/detail?";
    public static void main(String[] args) throws Exception {
//        String url=apiurl.replaceAll("经度","116.403963").replaceAll("纬度","39.915119");
//        String url=suggestapi.replaceAll("天安门", "望京");
          BaiduMap baiduMap=new BaiduMap();
          baiduMap.getPosition("公园");
    }
    public String getPosition(String query) throws Exception {
        String url=positionapi.replaceAll("问句",query).replaceAll("经度","116.403963").replaceAll("纬度", "39.915119");
        String content =Crawlerbases.spiderhtml(url, "utf-8");
        System.out.println(content);
        return content;
    }
}
