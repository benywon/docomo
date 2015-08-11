package main.DealQuestion;

import main.BaseMethod;
import main.DZDPquery.DianPingApiRelate;
import main.DZDPquery.DianPingResult;
import main.DZDPquery.DianPingSearch;
import main.DZDPquery.ResultDeal;
import main.Filebases;
import main.Initiation;
import main.Myconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by benywon on 2015/7/18.
 */
public class DealNearRelate {
    public static int NearStrategy=Integer.parseInt(Myconfig.Getconfiginfo("DZDPNearStrategy"));
    public static int NearPattern = 0;
    public static String NearPatternString;
    public List<String> Category=Initiation.ValidCategory;
    public DianPingSearch dianPingSearch = new DianPingSearch();
    public String QUESTION;
    public String tranferkeywords="";

    private static int innumber=0;

    public DealNearRelate(DianPingSearch dianPingSearch, String QUESTION) {
        this.dianPingSearch = dianPingSearch;
        this.QUESTION = QUESTION;
        innumber=0;
    }


    public DianPingResult dealnearst()
    {
        //首先我们需要将问句处理一下 把离我最近的一类的东西拿掉
        DianPingResult dianPingResult = new DianPingResult();
        dianPingSearch.setkeywords(getkeyword());
        switch (NearPattern) {
            case 1: {
                dianPingResult = getnearst();
            }
            break;
            default://都是附近的意思
            {
                dianPingResult = getnearby();
            }
            break;
        }
        return dianPingResult;
    }

    private DianPingResult getnearby()
    {
        innumber++;
        DianPingResult dd=new DianPingResult();
        if(innumber>=3)
        {
            dd = _dorecursivefindnearby();
            return dd;
        }
        if(NearStrategy==0) {
            String txt= dianPingSearch.dopost();
            if (ResultDeal.getcount(txt) > 0) {
                DianPingResult dianPing = new DianPingResult();
                dd.dealxml(txt);
                boolean isvalid = false;
                for (DianPingResult.BNdetail bNdetail : dd.bNdetails) {
                    boolean isvalidinner = false;
                    for (String cate : bNdetail.categories) {
                        for (String valid : Category) {
                            if (valid.contains(cate)) {
                                isvalid = true;
                                isvalidinner = true;
                                break;
                            }
                        }
                        if (isvalidinner) {
                            break;
                        }
                    }
                    if (isvalidinner) {
                        dianPing.bNdetails.add(bNdetail);
                    }
                }
                if (isvalid) {
                    dd.bNdetails = dianPing.bNdetails;
                    return dd;
                } else //说明用以前的信息没有在找到最近的 需要进一步处理 也就是关键词的处理
                {
                    dianPingSearch.setkeywords(this.tranferkeywords);
                    return getnearst();
                }
            } else//说明方圆5000米没有公园或者其他东西 需要在处理
            //处理方法就是还是以这个为圆心 但是找的关键词就没有了 但是还是以距离优先 然后看返回的
//        然后我们看返回结果 从每个结果里面还是这样的找 直到找到一个
            {

                dianPingSearch.setkeywords(this.tranferkeywords);
                return getnearst();

            }
        }
        else
        {
            dd = _dorecursivefindnearby();
        }
        return dd;
    }
    /**
     * 如果是求最近的  那么一定要返回一个
     * 这个里面来求 也就是按照经纬度来将数据post出去
     */
    private DianPingResult getnearst()
    {
        //我们首先要把这个最近的东西拿掉
        /*
        离我最近的公园 我们需要将离我最近的拿掉 将公园放进去
         */
        innumber++;
        DianPingResult dd=new DianPingResult();
        if(innumber>=3)
        {
            dd = _dorecursivefindnearby();
            return dd;
        }
        if(NearStrategy==0) {
            String txt = dianPingSearch.dopost();
            if (ResultDeal.getcount(txt) > 0) {
                dd.dealxml(txt);
                boolean isvalid = false;
                for (DianPingResult.BNdetail bNdetail : dd.bNdetails) {
                    for (String cate : bNdetail.categories) {
                        for (String valid : Category) {
                            if (valid.contains(cate)) {
                                isvalid = true;
                                break;
                            }
                        }
                        if (isvalid) {
                            break;
                        }
                    }
                    if (isvalid) {
                        dd.bNdetails = new ArrayList<>();
                        dd.bNdetails.add(bNdetail);
                        break;
                    }
                }
                if (isvalid) {
                    return dd;
                } else//说明用以前的信息没有在找到最近的 需要进一步处理 也就是关键词的处理
                {
                    dianPingSearch.setkeywords(this.tranferkeywords);
                    return getnearst();
                }

            } else//说明方圆5000米没有公园或者其他东西 需要在处理
            //处理方法就是还是以这个为圆心 但是找的关键词就没有了 但是还是以距离优先 然后看返回的
//        然后我们看返回结果 从每个结果里面还是这样的找 直到找到一个
            {

                dianPingSearch.setkeywords(this.tranferkeywords);
                return getnearst();
            }
        }
        else
        {
            dd = _dorecursivefind();
        }
        return dd;
    }

