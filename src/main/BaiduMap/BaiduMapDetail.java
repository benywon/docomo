package main.BaiduMap;

import main.Crawlerbases;
import main.Filebases;
import main.Myconfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benywon on 2015/7/19.
 */
public class BaiduMapDetail {
    public void setUid(String uid) {
        this.uid = uid;
    }
    public List<String> uids=new ArrayList<>();
    public String uid;

    public String getdetail()
    {
        String content="";
        String url=BaiduMap.detailapiurl;
        String uidstr="uids=";
        for(String uid:this.uids)
        {
            uidstr+=uid+",";
        }
        url+=uidstr.replaceAll(",$","");
        url+="&";
        url+="ak="+BaiduMap.Baiduak+"&";
        url+="scope=2";
        try {
            content= Crawlerbases.spiderhtml(url, "utf-8");
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void main(String[] args) {
        BaiduMapDetail baiduMapDetail=new BaiduMapDetail();
        baiduMapDetail.uids.add("45ef9a2b5508cbfda2d230e6");
        baiduMapDetail.uids.add("8c3adf1c6f135ad74d554726");
        String content=baiduMapDetail.getdetail();
        Filebases.Write2File(content, Myconfig.Getconfiginfo("tempxmlfile"), false);
        System.out.println(content);
    }
}
