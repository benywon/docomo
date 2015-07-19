package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by benywon on 2015/7/18.
 */
public class Initiation {
    public static List<String> ValidCategory=new ArrayList<>();
    public static List<String> RegionCategory=new ArrayList<>();
    public static Set<Integer> ValidCIds= new HashSet<>();
    public static int NearByCount=-1;
    static {
        ValidCategory=Filebases.GetListFromFile(Myconfig.Getconfiginfo("validcategoryfile"));
        RegionCategory=parsexml.GetValueByAttr(Myconfig.Getconfiginfo("beijingregionsxmlfile"), "name");
        List<String> temp=new ArrayList<>(RegionCategory);
        for(String bjregion:RegionCategory)//因为里面有很多 朝阳区 什么的 我们需要将朝阳也添加进去
        {
            if(bjregion.endsWith("区"))
            {
                temp.add(bjregion.replaceAll("区$",""));
            }
            String[] divides=bjregion.split("/");
            if(divides.length>1)
            {
                temp.remove(bjregion);
                for(String divide:divides)
                {
                    temp.add(divide);
                }
            }
        }
        RegionCategory=new ArrayList<>(temp);
        ValidCIds= (Set<Integer>) Filebases.ReadObj(Myconfig.Getconfiginfo("valididsfile"));
        NearByCount = Integer.parseInt(Myconfig.Getconfiginfo("NearByCount"));
    }

    public Initiation() {
    }
}
