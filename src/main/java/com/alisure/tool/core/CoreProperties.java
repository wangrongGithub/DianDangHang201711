package com.alisure.tool.core;

import java.io.*;
import java.util.Properties;

/**
 * 
 * ���ڶ�ȡ�����ļ�
 * 
 * @author ALISURE
 * @version 1507
 *
 */
public class CoreProperties {

	private static Properties prop = new Properties();
	
	/**
	 * ͨ��properties�ļ�������������ȡ����
	 * @param fileName
	 * @param propName
	 * @return
	 */
	public String getPropByFileAndProp(String fileName,String propName) throws Exception{
		prop.load(getClass().getResourceAsStream(fileName));
		return prop.getProperty(propName);
	}
	
	/**
	 * ͨ��properties�ļ�������������ȡ����
	 * @param fileName
	 * @param propName
	 * @return
	 */
	public static String getPropByFileAndProp2(String fileName,String propName) throws Exception{
		FileInputStream fileInputStream = new FileInputStream(fileName);
		prop.load(fileInputStream);
		fileInputStream.close();
		return  prop.getProperty(propName);
	}
	
	/**
	 * ͨ��properties�ļ�������������ȡ����
	 * @param fileName
	 * @param propName
	 * @return
	 */
	public static String getPropByFileAndProp3(String fileName,String propName) throws Exception{
		FileInputStream fileInputStream = new FileInputStream(fileName);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
		prop.load(bufferedInputStream);
		bufferedInputStream.close();
		fileInputStream.close();
		return  prop.getProperty(propName);
	}
	
	/**
	 * ͨ��properties�ļ�������������������
	 * @param fileName
	 * @param key
	 * @param value
	 */
	public static void updateOrWriteProp(String fileName, String key,String value)
			throws FileNotFoundException,IOException,Exception{
		FileInputStream fileInputStream = new FileInputStream(fileName);
		FileOutputStream fileOutputStream = new FileOutputStream(fileName);
		
		prop.load(fileInputStream);        
	    prop.setProperty(key, value);
	    prop.store(fileOutputStream, "Update or add " + key + " = "+ value);

	    fileInputStream.close();
		fileOutputStream.close();
    }
}
