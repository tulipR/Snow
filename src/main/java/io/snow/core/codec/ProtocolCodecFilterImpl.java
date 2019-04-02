package io.snow.core.codec;

/**
 * @author zhangliang 2019.03.05
 */
public class ProtocolCodecFilterImpl extends ProtocolCodecFilter
{

	private ProtocolDecode decode=new ProtocolDecodeImpl();

	private ProtocolEncode encode=new ProtocolEncodeImpl();

	@Override
	public ProtocolDecode getDecode()
	{
		return decode;
	}

	@Override
	public ProtocolEncode getEncode()
	{
		return encode;
	}

}
