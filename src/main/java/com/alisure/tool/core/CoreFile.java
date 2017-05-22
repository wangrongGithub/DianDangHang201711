package com.alisure.tool.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

/**
 * 
 * ���ļ��Ĳ���,�½���ɾ����д��
 * 
 * @author ALISURE
 * @version 1507
 *
 */
public class CoreFile {
	/**
	 * ����ļ����ھͰ�����׷�ӵ�����
	 */
	public static final int File_Append_After = 1;
	/**
	 * ����ļ����ھͰ�����׷�ӵ�ǰ��
	 */
	public static final int File_Append_Before = 2;
	/**
	 * ����ļ����ھͰ����ݸ���
	 */
	public static final int File_Cover_If_Have = 3;
	/**
	 * ����ļ����ھͰ���������
	 */
	public static final int File_Abandon_If_Have_File = 4;
	/**
	 * ����ļ������ھͰ���������
	 */
	public static final int File_Abandon_If_No_File = 5;
	
	/**
	 * д���ļ�
	 * @param pathAndName
	 * @param content
	 * @param method
	 */
	public static void write(String pathAndName, String content, int method) throws Exception{
		switch (method) {
		case File_Append_After:
			appendFile(pathAndName, content);
			break;
		case File_Append_Before:
			beforeFile(pathAndName, content);
			break;
		case File_Cover_If_Have:
			newFile(pathAndName, content);
			break;
		case File_Abandon_If_Have_File:
			newFile(pathAndName, content);
			break;
		case File_Abandon_If_No_File:
			if (new File(pathAndName).exists()) appendFile(pathAndName, content);
			break;
		default:
			break;
		}
	}

