package main.java.dragon.dao;

import java.util.List;

import main.java.dragon.pojo.Storage;

public interface StorageDao {
	public void insertStorage(Storage storage);
	public void updateStorage(Storage storage);
	public List<Storage> selectAllStorage();
	public Storage selectStorageById(String id);
	public List<Storage> selectStorageByCluster(String clusterId);
}
