package main.java.dragon.xenapi;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

import com.xensource.xenapi.*;
import com.xensource.xenapi.Types.StorageOperations;

import main.java.dragon.service.impl.ConnectionUtil;

public class VolumeAPI extends ConnectionUtil {
	public VolumeAPI() throws Exception {
	}

	/*public Set<StorageInfo> getAllStroage() throws Exception {
		Set<SR> srSet = SR.getAll(connection);
		Set<StorageInfo> infoSet = new HashSet<StorageInfo>();

		for (SR sr : srSet) {

			StorageInfo info = new StorageInfo();
			SR.Record record = sr.getRecord(connection);

			// 过滤掉共享存储iso,及本地存储
			if (record.type.equals("iso") || !record.shared) {
				continue;
			}

			info.name = record.nameLabel;
			info.type = record.type;
			info.uuid = record.uuid;

			// 判断存储是否断开连接。
			boolean isOk = true;
			for (PBD pbd : record.PBDs) {
				PBD.Record pbdRecord = pbd.getRecord(connection);
				if (!pbdRecord.currentlyAttached) {
					isOk = false;
					break;
				}
			}

			// 简单方式进行存储服务器IP的获取。
			for (PBD pbd : record.PBDs) {
				PBD.Record pbdRecord = pbd.getRecord(connection);
				info.ipaddress = pbdRecord.deviceConfig.get("target");
				if (null == info.ipaddress) {
					info.ipaddress = pbdRecord.deviceConfig.get("server");
				}
				break;
			}
			info.totalCapacity = record.physicalSize / 1024 / 1024 / 1024;
			info.freeCapacity = (record.physicalSize - record.physicalUtilisation) / 1024 / 1024 / 1024;
			info.state = isOk ? "Ok" : "Broken";
			infoSet.add(info);
		}
		return infoSet;
	}*/

	public VDI getVDIByName(String name) throws Exception {
		Set<VDI> vdiSet = VDI.getByNameLabel(connection, name);
		return vdiSet.iterator().next();
	}

	public VDI copyNewVDI(VDI srcVDI, String newName) throws Exception {
		VDI newVDI = null;
		SR dscSR = srcVDI.getSR(connection);
		newVDI = srcVDI.copy(connection, dscSR);
		newVDI.setNameLabel(connection, newName);

		return newVDI;
	}

	public VDI cloneNewVDI(VDI srcVDI, String newName) throws Exception {

		VDI newVDI = null;
		newVDI = srcVDI.createClone(connection, new HashMap<String, String>());
		newVDI.setNameLabel(connection, newName);
		return newVDI;
	}

	// 创建一个空的磁盘
	public VDI createUserDisk(String name, long size, SR sr) throws Exception {
		VDI.Record record = new VDI.Record();
		record.type = Types.VdiType.USER;
		record.SR = sr;
		record.nameLabel = name;
		record.virtualSize = size * 1024 * 1024 * 1024;
		VDI vdi = VDI.create(connection, record);

		return vdi;
	}

	// 与虚机挂载时，需要通过VBD。因此需要创建VBD并关联到VM。
	public void attatch(VM vm, VDI vdi) throws Exception {

		ComputerAPI computerApi = new ComputerAPI();
		VBD.Record vbdRecord = new VBD.Record();
		vbdRecord.VM = vm;
		vbdRecord.VDI = vdi;
		vbdRecord.mode = Types.VbdMode.RW;
		vbdRecord.type = Types.VbdType.DISK;
		vbdRecord.userdevice = computerApi.getVbdDevicePosition(vm);
		VBD.create(connection, vbdRecord);
	}

	// 销毁磁盘时，需要先销毁关联的vbd，再删除vdi.
	public void destory(VDI vdi) throws Exception {
		detatch(vdi);
		vdi.destroy(connection);

	}

	// 解除挂载。vbd中并不包含磁盘的数据，因此可以直接删除。
	// 由于一个vdi可以被多个虚机挂载，因此这里会是一个列表。
	public void detatch(VDI vdi) throws Exception {

		Set<VBD> vbdSet = new HashSet<VBD>();

		vbdSet = vdi.getVBDs(connection);

		if (vbdSet.isEmpty()) {
			throw new Exception("no vbd found");
		}

		for (VBD vbd : vbdSet) {
			vbd.destroy(connection);
		}
	}

	// 选择第一个SR
	public SR getDefaultSR() throws Exception {
		Set<Pool> pools = Pool.getAll(connection);
		Pool pool = (pools.toArray(new Pool[0]))[0];
		return pool.getDefaultSR(connection);
	}
	
	public void just() throws Exception {
		SR sr  = SR.getByUuid(connection, "");
		Set<StorageOperations> storageOperations = sr.getAllowedOperations(connection);
		for(StorageOperations storageOperation : storageOperations) {
			System.out.println(storageOperation.name());
		}
	}

}
