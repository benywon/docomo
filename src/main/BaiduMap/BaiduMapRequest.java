package main.BaiduMap;

import main.Crawlerbases;
import main.Filebases;
import main.Myconfig;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by benywon on 2015/7/19.
 */
public class BaiduMapRequest {

    //---------------------------------------------------------------------------------//
    public String query;//
    /*
    中关村、ATM、百度大厦
    检索关键字，周边检索和矩形区域内检索支持多个关键字并集检索，
    不同关键字间以$符号分隔，最多支持10个关键字检索。如:”银行$酒店”。
     */
    public String tag;
    /*
    日式烧烤/铁板烧、朝外大街
    标签项，与q组合进行检索，以“,”分隔
     */
    public String output="xml";
    /*
    json或xml
    输出格式为json或者xml
     */
    public Integer scope=2;
    /*
    1、2	检索结果详细程度。
    取值为1 或空，则返回基本信息；取值为2，返回检索POI详细信息
     */
    public Integer page_size=20;
    /*
    10	范围记录数量
    默认为10条记录，最大返回20条。多关键字检索时，返回的记录数为关键字个数*page_size。
     */
    public Integer page_num=0;//每页返回的记录数
    /*
    0、1、2	分页页码
    默认为0,0代表第一页，1代表第二页，以此类推。
     */
    public String ak="CGL2GfpsGcD4bpHT6vU8slf4";
    public String location="纬度,经度";//39.915119 116.403963
    /*
    38.76623,116.43213
    lat<纬度>,lng<经度>
    周边检索中心点，不支持多个点
     */
    public Integer radius=50000;//搜索半径是50公里
    /*
    2000
    周边检索半径，单位为米
     */
    public Map<String,String> ParaMap=new HashMap<>();
    /*
    我们提交参数用的map
     */
    public String region;
    /*
    北京、131、全国
    检索区域（市级以上行政区域），如果取值为“全国”或某省份，则返回指定区域的POI及数量。
     */
    public String filter="industry_type:life";
    public String sort_name="default";
    /*
    (2)industry_type取值cater时，sort_name取值：

    default：默认

    taste_rating：口味

    price：价格

    overall_rating：好评

    service_rating：服务
     */

    //-------------------------------------------------------------------------------//
    //-----------------------------------------------------------------------------------------//
    /**
     * 由一个属性得到结果
     * @return
     */
    public String dorequest()
    {
        this.setParaMap();
        String content="";
        String url=BaiduMap.positionapi;
        for(Map.Entry<String,String> entryobj:this.ParaMap.entrySet())
        {
            String key=entryobj.getKey();
            String value=entryobj.getValue();
            url+=key+"="+value+"&";
        }
        url=url.replaceAll("&$","");
        try {
            content= Crawlerbases.spiderhtml(url,"utf-8");
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 下面就是一系列操作方法
     * @param filter
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public void setPage_num(int page_num) {
        this.page_num = page_num;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }
    public void setLocation(float jingdu,float weidu) {
        String loc=weidu+","+jingdu;
        this.location = loc;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }
    public void setSort_nameBydefault()
    {
        this.sort_name="default";
    }
    public void setSort_nameByprice()
    {
        this.sort_name="price";
    }
    public void setSort_nameByoverall_rating()
    {
        this.sort_name="overall_rating";
    }
    public void setSort_nameBycomment_num()
    {
        this.sort_name="comment_num";
    }
    public void setSort_nameBydistance()
    {
        this.sort_name="distance";
    }

    /**
     * 填充内容的方法 注意我们只让这几类的内容被填充 integer string
     * @return
     */
    public boolean setParaMap() {
        Field[] fields = this.getClass().getDeclaredFields();
        for(int i=0;i<fields.length;i++)
        {
            Field f = fields[i];
            try {
                if(f.get(this)!=null)
                {
                    String type = f.getGenericType().toString();
                    if(type.equals("class java.lang.String"))
                    {
                        if(!f.get(this).toString().equals(""))
                        {
                            ParaMap.put(f.getName(), f.get(this).toString());
                        }
                    }
                    else if(type.equals("class java.lang.Integer"))
                    {
                        ParaMap.put(f.getName(), (int)f.get(this)+"");
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        BaiduMapRequest baiduMapRequest=new BaiduMapRequest();
        baiduMapRequest.setLocation(116.404f, 39.915f);
        baiduMapRequest.setQuery("公园");
        baiduMapRequest.setScope(2);
        baiduMapRequest.setPage_size(19);
        String contrny=baiduMapRequest.dorequest();
        BaiduMapResult baiduMapResult=new BaiduMapResult();
        BaiduMapDetailProcess baiduMapDetailProcess=baiduMapResult.getresult(contrny);
        Filebases.Write2File(contrny, Myconfig.Getconfiginfo("tempxmlfile"),false);
        System.out.println(contrny);
    }

}
