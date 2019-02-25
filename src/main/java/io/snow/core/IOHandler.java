package io.snow.core;

import io.snow.core.aio.AioConnect;

public interface IOHandler{

	void connect(AioConnect connect);
	void read();
	void write();
}
