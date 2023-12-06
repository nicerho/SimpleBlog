package SimpleBoard.sb.service;

import SimpleBoard.sb.domain.Article;
import SimpleBoard.sb.dto.AddArticleRequest;
import SimpleBoard.sb.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    //글 추가
    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());

    }
}
