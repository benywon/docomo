package main.BaiduMap;

import org.jsoup.nodes.Element;

import java.lang.reflect.Field;

/**
 * Created by benywon on 2015/7/19.
 */
public class BaiduMapDetail {
    public	String		name	;//	poi名称
    public	Float		lat	;//	纬度值
    public	Float		lng	;//	经度值
    public	String		address	;//	poi地址信息
    public	String		telephone	;//	poi电话信息
    public	String		uid	;//	poi的唯一标示
    public	Integer		distance	;//	距离中心点的距离
    public	String		type	;//	所属分类，如’hotel’、’cater’。
    public	String		tag	;//	标签
    public	String		detail_url	;//	poi的详情页
    public	String		price	;//	poi商户的价格
    public	String		shop_hours	;//	营业时间
    public	String		overall_rating	;//	总体评分
    public	String		taste_rating	;//	口味评分
    public	String		service_rating	;//	服务评分
    public	String		environment_rating	;//	环境评分
    public	String		facility_rating	;//	星级（设备）评分
    public	String		hygiene_rating	;//	卫生评分
    public	String		technology_rating	;//	技术评分
    public	String		image_num	;//	图片数
    public	Integer		groupon_num	;//	团购数
    public	Integer		discount_num	;//	优惠数
    public	String		comment_num	;//	评论数
    public	String		favorite_num	;//	收藏数
    public	String		checkin_num	;//	签到数
    public	String		alias	;//	别称
    public	String		scope_grade	;//	等级
    public	String		scope_type	;//	等级
    public	String		description	;//	介绍

    public BaiduMapDetail(String price, String name, Float lat, Float lng, String address, String telephone, String uid, Integer distance, String type, String tag, String detail_url, String shop_hours, String overall_rating, String taste_rating, String service_rating, String environment_rating, String facility_rating, String hygiene_rating, String technology_rating, String image_num, Integer groupon_num, Integer discount_num, String comment_num, String favorite_num, String checkin_num, String alias, String scope_grade, String scope_type, String description) {
        this.price = price;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.telephone = telephone;
        this.uid = uid;
        this.distance = distance;
        this.type = type;
        this.tag = tag;
        this.detail_url = detail_url;
        this.shop_hours = shop_hours;
        this.overall_rating = overall_rating;
        this.taste_rating = taste_rating;
        this.service_rating = service_rating;
        this.environment_rating = environment_rating;
        this.facility_rating = facility_rating;
        this.hygiene_rating = hygiene_rating;
        this.technology_rating = technology_rating;
        this.image_num = image_num;
        this.groupon_num = groupon_num;
        this.discount_num = discount_num;
        this.comment_num = comment_num;
        this.favorite_num = favorite_num;
        this.checkin_num = checkin_num;
        this.alias = alias;
        this.scope_grade = scope_grade;
        this.scope_type = scope_type;
        this.description = description;
    }

    public BaiduMapDetail() {
    }

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
            }
        }
    }

}
