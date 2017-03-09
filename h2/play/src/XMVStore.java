package src;

import org.h2.mvstore.*;

/**
 * Created by wanghao on 2017/3/7.
 */
public class XMVStore {
    public static void main(String[] args) throws Exception {
        MVStore s = MVStore.open("mvstore_dump");

        MVMap<Integer, String> map = s.openMap("data");
        for (int i = 0; i < 400; i++) {
            map.put(i, "Hello");
        }
        s.commit();
        for (int i = 0; i < 100; i++) {
            map.put(i, "Hi");
        }
        s.commit();
        s.close();

        MVStoreTool.dump("mvstore_dump",true);
    }

    public static void main2(String[] args) throws Exception {
        // open the store (in-memory if fileName is null)
        MVStore s = MVStore.open("test11");

        MVMap<Integer, String> map = s.openMap("data");

// add some data
        map.put(1, "Hello");
        map.put(2, "World");

// get the current version, for later use
        long oldVersion = s.getCurrentVersion();

// from now on, the old version is read-only
        s.commit();



// more changes, in the new version
// changes can be rolled back if required
// changes always go into "head" (the newest version)
        map.put(1, "Hi");
        map.remove(2);

// access the old data (before the commit)
        MVMap<Integer, String> oldMap =
                map.openVersion(oldVersion);

// print the old version (can be done
// concurrently with further modifications)
// this will print "Hello" and "World":
        System.out.println(oldMap.get(1));
        System.out.println(oldMap.get(2));

// print the newest version ("Hi")
        System.out.println(map.get(1));

        // close the store (this will persist changes)
        s.close();


        MVStoreTool.dump("test11",false);
    }
}
