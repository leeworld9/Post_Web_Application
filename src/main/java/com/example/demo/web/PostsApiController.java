package com.example.demo.web;

import com.example.demo.service.posts.PostsService;
import com.example.demo.web.dto.PostsResponseDto;
import com.example.demo.web.dto.PostsSaveRequestDto;
import com.example.demo.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    /*
    스프링에서 Bean을 주입하는 방법으로는 `@Autowired`, `setter`, `생성자` 와 같은 방식이 있으며,
    흔히들 사용하는 `@Autowired`로 Bean을 주입하는 방법은 권장되지 않고 있음.

    권장되는 방법은 `생성자`로 주입하는 방법이며, 롬복의 `@RequiredArgsConstructor` 어노테이션을 사용하면
    롬복이 final이 선언된 모든 필드의 생성자를 대신 생성해줘서 간편하게 이용할 수 있음.
    또한 롬복의 어노테이션의 장점으로는 생성자 코드를 계속해서 수정하는 번거로움을 해결할 수 있음.
     */
    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById (@PathVariable Long id) {
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);
        return id;
    }
}