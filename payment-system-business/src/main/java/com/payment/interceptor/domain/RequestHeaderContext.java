package com.payment.interceptor.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shen_dy@halcyonz.com
 * @date 2022/8/27
 */
@Slf4j
@Data
public class RequestHeaderContext {

  private static final String DE_VERSION_KEY = "version";
  private static final String DE_VERSION_NUM_KEY = "num";
  private static final String DE_VERSION_MANUAL_NUM = "manualNum";
  public static final String TERMINAL_WEB = "WEB";
  public static final int MAX_CHECK_NUM = 46;

  private static final ThreadLocal<RequestHeaderContext> REQUEST_HEADER_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

  private String terminalType;
  private String deVersionInfo;
  private String deDeviceInfo;
  private String appVersion;
  private int versionNum;
  private int manualNum;
  private String localAddr;
  private String remoteAddr;
  private String requestURI;

  public static RequestHeaderContext get() {
    return REQUEST_HEADER_CONTEXT_THREAD_LOCAL.get();
  }

  public void setContext(RequestHeaderContext context) {
    REQUEST_HEADER_CONTEXT_THREAD_LOCAL.set(context);
  }

  public static void clean() {
    REQUEST_HEADER_CONTEXT_THREAD_LOCAL.remove();
  }

  private RequestHeaderContext(Builder builder) {
    this.terminalType = builder.getTerminalType();
    this.deVersionInfo = builder.getDeVersionInfo();
    this.deDeviceInfo = builder.getDeDeviceInfo();
    this.appVersion = builder.getAppVersion();
    this.manualNum = builder.getManualNum();
    this.versionNum = builder.getVersionNum();
    this.localAddr = builder.getLocalAddr();
    this.remoteAddr = builder.getRemoteAddr();
    this.requestURI = builder.getRequestURI();
    setContext(this);
  }

  public static String splitAppVersion(String deVersionInfo) {
    Map<String, String> deVersionMap = splitVersion(deVersionInfo);
    if (CollectionUtils.isEmpty(deVersionMap)) {
      return null;
    }
    return deVersionMap.getOrDefault(DE_VERSION_KEY, null);
  }

  public static int splitAppVersionNum(String deVersionInfo) {
    Map<String, String> deVersionMap = splitVersion(deVersionInfo);
    if (CollectionUtils.isEmpty(deVersionMap)) {
      return 0;
    }
    return 0;
  }

  public static int splitManualNum(String deVersionInfo) {
    Map<String, String> deVersionMap = splitVersion(deVersionInfo);
    if (CollectionUtils.isEmpty(deVersionMap)) {
      return 0;
    }
    return 0;
  }

  private static Map<String, String> splitVersion(String deVersionInfo) {
    Map<String, String> deVersionMap = new HashMap<>(6);
    String[] deVersionInfos = StringUtils.splitPreserveAllTokens(deVersionInfo, "|");
    if (deVersionInfos != null) {
      for (String versionInfo : deVersionInfos) {
        String[] items = StringUtils.splitPreserveAllTokens(versionInfo, ":");
        if (items.length == 0) {
          continue;
        }
        if (items.length > 1) {
          deVersionMap.put(items[0], items[1]);
        } else {
          deVersionMap.put(items[0], "0");
        }
      }
    }
    return deVersionMap;
  }

  @Data
  public static class Builder {

    private String terminalType;
    private String deVersionInfo;
    private String deDeviceInfo;
    private String appVersion;
    private int versionNum;
    private int manualNum;
    private String localAddr;
    private String remoteAddr;
    private String requestURI;

    public Builder terminalType(String terminalType) {
      this.terminalType = terminalType;
      return this;
    }

    public Builder deVersionInfo(String deVersionInfo) {
      this.deVersionInfo = deVersionInfo;
      return this;
    }

    public Builder deDeviceInfo(String deDeviceInfo) {
      this.deDeviceInfo = deDeviceInfo;
      return this;
    }

    public Builder localAddr(String localAddr) {
      this.localAddr = localAddr;
      return this;
    }

    public Builder remoteAddr(String remoteAddr) {
      this.remoteAddr = remoteAddr;
      return this;
    }

    public Builder requestURI(String requestURI) {
      this.requestURI = requestURI;
      return this;
    }

    public String getAppVersion() {
      if (StringUtils.isNotBlank(deVersionInfo)) {
        return splitAppVersion(deVersionInfo);
      }
      return null;
    }

    public int getVersionNum() {
      if (StringUtils.isNotBlank(deVersionInfo)) {
        return splitAppVersionNum(deVersionInfo);
      }
      return -1;
    }

    public int getManualNum() {
      if (StringUtils.isNotBlank(deVersionInfo)) {
        return splitManualNum(deVersionInfo);
      }
      return -1;
    }

    public RequestHeaderContext build() {
      return new RequestHeaderContext(this);
    }

  }
}
