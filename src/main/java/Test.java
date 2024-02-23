import org.python.core.PyObject;
import sun.misc.Unsafe;
import util.ReflectionUtils;

import java.lang.reflect.Field;

public class Test {
    String a;
    static {
        System.out.println(1);
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        PyObject builtinFunctions = (PyObject) unsafe.allocateInstance(Class.forName("org.python.core.BuiltinFunctions"));

        Field index = ReflectionUtils.getFuckField(builtinFunctions.getClass(),"index");
        System.out.println(index);
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
