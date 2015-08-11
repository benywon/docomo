package main.DealQuestion;

import main.Myconfig;
import org.ansj.domain.Nature;
import org.ansj.domain.Term;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static main.LanguageTools.Seger.SegWordsReturnTerm;

/**
 * Created by benywon on 2015/8/3.
 */
public class QueryAnalysis {
    /**
     * 0代表默认
     * 1代表第一类问题的第二部分 （王府井的营业时间 王府井的地址==）
     * 2代表第一类问题的第一部分 （王府井的附近的 离我最近的 我周围的xxx）
     */
    private static Set<String> nertag=new HashSet<>();
    static {
        nertag.add("nt");
        nertag.add("nz");
        nertag.add("nw");
        nertag.add("userDefine");
    }
    public static int querytype=0;

    public QueryAnalysis(String question)
    {
        getourkeywords(question);
    }

    /**
     * 判断我们的问句类型的方法
     * 我们经过改变后发现用anj分词效果很好 所以不用这个了 然后我们把这个东西放到后面
     * 我们只要判断有我们模板里面写的那些属性就定义为是问的第一类问题
     * @param query
     */
    public void getourkeywords(String query)
    {
        List<String> impersonals= Myconfig.GetAllnodes("QueryRelate","2");
        for(String st:impersonals)
        {
            if(query.contains(st))
            {
                querytype=1;
                break;
            }
        }

//        for(String name: Initiation.allSpotNames)
//        {
//            if(query.contains(name))//如果我们的问句包含有一些景点的名称
//            {
//                //我们先判断是不是有一些客观属性
//                for(String st:impersonals)
//                {
//                    if(query.contains(st))
//                    {
//                        querytype=1;
//                        break;
//                    }
//                }
//
//            }
//        }
        //然后我们找到一系列的东西 比如附近的
        if(querytype==0)//还是没有任何变化 就是不是上面的这些
        {
            List<String> nearrelate = Myconfig.GetAllnodes("QueryRelate","1");
            for(String near:nearrelate)
            {
                if(query.contains(near))
                {
                    querytype=2;
                    break;
                }
            }
        }
    }

    /**
     * 判断一个句子里面是否包含有名词的东西
     * @param question
     * @return
     */
    private boolean _dohaveNE(String question)
    {
        List<Term> list=SegWordsReturnTerm(question);
        for(Term term:list)
        {
            Nature nature=term.natrue();
            if(nature.natureStr.startsWith("n")||nature.natureStr.contains("userDefine"))
            {

                return true;
            }
        }
        return false;
    }
}
