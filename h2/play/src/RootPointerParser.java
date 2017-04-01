import org.h2.mvstore.DataUtils;
import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;
import org.h2.mvstore.MVStoreTool;

/**
 * Created by wanghao on 2017/3/7.
 *
 * mvstore root pointer parser
 *
 */
public class RootPointerParser {
    public static void main(String[] args) throws Exception {
        long t = DataUtils.parseHexLong("16AF40000027C5");

        long type = (t & 0x01L);
        long length = (t>>1) & 0x001fL;
        long offset = (t>>6) & 0x00ffffffffL;
        long chunkid = (t>>38) & 0x003ffffffL;

        String s = "type:   ";
        if(type == 0){
            s = s + "leaf\n";
        }else{
            s = s + "internal\n";
        }

        s = s + "length:    " + length + "\n";
        s = s + "offset:    " + String.format("0x%x",offset) + "\n";
        s = s + "chunkid:   " + String.format("0x%x",chunkid) + "\n";
        System.out.println(s);
    }
}
