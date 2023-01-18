package shop.itbook.itbookauth.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import shop.itbook.itbookauth.exception.UsernamePasswordNotMatchException;

/**
 * JWT 인증을 구현하기 위한 UsernamePasswordAuthenticationFilter 대신 사용할 CustomFilter 클래스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */

@Slf4j
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public CustomAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
        throws AuthenticationException, IOException {

        Map<String, String> requestBody = getRequestBody(request);
        String memberId = requestBody.get("memberId");
        String password = requestBody.get("password");

        return getAuthenticationManager().authenticate(
            new UsernamePasswordAuthenticationToken(memberId, password)
        );
    }

    /**
     * 필터에 들어온 HttpServletRequest 로부터 RequestBody를 가져오는 메서드 입니다.
     *
     * @param request the request
     * @return the request body
     * @throws IOException the io exception
     */
    private Map<String, String> getRequestBody(HttpServletRequest request) throws IOException {

        ServletInputStream inputStream = request.getInputStream();

        if (Objects.isNull(inputStream)) {
            throw new UsernamePasswordNotMatchException();
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        bufferedReader.close();

        return jsonStringToMap(stringBuilder.toString());
    }

    /**
     * JsonString 타입을 Map 으로 변환해주는 역할을 하는 메서드 입니다.
     *
     * @param jsonString the json string
     * @return the map
     * @throws JsonProcessingException the json processing exception
     */
    private Map<String, String> jsonStringToMap(String jsonString) throws JsonProcessingException {
        return new ObjectMapper().readValue(jsonString, Map.class);
    }
}
