package io.snow.core.aio;

public interface IOHandler{

	void connect(AioConnect connect);
	void read();
	void write();
}
