package com.opencourse.chapter.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencourse.chapter.exceptions.CustomAuthenticationException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Component
@AllArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter{

    private final JwtProvider provider;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {
        String token=request.getHeader("Authentication");
        if(token!=null){//token exists
            if(provider.isValide(token)){//token is valid

            Authentication auth=provider.getAuth(token);
            SecurityContextHolder.getContext().setAuthentication(auth);

            }else{//token not valid
                ApiError error=new ApiError();
                error.setStatus(HttpStatus.UNAUTHORIZED);
                error.setMsg("your session has expired");
                ObjectMapper mapper=new ObjectMapper();
                ServletOutputStream stream= response.getOutputStream();
                stream.print(mapper.writeValueAsString(error));
                stream.flush();
                return;
            }

        }else{//token doe not exists
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        filterChain.doFilter(request, response);    
    }
    
}

@Getter
@Setter
class ApiError{
    private HttpStatus status;
    private String msg;
    private List<String> errors;
}