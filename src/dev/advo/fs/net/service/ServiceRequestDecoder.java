package dev.advo.fs.net.service;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public final class ServiceRequestDecoder extends FrameDecoder {
	
	/**
	 * Creates the decoder, enabling the 'unfold' mechanism.
	 */
	public ServiceRequestDecoder() {
		super(true);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel c, ChannelBuffer buf) throws Exception {
		if (buf.readable()) {
			ServiceRequest request = new ServiceRequest(buf.readUnsignedByte());
			
			//System.out.println("initial request " + request.getId());
			//if not 0. oof.
			
			ChannelPipeline pipeline = ctx.getPipeline();
			pipeline.remove(this);
			
			if (buf.readable()) {
				System.out.println("we should not be here, client should request 1 byte. It requested "+ buf.readableBytes());
				return new Object[] { request, buf.readBytes(buf.readableBytes()) };
			} else {
				return request;
			}
		}
		return null;
	}

}
