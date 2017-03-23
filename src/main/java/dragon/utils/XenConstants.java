package main.java.dragon.utils;

public class XenConstants {

	public static final String RRD_HOST_STR = "host";

	public static final String XEN_HOST_MEMORY_TOTAL_KIB = "memory_total_kib";
	
	public static final String XEN_HOST_MEMORY_FREE_KIB = "memory_free_kib";

	public static final String XEN_HOST_MEMORY_TOTAL = "memory_total";

	public static final String XEN_HOST_MEMORY_USED = "memory_used";

	public static final String IO_EXCEPTION_RETRY = "retry";

	public static final String XEN_COMMON_CPU = "cpu0";

	public static final String XEN_HOST_CPU_META = "cpu_avg";
	public static final String XEN_HOST_CPU = "cpu";

	public static final String XEN_VM_CPU_META = "cpu_avg";
	public static final String XEN_VM_CPU = "cpu";

	public static final String XEN_VM_MEMORY_FREE_META = "memory_internal_free";

	public static final String XEN_VM_MEMORY_TOTAL_META = "memory";

	public static final String XEN_VM_MEMORY_USED = "memory_used";

	public static final String XEN_VM_MEMORY_TOTAL = "memory_total";

	public static final String XEN_VM_MEMORY_FREE = "memory_internal_free";

	public static final String XEN_VBD_CD_TYPE = "CD";

	public static final String XEN_HOST_META_MEMORY_TOTAL = "memoryTotal";

	public static final String XEN_HOST_META_MEMORY_USED = "memoryUsed";

	public static final String XEN_VM_META_MEMORY_TOTAL = "memoryTotal";

	public static final String XEN_VM_META_MEMORY_USED = "memoryUsed";

	public static final String XEN_VOLUME_TAGS_STR = "ivy_volume";

	public static final double XEN_METER_BAD_VALUE = -1101;
	
	public static final String XEN_HOST_PIF_RX = "pif_eth[0-9]+_rx";
	
	public static final String XEN_HOST_PIF_TX = "pif_eth[0-9]+_tx";
	
	public static final String XEN_VM_VIF_RX = "vif_[0-9]+_rx";
	
	public static final String XEN_VM_VIF_TX = "vif_[0-9]+_tx";
	
	public static final String XEN_METER_STORAGEIOLATENCY_MATCH = "latency_";

	public static final String XEN_METER_DISK_READ_LATENCY_MATCH = "_read_latency";
	
	public static final String XEN_METER_DISK_WRITE_LATENCY_MATCH = "_write_latency";
	
	public static final String XEN_METER_IOWAIT_MATCH = "iowait_";
	
	public static final String XEN_METER_VM_IOWAIT_MATCH = "_iowait";
	
	public static final String XEN_LATITUDE_Y = "1Y";
	
	public static final String XEN_LATITUDE_O = "1O";
	
	public static final String XEN_LATITUDE_W = "1W";
	
	public static final String XEN_LATITUDE_D = "1D";
	
	public static final String XEN_LATITUDE_H = "1H";
	
	public static final String XEN_LATITUDE_M = "10M";
	
	public static final String XEN_METER_IOWAIT = "ioWait";
	
	public static final String XEN_METER_STORAGEIOLATENCY = "storageIoLatency";
	
	public static final String XEN_METER_DISK_READ_IOLATENCY = "diskIoReadLatency";
	
	public static final String XEN_METER_DISK_WRITE_IOLATENCY = "diskIoWriteLatency";
	
	public static final String XEN_METER_NETRECEIVED = "netReceived";
	
	public static final String XEN_METER_NETTRANSMITTED = "netTransmitted";
	
	public static final int XEN_METER_LATITUDE_1Y_FORMAT = 371;
	
	public static final int XEN_METER_LATITUDE_1Y_UNIT = 7; 
	
	public static final int XEN_METER_LATITUDE_1O_FORMAT = 30;
	
	public static final int XEN_METER_LATITUDE_1O_UNIT = 1; 
	
	public static final int XEN_METER_LATITUDE_1W_FORMAT = 168;
	
	public static final int XEN_METER_LATITUDE_1W_UNIT = 1; 
	
	public static final int XEN_METER_LATITUDE_1D_FORMAT = 25;
	
	public static final int XEN_METER_LATITUDE_1D_UNIT = 1; 
	
	public static final int XEN_METER_LATITUDE_1H_FORMAT = 60;
	
	public static final int XEN_METER_LATITUDE_1H_UNIT = 1;
	
	public static final int XEN_METER_LATITUDE_10M_FORMAT = 120;
	
	public static final int XEN_METER_LATITUDE_10M_UNIT = 1;
	
	public static final String XEN_METER_DISK_NAME = "磁盘";
	
	
	public static final String XEN_METER_NET_NAME = "网卡";
	
	public static final String ALL_LETTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static final String XEN_METER_XVD_SPLIT = "xvd";
	
	public static final String XEN_METER_UNKNOW = "unknow";
	
	public static final int XEN_STEP_5= 5;
	public static final String XEN_STEP_5_String= "5s";
	
	public static final int XEN_STEP_60= 60;
	public static final String XEN_STEP_60_String= "1M";
	
	public static final int XEN_STEP_3600= 3600;
	public static final String XEN_STEP_3600_String= "1H";
	
	public static final int XEN_STEP_86400= 86400;
	public static final String XEN_STEP_86400_String= "1D";
}
