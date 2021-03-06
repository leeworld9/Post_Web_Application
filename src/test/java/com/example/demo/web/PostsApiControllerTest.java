package com.example.demo.web;

import com.example.demo.config.RestDocConfiguration;
import com.example.demo.domain.posts.Posts;
import com.example.demo.domain.posts.PostsRepository;
import com.example.demo.web.dto.PostsSaveRequestDto;
import com.example.demo.web.dto.PostsUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(RestDocConfiguration.class) // ????????? ?????? import
public class PostsApiControllerTest {
    /*
    @SpringBootTest??? @WebMvcTest??? ?????? ????????? ????????????. ( ?????????, ????????? ??? ????????? ???????????????. )
    @WebMvcTest??? WebSecurityConfigurerAdapter, WebMvcConfigurer??? ????????? @ControllerAdvice, @Controller??? ????????????.
    ???, @Repository, @Service, @Component??? ?????? ????????? ????????????.
     */

    @LocalServerPort
    private int port;

    /*
     @WebMvcTest??? ?????? JPA ????????? ???????????? ??????, Controller??? ControllerAdvice ??? ?????? ????????? ????????? ????????? ???????????????
     ?????? ?????? JPA ???????????? ????????? ???????????? ?????? @SpringBootTest ??? TestRestTemplate??? ???????????? ??????.
     */
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    // @SpringBootTest?????? MockMvc??? ???????????? ??????
    //  - @WithMockUser??? MockMvc????????? ???????????????.
    //  - when ????????? ????????? ?????? mvc??? ???????????? ???????????? ?????????????????????.
    /////////////// start /////////////////
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    // Spring Rest Docs??? ???????????? Junit ???????????? ?????????. (????????? 4 ????????? ?????? ????????????.)
    // JUnitRestDocumentation ????????? ???????????? ???, setup() ???????????? MockMvcBuilders?????? apply ???????????????.
    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }
    /////////////// end /////////////////

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="USER") // @WithMockUser : ????????? ?????? ???????????? ???????????? ?????? (roles??? ?????? ?????? ??????)
    public void Post_????????????() throws Exception {
        //given
        String title = "??????1";
        String content = "??????1";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("user1")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        //ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("PostSave-POST",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("author").type(JsonFieldType.STRING).description("????????? ?????????")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                        )
                ));

        //then
        //assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        //assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_????????????() throws Exception {
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("??????1")
                .content("??????1")
                .author("user1")
                .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "??????2";
        String expectedContent = "??????2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/{id}";
        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        //ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
        mvc.perform(RestDocumentationRequestBuilders.put(url, updateId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("PostUpdate-PUT",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                        ),
                        pathParameters(
                                parameterWithName("id").description("????????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ??????")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                        )
                ));

        //then
        //assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        //assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_????????????() throws Exception {
        //given
        String title = "??????1";
        String content = "??????1";

        Posts savedPosts = postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("user1")
                .build());

        String url = "http://localhost:" + port + "/api/v1/posts/{id}";
        Long findId = savedPosts.getId();

        //when
        //ResponseEntity<PostsResponseDto> responseEntity = restTemplate.getForEntity(url, PostsResponseDto.class, findId);
        mvc.perform(RestDocumentationRequestBuilders.get(url, findId))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("PostFind-GET",
                        pathParameters(
                                parameterWithName("id").description("????????? ??????")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ??????"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("author").type(JsonFieldType.STRING).description("????????? ?????????")
                        )
                ));

        //then
        //assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_????????????() throws Exception{
        //given
        String title = "??????1";
        String content = "??????1";

        Posts savedPosts = postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("user1")
                .build());

        String url = "http://localhost:" + port + "/api/v1/posts/{id}";
        Long findId = savedPosts.getId();

        //when
        //restTemplate.delete(url, findId);
        mvc.perform(RestDocumentationRequestBuilders.delete(url, findId))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("PostDelete-DELETE",
                        pathParameters(
                                parameterWithName("id").description("????????? ??????")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                        )
                ));

        //then
        Optional result = postsRepository.findById(findId);
        assertThat(result.isEmpty()).isTrue();
    }

}