package keepalive;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.internal.ChannelUtils;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;


/**
 * @author 失了秩
 * @date 2020/6/6 13:46
 * @description
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // IdleHandler 将在被被触发时发送一个 IdleStateEvent 事件
        pipeline.addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));
        // 这个事件将会在 HeartbeatHandler 中的 userEventTriggered() 捕获
        pipeline.addLast(new HeartbeatHandler());
    }
}
