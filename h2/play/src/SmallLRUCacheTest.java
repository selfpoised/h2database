import org.h2.util.SmallLRUCache;

import java.util.Iterator;

/**
 * Created by wanghao on 2017/4/1.
 */
public class SmallLRUCacheTest {
    public static void main(String[] args) throws Exception {
        SmallLRUCache<Integer,String> m = SmallLRUCache.newInstance(3);
        m.put(1,"1");
        m.put(2,"2");
        m.put(3,"3");
        m.put(2,"4");

        Iterator<Integer> it = m.keySet().iterator();
        while(it.hasNext()){
            Integer i = it.next();
            System.out.println(i);
        }
    }
}
