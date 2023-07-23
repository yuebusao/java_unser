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

    public static Object getFuckField(Object object, String fieldName) {
        Field declaredField;
        Class clazz = object.getClass();
        while (clazz != Object.class) {
            try {
                declaredField = clazz.getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                return declaredField.get(object);
            } catch (Exception e) {
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }


    public static Object createWithoutConstructor(Class cls) throws InstantiationException, IllegalAccessException {
        return cls.newInstance();
    }
}
