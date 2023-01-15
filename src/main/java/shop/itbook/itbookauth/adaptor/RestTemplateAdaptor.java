package shop.itbook.itbookauth.adaptor;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import shop.itbook.itbookauth.dto.response.CommonResponseBody;
import shop.itbook.itbookauth.dto.response.MemberInfoResponseDto;

/**
 * RestTemplate 통신 메서드를 제공해주는 클래스 입니다.
 *
 * @author 강명관
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
public class RestTemplateAdaptor {

    private final RestTemplate restTemplate;

    /**
     * RestTemplate 을 통해 Http GET 요청을 하기 위한 메서드 입니다.
     *
     * @param url the url
     * @return the adaptor
     */
    public ResponseEntity<CommonResponseBody<MemberInfoResponseDto>> getMemberInfoAdaptor(
        String url) {

        return restTemplate.exchange(
            url,
            HttpMethod.GET,
            new HttpEntity<>(new HttpHeaders()),
            new ParameterizedTypeReference<>() {
            });
    }
}

