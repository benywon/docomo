package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benywon on 2015/7/16.
 */
public class Myconfig {

    public static String configerationfilepath="L:\\program\\cip\\DOCOMO\\parsequestion\\config.xml";

    public Myconfig(String configerationfilepath) {
        this.configerationfilepath = configerationfilepath;
    }
    public Myconfig()
    {
    }
    public void setConfigerationfilepath(String configerationfilepath)
    {
        this.configerationfilepath = configerationfilepath;
    }
    /**
     * 从配置文件中读取相应的信息
     * @param attr 要读取信息 属性值
     * @return 读取出的信息
     */
    public  static String Getconfiginfo(String attr)
    {
        String xml=Filebases.readfile(configerationfilepath);
        if(xml==null||xml=="")
        {
            return null;
        }
        else
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
            org.jsoup.nodes.Element returnattr=doc.getElementsByTag(attr).first();
            if(returnattr==null)
            {
                return null;
            }
            else
            {
                return returnattr.text();
            }

        }
    }
    /**
     * 从配置文件中读取相应的信息
     * @param attr 要读取信息 属性值
     *
     * @return 读取出的信息
     */
    public  static String Getconfiginfo(String attr,String value)
    {
        String xml=Filebases.readfile(configerationfilepath);
        if(xml==null||xml=="")
        {
            return null;
        }
        else
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
            Elements eles=doc.getElementsByTag(attr);
            org.jsoup.nodes.Element returnattr=eles.attr("value", value).first();
            if(returnattr==null)
            {
                return null;
            }
            else
            {
                return returnattr.text();
            }

        }
    }

    /**
     * 得到一系列的标签的方法 只是通过标签得到信息
     */
    public static List<String> GetconfiginfoList(String attr)
    {
        List<String> list=new ArrayList<>();
        String xml=Filebases.readfile(configerationfilepath);
        if(xml==null||xml=="")
        {
            return null;
        }
        else
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
            org.jsoup.select.Elements returnattr=doc.getElementsByTag(attr);
            for(org.jsoup.nodes.Element element:returnattr)
            {
                list.add(element.text());
            }
        }
        return list;
    }

    /**
     * 得到某个tag下的所有内容 返回一个列表
     * @param tag
     * @return
     */
    public static List GetAllnodes(String tag)
    {
        List<String> list=new ArrayList<>();
        String xml=Filebases.readfile(configerationfilepath);
        if(xml==null||xml=="")
        {
            return null;
        }
        else
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
            org.jsoup.nodes.Element returnattr=doc.getElementsByTag(tag).first();
            Elements eles=returnattr.children();
            for(org.jsoup.nodes.Element element:eles)
            {
                list.add(element.text());
            }
        }
        return list;
    }
    /**
     * 得到某个tag下的所有内容 返回一个列表
     * @param tag
     * @return
     */
    public static List GetAllnodes(String tag,String value)
    {
        List<String> list=new ArrayList<>();
        String xml=Filebases.readfile(configerationfilepath);
        if(xml==null||xml=="")
        {
            return null;
        }
        else
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
            Elements returnattr=doc.getElementsByTag(tag);
            for(Element ele:returnattr)
            {
                if(ele.attr("value").equals(value))
                {
                    Elements eles=ele.children();
                    for(org.jsoup.nodes.Element element:eles)
                    {
                        list.add(element.text());
                    }
                    break;
                }
            }

        }
        return list;
    }
    public static void main(String[] args) {
//        Myconfig myconfig=new Myconfig();
          List ii=Myconfig.GetAllnodes("QueryRelate","1");
         System.out.println("jhidf");
    }
//   public boolean newdoc()
//    {
//        boolean ok=true;
//        Element root = new Element("configerations");
//        Document Doc = new Document(root);
//        Element xmlfilepath = new Element("xmlfilepath");
//        xmlfilepath.addContent("L:\\program\\cip\\DOCOMO\\parsequestion\\beijingData.xml");
//        root.addContent(xmlfilepath);
//        Element namesfilepath = new Element("namesfilepath");
//        namesfilepath.addContent("L:\\program\\cip\\DOCOMO\\parsequestion\\name.txt");
//        root.addContent(namesfilepath);
//        Element urlcontent = new Element("urlpathfile");
//        urlcontent.addContent("L:\\\\program\\cip\\DOCOMO\\parsequestion\\name.txt");
//        root.addContent(urlcontent);
//        Format format = Format.getPrettyFormat();
//        XMLOutputter XMLOut = new XMLOutputter(format);
//        try {
//            XMLOut.output(Doc, new FileOutputStream(configerationfilepath));
//        } catch (IOException e) {
//            e.printStackTrace();e.printStackTrace();
//            ok=false;
//        }
//        return ok;
//    }
}
