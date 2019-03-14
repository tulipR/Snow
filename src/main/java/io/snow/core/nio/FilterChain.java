package io.snow.core.nio;

import io.snow.core.nio.IoFilter.NextFilter;

/**
 * 
 * @author zhangliang	2019.02.28
 *
 */
public class FilterChain {
	private final EntryImpl head;

    private final EntryImpl tail;
    
	public FilterChain() {
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
				public void messageReceived(String message) {
					EntryImpl nextEntry = EntryImpl.this.nextEntry;
					callNextMessageReceived(nextEntry, message);
				}

				@Override
				public void messageWrite(String writeRequest) {
					EntryImpl nextEntry = EntryImpl.this.prevEntry;
					callPreviousFilterWrite(nextEntry, writeRequest);
            	}
            };
        }
	}
	
	private void callPreviousFilterWrite(EntryImpl entry, String message) {
		IoFilter filter = entry.getFilter();
		NextFilter nextFilter = entry.getNextFilter();
		try {
			filter.messageWrite(nextFilter, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void callNextMessageReceived(EntryImpl entry, String message) {
		IoFilter filter = entry.getFilter();
        NextFilter nextFilter = entry.getNextFilter();
        try {
			filter.messageReceived(nextFilter, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/** 发送消息最后处理节点 */
	private class HeadFilter extends IoFilterAdapter {
		@Override
		public void messageWrite(NextFilter nextFilter, String message) throws Exception {
			// 将消息交给io线程去发送
			System.out.println("messageWrite:"+message);
		}
	}
	
	/** 读消息左后处理节点 */
	private class TailFilter extends IoFilterAdapter {
		@Override
		public void messageReceived(NextFilter nextFilter, String message) throws Exception {
			// 将消息交给IoHandle去处理
			System.out.println("messageReceived:"+message);
		}
	}
	
	
	public void fireMessageReceived(String message) {
		callNextMessageReceived(head, message);
	}
	
	public void fireMessageWrite(String message) {
		callPreviousFilterWrite(tail, message);
	}
	
	public void addLast(String name, IoFilter filter) {
		EntryImpl entryImpl=new EntryImpl(tail.prevEntry, tail, name, filter);
		tail.prevEntry.nextEntry=entryImpl;
		tail.prevEntry=entryImpl;
	}
	
	public void addFrist(String name, IoFilter filter) {
		EntryImpl entryImpl=new EntryImpl(head, head.nextEntry, name, filter);
		head.nextEntry.prevEntry=entryImpl;
		head.nextEntry=entryImpl;
	}
}
