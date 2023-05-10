import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String... args) throws IOException {
        ByteBuffer b = ByteBuffer.allocate(5)
                .put((byte) 0x01)
                .putShort((short) 254)
                .putShort((short) 65530);
        byte[] barray = b.array();
        System.out.println(barray);
    }

}
