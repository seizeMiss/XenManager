package main.java.dragon.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xensource.xenapi.SR;
import com.xensource.xenapi.Types.BadServerResponse;
import com.xensource.xenapi.Types.XenAPIException;

import main.java.dragon.dao.ClusterDao;
import main.java.dragon.dao.StorageDao;
import main.java.dragon.pojo.Cluster;
import main.java.dragon.pojo.Storage;
import main.java.dragon.service.StorageService;
import main.java.dragon.utils.StringUtils;

@Service
@Transactional
public class StorageServiceImpl extends ConnectionUtil implements StorageService {
	@Autowired
	private ClusterDao clusterDao;
	@Autowired
	private StorageDao storageDao;
	
	public StorageServiceImpl() throws Exception {
		super();
	}
	private Storage getStorage(String id, SR sr) throws Exception{
		Storage storage = null;
		String uuid = sr.getUuid(connection);
		String clusterId = "";
		String name = sr.getNameLabel(connection);
		int storageTotal = (int) (sr.getPhysicalSize(connection)/1024/1024/1024);
		int storageUsed = (int) (sr.getPhysicalUtilisation(connection)/1024/1024/1024);
		String storageType = sr.getType(connection);
		int status = 1;
		String ipAddress = "";
		
		List<Cluster> clusters = clusterDao.selectAllClusters();
		if(!StringUtils.isEmpty(clusters)){
			clusterId = clusters.get(0).getId();
		}
		storage = new Storage(id, uuid, clusterId, name, 
				storageTotal, storageUsed, storageType, status, ipAddress);
		return storage;
	}

	@Override
	public void addStorage() throws Exception{
		Set<SR> srs = SR.getAll(connection);
		List<Storage> storages = getAllStorage();
		boolean isStorageExist = false;
		for(SR sr : srs){
			if(sr.getPhysicalSize(connection) > 0){
				String uuid = sr.getUuid(connection);
				if(!StringUtils.isEmpty(storages)){
					for(Storage storage : storages){
						if(storage.getUuid().equals(uuid)){
							isStorageExist = true;
						}
					}
				}
				if(isStorageExist){
					isStorageExist = false;
					continue;
				}
				String id = StringUtils.generateUUID();
				Storage storage = getStorage(id, sr);
				storageDao.insertStorage(storage);
			}
		}
	}

	@Override
	public void saveStorage() {
		
	}
	@Override
	public List<Storage> getAllStorage() {
		List<Storage> storages = storageDao.selectAllStorage();
		if(!StringUtils.isEmpty(storages)){
			return storages;
		}
		return null;
	}
	@Override
	public Storage getStorageByUuid(String uuid) {
		// TODO Auto-generated method stub
		return storageDao.selectStorageByUuid(uuid);
	}

}
