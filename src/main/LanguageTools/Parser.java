package main.LanguageTools;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.*;
import main.Myconfig;

import java.util.Iterator;
import java.util.List;

/**
 * Created by benywon on 2015/8/11.
 */
public class Parser {
    private static String parserModel = Myconfig.Getconfiginfo("stanfordParserModelFile");
    private static LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
    private static TreebankLanguagePack tlp = lp.getOp().langpack();
    private static GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();

    /**
     * 打印出我们的句法树
     * @param s 输入的句子
     */
    public static void printparsetree(String s)
    {
        Tree parse = getParseTree(s);
        parse.pennPrint();
    }

    /**
     * 得到的我们的句法树
     * @param s 输入的句子
     * @return
     */
    public static Tree getParseTree(String s)
    {
        String[] sent=Seger.SegWords(s);
        List rawWords = Sentence.toCoreLabelList(sent);
        Tree parse = lp.apply(rawWords);
        return parse;
    }

    /**
     * 打印出我们的依存关系树
     * @param s 输入的句子
     */
    public static void printdependencytree(String s)
    {
        Tree parse=getParseTree(s);
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        List<TypedDependency> tdl = (List<TypedDependency>) gs.allTypedDependencies();
        System.out.println(tdl);
    }
    /**
     * 得到的我们的依存句法树
     * @param s 输入的句子
     * @return
     */
    public static List<TypedDependency> getDependencyList(String s)
    {
        Tree parse=getParseTree(s);
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        List<TypedDependency> tdl = (List<TypedDependency>) gs.allTypedDependencies();
        return  tdl;
    }

    public static void main(String[] args) {
        Tree parse1=Parser.getParseTree("有没有3D错觉艺术馆的联系电话");
        parse1.pennPrint();
        List<Tree> leaves = parse1.getLeaves();
        Iterator<Tree> it = leaves.iterator();
        while (it.hasNext()) {
            Tree leaf = it.next();
            String qq=leaf.nodeString();
            Tree start = leaf;
            start = start.parent(parse1);
            String tag = start.value().toString().trim();
            System.out.println(qq+tag);
        }
    }
}
