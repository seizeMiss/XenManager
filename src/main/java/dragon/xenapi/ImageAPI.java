package main.java.dragon.xenapi;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.xensource.xenapi.Task;
import com.xensource.xenapi.Types;
import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VM;

import main.java.dragon.pojo.VmInstance;
import main.java.dragon.service.impl.ConnectionUtil;

public class ImageAPI extends ConnectionUtil {

	public ImageAPI() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public VM getVmByUuid(String uuid) throws Exception {
		return VM.getByUuid(connection, uuid);
	}

	public VDI createImageByVm(VM vm, String name) throws Exception {
		//先创建快照
		VM newVm = vm.snapshot(connection, "ral_snapshot");
		VDI srcVdi = getSystemVdiByVm(newVm);
		// 对vdi仅限拷贝
		Task task = srcVdi.copyAsync(connection, srcVdi.getSR(connection));
		XenApiUtil.waitForTask(connection, task, 2000);
		VDI newVdi = Types.toVDI(task, connection);
		newVdi.setNameLabel(connection, name);
		// 最后销毁不在使用的快照虚机，及磁盘。
		Set<VBD> vbdSet = newVm.getVBDs(connection);
		for (VBD vbd : vbdSet) {
			VDI vmVdi = vbd.getVDI(connection);
			if(!vmVdi.isNull()){
				vmVdi.destroy(connection);
			}
			if(!vbd.isNull()){
				vbd.destroy(connection);
			}
		}
		newVm.destroy(connection);
		return newVdi;
	}

	public VDI getSystemVdiByVm(VM vm) throws Exception {

		Set<VBD> vbdSet = vm.getVBDs(connection);
		for (VBD vbd : vbdSet) {
			VBD.Record vbdRecord = vbd.getRecord(connection);
			// 如果是DISK,且bootable为 ture 则为启动盘。
			if (vbdRecord.type != Types.VbdType.DISK) {
				continue;
			}

			if (vbdRecord.bootable) {
				return vbd.getVDI(connection);
			}
			// 根据VBD,查询到VDI，并查询VDI属性，如果vid属性为SYSTEM则为系统盘。
			VDI vdi = vbd.getVDI(connection);
			VDI.Record record = vdi.getRecord(connection);
			if (record.type == Types.VdiType.SYSTEM) {
				return vdi;
			}
		}
		return null;
	}

}
