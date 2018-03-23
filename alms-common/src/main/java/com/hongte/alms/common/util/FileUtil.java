package com.hongte.alms.common.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author chenzs
 * @date 2018/3/15
 * 验证字段
 */
public class FileUtil {
	   /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
    
    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName
     *            要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
           
                return deleteFile(fileName);
        
        }
    }
    //将MultipartFile 转换为File
    public static void SaveFileFromInputStream(InputStream stream,String path,String savefile) throws IOException
       {      
           FileOutputStream fs=new FileOutputStream( path + "/"+ savefile);
           System.out.println("------------"+path + "/"+ savefile);
           byte[] buffer =new byte[1024*1024];
           int bytesum = 0;
           int byteread = 0; 
           while ((byteread=stream.read(buffer))!=-1)
           {
              bytesum+=byteread;
              fs.write(buffer,0,byteread);
              fs.flush();
           } 
           fs.close();
           stream.close();      
       }   

}
