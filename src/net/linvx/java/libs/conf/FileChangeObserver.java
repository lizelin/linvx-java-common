package net.linvx.java.libs.conf;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import net.linvx.java.libs.utils.MyDateUtils;
import net.linvx.java.libs.utils.MyFileUtils;

public class FileChangeObserver {
	private static FileChangeObserver instance = null;

	public static FileChangeObserver getInstance() {
		if (instance == null) {
			synchronized (FileChangeObserver.class) {
				if (instance == null)
					instance = new FileChangeObserver();
			}
		}
		return instance;

	}

	private Map<String/* filename */, OnFileChangeListener/* listener */> configFiles = new Hashtable<String/* filename */, OnFileChangeListener/* listener */>();
	private CheckFileChangeThread checkFileChangeThread = null;

	private FileChangeObserver() {
		checkFileChangeThread = new CheckFileChangeThread();
	}

	public Map<String/* filename */, OnFileChangeListener/* listener */> getConfigure() {
		return configFiles;
	}

	public FileChangeObserver addConfigFile(String filename, OnFileChangeListener listener) {
		configFiles.put(filename, listener);
		return this;
	}

	public FileChangeObserver removeConfigFile(String filename, OnFileChangeListener listener) {
		if (configFiles.containsKey(filename))
			configFiles.remove(filename);
		return this;
	}

	public void startMonitorFileChangeThread() {
		checkFileChangeThread.start();
	}

	public void stopMonitorFileChangeThread() {
		checkFileChangeThread.attempToStop();
	}

	class CheckFileChangeThread extends Thread {
		/**
		 * 执行周期，毫秒
		 */
		private Long period = 1000l;
		private Boolean stopFlag = false;
		private Map<String, Long> filesLastModifedTime = new HashMap<String, Long>();

		@Override
		public void run() {
			while (true) {
				if (stopFlag.booleanValue() == true) {
					System.out.println("thread will stop");
					return;
				}
				Map<String/* filename */, OnFileChangeListener/* listener */> map = FileChangeObserver.getInstance()
						.getConfigure();
				if (map != null) {
					for (Entry<String, OnFileChangeListener> entry : map.entrySet()) {
						boolean changed = false;
						Long last = MyFileUtils.getFileLastModified(entry.getKey());
						if (last == 0l) {
							System.err.println("file " + entry.getKey() + " is not exists!");
							continue;
						}
						if (!filesLastModifedTime.containsKey(entry.getKey())) {
							filesLastModifedTime.put(entry.getKey(), last);
						}
						if (last > filesLastModifedTime.get(entry.getKey())) {
							filesLastModifedTime.put(entry.getKey(), last);
							changed = true;
						}
						if (changed && entry.getValue() != null) {
							entry.getValue().onFileChange(entry.getKey(), last);
						}
					}
				}
				try {
					Thread.sleep(period);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public Long getPeriod() {
			return period;
		}

		public CheckFileChangeThread setPeriod(Long period) {
			this.period = period;
			return this;
		}

		public void attempToStop() {
			synchronized (stopFlag) {
				stopFlag = true;
			}
		}

		@Override
		public synchronized void start() {
			synchronized (stopFlag) {
				stopFlag = false;
			}
			if (this.isAlive())
				return;
			filesLastModifedTime.clear();
			super.start();
		}
	}

	public static void main(String[] args) {
		FileChangeObserver config = FileChangeObserver.getInstance();
		OnFileChangeListener listener = new OnFileChangeListener() {
			@Override
			public void onFileChange(String fileName, Long lastModified) {
				System.out.println("" + MyDateUtils.getTimeStampFormatString() + "  " + fileName + " changed ");
				String l = MyDateUtils.dateToGMTString(MyDateUtils.millsLong2Date(lastModified.longValue()));
				System.out.println("last modify time is :" + l);
			}

		};
		config.addConfigFile("/Users/lizelin/a.txt", listener).startMonitorFileChangeThread();
		config.addConfigFile("/Users/lizelin/a1.txt", listener).startMonitorFileChangeThread();
		config.startMonitorFileChangeThread();
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		config.stopMonitorFileChangeThread();
		System.out.println(MyDateUtils.getTimeStampFormatString());
	}

}
