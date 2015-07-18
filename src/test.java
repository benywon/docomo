/**
 * Created by benywon on 2015/7/18.
 */
public class test {
    public static void main(String[] args) {
        String txt="我们都是共产党88iudfhi好的";
        String jj=txt.substring(txt.lastIndexOf("共产党")+"共产党".length());
        System.out.println(jj);
    }
}
