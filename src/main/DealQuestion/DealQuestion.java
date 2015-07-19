package main.DealQuestion;

import main.BaiduMap.BaiduMapDetailProcess;
import main.BaiduMap.BaiduMapRequest;
import main.BaiduMap.BaiduSearch;
import main.DZDPquery.DianPingResult;
import main.DZDPquery.DianPingSearch;
import main.Myconfig;

import java.util.List;

/**
 * Created by benywon on 2015/7/18.
 */
public class DealQuestion{

    public static int baiduORdzdp=Integer.parseInt(Myconfig.Getconfiginfo("baiduORdzdp"));
    public String Question;
    public boolean IsNeetCoordinate=false;
    private DianPingSearch dianPingSearch=new DianPingSearch();
    public DianPingResult dianPingResult=new DianPingResult();

    public DealQuestion(String question)
    {
        Question = question;
    }

    public DealQuestion(DianPingSearch dianPingSearch)
    {
        this.dianPingSearch=dianPingSearch;
    }

    /**
     * 处理问题的方法
     * 由于我们的用可能问很多类型的问题
     * 所以我们需要回答的类型也很多 比如离我最近 什么什么区的等等
     * 因为大众点评api首先会得到的是我们的经纬度信息 所以我们需要做一下处理
     * 因为肯定最开始是有经纬度信息的 而且这个信息比你的城市city信息优先级更高 所以我们需要在开始的时候将这些信息过滤掉
     * 比如 如果我们用经纬度信息没有返回结果 那么我们需要把经纬度信息去掉 然后用city信息去查找
     * 其次 我们需要首先查找这个城市信息 然后看看是不是包含我们需要的region 然后包含region或者city之后 我们就将经纬度信息去掉
     * 所以 经纬度信息只有在出现 最近 离我最近 比较近  附近等词的时候才有用
     * @param question
     */
    public void dealthisquesion(String question)
    {
        this.Question=question;
        deal();
    }
    public void dealthisquesion()
    {
        deal();
    }
    private void deal()
    {
        _isneedcoordinate();
        if(baiduORdzdp==0)//说明是用大众点评的map
        {

            if (this.IsNeetCoordinate) {
                DealNearRelate dealNearRelate = new DealNearRelate(this.dianPingSearch, this.Question);
                this.dianPingResult = dealNearRelate.dealnearst();
            } else //没有最近 附近一类的 东西 可能就是我们需要找一个区域的东西
            {
                DealRegion dealRegion = new DealRegion(this.dianPingSearch, this.Question);
                this.dianPingResult = dealRegion.dealDZDPregion();
            }
        }
        else//用百度的东西
        {
            BaiduMapRequest baiduMapRequest=new BaiduMapRequest();
            baiduMapRequest.setLocation(this.dianPingSearch.longitude, this.dianPingSearch.latitude);
            baiduMapRequest.setQuery(this.Question);
            BaiduSearch baiduSearch=new BaiduSearch();
            if(this.IsNeetCoordinate)
            {
                BaiduMapDetailProcess baiduMapDetailProcess=baiduSearch.dothiswithlocation(baiduMapRequest);
                System.out.println("发给你");
            }
            else//找区域 海淀区的什么什么
            {
                BaiduMapDetailProcess baiduMapDetailProcess=baiduSearch.dothiswithoutlocation(baiduMapRequest);
            }
        }
    }

    private void _isneedcoordinate()
    {
        List<String> list= Myconfig.GetconfiginfoList("nearpatter");
        this.IsNeetCoordinate=false;
        if(list!=null)
        {
            for(String pattern:list)
            {
                if(this.Question.contains(pattern))
                {
                    this.IsNeetCoordinate=true;
                    DealNearRelate.NearPattern=list.indexOf(pattern)+1;
                    DealNearRelate.NearPatternString=pattern;
                    break;
                }
            }
        }
    }
    public static void main(String[] args) {
        DianPingSearch dianPingSearch=new DianPingSearch();
        dianPingSearch.latitude=39.9025f;
        dianPingSearch.longitude=116.3973f;
        dianPingSearch.radius=5000;
        dianPingSearch.sort=7;
        dianPingSearch.format="xml";
        DealQuestion dealQuestion=new DealQuestion(dianPingSearch);
        dealQuestion.dealthisquesion("海淀区的名胜古迹有什么");
        System.out.println("成功");
    }
}
