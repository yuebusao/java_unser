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

    public static Field getFuckField(Class<?> clazz, String fieldName) {
        Field declaredField;
//        Class clazz = object.getClass();
        while (clazz != Object.class) {
            try {
                declaredField = clazz.getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                return declaredField;
            } catch (Exception e) {
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }
    public static Object createInstanceUnsafely(Class<?> clazz) throws Exception {
        Class unsafeClass    = Class.forName("sun.misc.Unsafe");
        Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        return getMethodAndInvoke(theUnsafeField.get(null), "allocateInstance", new Class[]{Class.class}, new Object[]{clazz});
    }
    public static Object getMethodAndInvoke(Object obj, String methodName, Class[] parameterClass, Object[] parameters) {
        try {
            java.lang.reflect.Method method = getMethodByClass(obj.getClass(), methodName, parameterClass);
            if (method != null)
                return method.invoke(obj, parameters);
        } catch (Exception ignored) {
        }
        return null;
    }
    public static java.lang.reflect.Method getMethodByClass(Class cs, String methodName, Class[] parameters) {
        java.lang.reflect.Method method = null;
        while (cs != null) {
            try {
                method = cs.getDeclaredMethod(methodName, parameters);
                method.setAccessible(true);
                cs = null;
            } catch (Exception e) {
                cs = cs.getSuperclass();
            }
        }
        return method;
    }


    public static Object createWithoutConstructor(Class cls) throws InstantiationException, IllegalAccessException {
        return cls.newInstance();
    }
}
