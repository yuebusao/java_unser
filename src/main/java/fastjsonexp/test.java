package fastjsonexp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import entity.User;

public class test {
    public static void main(String[] args) {
        User user = new User("张三",18,"学习");

        String s1 = JSON.toJSONString(user);
        String s2 = JSON.toJSONString(user, SerializerFeature.WriteClassName);
        System.out.println(s1);
        System.out.println(s2);
        System.out.println("-----------------------------------------------------");
        Object parse = JSON.parse(s2);
        System.out.println(parse);
        System.out.println(parse.getClass().getName());
        System.out.println("-----------------------------------------------------");
        Object parse1 = JSON.parseObject(s2);
        System.out.println(parse1);
        System.out.println(parse1.getClass().getName());
        System.out.println("-----------------------------------------------------");
        Object parse2 = JSON.parseObject(s2,Object.class);
        System.out.println(parse2);
        System.out.println(parse2.getClass().getName());
    }
}
