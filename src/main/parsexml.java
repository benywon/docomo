package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by benywon on 2015/7/14.
 */
public class parsexml {
    private String  xmlfilepath="";
    private String aspect="";

    private String objectsfilepath="aspect.txt";

    public parsexml(String xmlfilepath) {
        this.xmlfilepath = xmlfilepath;
    }

    /**
     * 设置我们xml的路径
     * @param path
     */
    public void SetXmlFilepath(String path)
    {
        this.xmlfilepath=path;
    }
    public void setObjectsfilepath(String objectsfilepath)
    {
        this.objectsfilepath = objectsfilepath;
    }

    /**
     * 得到所有旅游景点的名称url或者其他信息 输入的就是shops和景点的属性
     * @param shops 就是得到的那个所有景点的element list
     * @return 返回的是一个list 景点的名称或者其他信息
     */
    private List<String> _getshopname(Elements shops,String arg)
    {
        List<String> shoplist=new ArrayList<>();
        for(Element shop:shops)
        {
            Elements name=shop.getElementsByTag(arg);
            if(name!=null||name.size()>0)
            {
                for(Element ele:name)
                {
                    String txt = ele.text();
                    if (txt.length() > 0) {
                        shoplist.add(txt);
                    }
                }
            }
        }
        return shoplist;
    }
    public boolean ParsethisXml()
    {
        String xml=Filebases.readfile(this.xmlfilepath);
        if(xml==null||xml=="")
        {
            return false;
        }
        else
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
            Elements shops=doc.getElementsByTag("shop");

            List<String> list=_getshopname(shops, "category");
            Map<Object, Integer> map=BaseMethod.List2Map(list);
            map=BaseMethod.sortByValue(map,false);
            System.out.println();
            Set<String> set=new HashSet<>(list);
            Filebases.WriteList2File(new ArrayList<>(set),"L:\\program\\cip\\DOCOMO\\parsequestion\\category.xml");
            System.out.println("结束");
        }
        return true;
    }
    public boolean getallcatgory()
    {
        String xml=Filebases.readfile(this.xmlfilepath);
        if(xml==null||xml=="")
        {
            return false;
        }
        else
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
            Elements shops=doc.getElementsByTag("shop");

            List<String> shoplist=new ArrayList<>();
            for(Element shop:shops)
            {
                Element name=shop.getElementsByTag("category").last();
                if(name!=null)
                {
                    if(name.text().length()>0)
                    {
                        shoplist.add(name.text());
                    }
                }
            }


            Map<Object, Integer> map=BaseMethod.List2Map(shoplist);
            map=BaseMethod.sortByValue(map,false);
            System.out.println();
            Set<String> set=new HashSet<>(shoplist);
            Filebases.WriteList2File(new ArrayList<>(set), Myconfig.Getconfiginfo("validcategoryfile"));
            List list=new ArrayList<>();
            Filebases.GetListFromFile(Myconfig.Getconfiginfo("validcategoryfile"));
            System.out.println("结束");
        }
        return true;
    }
    public boolean getallids()
    {
        String xml=Filebases.readfile(this.xmlfilepath);
        if(xml==null||xml=="")
        {
            return false;
        }
        else
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
            Elements shops=doc.getElementsByTag("shop");

            List<Integer> shoplist=new ArrayList<>();
            for(Element shop:shops)
            {
                Element name=shop.getElementsByTag("id").last();
                if(name!=null)
                {
                    if(name.text().length()>0)
                    {
                        int tt=Integer.parseInt(name.text());
                        shoplist.add(tt);
                    }
                }
            }


            Map<Object, Integer> map=BaseMethod.List2Map(shoplist);
            map=BaseMethod.sortByValue(map,false);
            System.out.println();
            Set<Integer> set=new HashSet<>(shoplist);
            Filebases.WriteObj(set, Myconfig.Getconfiginfo("valididsfile"));
//            List list=new ArrayList<>();
//            Filebases.GetListFromFile(Myconfig.Getconfiginfo("validcategoryfile"));
            System.out.println("结束");
        }
        return true;
    }
    public static void main(String[] args)
    {
        Myconfig myconfig=new Myconfig();
        String xmlpath=myconfig.Getconfiginfo("xmlfilepath");
        parsexml parsexml=new parsexml(xmlpath);
        parsexml.getallids();
    }
}
