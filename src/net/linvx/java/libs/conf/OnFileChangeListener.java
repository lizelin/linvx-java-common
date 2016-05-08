package net.linvx.java.libs.conf;

public interface OnFileChangeListener {
	void onFileChange(String fileName, Long lastModified);
}
