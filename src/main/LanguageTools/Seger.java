package main.LanguageTools;

import main.DealQuestion.QuestionFileRelate;
import main.Myconfig;
import org.ansj.domain.Nature;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by benywon on 2015/8/11.
 */
public class Seger {
    /**设置我们的分词模式 总共有3种
     * BaseAnalysis 基本分词    1
     * ToAnalysis 精准分词 能够识别人名   2
     * NlpAnalysis nlp分词 能够识别出来新的名词 但是缺点就是占用时间太长 需要load crf模型   3
     */
    private static int SegModel=Integer.parseInt(Myconfig.Getconfiginfo("WordSegMode"));
    public static void SetSegMode(int mode)
    {
        SegModel=mode;
    }
    /**
     * 我们的分词工具
     * @param words 传进来的一个words
     * @return
     */
    public static String[] SegWords(String words) {
        List<String> listwords = new ArrayList<>();
        List<Term> list = new ArrayList<>();
        if (SegModel == 1) {
            list = BaseAnalysis.parse(words);
        } else if (SegModel == 2)
        {
            list = ToAnalysis.parse(words);
        }
        else if(SegModel==3)
        {
            list = NlpAnalysis.parse(words);
        }
        else
        {
            list = NlpAnalysis.parse(words);
        }
        for(Term term:list)
        {
            listwords.add(term.getName());
        }
        String[] result = listwords.toArray(new String[listwords.size()]);
        return  result;
    }
    public static List<Term> SegWordsReturnTerm(String words)
    {
        List<Term> list = new ArrayList<>();
        if (SegModel == 1) {
            list = BaseAnalysis.parse(words);
        } else if (SegModel == 2)
        {
            list = ToAnalysis.parse(words);
        }
        else if(SegModel==3)
        {
            list = NlpAnalysis.parse(words);
        }
        else
        {
            list = NlpAnalysis.parse(words);
        }

        return  list;
    }


    public static void main(String[] args) {
        String c="王府井近的游乐场有哪些?";
        List<Term> list=SegWordsReturnTerm(c);
        System.out.println(list);
        QuestionFileRelate questionFileRelate=new QuestionFileRelate();
        for(Map.Entry<String, String> entry: QuestionFileRelate.QuestionAnswerPairType1.entrySet())
        {
            c=entry.getKey();
            list=SegWordsReturnTerm(c);
            String tt="";
            String ts="";
            boolean isadd=false;
            for(Term term:list)
            {
                Nature nature=term.natrue();
                if(nature.natureStr.startsWith("n")||nature.natureStr.contains("userDefine"))
                {
                    isadd=true;
                    tt+=term.getName();
                    ts+=nature.natureStr;
                }
                else if(isadd)
                {
                    break;
                }
            }
            if(tt.length()>1) {
                System.out.println(tt+"\t"+ts);
            }
        }

    }
}
    //    public static void main(String[] args) throws IOException {
//        String txt = "我想去北京欢乐谷，京华时报1月23日报道 昨天，受一股来自中西伯利亚的强冷空气影响，本市出现大风降温天气，白天最高气温只有零下7摄氏度，同时伴有6到7级的偏北风。";
//        String   dictfilepath="L:\\program\\cip\\DOCOMO\\program\\resource\\dictionary\\allSpotNameFile.dic";
//        File dictfile=new File(dictfilepath);
//        Dictionary dictionary=Dictionary.getInstance(dictfile);
//        Seg seg=new MaxWordSeg(dictionary);
//        StringBuilder sb = new StringBuilder();
//        Reader reader=(Reader)(new StringReader(txt));
//        MMSeg mmSeg = new MMSeg(reader, seg);
//        Word word = null;
//
//        for(boolean first = true; (word = mmSeg.next()) != null; first = false)
//        {
//            if (!first)
//            {
//                sb.append("|");
//            }
//            String w = word.getString();
//            sb.append(w);
//        }
//        String  out=sb.toString();
//        System.out.println(out);
//
//    }
//public static void main(String[] args) throws IOException {
//    String txt = "中国国家博物馆在哪里？";
//
//        Reader reader=(Reader)(new StringReader(txt));
//        IKSegmentation ikSegmentation=new IKSegmentation(reader,true);
//        Lexeme lem=ikSegmentation.next();
//        String sentence="";
//        while(lem!=null)
//        {
//            String tt=lem.getLexemeText();
//            sentence+=tt+"|";
//            lem=ikSegmentation.next();
//        }
//    System.out.println(sentence);
//}
//    public static void main(String[] args) {
//
//        String words = "离我最近的游乐场在哪?";
//        List<Term> list=NlpAnalysis.parse(words);
//        for(Term term:list)
//        {
//            System.out.println(term.getName());
//        }
//        System.out.println(NlpAnalysis.parse(words));
//
//    }
