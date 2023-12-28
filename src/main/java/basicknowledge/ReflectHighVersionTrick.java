package basicknowledge;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Base64;

public class ReflectHighVersionTrick {
    public class FinalTest {
        private static final String secret = "Squirt1e is a cute girl.";
    }
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        reflectFinalAboveJdk17();
    }
    //jdk版本小于等于11
    public static void reflectFinalUnderJdk11 ()  throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException{
        Field modifierField = Class.forName("java.lang.reflect.Field").getDeclaredField("modifiers");
        modifierField.setAccessible(true);
        Field secret = FinalTest.class.getDeclaredField("secret");
        secret.setAccessible(true);
        modifierField.setInt(secret, secret.getModifiers() & ~Modifier.FINAL);
        secret.set(null, "Squirt1e is a boy.");
        System.out.println(secret.get(null));  // G0T_Y0U
    }
    public static void reflectFinalAboveJdk11() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Method getDeclaredFields0 = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
        getDeclaredFields0.setAccessible(true);
        Field[] fields = (Field[]) getDeclaredFields0.invoke(Field.class, false);
        Field modifierField = null;
        for (Field f : fields) {
            if ("modifiers".equals(f.getName())) {
                modifierField = f;
                break;
            }
        }
        modifierField.setAccessible(true);
        Field secret = FinalTest.class.getDeclaredField("secret");
        secret.setAccessible(true);
        modifierField.setInt(secret, secret.getModifiers() & ~Modifier.FINAL);
        secret.set(null, "Squirt1e is a boy.");
        System.out.println(secret.get(null));
    }

    public static void reflectFinalAboveJdk17() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException {


        String evilClassBase64 = "yv66vgAAADQAIwoACQATCgAUABUIABYKABQAFwcAGAcAGQoABgAaBwAbBwAcAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEACDxjbGluaXQ+AQANU3RhY2tNYXBUYWJsZQcAGAEAClNvdXJjZUZpbGUBAAlFdmlsLmphdmEMAAoACwcAHQwAHgAfAQAEY2FsYwwAIAAhAQATamF2YS9pby9JT0V4Y2VwdGlvbgEAGmphdmEvbGFuZy9SdW50aW1lRXhjZXB0aW9uDAAKACIBAARFdmlsAQAQamF2YS9sYW5nL09iamVjdAEAEWphdmEvbGFuZy9SdW50aW1lAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsBABgoTGphdmEvbGFuZy9UaHJvd2FibGU7KVYAIQAIAAkAAAAAAAIAAQAKAAsAAQAMAAAAHQABAAEAAAAFKrcAAbEAAAABAA0AAAAGAAEAAAADAAgADgALAAEADAAAAFQAAwABAAAAF7gAAhIDtgAEV6cADUu7AAZZKrcAB7+xAAEAAAAJAAwABQACAA0AAAAWAAUAAAAGAAkACQAMAAcADQAIABYACgAPAAAABwACTAcAEAkAAQARAAAAAgAS";
        byte[] bytes = Base64.getDecoder().decode(evilClassBase64);

        Class unsafeClass = Class.forName("sun.misc.Unsafe");
        Field field = unsafeClass.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
//        Module baseModule = Object.class.getModule();
        Class currentClass = ReflectHighVersionTrick.class;
        long offset = unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
//        unsafe.putObject(currentClass, offset, baseModule);

        // or
        //unsafe.getAndSetObject(currentClass, offset, baseModule);

        Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        method.setAccessible(true);
        ((Class)method.invoke(ClassLoader.getSystemClassLoader(), "Evil", bytes, 0, bytes.length)).newInstance();
    }


}
