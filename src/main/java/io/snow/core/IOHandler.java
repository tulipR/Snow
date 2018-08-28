package io.snow.core;


public interface IOHandler{

	void connect(AioConnect connect);
	void read();
	void write();
}
