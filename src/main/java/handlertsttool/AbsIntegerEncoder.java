package handlertsttool;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author 失了秩
 * @date 2020/6/5 15:52
 * @description 用于将复数转换为正数
 *              持有 AbsIntegerEncoder 的 EmbeddedChannel 将会以4字节的负整数的形式输出出站数据
 *              编码器将从传入的 ByteBuf 中读取每个负整数，并取得其绝对值
 *              编码器将转换后的绝对值写入 ChannelPipeline 中
 */

public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List out) throws Exception {
        while (msg.readableBytes() >= 4) {
            int value = Math.abs(msg.readInt());
            out.add(value);
        }
    }
}
