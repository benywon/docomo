package main;

import java.util.*;

/**
 * Created by benywon on 2015/7/16.
 */
public class BaseMethod {
    /**
     * 从一个map中得到指定值的key的集合
     * @param map
     * @param value
     * @return
     */
    public static List<Object> Getkeyfromvalue(Map map,Object value)
    {
        List<Object> list=new ArrayList<>();
        for(Object entryobj:map.entrySet())
        {
            Map.Entry<Object, Object> entry= (Map.Entry<Object, Object>) entryobj;
            Object obj=entry.getValue();
            if(obj.equals(value))
            {
                list.add(entry.getKey());
            }
        }
        return list;
    }
    /**
     * 从list到map
     */
    public static Map<Object,Integer> List2Map(List list)
    {
        Map<Object,Integer> map=new HashMap<>();
        for(Object obj:list)
        {
            if(map.containsKey(obj))
            {
                map.put(obj,map.get(obj)+1);
            }
            else
            {
                map.put(obj, 1);
            }
        }
        return map;
    }
    /**
     * 按照第二个值来表明是升序还是降序
     * @param map
     * @param accent false表示从大到小 true表示从小到大
     * @return
     */
    public static Map sortByValue(Map map,boolean accent)
    {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                if(accent) {
                    return ((Comparable) ((Map.Entry) (o1)).getValue())
                            .compareTo(((Map.Entry) (o2)).getValue());
                }
                else
                {
                    return ((Comparable) ((Map.Entry) (o2)).getValue())
                            .compareTo(((Map.Entry) (o1)).getValue());
                }

            }
        });
        Map result = new LinkedHashMap();

        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * 得到两个坐标之间的距离
     * @param f1
     * @param f2
     * @return
     */
    public static double getdistance(float[] f1,float[] f2)
    {
        double distance=0.0f;
        distance=Math.pow((f1[0] - f2[0]),2)+Math.pow((f1[1] - f2[1]),2);
        distance=Math.sqrt(distance);
        return distance;
    }
}
