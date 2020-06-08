package proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author 失了秩
 * @date 2020/6/4 23:30
 * @description 作为代理服务器需要调用第三方的服务器
 *              所以需要Bootstrap引导器
 *              常规的Bootstrap引导器每次都要创建一个新的EventLoop（即一个线程），十分耗费资源
 *              所以可以在第一次创建EventLoop后，将其与Bootstrap保存（group）
 *              下次再新建Channel的时候就不用重新创建EventLoop了
 */
public class ProxyServer {
    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        //创建连接第三方的bootstrap
                        Bootstrap b = new Bootstrap();
                        b.group(ctx.channel().eventLoop())  //远端访问成功后，group创建的EventLoop
                                .channel(NioSocketChannel.class)
                                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                        System.out.println("get response from remote server");
                                    }
                                });
                        b.connect(new InetSocketAddress("www.remoteServer.com", 2333));
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        //获取远端服务器回应之后输出内容
                        System.out.println(msg);
                    }
                });

        ChannelFuture sync = serverBootstrap.bind(new InetSocketAddress(2333)).sync();
        sync.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("服务器启动成功");
                } else {
                    System.out.println("服务器启动失败");
                    future.cause().printStackTrace();
                }
            }
        });
    }
}