	/**
	 * ���ļ���׷������
	 * 
	 * @param filePathAndName
	 * @param content
	 */
	public static void beforeFile(String filePathAndName, String content)
			throws Exception {
		String filePath = filePathAndName.toString();
		File myFilePath = new File(filePath);
		if (!myFilePath.exists()) myFilePath.createNewFile();

		// ������ʱ�ļ�
		String temp = System.getProperty("user.dir")
				+ System.getProperty("file.separator")
				+ System.currentTimeMillis() + ".txt";
		// ��Ҫд������д�뵽��ʱ�ļ�
		FileWriter fileWriter = new FileWriter(temp, true);
		fileWriter.write(content + System.getProperty("line.separator"));
		// ��ȡԭ�ļ����ݵ���ʱ�ļ�
		FileReader fileReader = new FileReader(filePathAndName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String tempString = "";
		while ((tempString = bufferedReader.readLine()) != null) fileWriter.write(tempString);

		bufferedReader.close();
		fileReader.close();
		fileWriter.close();
		// �����ļ�
		fileChannelCopy(new File(temp), myFilePath);
		// ɾ����ʱ�ļ�
		delFile(temp);
	}

	/**
	 * �ļ�ͨ����ʽ�����ļ�
	 * 
	 * @param inFile
	 * @param outFile
	 */
	public static void fileChannelCopy(File inFile, File outFile)
			throws Exception {
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		FileChannel inFileChannel = null;
		FileChannel outFileChannel = null;

		fileInputStream = new FileInputStream(inFile);
		fileOutputStream = new FileOutputStream(outFile);
		inFileChannel = fileInputStream.getChannel();
		outFileChannel = fileOutputStream.getChannel();
		// ��������ͨ�������Ҵ�inͨ����ȡ��Ȼ��д��outͨ��
		inFileChannel.transferTo(0, inFileChannel.size(), outFileChannel);

		outFileChannel.close();
		inFileChannel.close();
		fileOutputStream.close();
		fileInputStream.close();
	}

	/**
	 * ���ļ���׷������
	 * 
	 * @param filePathAndName
	 * @param content
	 */
	public static void appendFile(String filePathAndName, String content)
			throws Exception {
		File myFilePath = new File(filePathAndName);
		if (!myFilePath.exists()) myFilePath.createNewFile();

		FileWriter fileWriter = new FileWriter(myFilePath, true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(content);
		bufferedWriter.flush();

		bufferedWriter.close();
		fileWriter.close();
	}

	/**
	 * ���ļ���׷������
	 * 
	 * @param filePathAndName
	 * @param content
	 */
	public static void appendFile2(String filePathAndName, String content)
			throws Exception {
		String filePath = filePathAndName.toString();
		File myFilePath = new File(filePath);
		if (!myFilePath.exists()) myFilePath.createNewFile();

		FileWriter fileWriter = new FileWriter(myFilePath, true);
		fileWriter.write(content);
		fileWriter.close();
	}

	/**
	 * ���ļ���׷������
	 * 
	 * @param filePathAndName
	 * @param content
	 */
	public static void appendFile(String filePathAndName, byte[] content)
			throws Exception {
		String filePath = filePathAndName.toString();
		File myFilePath = new File(filePath);
		if (!myFilePath.exists()) myFilePath.createNewFile();

		FileOutputStream fileOutputStream = new FileOutputStream(myFilePath,true);
		fileOutputStream.write(content);
		fileOutputStream.close();
	}

	/**
	 * �½��ļ�,�������ݴ浽�ļ���
	 * 
	 * @param filePathAndName
	 * @param fileContent
	 */
	public static void newFile(String filePathAndName, byte[] fileContent)
			throws Exception {
		String filePath = filePathAndName.toString();
		File myFilePath = new File(filePath);
		if (!myFilePath.exists()) myFilePath.createNewFile();

		FileOutputStream out = new FileOutputStream(filePathAndName);
		out.write(fileContent);
		out.flush();
		out.close();
	}

	/**
	 * �½��ļ�,������������½���������Ϣ��������������
	 * 
	 * @param filePathAndName
	 * @param fileContent
	 */
	public static void newFile(String filePathAndName, String fileContent)
			throws Exception {
		String filePath = filePathAndName.toString();
		File myFilePath = new File(filePath);
		if (!myFilePath.exists()) {
			myFilePath.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(filePathAndName);
			OutputStreamWriter out = new OutputStreamWriter(fileOutputStream,"UTF-8");
			out.write(fileContent);
			out.flush();
			out.close();
			fileOutputStream.close();
		}
	}

	/**
	 * �½��ļ�,������������½���������������
	 * 
	 * @param filePathAndName
	 */
	public static void newFile(String filePathAndName) throws Exception {
		File myFilePath = new File(filePathAndName);
		if (!myFilePath.exists()) myFilePath.createNewFile();
	}

	/**
	 * �½��ļ���,������������½���������������
	 * 
	 * @param folderPath
	 */
	public static void newFolder(String folderPath) throws Exception {
		File myFilePath = new File(folderPath);
		if (!myFilePath.exists()) myFilePath.mkdir();
	}

	/**
	 * �½��ļ��У���·�����Բ�����
	 * 
	 * ��Ҫ�Ż��㷨
	 * @param path
	 * @return
	 */
	public static boolean newFullPath(String path) {
		if (path.contains(".")) return false;
		File file = new File(path);
		while (!file.exists()) {
			if (file.getParentFile().exists()) file.mkdir();
			else newFullPath(file.getParent());
		}
		return true;
	}

	/**
	 * �ļ��Ƿ����
	 * 
	 * @param pathOrFileName
	 * @return
	 */
	public static boolean isExists(String pathOrFileName) {
		return new File(pathOrFileName).exists();
	}

	/**
	 * �½��ļ�����·�����Բ�����
	 * 
	 * @param pathAndFileName
	 * @return
	 */
	public static boolean newFullPathAndFileName(String pathAndFileName) throws IOException{
		if (!pathAndFileName.contains(".")) return false;
		if (!pathAndFileName.contains(":")) pathAndFileName = System.getProperty("user.dir") + System.getProperty("file.separator") + pathAndFileName;
		
		File file = new File(pathAndFileName);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) newFullPath(file.getParent());
			
			return file.createNewFile();
		}
		return false;
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param filePathAndName
	 */
	public static void delFile(String filePathAndName) throws Exception {
		File myDelFile = new File(filePathAndName);
		if (!myDelFile.isDirectory()) myDelFile.delete();
	}

	/**
	 * ɾ���ļ��������Ƿ�ɹ�
	 * 
	 * @param filePathAndName
	 */
	public static boolean delFileAndReturn(String filePathAndName)
			throws Exception {
		File myDelFile = new File(filePathAndName);
		if (!myDelFile.isDirectory()) return myDelFile.delete();
		return false;
	}

	/**
	 * ɾ���ļ��м����������ļ�
	 * 
	 * @param folderPath
	 */
	public static void delFolder(String folderPath) throws Exception {
		delAllFile(folderPath); // ɾ����������������
		new File(folderPath.toString()).delete(); // ɾ�����ļ���
	}

	/**
	 * ɾ���ļ�������������ļ�
	 * 
	 * @param path
	 */
	public static void delAllFile(String path) throws Exception {
		File file = new File(path);
		if (!file.exists()) return;
		if (!file.isDirectory()) return;
		
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			temp = path.endsWith(File.separator) ? new File(path + tempList[i]) : new File(path + File.separator + tempList[i]);
			
			if (temp.isFile()) temp.delete();
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// ��ɾ���ļ���������ļ�
				delFolder(path + "/" + tempList[i]);// ��ɾ�����ļ���
			}
		}
	}

	/**
	 * ɾ���ļ�������������ļ� ,�����ļ���
	 * 
	 * @param path
	 */
	public static void delAllFileD(String path) throws Exception {
		File file = new File(path);
		
		if (!file.exists()) return;
		if (!file.isDirectory()) return;
		
		String[] fileList = file.list();
		for (int i = 0; i < fileList.length; i++) delFile(path + fileList[i]);
	}

	/**
	 * ��ȡ�ļ�
	 * 
	 * @param fileName
	 * @return
	 */
	public static String readFromFile(String fileName)
			throws FileNotFoundException, IOException {
		StringBuilder stringBuilder = new StringBuilder();

		File file = new File(fileName);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String temp = null;
		while ((temp = bufferedReader.readLine()) != null) stringBuilder.append(temp + System.getProperty("line.separator"));
		
		fileReader.close();
		bufferedReader.close();

		return stringBuilder.toString();
	}
}
