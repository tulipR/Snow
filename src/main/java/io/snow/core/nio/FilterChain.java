package io.snow.core.nio;

import io.snow.core.nio.IoFilter.NextFilter;

/**
 * 
 * @author zhangliang 2019.02.28
 *
 */
public class FilterChain {

	private final EntryImpl head;

	private final EntryImpl tail;

	private NioHandler handler;

	/**
	 * 
	 * @param handler 不能为空
	 */
	public FilterChain(NioHandler handler) {
		if (handler == null) {
			throw new NullPointerException("handle can not be null");
		}
		this.handler = handler;
		head = new EntryImpl(null, null, "head", new HeadFilter());
		tail = new EntryImpl(head, null, "tail", new TailFilter());
		head.nextEntry = tail;
	}

	private class EntryImpl {
		private EntryImpl prevEntry;

		private EntryImpl nextEntry;

		private final String name;

		private IoFilter filter;

		public EntryImpl getPrevEntry() {
			return prevEntry;
		}

		public EntryImpl getNextEntry() {
			return nextEntry;
		}

		public String getName() {
			return name;
		}

		public IoFilter getFilter() {
			return filter;
		}

		public NextFilter getNextFilter() {
			return nextFilter;
		}

		private final NextFilter nextFilter;

		private EntryImpl(EntryImpl prevEntry, EntryImpl nextEntry, String name, IoFilter filter) {
			if (filter == null) {
				throw new IllegalArgumentException("filter");
			}

			if (name == null) {
				throw new IllegalArgumentException("name");
			}

			this.prevEntry = prevEntry;
			this.nextEntry = nextEntry;
			this.name = name;
			this.filter = filter;
			this.nextFilter = new NextFilter() {

				@Override
				public void messageReceived(NioConnect connect, Object object) {
					EntryImpl nextEntry = EntryImpl.this.nextEntry;
					callNextMessageReceived(connect, nextEntry, object);
				}

				@Override
				public void messageWrite(NioConnect connect, Object writeRequest) {
					EntryImpl nextEntry = EntryImpl.this.prevEntry;
					callPreviousFilterWrite(connect, nextEntry, writeRequest);
				}
			};
		}
	}

	private void callPreviousFilterWrite(NioConnect connect, EntryImpl entry, Object message) {
		IoFilter filter = entry.getFilter();
		NextFilter nextFilter = entry.getNextFilter();
		try {
			filter.messageWrite(connect, nextFilter, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void callNextMessageReceived(NioConnect connect, EntryImpl entry, Object message) {
		IoFilter filter = entry.getFilter();
		NextFilter nextFilter = entry.getNextFilter();
		try {
			filter.messageReceived(connect, nextFilter, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 发送消息最后处理节点 */
	private class HeadFilter extends IoFilterAdapter {
		@Override
		public void messageWrite(NioConnect connect, NextFilter nextFilter, Object message) throws Exception {
			// 将消息交给io线程去发送
			System.out.println("messageWrite:" + message);
		}
	}

	/** 读消息左后处理节点 */
	private class TailFilter extends IoFilterAdapter {
		@Override
		public void messageReceived(NioConnect connect, NextFilter nextFilter, Object message) throws Exception {
			if (message instanceof Message) {
				handler.received(connect, (Message) message);
			}
		}
	}

	public void fireMessageReceived(NioConnect connect, Object message) {
		callNextMessageReceived(connect, head, message);
	}

	public void fireMessageWrite(NioConnect connect, Object message) {
		callPreviousFilterWrite(connect, tail, message);
	}

	public void addLast(String name, IoFilter filter) {
		EntryImpl entryImpl = new EntryImpl(tail.prevEntry, tail, name, filter);
		tail.prevEntry.nextEntry = entryImpl;
		tail.prevEntry = entryImpl;
	}

	public void addFrist(String name, IoFilter filter) {
		EntryImpl entryImpl = new EntryImpl(head, head.nextEntry, name, filter);
		head.nextEntry.prevEntry = entryImpl;
		head.nextEntry = entryImpl;
	}
}
