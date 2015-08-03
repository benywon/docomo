package main.BaiduMap;

import main.BaseMethod;
import main.DealQuestion.DealNearRelate;
import main.Initiation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benywon on 2015/7/19.
 */
public class BaiduSearch {
    private String tranferkeywords="";
    private String QUESTION="";
    public BaiduMapDetailProcess dothiswithlocation(BaiduMapRequest baiduMapRequest)
    {
        this.QUESTION=baiduMapRequest.query;
        baiduMapRequest.setQuery(getkeyword());
        //我们还是要先分析关键词
        if(DealNearRelate.NearPattern==1)//说明是求最近的
        {
            baiduMapRequest.setMax_num(1);
            baiduMapRequest.setSort_nameBydistance();
            baiduMapRequest.setsortaccent();
            String contrny=baiduMapRequest.dorequest();
            BaiduMapResult baiduMapResult=new BaiduMapResult();
            BaiduMapDetailProcess baiduMapDetailProcess=baiduMapResult.getresult(contrny);
            BaiduMapDetail baiduMapDetail=getthenearst(baiduMapDetailProcess);
            baiduMapDetailProcess.detailList.clear();
            baiduMapDetailProcess.detailList.add(baiduMapDetail);
            return baiduMapDetailProcess;
        }
        else//附近的
        {
            baiduMapRequest.setMax_num(Initiation.NearByCount/baiduMapRequest.page_size+1);
            baiduMapRequest.setSort_nameBydistance();
            baiduMapRequest.setsortaccent();
            String contrny=baiduMapRequest.dorequest();
            BaiduMapResult baiduMapResult=new BaiduMapResult();
            BaiduMapDetailProcess baiduMapDetailProcess=baiduMapResult.getresult(contrny);
            return baiduMapDetailProcess;
        }

    }
    public BaiduMapDetailProcess dothiswithoutlocation(BaiduMapRequest baiduMapRequest) {
        this.QUESTION = baiduMapRequest.query;
        baiduMapRequest.setQuery(getkeyword());
        baiduMapRequest.setMax_num(this.max_num_cat(baiduMapRequest.page_size));
        baiduMapRequest.setSort_nameBydefault();
        baiduMapRequest.setsortdescent();
        String contrny = baiduMapRequest.dorequest();
        BaiduMapResult baiduMapResult = new BaiduMapResult();
        BaiduMapDetailProcess baiduMapDetailProcess = baiduMapResult.getresult(contrny);
        BaiduMapDetail baiduMapDetail = getthenearst(baiduMapDetailProcess);
        baiduMapDetailProcess.detailList.clear();
        baiduMapDetailProcess.detailList.add(baiduMapDetail);
        return baiduMapDetailProcess;
    }
        /**
         * 仅仅只搜寻一个的程序
         * getresultonly1
          */
    public BaiduMapDetailProcess doonlyone(BaiduMapRequest baiduMapRequest)
    {
        baiduMapRequest.setMax_num(this.max_num_cat(baiduMapRequest.page_size));
        baiduMapRequest.setSort_nameBydefault();
        baiduMapRequest.setsortdescent();
        String contrny=baiduMapRequest.dorequest();
        BaiduMapResult baiduMapResult=new BaiduMapResult();
        BaiduMapDetailProcess baiduMapDetailProcess=baiduMapResult.getresultonly1(contrny);
        return baiduMapDetailProcess;
    }
    /**
     *计算应该弄几页
     * @param page_size
     * @return
     */
    private int max_num_cat(int page_size)
    {
        if(Initiation.NearByCount%page_size!=0)
        {
            return Initiation.NearByCount / page_size + 1;
        }
        else
        {
            return Initiation.NearByCount / page_size;
        }
    }
    private BaiduMapDetail getthenearst(BaiduMapDetailProcess baiduMapDetailProcess)
    {
        BaiduMapDetail baiduMapDetail=new BaiduMapDetail();
        int max=Integer.MAX_VALUE;
        for(BaiduMapDetail baiduMapDetail1:baiduMapDetailProcess.detailList)
        {
            int distance=baiduMapDetail1.distance;
            if(distance<max)
            {
                max=distance;
                baiduMapDetail=baiduMapDetail1;
            }
        }
        return baiduMapDetail;
    }
    /**
     * 寻找关键字的程序
     * 并且会处理一下这个类别
     * @return
     */
    private String getkeyword()
    {

//        String  keyword=this.QUESTION.substring(this.QUESTION.lastIndexOf(NearPatternString)+NearPatternString.length());
//
//        keyword=keyword.replaceAll("的", "");
        String  keyword="";
        List<String> cat=new ArrayList<>();
        for(String keycategory: Initiation.ValidCategory)
        {
            String[] contents=keycategory.split("/");
            for(String content:contents)
            {
                if(this.QUESTION.contains(content))
                {
                    this.tranferkeywords+=content+",";
                    cat.add(keycategory);
                }
            }
        }
        this.tranferkeywords=this.tranferkeywords.replaceAll(",$","");
        keyword+=this.tranferkeywords;
        //找到了有关的区域
        List<String> list= BaseMethod.doListContainsString(Initiation.RegionCategory, this.QUESTION);
        if(list.size()>0)
        {
            int max=-1;
            String temp="";
            for(String region:list)
            {
                if(region.length()>max)
                {
                    max=region.length();
                    temp=region;
                }
            }
            keyword+=","+temp;
        }
        return  keyword;
    }
}