package main.DealQuestion;

import main.BaiduMap.BaiduMapDetail;
import main.BaiduMap.BaiduMapDetailProcess;
import main.BaiduMap.BaiduMapRequest;
import main.BaiduMap.BaiduSearch;
import main.Filebases;
import main.Myconfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 由于可能会产生高阶的问题 所以我们要对这些问题进行单独处理
 * 王府井最近的公园的门票是多少
 * Created by benywon on 2015/8/18.
 */
public class SecondOrderQuestion {

    private int neartype=0;//距离模式

    private int attribute=0;//属性模式

    public String getanswer(String question)
    {
        String answer="";
        this.neartype=QueryAnalysis.NearPattern;

        this.attribute=_gettype(question);

        BaiduMapDetailProcess baiduMapDetailProcess=_findNear(question);
        for(BaiduMapDetail baiduMapDetail:baiduMapDetailProcess.detailList)
        {
            String tempname=baiduMapDetail.name;
            String tempanswer=ImpersonalQuestion.getBaiduDetailAttribute(baiduMapDetail,this.attribute);
            if(tempanswer==null)
            {
                tempanswer=ImpersonalQuestion.GetAnswerFromDZDP(tempname,this.attribute);
            }
            answer+=tempname+":\t"+tempanswer+"\r\n";

        }
        return answer;
    }

    /**
     * 将关键词扔进百度用以查找相关的信息
     * @param question
     * @return
     */
    private BaiduMapDetailProcess _findNear(String question)
    {
        //我们首先将最近的公园给找到
        BaiduMapRequest baiduMapRequest=new BaiduMapRequest();
        baiduMapRequest.setLocation(DealQuestion.longitude, DealQuestion.latitude);
        baiduMapRequest.setQuery(question);
        BaiduSearch baiduSearch=new BaiduSearch();
        BaiduMapDetailProcess baiduMapDetailProcess=baiduSearch.dothiswithlocation(baiduMapRequest);
        return baiduMapDetailProcess;
    }

    /**
     * get the question type 就是得到问的是什么属性
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

}
