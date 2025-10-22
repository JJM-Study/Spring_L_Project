package org.example.myproject.config;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.web.util.HtmlUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class XssReReadableRequestWrapper extends HttpServletRequestWrapper {

    private byte[] cachedBody;
    private final Map<String, String[]> sanitizedParameters;

    public XssReReadableRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        cacheRequestBody(request);

        this.sanitizedParameters = sanitizeParameterMap(request.getParameterMap());

    }


    // 요청 바디에 메모리 캐싱
    private void cacheRequestBody(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        this.cachedBody = inputStream.readAllBytes();
    }

    private Map<String, String[]> sanitizeParameterMap(Map<String, String[]> parameterMap) {
        Map<String, String[]> sanitizedMap = new HashMap<>();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String[] sanitizedValues = Arrays.stream(entry.getValue())
                    .map(this::sanitizeValue)
                    .toArray(String[]::new);
            sanitizedMap.put(entry.getKey(), sanitizedValues);
        }

        return sanitizedMap;
    }


    private String sanitizeValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String decodedValue = Jsoup.parse(value).text();

        return Jsoup.clean(decodedValue, Safelist.none());

    }


    @Override
    public String getParameter(String name) {
        String[] values = sanitizedParameters.get(name);
        return (values != null && values.length > 0) ? values[0] : null;
    }

    @Override
    public String[] getParameterValues(String name) {
        return sanitizedParameters.get(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(sanitizedParameters);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(sanitizedParameters.keySet());
    }

    // ============== 바디 재사용을 위한 메서드 오버라이드 ============

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedBody);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    /**
     * JSON 바디도 정화하여 반환 (필요시 사용)
     */
    public String getSanitizedBody() throws IOException {
        String body = new String(cachedBody, StandardCharsets.UTF_8);
        return sanitizeValue(body);
    }



}
