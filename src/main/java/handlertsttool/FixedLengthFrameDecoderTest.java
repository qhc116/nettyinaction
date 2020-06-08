package handlertsttool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author 失了秩
 * @date 2020/6/5 13:19
 * @description 测试入站消息
 */
public class FixedLengthFrameDecoderTest {
    @Test
    public void testFixedLengthFrameDecoder() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new FixedLengthFrameDecoder());
        // write bytes
        assertTrue(embeddedChannel.writeInbound(input.retain()));
        assertTrue(embeddedChannel.finish());

        // read bytes
        ByteBuf read;

        read = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();
    }

    @Test
    public void testFixedLengthFrameDecoder2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new FixedLengthFrameDecoder());

        assertFalse(embeddedChannel.writeInbound(input.readBytes(2)));
        ByteBuf buf1 = input.readBytes(7);
        System.out.println(buf1.hasArray());
        assertTrue(embeddedChannel.writeInbound(buf1));

        assertTrue(embeddedChannel.finish());
        ByteBuf read;
        read = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(embeddedChannel.readInbound());
        buf.release();
    }
}
