package main.DZDPquery;

import main.Filebases;
import main.Myconfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by benywon on 2015/8/3.
 */
public class DianPingXY implements Serializable {
    public class Shop implements Serializable{
        public basicInfo basicinfo=new basicInfo();
        public moreInfo moreinfo=new moreInfo();
        public class basicInfo implements Serializable{
            public  int id;
            public String url;
            public  String name;
            public  List<String> categories=new ArrayList<>();
            public  String star_c;
            public  Map<String,String > aspect=new HashMap<>();
            public  String addressRegion;
            public  String addressStreet;
            public  String phone;
        }
        public class moreInfo implements Serializable{
            public Map<String,String > aspect=new HashMap<>();
            public List<String> tags=new ArrayList<>();
        }
        class reviews implements Serializable
        {

        }
    }
    public List<Shop> shops=new ArrayList<>();
    public DianPingXY()
    {
//        getdianpingfromxmlfile();
        this.shops= (List<Shop>) Filebases.ReadObj(Myconfig.Getconfiginfo("dianpingXYobjfilepath"));

    }

    /**
     * 从xml文档里面读取
     */
    public void getdianpingfromxmlfile()
    {
        String xml= Filebases.readfile(Myconfig.Getconfiginfo("xmlfilepath"));
        org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
        Elements shops=doc.getElementsByTag("shop");
        for(Element shop:shops)
        {
            Shop shop1=new Shop();
            Element basicinfo=shop.getElementsByTag("basicInfo").first();
            shop1.basicinfo=_getbasicinfo(basicinfo);
            Element moreinfo=shop.getElementsByTag("moreInfo").first();
            shop1.moreinfo=_getmoreinfo(moreinfo);
            this.shops.add(shop1);
        }
        Filebases.WriteObj(this.shops,Myconfig.Getconfiginfo("dianpingXYobjfilepath"));
    }
    /**
     * 得到basicInfo
     */
    private Shop.basicInfo _getbasicinfo(Element ele)
    {
        Shop.basicInfo basicInfo=new Shop().new basicInfo();
        String txt=ele.getElementsByTag("id").text();
        basicInfo.id=Integer.parseInt(txt);
        basicInfo.name=ele.getElementsByTag("name").text();
        basicInfo.url=ele.getElementsByTag("url").text();
        Elements cates=ele.getElementsByTag("category");
        for(Element cat:cates)
        {
            basicInfo.categories.add(cat.text());
        }
        basicInfo.star_c=ele.getElementsByTag("star").text();
        Elements aspe=ele.getElementsByTag("aspect");
        for(Element aps:aspe)
        {
            Element valu=aps.nextElementSibling();
            basicInfo.aspect.put(aps.text(), valu.text());
        }
        basicInfo.addressRegion=ele.getElementsByTag("addressRegion").text();
        basicInfo.addressStreet=ele.getElementsByTag("addressStreet").text();
        basicInfo.phone=ele.getElementsByTag("phone").text();
        return basicInfo;
    }
    /**
     * 得到moreInfo
     */
    private Shop.moreInfo _getmoreinfo(Element ele)
    {
        Shop.moreInfo moreInfo=new Shop().new moreInfo();
        Elements aspe=ele.getElementsByTag("aspect");
        for(Element aps:aspe)
        {
            Element valu=aps.nextElementSibling();
            moreInfo.aspect.put(aps.text(), valu.text());
        }
        Elements tageles=ele.getElementsByTag("tag");
        for(Element tagele:tageles)
        {
            moreInfo.tags.add(tagele.text());
        }
        return moreInfo;
    }
    public static void main(String[] args) {
        DianPingXY dianPingXY=new DianPingXY();
        System.out.println("就都I睡觉哦");
    }

    /**
     * 得到某个名称的公司
     * @param name
     * @return
     */
    public DianPingXY.Shop getshopbyname(String name)
    {
        List<DianPingXY.Shop> shops=new ArrayList<>();
        for(DianPingXY.Shop shop:this.shops)
        {
            if(shop.basicinfo.name!=null&&shop.basicinfo.name.length()>0) {
                if (shop.basicinfo.name.contains(name))
                {
                    shops.add(shop);
                }
            }
        }
        int min=10001;
        DianPingXY.Shop tempshop=null;
        for(DianPingXY.Shop shop:shops)
        {
            String thisname=shop.basicinfo.name;
            if(thisname.replaceAll(name,"").length()<min)
            {
                min=thisname.replaceAll(name,"").length();
                tempshop=shop;
            }
        }
        return tempshop;
    }

    /**
     * 是为了清除大众点评中数据有的是有附加的 如北京3D错觉艺术馆+体感游戏节
     * @param name1
     * @param name2
     * @return
     */
    private boolean _getaddwords(String name1,String name2)
    {
        if(name2.contains("·")||name2.contains("(")||name2.contains("-"))
        {
            String[] cc=name2.split("·|(|-)");
            for(String c:cc)
            {
                if(name1.contains(c))
                {
                    return true;
                }
                else if(name1.contains((c.replaceAll("北京",""))))
                {
                    return true;
                }

            }
            return false;
        }else{
            if(name1.contains(name2))
            {
                return true;
            }
            else if(name1.contains((name2.replaceAll("北京",""))))
            {
                return true;
            }
            return false;
        }
    }
}
