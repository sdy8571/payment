package com.payment.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.payment.interceptor.domain.RequestHeaderContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shen_dy@halcyonz.com
 * @date 2022/8/27
 */
@Slf4j
@Component
public class RequestHeaderContextInterceptor implements HandlerInterceptor {

  private static final String REAL_IP_KEY = "X-Real-IP";
  private static final String FORWARDED_FOR_KEY = "X-Forwarded-For";
  private static final String PROXY_CLIENT_KEY = "Proxy-Client-IP";
  private static final String WL_PROXY_CLIENT_KEY = "WL-Proxy-Client-IP";
  private static final String HTTP_CLIENT_IP_KEY = "HTTP_CLIENT_IP";
  private static final String HTTP_FORWARDED_FOR_KEY = "HTTP_X_FORWARDED_FOR";
  private static final String UNKNOWN = "unknown";
  private static final String JWT_PAYLOAD_USERNAME = "username";
  private static final String JWT_PAYLOAD_USERID = "userId";
  private static final String JWT_PAYLOAD_SHOPID = "shopId";

  @Resource
  private Environment env;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    handleRequestHeader(request);

    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    // 清空线程
    RequestHeaderContext.clean();
  }

  private void handleRequestHeader(HttpServletRequest request) {
    // 获取参数
    String path = request.getServletPath();
    log.info("请求路径：{}", path);
    String terminalType = request.getHeader("DE-terminalType");
    String versionInfo = request.getHeader("DE-versionInfo");
    String deviceInfo = request.getHeader("DE-deviceInfo");
    String localAdr = getIpAddress(request);
    String remoteAdr = localAdr;
    String requestURI = request.getRequestURI();
    // 设置参数
    new RequestHeaderContext.Builder().terminalType(terminalType).deVersionInfo(versionInfo)
            .deDeviceInfo(deviceInfo).localAddr(localAdr).remoteAddr(remoteAdr).requestURI(requestURI).build();
  }

  private static String getIpAddress(HttpServletRequest request) {
    String xFor = request.getHeader(FORWARDED_FOR_KEY);
    if (StringUtils.isNotBlank(xFor) && !UNKNOWN.equalsIgnoreCase(xFor)) {
      int index = xFor.indexOf(",");
      if (index > 0) {
        return xFor.substring(0, index);
      }
      return xFor;
    }
    xFor = request.getHeader(REAL_IP_KEY);
    if (StringUtils.isNotBlank(xFor) && !UNKNOWN.equalsIgnoreCase(xFor)) {
      return xFor;
    }
    if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
      xFor = request.getHeader(PROXY_CLIENT_KEY);
    }
    if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
      xFor = request.getHeader(WL_PROXY_CLIENT_KEY);
    }
    if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
      xFor = request.getHeader(HTTP_CLIENT_IP_KEY);
    }
    if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
      xFor = request.getHeader(HTTP_FORWARDED_FOR_KEY);
    }
    if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
      xFor = request.getRemoteAddr();
    }
    return xFor;
  }

}
