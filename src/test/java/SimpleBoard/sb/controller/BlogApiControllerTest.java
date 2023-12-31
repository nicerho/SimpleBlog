package SimpleBoard.sb.controller;

import SimpleBoard.sb.domain.Article;
import SimpleBoard.sb.dto.AddArticleRequest;
import SimpleBoard.sb.dto.UpdateArticleRequest;
import SimpleBoard.sb.repository.BlogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.xml.transform.Result;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }

    @DisplayName("addTest")
    @Test
    public void addArticle() throws Exception {
        //given
        final String url = "/api/articles";
        final String title = "제목";
        final String content = "testContent";
        final AddArticleRequest request = new AddArticleRequest(title, content);
        //request를 JSON으로
        final String requestBody = objectMapper.writeValueAsString(request);
        //when
        ResultActions result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody));
        //then
        result.andExpect(status().isCreated());
        List<Article> articles = blogRepository.findAll();
        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAllArticles")
    @Test
    public void findAllArticles() throws Exception {
        //given
        final String url = "/api/articles";
        final String title = "제목";
        final String content = "testContent";
        blogRepository.save(Article.builder().title(title).content(content).build());
        //when
        final ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));
    }

    @Test
    public void findArticle() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "제목";
        final String content = "testContent";
        Article testArticle = blogRepository.save(Article.builder().title(title).content(content).build());
        final ResultActions resultActions = mockMvc.perform(get(url, testArticle.getId()));
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.content").value(content)).andExpect(jsonPath("$.title").value(title));
    }

    @Test
    public void deleteArticle() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "제목";
        final String content = "testContent";
        Article testArticle = blogRepository.save(Article.builder().title(title).content(content).build());
        mockMvc.perform(delete(url, testArticle.getId())).andExpect(status().isOk());
        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();

    }

    @Test
    public void updateArticle() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "제목";
        final String content = "testContent";
        Article testArticle = blogRepository.save(Article.builder().title(title).content(content).build());
        final String newTitle = "newTitle";
        final String newContent = "newContent";
        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);
        ResultActions result = mockMvc.perform(put(url, testArticle.getId()).contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request)));
        result.andExpect(status().isOk());
        Article article = blogRepository.findById(testArticle.getId()).get();
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }
}