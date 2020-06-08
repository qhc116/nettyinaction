package http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author 失了秩
 * @date 2020/6/6 9:19
 * @description
 */
public class HtpPipelineInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HtpPipelineInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        /**
         *  客户端：
         *  添加 HttpResponseDecoder 处理来自服务器的响应
         *  添加 HttpRequestEncoder 向服务器发送请求
         *
         *  服务器端：
         *  添加 HttpRequestEncoder 解析来自客户端的请求
         *  添加 HttpResponseEncoder 向客户端发送响应
         */

        if (client) {
            pipeline.addLast("decoder", new HttpResponseDecoder());
            pipeline.addLast("encoder", new HttpRequestEncoder());
        } else {
            pipeline.addLast("decoder", new HttpRequestEncoder());
            pipeline.addLast("encoder", new HttpResponseEncoder());
        }
    }
}
