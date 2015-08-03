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
  public void getallname()
  {
      this.positionname= Filebases.GetSetFromFile(Myconfig.Getconfiginfo("allSpotNameFile"));
      BaiduMapRequest baiduMapRequest=new BaiduMapRequest();
      baiduMapRequest.setTag("");
      baiduMapRequest.setQuery("游乐场");
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
          if(!txt.equals(""))
          {
              this.positionname.add(name.text());
          }
      }
      Filebases.WriteList2File(this.positionname, Myconfig.Getconfiginfo("allSpotNameFile"));
      System.out.println("后is都会");
  }

    public static void main(String[] args) {
        BaiduApi baiduApi=new BaiduApi();
        baiduApi.getallname();
    }
}
