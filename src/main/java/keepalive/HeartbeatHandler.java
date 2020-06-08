package keepalive;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * @author 失了秩
 * @date 2020/6/6 13:58
 * @description
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
    private static final ByteBuf HEART_SEQUENCE = Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.ISO_8859_1);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 捕获事件，判断一下是不是心跳事件
        if (evt instanceof IdleStateEvent) {
            ctx.writeAndFlush(HEART_SEQUENCE.duplicate())
                    // 如果发送失败则关闭连接
                    .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
