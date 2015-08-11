package main.DealQuestion;

import main.Filebases;
import main.Myconfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将问题的文件转化为我们的map 最后直接进行处理的函数
 * Created by benywon on 2015/8/10.
 */
public class QuestionFileRelate {
    public static Map<String,String> QuestionAnswerPairType1=new HashMap<>();

    public QuestionFileRelate() {
        this._tranferquestionfile();
    }

    private void _tranferquestionfile()
    {
        String originalfilename= Myconfig.Getconfiginfo("QuestionFileType1");
        List<String> list= Filebases.GetListFromFile(originalfilename);
        if(list.size()%2!=0)
        {
            return;
        }
        else
        {
            String[] strings= (String[]) list.toArray();
            int len=strings.length;
            for (int i = 0; i <len ; i+=2)
            {
                String answer=strings[i+1].replaceAll("答：","");
                QuestionAnswerPairType1.put(strings[i],answer);
            }
        }
    }

}
