import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import utils.CryptUtil;


public class CryptUtilTest {

    @Test
    @DisplayName("CryptUtil add test")
    public void testadd(){
        double a = 12.56;
        double b = 23.57;
        Assertions.assertEquals(36.13, CryptUtil.add(a,b), 0.00000001);
    }

    @Test
    @DisplayName("Encrypt this string method test")
    public void testencryptThisString(){
        Assertions.assertNotNull(CryptUtil.encryptThisString("dasdasdasdas"));
    }
}
