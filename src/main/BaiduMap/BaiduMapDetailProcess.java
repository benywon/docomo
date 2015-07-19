package main.BaiduMap;

import main.Crawlerbases;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benywon on 2015/7/19.
 */
public class BaiduMapDetailProcess {

    public List<String> uids=new ArrayList<>();
    public String uid;
    public float lat;
    public float lng;
    public float radius=10000;
    public String location;

    public void setLocation(float lat,float lng)
    {
        this.location+=lat+","+lng;
    }
    public List<BaiduMapDetail> detailList=new ArrayList<>();

    public String getdetail()
    {
        String content="";
        String url=BaiduMap.detailapiurl;
        String uidstr="uids=";
        for(String uid:this.uids)
        {
            uidstr+=uid+",";
        }
        url+=uidstr.replaceAll(",$","");
        url+="&";
        url+="ak="+BaiduMap.Baiduak+"&";
        url+="location="+this.location+"&";
        url+="radius="+this.radius+"&";
        url+="scope=2";
        try {
            content= Crawlerbases.spiderhtml(url, "utf-8");
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
    public void dealwiththiscontent(String content)
    {
        org.jsoup.nodes.Document doc = Jsoup.parse(content);//
        Elements elements=doc.getElementsByTag("result");
        for(Element element:elements)
        {
            BaiduMapDetail baiduMapDetail=new BaiduMapDetail();
            baiduMapDetail.dealelement(element);
            this.detailList.add(baiduMapDetail);
        }
    }

}
