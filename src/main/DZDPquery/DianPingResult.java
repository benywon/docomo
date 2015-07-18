package main.DZDPquery;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by benywon on 2015/7/17.
 */
public class DianPingResult implements Serializable {

    public String	status;	//本次API访问状态，如果成功返回"OK"，并返回结果字段，如果失败返回"ERROR"，并返回错误说明
    public Integer	count;	//本次API访问所获取的商户数量
    public Integer	total_count;	//所有页面商户总数，最多为40条
    //上面三个是必须的
    public List<BNdetail> bNdetails=new ArrayList<>();

    public class BNdetail implements Serializable  {
        public Integer avg_price;//	人均价格，单位:元，若没有人均，返回-1
        public Integer review_count;//	点评数量
        public List<String> review_list_url = new ArrayList<>();//	点评页面URL链接
        public Integer distance;//	商户与参数坐标的距离，单位为米，如不传入经纬度坐标，结果为-1
        public String business_url;//	商户页面链接
        public String photo_url;//	照片链接，照片最大尺寸700×700
        public String s_photo_url;//	小尺寸照片链接，照片最大尺寸278×200
        public Integer photo_count;//	照片数量
        public List<String> photo_list_url;//	照片页面URL链接
        public Integer has_coupon;//	是否有优惠券，0:没有，1:有
        public Integer coupon_id;//	优惠券ID
        public String coupon_description;//	优惠券描述
        public String coupon_url;//	优惠券页面链接
        public Integer has_deal;//	是否有团购，0:没有，1:有
        public Integer deal_count;//	商户当前在线团购数量
        public List<String> deals = new ArrayList<>();//	团购列表
        public String deals_id;//	团购ID
        public String deals_description;//	团购描述
        public String deals_url;//	团购页面链接
        public Integer has_online_reservation;//	是否有在线预订，0:没有，1:有
        public String online_reservation_url;//	在线预订页面链接，目前仅返回HTML5站点链接
        public Integer business_id;//	商户ID
        public String name;//	商户名
        public String branch_name;//	分店名
        public String address;//	地址
        public String telephone;//	带区号的电话
        public String city;//	所在城市
        public List<String> regions;//	所在区域信息列表，如[徐汇区，徐家汇]
        public List<String> categories;//	所属分类信息列表，如[宁波菜，婚宴酒店]
        public Float latitude;//	纬度坐标
        public Float longitude;//	经度坐标
        public Float avg_rating;//	星级评分，5.0代表五星，4.5代表四星半，依此类推
        public String rating_img_url;//	星级图片链接
        public String rating_s_img_url;//	小尺寸星级图片链接
        public Integer product_grade;//	产品/食品口味评价，1:一般，2:尚可，3:好，4:很好，5:非常好
        public Integer decoration_grade;//	环境评价，1:一般，2:尚可，3:好，4:很好，5:非常好
        public Integer service_grade;//	服务评价，1:一般，2:尚可，3:好，4:很好，5:非常好
        public Float product_score;//	产品/食品口味评价单项分，精确到小数点后一位（十分制）
        public Float decoration_score;//	环境评价单项分，精确到小数点后一位（十分制）
        public Float service_score;//	服务评价单项分，精确到小数点后一位（十分制）

        /**
         * 从一个xmltxt中得到当前信息的程序
         * @param element
         * @throws IllegalAccessException
         */
        public void dealelement(Element element) {
            Field[] fields = this.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                String type = f.getGenericType().toString();
                if (type.equals("class java.lang.String")) {
                    Element temp = element.getElementsByTag(f.getName()).first();
                    if (temp != null) {
                        try {
                            f.set(this, temp.text());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (type.equals("class java.lang.Integer")) {
                    Element temp = element.getElementsByTag(f.getName()).first();
                    if (temp != null) {
                        int txt = Integer.parseInt(temp.text());
                        try {
                            f.set(this, txt);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (type.equals("class java.lang.Float")) {
                    Element temp = element.getElementsByTag(f.getName()).first();
                    if (temp != null) {
                        Float txt = Float.parseFloat(temp.text());
                        try {
                            f.set(this, txt);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (type.equals("java.util.List<java.lang.String>")) {
                    Elements temp = element.getElementsByTag(f.getName());
                    if (temp.size() > 0) {
                        List<String> list = new ArrayList<>();
                        for (Element ele : temp) {
                            list.add(ele.text());
                        }
                        try {
                            f.set(this, list);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    /**
     *
     * @param xmlcontent
     * @return
     * @throws IllegalAccessException
     */
    public void dealxml(String xmlcontent)  {
        org.jsoup.nodes.Document document= Jsoup.parse(xmlcontent);
        List<DianPingResult> mylist=new ArrayList<>();
        this.status=document.getElementsByTag("status").first().text();
        if(this.status.equals("OK"))
        {
            String counts = document.getElementsByTag("count").first().text();
            this.count = Integer.parseInt(counts);
            Element total=document.getElementsByTag("total_count").first();
            if(total!=null)
            {
                String total_count = total.text();
                this.total_count = Integer.parseInt(total_count);
            }
            Elements elements = document.getElementsByTag("business");
            for (Element XMLDocument : elements) {
                BNdetail bNdetail = new BNdetail();
                bNdetail.dealelement(XMLDocument);
                this.bNdetails.add(bNdetail);
            }
        }
        else
        {
//            IsResultOk
        }
    }
}
