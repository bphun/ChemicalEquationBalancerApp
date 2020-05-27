package com.bphan.ChemicalEquationBalancerApi.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureEntryPoint implements AuthenticationEntryPoint, Serializable {

  private static final long serialVersionUID = -8970718410437077606L;

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");

    Map<String, Object> data = new HashMap<>();

    data.put("timestamp", Calendar.getInstance().getTime());
    data.put("error", authException.getLocalizedMessage());

    response.getOutputStream().println(objectMapper.writeValueAsString(data));
  }
}
