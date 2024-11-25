package com.editorial.controller;

import com.editorial.model.dto.ArticleProposalDto;
import com.editorial.model.entity.ArticleProposal;
import com.editorial.model.entity.User;
import com.editorial.service.ArticleProposalService;
import com.editorial.service.UserActionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/editorial/proposal")
public class ArticleProposalController {

    private final ArticleProposalService articleProposalService;
    private final UserActionService userActionService;

    @Autowired
    public ArticleProposalController(ArticleProposalService articleProposalService, UserActionService userActionService) {
        this.articleProposalService = articleProposalService;
        this.userActionService = userActionService;
    }

    @PostMapping
    public ResponseEntity<String> addArticle(@RequestBody @Valid ArticleProposalDto articleProposalDto) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");
    }

    @PutMapping
    public ResponseEntity<String> updateArticle(@RequestBody @Valid ArticleProposalDto articleProposalDto) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");
    }

    @GetMapping
    public ResponseEntity<List<ArticleProposalDto>> getArticles(Pageable pageable, @RequestParam(value = "title", required = false) String title,
                                                                @RequestParam(value = "acceptance", required = false) ArticleProposal.Acceptance acceptance) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of());
    }
}
