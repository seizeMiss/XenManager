package test.java;

import java.util.Set;

import org.junit.Test;

import com.xensource.xenapi.Network;
import com.xensource.xenapi.VM;


public class Demo extends ConnectionUtil{
	
	public Demo() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	@Test
	public void demo(){
		System.out.println("hello world");
	}
	
	@Test
	public void getAllVm() throws Exception{
		Set<VM> vms = VM.getAll(connection);
		System.out.println(vms.size());
		for (VM vm : vms){
			System.out.println(vm.getNameLabel(connection));
		}
		
	}
}
