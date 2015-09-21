/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpayg.cgbBank.cgbInitializerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.apache.camel.component.netty4.NettyConsumer;
import org.apache.camel.component.netty4.ServerInitializerFactory;
import org.apache.camel.component.netty4.handlers.ServerChannelHandler;

/**
 *
 * @author Administrator
 */
public class CGBServerInitalFactory extends ServerInitializerFactory {

    @Override
    public ServerInitializerFactory createPipelineFactory(NettyConsumer nc) {
        return new CGBServerInitalFactory(nc);
    }

    @Override
    protected void initChannel(Channel c) throws Exception {
        ChannelPipeline channelPipeline = c.pipeline();
        channelPipeline.addLast("encoder-SD", new StringEncoder(CharsetUtil.US_ASCII));
        channelPipeline.addLast("decoder-DELIM", new DelimiterBasedFrameDecoder(maxLineSize, true, Delimiters.lineDelimiter()));
        channelPipeline.addLast("decoder-SD", new StringDecoder(CharsetUtil.US_ASCII));
        // here we add the default Camel ServerChannelHandler for the consumer, to allow Camel to route the message etc.
        channelPipeline.addLast("handler", new ServerChannelHandler(consumer));
    }
    private final int maxLineSize = 1024;
    private NettyConsumer consumer;

    public CGBServerInitalFactory() {
    }

    public CGBServerInitalFactory(NettyConsumer nc) {
        this.consumer = nc;

    }

}
