package main.DealQuestion;

import main.Filebases;
import main.Myconfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by benywon on 2015/8/10.
 */
public class QuestionFileRelate {
    public static Map<String,String> QuestionAnswerPair=new HashMap<>();
    public void tranferquestionfile()
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
                QuestionAnswerPair.put(strings[i],answer);
            }
        }
    }

    public static void main(String[] args) {
        QuestionFileRelate questionFileRelate=new QuestionFileRelate();
        questionFileRelate.tranferquestionfile();
        System.out.println("hfdio");
    }

}
