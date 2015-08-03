package main.BaiduMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by benywon on 2015/7/19.
 */
public class BaiduMapResult {

    public Map<String,Integer> distanceMap=new HashMap<>();
    public BaiduMapDetailProcess getresult(String content)
    {
        BaiduMapDetailProcess baiduMapDetailProcess=new BaiduMapDetailProcess();
        org.jsoup.nodes.Document doc = Jsoup.parse(content);//

        Element totalcountele=doc.getElementsByTag("total").first();
        if(totalcountele==null)
        {
            return baiduMapDetailProcess;
        }
        baiduMapDetailProcess.totalcount=Integer.parseInt(totalcountele.text());
        if(baiduMapDetailProcess.totalcount<1)
        {
            return baiduMapDetailProcess;
        }
        Elements elements=doc.getElementsByTag("result");
        for(Element element:elements)
        {
            String txt=element.getElementsByTag("uid").first().text();
            baiduMapDetailProcess.uids.add(txt);
            Element numtxt=element.getElementsByTag("distance").first();
            if(numtxt!=null)
            {
                int number = Integer.parseInt(numtxt.text());
                this.distanceMap.put(txt, number);
            }
            if(baiduMapDetailProcess.uids.size()==10)
            {
                String detail=baiduMapDetailProcess.getdetail();
                baiduMapDetailProcess.dealwiththiscontent(detail);
                baiduMapDetailProcess=_addDistance(baiduMapDetailProcess);
                baiduMapDetailProcess.uids.clear();
            }
        }
        String detail=baiduMapDetailProcess.getdetail();
        baiduMapDetailProcess.dealwiththiscontent(detail);
        baiduMapDetailProcess=_addDistance(baiduMapDetailProcess);
        return baiduMapDetailProcess;
    }
    public BaiduMapDetailProcess getresultonly1(String content)
    {
        BaiduMapDetailProcess baiduMapDetailProcess=new BaiduMapDetailProcess();
        org.jsoup.nodes.Document doc = Jsoup.parse(content);//

        Element totalcountele=doc.getElementsByTag("total").first();
        if(totalcountele==null)
        {
            return baiduMapDetailProcess;
        }
        baiduMapDetailProcess.totalcount=Integer.parseInt(totalcountele.text());
        if(baiduMapDetailProcess.totalcount<1)
        {
            return baiduMapDetailProcess;
        }
        Element element=doc.getElementsByTag("result").first();
        String txt=element.getElementsByTag("uid").first().text();
        baiduMapDetailProcess.uids.add(txt);
        String detail=baiduMapDetailProcess.getdetail();
        baiduMapDetailProcess.dealwiththiscontent(detail);
        return baiduMapDetailProcess;
    }
    private BaiduMapDetailProcess _addDistance(BaiduMapDetailProcess baiduMapDetailProcess)
    {
        for(BaiduMapDetail baiduMapDetail:baiduMapDetailProcess.detailList)
        {
            baiduMapDetail.distance=this.distanceMap.get(baiduMapDetail.uid);
        }
        return baiduMapDetailProcess;
    }

}
