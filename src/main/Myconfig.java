package main;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jsoup.Jsoup;

import java.io.FileOutputStream;
import java.io.IOException;
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
    public static void main(String[] args) {
//        Myconfig myconfig=new Myconfig();

    }
   public boolean newdoc()
    {
        boolean ok=true;
        Element root = new Element("configerations");
        Document Doc = new Document(root);
        Element xmlfilepath = new Element("xmlfilepath");
        xmlfilepath.addContent("L:\\program\\cip\\DOCOMO\\parsequestion\\beijingData.xml");
        root.addContent(xmlfilepath);
        Element namesfilepath = new Element("namesfilepath");
        namesfilepath.addContent("L:\\program\\cip\\DOCOMO\\parsequestion\\name.txt");
        root.addContent(namesfilepath);
        Element urlcontent = new Element("urlpathfile");
        urlcontent.addContent("L:\\\\program\\cip\\DOCOMO\\parsequestion\\name.txt");
        root.addContent(urlcontent);
        Format format = Format.getPrettyFormat();
        XMLOutputter XMLOut = new XMLOutputter(format);
        try {
            XMLOut.output(Doc, new FileOutputStream(configerationfilepath));
        } catch (IOException e) {
            e.printStackTrace();e.printStackTrace();
            ok=false;
        }
        return ok;
    }
}
