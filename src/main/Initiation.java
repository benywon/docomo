package main;

import main.DZDPquery.DianPingXY;

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
    public static Set<String> allSpotNames = new HashSet<>();
    public static DianPingXY dianPingXY=new DianPingXY();
    public static int NearByCount=-1;
    static {
        allSpotNames=Filebases.GetSetFromFile(Myconfig.Getconfiginfo("allSpotNameFile"));
        Set<String> set=new HashSet<>();
        for(DianPingXY.Shop shop:dianPingXY.shops)
        {
            String name=shop.basicinfo.name;
            if(name.contains("3D错觉"))
            {
                System.out.println(name);
            }
            if(name.length()>1)
            {
                if(name.contains("·")||name.contains("(")||name.contains("+")||name.contains("-"))
                {
                    String[] cc=name.split("\\(|·|\\+|-");
                    int max=-1;
                    String temp="";
                    for(String c:cc)
                    {
                        if(c.length()>max)
                        {
                            temp=c;
                        }
                    }
                    set.add(temp);
                }
                else
                {
                    set.add(name);
                }
            }
        }
        allSpotNames.addAll(set);
        allSpotNames.remove("\r\n");
        allSpotNames.remove("\r");
        allSpotNames.remove("\n");
        allSpotNames.remove("");
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

    public Initiation()
    {

    }
}
