package main;

/**
 * Created by benywon on 2015/7/19.
 */
public class ErrorAndWarn {
    public int erroandwarn=-1;
    public int getErroandwarn() {
        return erroandwarn;
    }

    public void setErroandwarn(int erroandwarn) {
        this.erroandwarn = erroandwarn;
    }

    /**
     * 错误代码和类别
     */
    public final static int 成功=0;
    public final static int 在找最近的时候没有目标=1;//就是最近的公园 没有识别出公园 这一类的

}
