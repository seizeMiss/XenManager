package main.java.dragon.dao;

import java.util.List;

import main.java.dragon.pojo.HostInstance;

public interface HostDao {
	public void insertHost(HostInstance host);
	public void updateHost(HostInstance host);
	public List<HostInstance> selectAllHost();
	public HostInstance selectHostById(String id);
	public List<HostInstance> selectHostByClusterId(String id);

}
