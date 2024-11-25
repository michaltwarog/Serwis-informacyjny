package com.client.controller;

import com.client.model.dto.ArticleCorrectToClientDto;
import com.client.model.dto.ArticleDto;
import com.client.model.entity.User;
import com.client.service.ArticleService;
import com.client.service.UserActionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client/articles")
public class ArticleController {

    private ArticleService articleService;
    private UserActionService userActionService;

    @Autowired
    public ArticleController(ArticleService articleService, UserActionService userActionService) {
        this.articleService = articleService;
        this.userActionService = userActionService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleDto>> getAllArticles(@RequestParam(name = "category", required = false) String category) {

        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/pages")
    public ResponseEntity<List<ArticleDto>> getAllArticlesPaged(@RequestParam(name = "page", required = false) Integer page,
                                                                @RequestParam(name = "size", required = false) Integer size,
                                                                @RequestParam(name = "category", required = false) String category) {

        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/fe")
    public ResponseEntity<String> addArticleFromEditorial(@Valid @RequestBody ArticleCorrectToClientDto articleCorrectToClientDto, @RequestHeader("X-Caller") String caller) {

        return ResponseEntity.ok("Successful moved");
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<String> deleteAndMoveArticleToEditorialService(@RequestParam(name = "id") Long articleId, HttpServletRequest request) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");
    }
}
