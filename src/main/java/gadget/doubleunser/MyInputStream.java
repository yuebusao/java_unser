package gadget.doubleunser;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class MyInputStream extends ObjectInputStream {
    private final List<Object> BLACKLIST = Arrays.asList("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl", "com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter", "com.sun.syndication.feed.impl.ObjectBean", "import com.sun.syndication.feed.impl.ToStringBean");

    public MyInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    protected Class<?> resolveClass(ObjectStreamClass cls) throws ClassNotFoundException, IOException {
        if (this.BLACKLIST.contains(cls.getName())) {
            throw new InvalidClassException("The class " + cls.getName() + " is on the blacklist");
        } else {
            return super.resolveClass(cls);
        }
    }
}
