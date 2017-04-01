import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;

public class MVStoreFileChangeDemo {
    public static void main(String[] args) throws Exception {
        MVStore s = MVStore.open("file_change_demo");
        MVMap<Integer, String> map = s.openMap("data");
        for (int i = 0; i < 400; i++) {
            map.put(i, "Hello");
        }
        s.commit();
        for (int i = 0; i < 100; i++) {
            map.put(i, "Hi");
        }
        s.commit();

        for (int i = 0; i < 1; i++) {
            map.put(i, "lqy");
        }
        s.commit();

        s.close();
    }
}
