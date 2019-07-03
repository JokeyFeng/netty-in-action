package handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author Zhengjingfeng
 * created 2019/7/3 10:54
 * comment
 */
@ChannelHandler.Sharable
public class HttpClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 服务器的连接被建立后调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("嗨，我来了", CharsetUtil.UTF_8));
    }

    /**
     * 数据后从服务器接收到调用
     *
     * @param ctx
     * @param in
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println("客户端收到消息===>>" + in.toString(CharsetUtil.UTF_8));
    }

    /**
     * 捕获一个异常时调用
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