    /**
     * 没有找到我们只能从我们的知识库找了
     * @return
     */
    private DianPingResult _dorecursivefind()
    {
        DianPingResult resu=new DianPingResult();
        DianPingResult.BNdetail dd=resu.new BNdetail();
        DianPingApiRelate dianPingApiRelate=new DianPingApiRelate();
        float[] customer=new float[2];
        customer[0]=dianPingSearch.longitude;
        customer[1]=dianPingSearch.latitude;
//        DianPingResult dianPingResult=dianPingApiRelate.getbyid(Initiation.ValidCIds);
        DianPingResult dianPingResult= (DianPingResult) Filebases.ReadObj(Myconfig.Getconfiginfo("TempDianPingResultfile"));
        double maxdist=Double.MAX_VALUE;
        for(DianPingResult.BNdetail bNdetail:dianPingResult.bNdetails)
        {
            if(_iscateok(bNdetail))
            {
                float[] result = new float[2];
                result[0] = bNdetail.longitude;
                result[1] = bNdetail.latitude;
                double distance = BaseMethod.getdistance(customer, result);
                if (distance < maxdist) {
                    maxdist = distance;
                    dd = bNdetail;
                }
            }
        }
        dianPingResult.bNdetails.clear();
        dianPingResult.bNdetails.add(dd);
        return dianPingResult;
    }
    /**
     * 从知识库里面找到我们需要的数据
     * 没有找到我们只能从我们的知识库找了 这个是更广范围的
     * 但是我们只有一个约束信息那就是标签
     * @return
     */
    private DianPingResult _dorecursivefindnearby()
    {
        Map<DianPingResult.BNdetail,Double> map=new HashMap<>();
        float[] customer=new float[2];
        customer[0]=dianPingSearch.longitude;
        customer[1]=dianPingSearch.latitude;
//        DianPingResult dianPingResult=dianPingApiRelate.getbyid(Initiation.ValidCIds);
        DianPingResult dianPingResult= (DianPingResult) Filebases.ReadObj(Myconfig.Getconfiginfo("TempDianPingResultfile"));

        for(DianPingResult.BNdetail bNdetail:dianPingResult.bNdetails)
        {
            if(_iscateok(bNdetail))
            {
                float[] result = new float[2];
                result[0] = bNdetail.longitude;
                result[1] = bNdetail.latitude;
                double distance = BaseMethod.getdistance(customer, result);
                map.put(bNdetail, distance);
            }
        }
        dianPingResult.bNdetails.clear();
        map=BaseMethod.sortByValue(map,true);
        int count=0;
        for(Map.Entry<DianPingResult.BNdetail, Double> entry:map.entrySet())
        {
            dianPingResult.bNdetails.add(entry.getKey());
            count++;
            if(count>=Initiation.NearByCount)
            {
                break;
            }
        }
        return dianPingResult;
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
        for(String keycategory:Initiation.ValidCategory)
        {
            String[] contents=keycategory.split("/");
            for(String content:contents)
            {
                if(this.QUESTION.contains(content))
                {
                    this.tranferkeywords+=content+" ";
                    cat.add(keycategory);
                }
            }
        }
        keyword+=this.tranferkeywords;
        //找到了有关的区域
        List<String> list=BaseMethod.doListContainsString(Initiation.RegionCategory,this.QUESTION);
        if(list.size()>0)
        {
            for(String region:list)
            {
                keyword+=region+" ";
            }
        }
        if(cat.size()>0)//那29个类别里面是否有一个
        {
            this.Category=new ArrayList<>();
            this.Category.addAll(cat);
        }
        else
        {
            //添加错误处理内容
        }
        return  keyword;
    }

    /**
     * 判断某个bussiness是不是有效的类型
     * @param bNdetail
     * @return
     */
    private boolean _iscateok(DianPingResult.BNdetail bNdetail)
    {
        for(String incategory:bNdetail.categories)
        {
            for(String validcat:this.Category)
            {
                if(validcat.contains(incategory))
                {
                    return true;
                }
            }
        }
        return false;
    }
}