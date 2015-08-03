package main.DealQuestion;

import main.Initiation;
import main.Myconfig;

import java.util.List;

/**
 * Created by benywon on 2015/8/3.
 */
public class QueryAnalysis {
    /**
     * 0代表默认
     * 1代表第一类问题的第二部分 （王府井的营业时间 王府井的地址==）
     * 2代表第一类问题的第一部分 （王府井的附近的 离我最近的 我周围的xxx）
     */
    public static int querytype=0;

    public QueryAnalysis(String question)
    {
        getourkeywords(question);
    }

    /**
     * 判断我们的问句类型的方法
     * @param query
     */
    public void getourkeywords(String query)
    {
        for(String name: Initiation.allSpotNames)
        {
            if(query.contains(name))//如果我们的问句包含有一些景点的名称
            {
                //我们先判断是不是有一些客观属性
                List<String> impersonals= Myconfig.GetAllnodes("QueryRelate","2");
                for(String st:impersonals)
                {
                    if(query.contains(st))
                    {
                        querytype=1;
                        break;
                    }
                }

            }
        }
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
}
