package main.java.dragon.utils;

public class CommonConstants {
	
	public static final String VM_IS_CLOSE = "关闭";//6
	public static final String VM_IS_OPEN = "启动";//7
	public static final String VM_IS_CREATING = "创建中";//2 status设值情况 0表示无
	public static final String VM_IS_RESTARTING = "重启中";//3
	public static final String VM_IS_CLOSING = "关闭中";//4
	public static final String VM_IS_DELETING = "删除中";//5
	public static final String VM_IS_OPENING = "启动中";//8
	public static final String VM_IS_AVAILABEL = "可用";//1
	public static final String VM_IS_NO_AVAILABEL = "不可用";//-1
	public static final String VM_IS_EDITING = "编辑中";
	
	public static final String VM_POWER_START = "Running";
	public static final String VM_POWER_CLOSED = "Halted";
	public static final String VM_POWER_CREATING = "Creating";
	public static final String VM_POWER_RESTARTING = "Restarting";
	public static final String VM_POWER_CLOSING = "Closing";
	public static final String VM_POWER_DELETING = "Deleting";
	public static final String VM_POWER_STARTING = "Starting";
	public static final String VM_POWER_DELETED = "Deleted";
	public static final String VM_POWER_EDITING = "Editing";
	
	public static final int VM_OPENING_STATUS = 8;
	public static final int VM_CLOSE_STATUS = 6;//6
	public static final int VM_OPEN_STATUS = 7;//7
	public static final int VM_CREATING_STATUS = 2;//2 status设值情况 0表示无
	public static final int VM_RESTARTING_STATUS = 3;//3
	public static final int VM_CLOSING_STATUS = 4;//4
	public static final int VM_DELETING_STATUS = 5;//5
	public static final int VM_AVAILABEL_STATUS = 1;//1
	public static final int VM_NO_AVAILABEL_STATUS = -1;//-1
	public static final int VM_DELETED_STATUS = 9;//已删除
	public static final int VM_EDITING_STATUS = 10;//编辑中
	
	public static final int IMAGE_CREATING_STATUS = 2;
	public static final int IMAGE_AVAILABLE_STATUS = 1;
	public static final int IMAGE_NO_AVAILABEL_STATUS = -1;
	public static final int IMAGE_DELETING_STATUS = 4;
	public static final int IMAGE_DELETED_STATUS = 5;
	public static final String IMAGE_AVAILABLE = "可用";//1
	public static final String IMAGE_NO_AVAILABEL = "不可用";//-1
	public static final String IMAGE_CREATING = "创建中";//2
	public static final String IMAGE_DELETING = "删除中";//4
}
