package com.begac.minicrm.shared.infrastructure.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter{
	public static final String HEADER = "X-Correlation-Id";
	
	public static final String MDC_KEY = "correlationId";
	
	@Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
		
		System.out.println("CorrelationIdFilter triggered");

        // 1) Request’ten correlationId al
        String correlationId = request.getHeader(HEADER);

        // 2) Yoksa üret: Çünkü her request bir iz bırakmalı
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        // 3) MDC’ye koy: Aynı thread içindeki tüm loglar bunu görsün
        MDC.put(MDC_KEY, correlationId);

        // 4) Response’a geri yaz: Client tarafı debug edebilsin
        response.setHeader(HEADER, correlationId);

        try {
            // 5) Request’i zincirde devam ettir
            filterChain.doFilter(request, response);
        } finally {
            // 6) Temizle: Thread pool reuse yüzünden sızıntı olmasın
            MDC.remove(MDC_KEY);
        }
    }
}
