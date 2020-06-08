package echo2.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author 失了秩
 * @date 2020/5/31 15:27
 * @description
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        EchoServer echoServer = new EchoServer(2334);
        echoServer.start();
    }

    private void start() throws InterruptedException {
        final EchoServiceHandler serviceHandler = new EchoServiceHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap sb = new ServerBootstrap();
        sb.group(group)
                //指定使用NIO传输Channel
                .channel(NioServerSocketChannel.class)
                //指定监听的端口
                .localAddress(new InetSocketAddress(port))
                //添加一个EchoServiceHandler到子Channel的ChannelPipeLine
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(serviceHandler);
                    }
                });
        try {
            //异步地绑定服务器，调用sync方法阻塞等待直到绑定完成
            ChannelFuture cf = sb.bind().sync();
            //获取Channel的CloseFuture并且阻塞当前线程，直到它完成
            cf.channel().closeFuture().sync();
        } finally {
            //关闭EventLoop，释放所有资源
            group.shutdownGracefully().sync();
        }
    }
}