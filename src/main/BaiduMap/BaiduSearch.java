package main.BaiduMap;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.TypedDependency;
import main.BaseMethod;
import main.DealQuestion.DealNearRelate;
import main.Initiation;
import main.LanguageTools.Parser;

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
        if(baiduMapRequest.query==null||baiduMapRequest.query.length()<1)
        {
            return null;
        }
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
        boolean findcategory=false;
        for(String keycategory: Initiation.ValidCategory)
        {
            String[] contents=keycategory.split("/");
            for(String content:contents)
            {
                if(this.QUESTION.contains(content))
                {
                    this.tranferkeywords+=content+",";
                    cat.add(keycategory);
                    findcategory=true;
                }
            }
        }

        boolean isaddlocation=true;


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
            isaddlocation=false;
        }

        if(!findcategory)//说明没找到相关类别 我们需要进一步处理
        {
            //我们需要在依存句法分析里面找到这个类别
            keyword+=GetWordFromDependency(this.QUESTION,isaddlocation);
        }
        this.tranferkeywords=this.tranferkeywords.replaceAll(",$","");
        keyword+=this.tranferkeywords;
        return  keyword;
    }

    /**
     * 从一个依存句法分析里面找到这个中心的词
     * @param question
     * 判断是否要加入地点信息
     * @return
     */
    public static String GetWordFromDependency(String question,boolean isaddlocation)
    {
        List<TypedDependency> depen= Parser.getDependencyList(question);
        String keyword="";
        for(TypedDependency typedDependency:depen)
        {
            //我们首先找到有没有 gov是 nt/nr  然后依存关系是 assmod  depend是nn的
            String gov=typedDependency.gov().tag();
            String dep=typedDependency.dep().tag();
            String reln=typedDependency.reln().getShortName();
            //type1 NT assmod NN
            if(reln.equals("assmod"))
            {
                if(gov.equals("NN"))//有最近公园等等句子
                {
                    if(dep.equals("NT")) //最近的公园有哪些这一类的问题
                    {
                        keyword = typedDependency.gov().value();
                    }
                    else if(dep.equals("NR"))//海淀区的公园有哪些这一类的
                    {
                        keyword=typedDependency.gov().value();
                        if(isaddlocation)
                        {
                            keyword += " " + typedDependency.dep().value();//也要把海淀区等加上
                        }
                    }
                    break;
                }
            }
        }
        if(keyword.equals(""))//说明没找到 然后我们从root找 先找 NN dep Root
        {
            IndexedWord Root=new IndexedWord();
            for(TypedDependency typedDependency:depen)
            {
                String reln=typedDependency.reln().getShortName();
                if(reln.equals("root"))
                {
                    Root=typedDependency.dep();
                    if(Root.tag().startsWith("N"))//这一类就不用找了
                    {
                        return "";
                    }
                    break;
                }
            }
            //然后找这个root的东西相关的
            for(TypedDependency typedDependency:depen)
            {
                IndexedWord indexedWord=typedDependency.gov();
                if(indexedWord.equals(Root))
                {
                    String dep=typedDependency.dep().tag();
                    String reln=typedDependency.reln().getShortName();
                    if(reln.equals("dep")&&dep.equals("NN"))//公园NN dep 有root
                    {
                        keyword+=typedDependency.dep().value();
                    }
                    else if(reln.equals("nsubj"))//公园的门票有哪些
                    {
                        IndexedWord nsubj=typedDependency.dep();
                        for(TypedDependency typedDependency1:depen)
                        {
                            if(typedDependency1.gov().equals(nsubj))
                            {
                                if(typedDependency1.reln().getShortName().equals("assmod"))
                                {
                                    if(typedDependency1.dep().tag().equals("NN"))
                                    {
                                        keyword+=typedDependency1.dep().value();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return keyword;
    }

    public static void main(String[] args) {
        String bb=GetWordFromDependency("国家大剧院的营业时间是什么时候？",false);
        System.out.println(bb);
    }
}