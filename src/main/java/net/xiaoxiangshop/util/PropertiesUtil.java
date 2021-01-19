package net.xiaoxiangshop.util;

import java.util.ResourceBundle;

public class PropertiesUtil {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("conf.config-jetty");
    public static String SUPER_USER_ID = "superUserId";

    public static String SUPER_ROLE_CODE = "superRoleCode";

    public static String OPEN_SUPER_PERMISSION = "openSuperPermission";

    public static String ALI_OSS_ENDPOINT = "endpoint";
    public static String ALI_OSS_ENDPOINT_INTERNAL = "endpoint_internal";
    public static String ALI_OSS_ACCESSKEYID = "accessKeyId";
    public static String ALI_OSS_ACCESSKEYSECRET = "accessKeySecret";
    public static String ALI_OSS_BUCKETNAME = "bucketName";

    public static String OSS_SERVER_NAME = "oss_serverName";

    public static String OSS_IMG_SERVER_NAME = "oss_imgServerName";

    public static String OSS_FILE_PATH = "oss_filepath";

    public static String WEB_PATH = "web_path";

    //公用路径部分，便于维护
    public static String OSS_COMMON_PATH ="oss_common_path";

    public static String ORG_POSITION_LEVELID = "org.level.position.id";

    public static String ORG_POSITION_LEVELCODE = "org.level.position.code";

    public static String ORG_COMMON_LEVELID = "org.level.common.id";

    public static String ORG_COMMON_LEVELCODE = "org.level.common.code";

    public static String ORG_TYPE_ID = "org.type.id";

    public static String ORG_TYPE_CODE = "org.type.code";

    public static String ORG_ROOT_CODE = "org.root.code";

    public static String ORG_ROOT_ID = "org.root.id";

    public static String ORG_ROOT_NAME = "org.root.name";

    public static String IS_NEED_PASSWORD = "isNeedPassword";

    public static String PARTY_CONGRESS = "partyCongress";
    public static String PARTY_GROUP = "partyGroup";
    public static String BRACH_COMMITTEE = "brachCommittee";
    public static String PARTY_CLASS = "partyClass";

    public static String SIAM_ADDRESS = "siam.address";
    public static String SIAM_HEADER_NAMESPACE="siam.header.namespace";
    public static String SIAM_HEADER_LOCALPART="siam.header.localpart";
    public static String SIAM_HEADER_USERNAME="siam.header.username";
    public static String SIAM_HEADER_PASSWORD="siam.header.password";
    /**
     * ad域认证账号
     */
    public static String AD_AUTH_USERNAME = "ad.auth.username";
    /**
     * ad域认证密码
     */
    public static String AD_AUTH_PASSWORD = "ad.auth.password";
    /**
     * ad域认证地址
     */
    public static String AD_AUTH_URL = "ad.auth.url";
    /**
     * 项目域名地址
     */
    public static String APP_URL = "app_url";
    public static String getString(String key)
    {
        return RESOURCE_BUNDLE.getString(key);
    }
}
