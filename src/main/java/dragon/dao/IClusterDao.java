package main.java.dragon.dao;


import java.util.List;

import main.java.dragon.pojo.Cluster;

public interface IClusterDao {
	public void insertCluster(Cluster cluster);
	public void updateCluster(Cluster cluster);
	public List<Cluster> selectAllClusters();
	public Cluster selectClusterById(String id);
}
