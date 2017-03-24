package main.java.dragon.service;

import java.util.List;

import main.java.dragon.pojo.Cluster;

public interface ClusterService {
	public Cluster addCluster();
	public void updateCluster();
	public Cluster getClusterById(String id);
	public List<Cluster> getAllCluster();
}
