package main.java.dragon.service;

import java.util.List;

import main.java.dragon.pojo.Storage;

public interface StorageService {
	public void addStorage() throws Exception;
	public void saveStorage();
	public List<Storage> getAllStorage();
	public Storage getStorageByUuid(String uuid);
}
