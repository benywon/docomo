package main.DZDPquery;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by benywon on 2015/7/17.
 */
public class DianPingSearch {
    /**
     * latitude	 float	 纬度坐标，须与经度坐标同时传入，与城市名称二者必选其一传入
     longitude	 float	 经度坐标，须与纬度坐标同时传入，与城市名称二者必选其一传入
     offset_type	 int	 偏移类型，0:未偏移，1:高德坐标系偏移，2:图吧坐标系偏移，如不传入，默认值为0
     radius	 int	 搜索半径，单位为米，最小值1，最大值5000，如不传入默认为1000
     city	 string	 城市名称，可选范围见相关API返回结果，与经纬度坐标二者必选其一传入
     region	 string	 城市区域名，可选范围见相关API返回结果（不含返回结果中包括的城市名称信息），如传入城市区域名，则城市名称必须传入
     category	 string	 分类名，可选范围见相关API返回结果；支持同时输入多个分类，以逗号分隔，最大不超过5个。
     keyword	 string	 关键词，搜索范围包括商户名、地址、标签等
     out_offset_type	 int	 传出经纬度偏移类型，1:高德坐标系偏移，2:图吧坐标系偏移，如不传入，默认值为1
     platform	 int	 传出链接类型，1:web站链接（适用于网页应用），2:HTML5站链接（适用于移动应用和联网车载应用），如不传入，默认值为1
     has_coupon	 int	 根据是否有优惠券来筛选返回的商户，1:有，0:没有
     has_deal	 int	 根据是否有团购来筛选返回的商户，1:有，0:没有
     has_online_reservation	 int	 根据是否支持在线预订来筛选返回的商户，1:有，0:没有
     sort	 int	 结果排序，1:默认，2:星级高优先，3:产品评价高优先，4:环境评价高优先，5:服务评价高优先，6:点评数量多优先，7:离传入经纬度坐标距离近优先，8:人均价格低优先，9：人均价格高优先
     limit	 int	 每页返回的商户结果条目数上限，最小值1，最大值40，如不传入默认为20
     page	 int	 页码，如不传入默认为1，即第一页
     format	 string	 返回数据格式，可选值为json或xml，如不传入，默认值为json
     */
    public Float latitude;
    public Float longitude;
    public Integer offset_type;
    public Integer radius;
    public Integer out_offset_type;
    public Integer platform;
    public Integer has_coupon;
    public Integer has_online_reservation;
    public Integer sort;
    public Integer limit;
    public Integer page;
    public Integer has_deal;
    public String city;
    public String region;
    public String category;
    public String keyword;
    public String format;

    public DianPingSearch(float latitude, float longitude, int offset_type, int radius, int out_offset_type, int platform, int has_coupon, int has_online_reservation, int sort, int limit, int page, int has_deal, String city, String region, String category, String keyword, String format) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.offset_type = offset_type;
        this.radius = radius;
        this.out_offset_type = out_offset_type;
        this.platform = platform;
        this.has_coupon = has_coupon;
        this.has_online_reservation = has_online_reservation;
        this.sort = sort;
        this.limit = limit;
        this.page = page;
        this.has_deal = has_deal;
        this.city = city;
        this.region = region;
        this.category = category;
        this.keyword = keyword;
        this.format = format;
    }

    public DianPingSearch() {
    }

    public DianPingSearch(DianPingSearch dianPingSearch) {


    }

    public void setkeywords(String keyword)
    {
        this.keyword=keyword;
    }
    private  Map<String,String> pullargs()
    {
        Map<String,String> ParamMap=new HashMap<>();
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
                            ParamMap.put(f.getName(), f.get(this).toString());
                        }
                    }
                    else if(type.equals("class java.lang.Integer"))
                    {
                        ParamMap.put(f.getName(), (int)f.get(this)+"");
                    }
                    else if(type.equals("class java.lang.Float"))
                    {
                        ParamMap.put(f.getName(), (float)f.get(this)+"");
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
      return ParamMap;
    }
    public void ClearCordinate()
    {
        this.longitude=null;
        this.latitude=null;
        this.radius=null;
        this.sort=null;
        this.city="北京";
    }
    public String dopost()
    {
        Map<String,String> map=pullargs();
        if(map!=null)
        {
            return SDianPing.get_dianping_data(map, SDianPing.apiUrl);
        }
        else
        {
            return null;
        }
    }
}
