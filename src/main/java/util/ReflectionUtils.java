package util;

import java.lang.reflect.Field;

public class ReflectionUtils {
    public static void setFieldValue(Object obj1,String str,Object obj2) throws NoSuchFieldException, IllegalAccessException {
        Field field2 = obj1.getClass().getDeclaredField(str);//获取PriorityQueue的comparator字段
        field2.setAccessible(true);//暴力反射
        field2.set(obj1, obj2);//设置queue的comparator字段值为comparator
    }
    public static Field getField(final Class<?> clazz, final String fieldName) throws NoSuchFieldException {
        Field field = null;
        field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }


    public static Object createWithoutConstructor(Class cls) throws InstantiationException, IllegalAccessException {
        return cls.newInstance();
    }
}
