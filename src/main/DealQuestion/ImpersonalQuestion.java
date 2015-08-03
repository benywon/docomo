package main.DealQuestion;

import main.BaiduMap.BaiduMapDetail;
import main.BaiduMap.BaiduMapDetailProcess;
import main.BaiduMap.BaiduMapRequest;
import main.BaiduMap.BaiduSearch;
import main.DZDPquery.DianPingXY;
import main.Filebases;
import main.Initiation;
import main.Myconfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 回答客观问句的东西
 * 由于是客观问句 所以我们的答案也是一个String
 * Created by benywon on 2015/8/3.
 */
public class ImpersonalQuestion {
    public String Question="";

    public String  deal(String question)
    {
        String answer="";
        //首先我们找出是什么关键词
        String keyword=_keywords(question);
        int type= _gettype(question);
        if(DealQuestion.baiduORdzdp==1)//用百度的
        {
            BaiduMapRequest baiduMapRequest=new BaiduMapRequest();
            baiduMapRequest.setQuery(keyword);
            baiduMapRequest.setRegion("北京");
            BaiduSearch baiduSearch=new BaiduSearch();
            BaiduMapDetailProcess baiduMapDetailProcess=baiduSearch.doonlyone(baiduMapRequest);
            BaiduMapDetail baiduMapDetail=baiduMapDetailProcess.detailList.get(0);
            if (type == 1)//问价格
            {
                answer=baiduMapDetail.price;
                if(baiduMapDetail.price==null)
                {
                 //考虑使用别的方法得到价格
                }
            }
            else if(type==2)
            {
                answer=baiduMapDetail.address;
            }
            else if(type==3)
            {
                answer=baiduMapDetail.shop_hours;
            }
            else if(type==4)
            {
                answer=baiduMapDetail.price;
            }
             else if(type==5)
            {
                answer=baiduMapDetail.telephone;
            }
            //
            //还要判断是不是空的 要是空的 就得用其他的搜索方式

            //
            return answer;
        }
        else//说明是用大众点评
        {
            DianPingXY.Shop shop=Initiation.dianPingXY.getshopbyname(keyword);
            if (type == 1)//问价格
            {
                answer=shop.basicinfo.aspect.get("人均");
            }
            else if(type==2)
            {
                answer=shop.basicinfo.addressStreet;
            }
            else if(type==3)
            {
                answer=shop.moreinfo.aspect.get("营业时间");
            }
            else if(type==4)
            {
                answer=shop.basicinfo.aspect.get("人均");
            }
            else if(type==5)
            {
                answer=shop.basicinfo.phone;
            }
            return answer;
        }
    }

    /**
     * 得到最关键的 词语
     * @param question
     * @return
     */
    private String _keywords(String question)
    {
        for(String name: Initiation.allSpotNames)
        {
            if(question.contains(name))
            {
                return name;
            }
        }
        return null;
    }
    /**
     * get the question type
     * @param question
     * @return
     */
    private int _gettype(String question)
    {
        List<String> list=new ArrayList<>();
        String xml= Filebases.readfile(Myconfig.configerationfilepath);
        if(xml==null||xml=="")
        {
            return 0;
        }
        else
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
            Elements eles=doc.getElementsByTag("QueryRelate");
            for(Element ele:eles)
            {
                if(ele.attr("value").equals("2"))
                {
                    Elements nodes=ele.children();
                    for(Element node:nodes)
                    {
                        String txt=node.text();
                        if(question.contains(txt))
                        {
                            String tagname=node.tagName();
                            if(tagname.equals("pricepattern"))//价格的
                            {
                                return 1;
                            }
                            else if(tagname.equals("position"))
                            {
                                return 2;
                            }
                            else if(tagname.equals("shophour"))
                            {
                                return 3;
                            }
                            else if(tagname.equals("aveprice"))
                            {
                                return 4;
                            }
                            else if(tagname.equals("phonenumber"))
                            {
                                return 5;
                            }
                        }
                    }

                }

            }
            return 0;

        }
    }

    public static void main(String[] args) {
        ImpersonalQuestion impersonalQuestion=new ImpersonalQuestion();
        String txt=impersonalQuestion.deal("雍和宫的开门的时间是什么时候");
        System.out.println(txt);
    }
}
