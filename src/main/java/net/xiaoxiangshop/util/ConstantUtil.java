package net.xiaoxiangshop.util;

public class ConstantUtil {
    /**
     * 获取上传的服务名
     */
    public static final String OSS_SERVER_NAME = PropertiesUtil.getString(PropertiesUtil.OSS_SERVER_NAME);
    /**
     * 获取图片的上传服务名
     */
    public static final String OSS_IMG_SERVER_NAME = PropertiesUtil.getString(PropertiesUtil.OSS_IMG_SERVER_NAME);
    //附件上传云服务器地址
    public static final String OSS_FILE_PATH= PropertiesUtil.getString(PropertiesUtil.OSS_FILE_PATH);;
}
