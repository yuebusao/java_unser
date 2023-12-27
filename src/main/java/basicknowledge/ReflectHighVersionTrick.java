package basicknowledge;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectHighVersionTrick {
    public class FinalTest {
        private static final String secret = "Squirt1e is a cute girl.";
    }
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        reflectFinalAboveJdk11();
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
}
