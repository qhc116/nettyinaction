package handlertsttool;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author 失了秩
 * @date 2020/6/5 13:02
 * @description 入站功能测试
 *              这个Handler实现的功能为
 *              给定足够的数据时（>=3byte）,将产生一个或多个固定大小（3byte）的帧
 *              如果数据不足（<3byte），等待下一个数据块的到来，直到数据量满足条件（>3byte)
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
    private final int frameLength = 3;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() >= frameLength) {
            ByteBuf buf = in.readBytes(frameLength);
            byte[] arr = new byte[3];
            ByteBuf bytes = buf.getBytes(0, arr);

            // List<Object> out 表示已经被解码的列表
            out.add(buf);
        }
    }
}
