package main.java.dragon.timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import main.java.dragon.dao.ClusterDao;
import main.java.dragon.dao.HostDao;

@Component
public class ClusterAndHostTimer {
	
	@Autowired
	private ClusterDao clusterDao;
	@Autowired
	private HostDao hostDao;
	
	@Scheduled(cron="0 0 10 * * ?")
	public void refreshClusterAndHostInfo(){
		
	}

}
