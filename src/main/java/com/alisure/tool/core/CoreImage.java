package com.alisure.tool.core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * ���ڴ���ͼƬ
 * 
 * @author ALISURE
 * @version 1507
 *
 */
public class CoreImage {

	/**
	 * ����ͼƬ���趨�߶�
	 * 
	 * @param beforePath
	 * @param afterPath
	 * @param setHeight
	 */
	public static void thumbnailImageSetHeight(String beforePath,
			String afterPath, int setHeight) throws Exception {

		File fi = new File(beforePath);
		File fo = new File(afterPath);
		AffineTransform transform = new AffineTransform();
		BufferedImage bis = ImageIO.read(fi);
		int width = bis.getWidth();
		int height = bis.getHeight();

		double n = 1;
		if (height > setHeight) {
			n = (double) height / (double) setHeight;
		}

		int setWidth = (int) ((double) width / n);
		double sx = (double) setWidth / width;
		double sy = (double) setHeight / height;
		transform.setToScale(sx, sy);

		BufferedImage bid = new BufferedImage(setWidth, setHeight,
				BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D gd2 = bid.createGraphics();
		gd2.drawImage(bis, transform, null);
		gd2.dispose();

		ImageIO.write(bid, "jpeg", fo);
	}

	/**
	 * ����ͼƬ���趨���
	 * 
	 * @param beforePath
	 * @param afterPath
	 * @param setWidth
	 */
	public static void thumbnailImageSetWidth(String beforePath,
			String afterPath, int setWidth) throws Exception {
		File fi = new File(beforePath);
		File fo = new File(afterPath);
		AffineTransform transform = new AffineTransform();
		BufferedImage bis = ImageIO.read(fi);
		int width = bis.getWidth();
		int height = bis.getHeight();

		double n = 1;
		if (width > setWidth) {
			n = (double) width / (double) setWidth;
		}

		int setHeight = (int) ((double) width / n);
		double sx = (double) setWidth / width;
		double sy = (double) setHeight / height;
		transform.setToScale(sx, sy);

		BufferedImage bid = new BufferedImage(setWidth, setHeight,
				BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D gd2 = bid.createGraphics();
		gd2.drawImage(bis, transform, null);
		gd2.dispose();

		ImageIO.write(bid, "jpeg", fo);
	}

	/**
	 * ����������ͼƬ
	 * 
	 * @param beforePath
	 * @param afterPath
	 * @param n
	 */
	public static void thumbnailImageSetRatio(String beforePath,
			String afterPath, int n) throws Exception {
		File fi = new File(beforePath);
		File fo = new File(afterPath);
		AffineTransform transform = new AffineTransform();
		BufferedImage bis = ImageIO.read(fi);

		int width = bis.getWidth();
		int height = bis.getHeight();
		int setHeight = (int) ((double) height / n);
		int setWidth = (int) ((double) width / n);

		double sx = (double) setWidth / width;
		double sy = (double) setHeight / height;
		transform.setToScale(sx, sy);

		BufferedImage bid = new BufferedImage(setWidth, setHeight,
				BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D gd2 = bid.createGraphics();
		gd2.drawImage(bis, transform, null);
		gd2.dispose();

		ImageIO.write(bid, "jpeg", fo);
	}

    /**
     * ������������ͼƬ
     * @param url
     * @param path
     * @return
     */
	public static boolean downImage(String url, String path){
        try {
            URLConnection connection = new URL(url).openConnection();
            String contentType = connection.getHeaderField("Content-Type");

            /*�������ͼƬ˵����������*/
            if(!contentType.contains("image")){
                System.out.println("Content-Type:"+ contentType);
                return false;
            }

            System.out.println(contentType);
            InputStream inputStream = new DataInputStream(connection.getInputStream());

            //�õ�ͼƬ�Ķ��������ݣ��Զ����Ʒ�װ�õ����ݣ�����ͨ����
            byte[] data = readInputStream(inputStream);
            //newһ���ļ�������������ͼƬ��Ĭ�ϱ��浱ǰ���̸�Ŀ¼
            File imageFile = new File(path);
            //���������
            FileOutputStream outStream = new FileOutputStream(imageFile);
            //д������
            outStream.write(data);
            //�ر������
            outStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //����һ��Buffer�ַ���
        byte[] buffer = new byte[1024];
        //ÿ�ζ�ȡ���ַ������ȣ����Ϊ-1������ȫ����ȡ���
        int len = 0;
        //ʹ��һ����������buffer������ݶ�ȡ����
        while( (len=inStream.read(buffer)) != -1 ){
            //���������buffer��д�����ݣ��м����������ĸ�λ�ÿ�ʼ����len�����ȡ�ĳ���
            outStream.write(buffer, 0, len);
        }
        //�ر�������
        inStream.close();
        //��outStream�������д���ڴ�
        return outStream.toByteArray();
    }
}
