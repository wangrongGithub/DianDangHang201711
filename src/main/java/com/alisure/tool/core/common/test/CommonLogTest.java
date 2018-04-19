package com.alisure.common.test;

import com.alisure.tool.common.CommonLog;
import com.alisure.tool.core.CoreFile;
import com.alisure.tool.core.CorePrint;
import com.alisure.tool.core.CoreReflect;

public class CommonLogTest {
	
	public static void main(String[] args) throws Exception {
		
		CommonLog.writeAlisureLog("???");
		
		String file = "ali.log";
		CommonLog.writeAlisureLog(file, "haha",CoreReflect.getExceptionPlace(),"xxxxxxxx");
		
		
		CorePrint.println(CoreFile.readFromFile(CommonLog.getDefaultFilePath()));
		CorePrint.println(CoreFile.readFromFile(file));
		
		
		CommonLog.deleteAlisureLog();
		CoreFile.delFolder("log");
		
		CommonLog.deleteAlisureLog(file);
	}
}