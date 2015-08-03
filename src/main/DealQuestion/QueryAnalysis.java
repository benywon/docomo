package main.DealQuestion;

import main.Initiation;

/**
 * Created by benywon on 2015/8/3.
 */
public class QueryAnalysis {
    public static int querytype=0;

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

            }
        }
    }
}
