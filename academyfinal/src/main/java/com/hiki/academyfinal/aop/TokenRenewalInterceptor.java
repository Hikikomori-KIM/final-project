package com.hiki.academyfinal.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.hiki.academyfinal.configuration.TokenProperties;
import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.vo.ClaimVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TokenRenewalInterceptor implements HandlerInterceptor{
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private TokenProperties tokenProperties;
	
	@Override
	public boolean preHandle(HttpServletRequest request, 
										HttpServletResponse response, Object handler)
			throws Exception {
		try {
			if(request.getMethod().equalsIgnoreCase("options")) {
				return true;
			}

			String accessToken = request.getHeader("Authorization");
			if(accessToken == null) return true;
			
			//시간계산이거어캐하는데
			long ms = tokenService.getRemainTime(accessToken);
			if(ms >= tokenProperties.getRenewalLimit() * 60L * 1000L) return true; 
			
			//재발행
			ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
			response.setHeader("Access-Control-Expose-Headers", "Access-Token");
			response.setHeader("Access-Token", tokenService.generateAccessToken(claimVO));
			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return true;
		}
	}

}
