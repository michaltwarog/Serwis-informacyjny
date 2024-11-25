package com.editorial.service;

import com.editorial.model.dto.ArticleCorrectDto;
import com.editorial.model.dto.ArticleCorrectToClientDto;
import com.editorial.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ArticleCorrectService {
    ResponseEntity<List<ArticleCorrectDto>> getCorrects(Pageable pageable, String title, Boolean isCorrected);
    ResponseEntity<String> updateArticle(ArticleCorrectDto articleCorrectDto, User loggedUser);
    ResponseEntity<String> deleteAndMoveArticleToArticleDraft(Long correctId);
    ResponseEntity<String> deleteAndMoveArticleToClientService(Long correctId, HttpServletRequest request, String category);
    void saveArticleCorrect(ArticleCorrectToClientDto articleCorrectToClientDto);
}
