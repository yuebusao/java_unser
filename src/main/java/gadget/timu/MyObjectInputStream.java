package gadget.timu;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;

public class MyObjectInputStream extends ObjectInputStream {
    private static ArrayList<String> blackList = new ArrayList();

    public MyObjectInputStream(InputStream inputStream) throws Exception {
        super(inputStream);
    }

    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass readDesc = super.readClassDescriptor();

        for(int i = 0; i < 4; ++i) {
            if (readDesc.getName().contains((CharSequence)blackList.get(i))) {
                throw new InvalidClassException("bad hacker!");
            }
        }

        return readDesc;
    }

    static {
        blackList.add("javax.management.BadAttributeValueExpException");
        blackList.add("com.sun.syndication.feed.impl.ToStringBean");
        blackList.add("java.security.SignedObject");
        blackList.add("com.sun.rowset.JdbcRowSetImpl");
    }
}

