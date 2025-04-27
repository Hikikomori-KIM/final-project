package com.hiki.academyfinal.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.vo.ClaimVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class LoginInterceptor  implements HandlerInterceptor{
	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//옵션통과(테스트용)
		if(request.getMethod().equalsIgnoreCase("options")) {
			return true;
		}
		//(authorization조회)
		try {
			String authorization = request.getHeader("Authorization");
			if(authorization == null) throw new Exception("Authorization 헤더가 없습니다");
			if(authorization.startsWith("Bearer ") == false)
				throw new Exception("Authorization 헤더가 Bearer로 시작하지 않습니다");
			
			//베얼어뒤에있는토큰필요
			String token = authorization.substring("Bearer ".length());
			ClaimVO claimVO = tokenService.parse(token);
//			request.setAttribute("claim", claimVO);
			return true;
		}
		catch(Exception e) {
			response.sendError(401);
			return false;
		}
	}
}
