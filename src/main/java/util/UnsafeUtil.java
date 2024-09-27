package util;

import gadget.memshell.SpringEcho;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UnsafeUtil{

    private static Unsafe unsafe;

    static{
        try{
            final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        unsafe = (Unsafe) unsafeField.get(null);
        return unsafe;
    }
    public static void patchModule(Class obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class unsafeClass = Class.forName("sun.misc.Unsafe");
        Field field = unsafeClass.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
//        Module baseModule = Object.class.getModule();
        Class currentClass = obj;
        long offset = unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
//        unsafe.getAndSetObject(currentClass, offset, baseModule);
    }

    public static void setFinalStatic(Field field, Object value) {
        try {
            Object fieldBase = unsafe.staticFieldBase(field);
            long fieldOffset = unsafe.staticFieldOffset(field);
            unsafe.putObject(fieldBase, fieldOffset, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}