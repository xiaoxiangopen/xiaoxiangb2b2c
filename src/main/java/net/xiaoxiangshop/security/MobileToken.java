package net.xiaoxiangshop.security;

import net.xiaoxiangshop.entity.User;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

import java.io.Serializable;

public class MobileToken implements HostAuthenticationToken, RememberMeAuthenticationToken, Serializable {

    // 手机号码
    private String mobile;
    private String smsCode;
    private boolean rememberMe;
    private String host;

    /**
     * 重写getPrincipal方法
     */
    public Object getPrincipal() {
        return mobile;
    }

    /**
     * 重写getCredentials方法
     */
    public Object getCredentials() {
        return smsCode;
    }

    public MobileToken() { this.rememberMe = false; }

    public MobileToken(String mobile,String smsCode) { this(mobile,smsCode, false, null); }

    public MobileToken(String mobile,String smsCode, boolean rememberMe) { this(mobile,smsCode, rememberMe, null); }

    public MobileToken(String mobile,String smsCode, boolean rememberMe, String host) {
        this.rememberMe = false;
        this.mobile = mobile;
        this.smsCode=smsCode;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void clear() {
        this.mobile = null;
        this.host = null;
        this.rememberMe = false;
        this.smsCode=null;

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" - ");
        sb.append(this.mobile);
        sb.append(", rememberMe=").append(this.rememberMe);
        if (this.host != null) {
            sb.append(" (").append(this.host).append(")");
        }

        return sb.toString();
    }
}