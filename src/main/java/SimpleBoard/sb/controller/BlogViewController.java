package SimpleBoard.sb.controller;

import SimpleBoard.sb.domain.Article;
import SimpleBoard.sb.dto.ArticleListViewResponse;
import SimpleBoard.sb.dto.ArticleViewResponse;
import SimpleBoard.sb.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogViewController {
    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream().map(ArticleListViewResponse::new).toList();
        model.addAttribute("articles", articles);
        System.out.println("test");
        return "articleList";
    }

    @GetMapping("articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));
        return "article";
    }
}
