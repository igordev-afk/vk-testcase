package ru.wwerlosh.vktestcase.handlers.posts;

import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PatchValidationGroup;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PostValidationGroup;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PutValidationGroup;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/posts/{id}")
    public PostDTO getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @DeleteMapping("/posts/{id}")
    public Long deletePostById(@PathVariable Long id) {
        return postService.deletePostById(id);
    }

    @PostMapping("/posts")
    public PostDTO addNewPost(@Validated(PostValidationGroup.class) @RequestBody PostDTO postDTO) {
        return postService.save(postDTO);
    }

    @PutMapping("/posts/{id}")
    public PostDTO updatePostById(@PathVariable Long id,
                                  @Validated(PutValidationGroup.class) @RequestBody PostDTO postDTO) {
        return postService.updatePostById(id, postDTO);
    }

    @PatchMapping("/posts/{id}")
    public PostDTO updatePostFieldsById(@PathVariable Long id,
                                        @Validated(PatchValidationGroup.class) @RequestBody PostDTO postDTO) {
        return postService.updatePostFieldsById(id, postDTO);
    }

}
