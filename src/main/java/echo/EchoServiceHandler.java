package echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author 失了秩
 * @date 2020/5/31 15:05
 * @description
 */
@ChannelHandler.Sharable    //表示一个ChannelHandler可以被多个Channel安全地共享
public class EchoServiceHandler extends ChannelInboundHandlerAdapter {
    //发生读事件
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //将收到的消息输出到控制台
        ByteBuf in = (ByteBuf) msg;
        System.out.println("client:" + in.toString(CharsetUtil.UTF_8));

        //将收到的消息写给发送者，但是不冲刷出站消息，因为冲刷通常留给下一个步骤进行
        ctx.write(in);
        System.out.println("server: Read");
    }

    //读事件处理完成时
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将未决事件冲刷到远程节点，然后关闭这个通道
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    //异常事件回调
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常并关闭Channel
        cause.printStackTrace();
        ctx.close();
    }
}
