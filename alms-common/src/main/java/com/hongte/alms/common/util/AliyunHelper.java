
package com.hongte.alms.common.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * <br>
 * <br>
 * 阿里云 OSS操作辅助类
 *
 * @author 伦惠峰
 * @Date 2018/1/26 20:43
 */
@Component
public class AliyunHelper {
    /**
     * OSS操作对象
     */
    private OSSClient ossClient;
    /**
     * AccessKeyID
     */
    @Value(value="${oss.accessKeyID}")
    private String accessKeyID;
    /**
     * AccessKeySecret
     */
    @Value(value="${oss.accessKeySecret}")
    private String accessKeySecret;
    /**
     * 上传服务的路径
     */
    @Value(value="${oss.upLoadUrl}")
    private String upLoadUrl;
    /**
     * 命名空间
     */
    @Value(value="${oss.ossBucketName}")
    private String ossBucketName;

    /**
     * 命名空间
     */
    @Value(value="${oss.readUrl}")
    private String readUrl;

    public AliyunHelper()
    {

    }

    private void initOSSClient(){
        //构造实例
        ossClient = new OSSClient(upLoadUrl, accessKeyID, accessKeySecret);
    }

    /**
     * TODO<一句话功能描述><br>
     * TODO<功能详细描述><br>
     * 上传OSS文件
     * @param saveFilePath 保存的文件路径
     * @param fileContent 文件二进制内容
     * @return com.ht.litigation.service.Tool.UpLoadResult
     * @author 伦惠峰
     * @Date 2018/1/26 20:58
     */
    public OssResult putObject(String saveFilePath, byte[] fileContent)
    {
        initOSSClient();
        OssResult result=new OssResult();
        result.setSuccess(true);
        try {
            // 上传
            ossClient.putObject(ossBucketName, saveFilePath, new ByteArrayInputStream(fileContent));
            // 关闭client
            ossClient.shutdown();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result.setSuccess(false);
            result.setErrMessage(ex.getMessage());
        }

        return result;
    }

    /**
     * TODO<一句话功能描述><br>
     * TODO<功能详细描述><br>
     *判断存储空间是否存在
     * @param bucketName 存储空间的名称
     * @return java.lang.Boolean
     * @author 伦惠峰
     * @Date 2018/1/27 9:02
     */
    public Boolean doesBucketExist(String bucketName)
    {
        initOSSClient();
        try
        {
            Boolean isExist = ossClient.doesBucketExist(bucketName);
            return isExist;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    /// <summary>
    /// 创建一个新的存储空间（Bucket）
    /// </summary>
    /// <param name="bucketName">存储空间的名称</param>
    public OssResult createBucket(String bucketName)
    {
        initOSSClient();
        OssResult result=new OssResult();
        result.setSuccess(true);
        try
        {
            // 新建一个Bucket
            ossClient.createBucket(bucketName);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result.setSuccess(false);
            result.setErrMessage(ex.getMessage());
        }
        return result;
    }

//    public Boolean checkFolder(String bucketName, String folderName)
//    {
//        try
//        {
//            // 重要: 这时候作为目录的key必须以斜线（／）结尾
//            String key = folderName+"/";
//            // 此时的目录是一个内容为空的文件
//            using (var stream = new MemoryStream())
//            {
//                ossClient.putObject(bucketName, key, stream);
//                return true;
//            }
//        }
//        catch (Exception ex)
//        {
//            BM.Core.Log.Log4NetHelper.Error("AliyunHelper/CheckFolder", "判断是否存在", ex);
//            return false;
//        }
//    }

    /**
     * TODO<一句话功能描述><br>
     * TODO<功能详细描述><br>
     * 删除OSS文件
     * @param bucketName 命名空间
     * @param key         文件相对路径
     * @return void
     * @author 伦惠峰
     * @Date 2018/1/27 9:17
     */
    public void deleteObject(String bucketName, String key)
    {
        initOSSClient();
        ossClient.deleteObject(bucketName,key);
    }

    /**
     * TODO<一句话功能描述><br>
     * TODO<功能详细描述><br>
     * 删除默认命名空间单个文件
     * @author 伦惠峰
     * @Date 2018/1/29 15:00
     */
    public void deleteObject( String key)
    {
        deleteObject(ossBucketName,key);
    }

    /**
     * 批量删除文件集合
     * @param mybucketName 当前命名空间
     * @param keyList  删除的Key集合
     * @return void
     * @author 伦惠峰
     * @Date 2018/1/29 15:03
     */
    public void deleteObjectList(String mybucketName, List<String> keyList)
    {

        initOSSClient();
        DeleteObjectsRequest deleteObjectsRequest=new DeleteObjectsRequest(mybucketName);
        deleteObjectsRequest.setKeys(keyList);
        ossClient.deleteObjects(deleteObjectsRequest);
    }


    /**
     * 批量删除文件集合
     * @param keyList  删除的Key集合
     * @return void
     * @author 伦惠峰
     * @Date 2018/1/29 15:03
     */
    public void deleteObjectList(List<String> keyList)
    {
        deleteObjectList(ossBucketName,keyList);
    }

    public OSSClient getOssClient() {
        return ossClient;
    }

    public String getAccessKeyID() {
        return accessKeyID;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public String getUpLoadUrl() {
        return upLoadUrl;
    }

    public String getOssBucketName() {
        return ossBucketName;
    }

    public String getReadUrl() {
        return readUrl;
    }
}
