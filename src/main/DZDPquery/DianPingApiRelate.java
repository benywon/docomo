package main.DZDPquery;

import main.Filebases;
import main.Myconfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by benywon on 2015/7/17.
 */
public class DianPingApiRelate {
    public final String categories_with_businesses_apiurl = "http://api.dianping.com/v1/metadata/get_categories_with_businesses";
    public final String cities_with_businesses_apiurl = "http://api.dianping.com/v1/metadata/get_cities_with_businesses";
    public final String regions_with_businesses_apiurl = "http://api.dianping.com/v1/metadata/get_regions_with_businesses";
    public final String batch_businesses_by_id_apiurl = "http://api.dianping.com/v1/business/get_batch_businesses_by_id";


    public void getcitys() {
        Map<String, String> param = new HashMap<>();
        param.put("format", "json");
        String txt = SDianPing.get_dianping_data(param, cities_with_businesses_apiurl);
        Filebases.Write2File(txt, Myconfig.Getconfiginfo("citiesxmlfile"), false);
        System.out.println(txt);
    }
    public void getcategory()
    {
        Map<String, String> param = new HashMap<>();
        param.put("format", "xml");
        param.put("city", "北京");
        String txt = SDianPing.get_dianping_data(param, categories_with_businesses_apiurl);
        Filebases.Write2File(txt, Myconfig.Getconfiginfo("categoriesxmlfile"), false);
        System.out.println(txt);
    }
    public void getregions()
    {
        Map<String, String> param = new HashMap<>();
        param.put("format", "xml");
        param.put("city", "北京");
        String txt = SDianPing.get_dianping_data(param, regions_with_businesses_apiurl);
        Filebases.Write2File(txt, Myconfig.Getconfiginfo("beijingregionsxmlfile"), false);
//        System.out.println(txt);
    }
    public DianPingApiRelate()
    {

    }
    /**
     * 遍历所有的得到有用的信息
     * @param ids
     * @return
     */
    public  DianPingResult getbyid(Set<Integer> ids)
    {
        String idsstr="";
        DianPingResult dianPingResult=new DianPingResult();
        int count=0;
        for(Integer id:ids)
        {
            idsstr+=id+",";
            count++;
            if(count==40)
            {
                count=0;
                Map<String, String> param = new HashMap<>();
                idsstr=idsstr.replaceAll(",$","");
                param.put("business_ids", idsstr);
                param.put("format", "xml");
                idsstr="";
                String txt = SDianPing.get_dianping_data(param, batch_businesses_by_id_apiurl);
                dianPingResult.dealxml(txt);
            }
        }
        //最后一次
        Map<String, String> param = new HashMap<>();
        idsstr=idsstr.replaceAll(",$","");
        param.put("business_ids", idsstr);
        param.put("format", "xml");
        String txt = SDianPing.get_dianping_data(param, batch_businesses_by_id_apiurl);
        dianPingResult.dealxml(txt);
        WriteOBJ2file(dianPingResult);
        return dianPingResult;
    }
    public void WriteOBJ2file(DianPingResult dianPingResult)
    {
        String filepath=Myconfig.Getconfiginfo("TempDianPingResultfile");
        Filebases.WriteObj(dianPingResult,filepath);
    }
    public static void main(String[] args) {
        DianPingApiRelate dianPingApiRelate =new DianPingApiRelate();
        dianPingApiRelate.getregions();
    }
}
