package com.seoul_competition.senior_jobtraining.domain.post.api;

import com.seoul_competition.senior_jobtraining.domain.post.application.PostService;
import com.seoul_competition.senior_jobtraining.domain.post.dto.request.PostSaveReqDto;
import com.seoul_competition.senior_jobtraining.domain.post.dto.request.PostUpdateReqDto;
import com.seoul_competition.senior_jobtraining.domain.post.dto.response.PostDetailResDto;
import com.seoul_competition.senior_jobtraining.domain.post.dto.response.PostListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시글", description = "게시글에 대한 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

  private final PostService postService;

  @Operation(summary = "게시글 저장", description = "게시글을 저장합니다.")
  @PostMapping
  public ResponseEntity<Void> savePost(@RequestBody @Valid PostSaveReqDto reqDto) {
    Long postId = postService.savePost(reqDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .location(URI.create("/posts/" + postId))
        .build();
  }

  @Operation(summary = "게시글 목록 조회", description = "페이징 처리된 게시글 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<PostListResponse> getPosts(
      @PageableDefault(size = 20, sort = "createdAt", direction = Direction.DESC)
      Pageable pageable) {
    return ResponseEntity.ok(postService.getPosts(pageable));
  }


  @Operation(summary = "게시글 상세 조회", description = "특정 게시글의 상세 정보를 조회합니다.")
  @GetMapping("/{postId}")
  public ResponseEntity<PostDetailResDto> getPost(@PathVariable Long postId) {
    PostDetailResDto postDetailResDto = postService.getPost(postId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(postDetailResDto);
  }

  @Operation(summary = "게시글 수정", description = "특정 게시글을 수정합니다.")
  @PutMapping("/{postId}")
  public ResponseEntity<Void> updatePost(@PathVariable Long postId,
      @RequestBody @Valid PostUpdateReqDto reqDto) {
    postService.update(postId, reqDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .location(URI.create("/posts/" + postId))
        .build();
  }

  @Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제합니다.")
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable Long postId,
      @RequestBody Map<String, String> password) {
    postService.delete(postId, password.get("password"));
    return ResponseEntity.noContent().build();
  }
}
