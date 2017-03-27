package main.java.dragon.service;

import java.util.List;

import main.java.dragon.pojo.HostInstance;

public interface HostService {
	public HostInstance addHost();
	public List<HostInstance> getAllHost();
	public HostInstance saveHost();

}
