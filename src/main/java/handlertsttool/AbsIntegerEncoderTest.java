package handlertsttool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author 失了秩
 * @date 2020/6/5 16:03
 * @description
 */
public class AbsIntegerEncoderTest {
    @Test
    public void testAbsIntegerEncoder() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 10; i++) {
            buf.writeInt(i * -1);
        }

        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());

        assertTrue(channel.writeOutbound(buf));
        assertTrue(channel.finish());

        for (int i = 0; i < 10; i++) {
            assertEquals((Integer) i, channel.readOutbound());
        }
    }
}
