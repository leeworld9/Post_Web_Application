package com.example.demo.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final Environment env;

    @GetMapping("/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles()); // 현재 실행 중인 ActiveProfile을 모두 가져옵니다. (real, oauth, real-db)
        List<String> realProfiles = Arrays.asList("real", "real1", "real2"); // 무중단 배포(step3)에서는 real1과 real2만 사용되지만, step2를 다시 사용해볼 수도 있으니 real도 남겨둡니다.
        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);
        return profiles.stream().filter(realProfiles::contains).findAny().orElse(defaultProfile);
    }
}
