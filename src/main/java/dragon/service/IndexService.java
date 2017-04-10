package main.java.dragon.service;

public interface IndexService {
	public int getVmCount(boolean isRunning);
	public String getStorageTotal();
	public String getStorageUsedRate();
	public String getCpuUsedRate();
	public String getMemoryUsedRate();
}
