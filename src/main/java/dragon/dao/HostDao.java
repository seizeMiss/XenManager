package main.java.dragon.dao;

import java.util.List;

import main.java.dragon.pojo.HostInstance;

public interface HostDao {
	public void insertHost(List<HostInstance> host);
	public void updateHost(List<HostInstance> host);
	public List<HostInstance> selectAllHost();
	public HostInstance selectHostById(String id);
	public HostInstance selectHostByUuid(String uuid);
	public List<HostInstance> selectHostByClusterId(String id);

}
