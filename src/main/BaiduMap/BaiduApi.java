package main.BaiduMap;

import main.Filebases;
import main.Myconfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by benywon on 2015/8/3.
 */
public class BaiduApi {
    public Set<String> positionname=new HashSet<>();

    /**
     * 得到百度地图所有娱乐场所名称的api
     */
  public void getallname()
  {
      this.positionname= Filebases.GetSetFromFile(Myconfig.Getconfiginfo("allSpotNameFile"));
      BaiduMapRequest baiduMapRequest=new BaiduMapRequest();
      baiduMapRequest.setTag("");
      baiduMapRequest.setQuery("旅游景点");
      baiduMapRequest.setRegion("北京");
      baiduMapRequest.setMax_num(5000);
      baiduMapRequest.setScope(1);
      baiduMapRequest.setSort_nameBydefault();
      baiduMapRequest.setsortdescent();
      String content=baiduMapRequest.dorequest();
      org.jsoup.nodes.Document doc = Jsoup.parse(content);//
      Elements names=doc.getElementsByTag("name");
      for(Element name:names)
      {
          String txt=name.text();
          if(txt.length()>1)
          {
              if(txt.contains("(")||txt.contains("（"))
              {
                  String[] cc=txt.split("\\(|（");
                  this.positionname.add(cc[0]);
              }
              else {
                  this.positionname.add(txt);
              }
          }
      }
      Filebases.WriteList2File(this.positionname, Myconfig.Getconfiginfo("allSpotNameFile"));
      System.out.println("后is都会");
  }
    public void getalltag()
    {
//        this.positionname= Filebases.GetSetFromFile(Myconfig.Getconfiginfo("BaiduallSpotTagFile"));
        BaiduMapRequest baiduMapRequest=new BaiduMapRequest();
        baiduMapRequest.setTag("");
        baiduMapRequest.setQuery("美术馆");
        baiduMapRequest.setRegion("北京");
        baiduMapRequest.setMax_num(5000);
        baiduMapRequest.setScope(2);
        baiduMapRequest.setSort_nameBydefault();
        baiduMapRequest.setsortdescent();
        String content=baiduMapRequest.dorequest();
        org.jsoup.nodes.Document doc = Jsoup.parse(content);//
        Elements names=doc.getElementsByTag("tag");
        for(Element name:names)
        {
            String txt=name.text();
            if(!txt.equals(""))
            {
                this.positionname.add(name.text());
            }
        }
        Filebases.WriteList2File(this.positionname, Myconfig.Getconfiginfo("BaiduallSpotTagFile"));
        System.out.println("后is都会");
    }
    public static void main(String[] args) {
        BaiduApi baiduApi=new BaiduApi();
        baiduApi.getallname();
    }
}
