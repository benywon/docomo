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
    public static Set<Integer> ValidCIds= new HashSet<>();
    public static int NearByCount=-1;
    static {
        ValidCategory=Filebases.GetListFromFile(Myconfig.Getconfiginfo("validcategoryfile"));
        ValidCIds= (Set<Integer>) Filebases.ReadObj(Myconfig.Getconfiginfo("valididsfile"));
        NearByCount=Integer.parseInt(Myconfig.Getconfiginfo("NearByCount"));
    }
}
