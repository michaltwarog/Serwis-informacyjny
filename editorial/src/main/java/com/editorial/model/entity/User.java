package com.editorial.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    @NotNull
    private Boolean enabled;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Authority authority;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserDetails userDetails;

    @OneToMany(mappedBy = "journalist", cascade = CascadeType.ALL)
    private List<ArticleProposal> articleProposals;

    @OneToMany(mappedBy = "journalist", cascade = CascadeType.ALL)
    private List<ArticleDraft> articleDrafts;

    @OneToMany(mappedBy = "corrector", cascade = CascadeType.ALL)
    private List<ArticleCorrect> articleCorrectsCorrector;

    public void connectAuthority(Authority authority) {
        if (authority == null)
            return;
        this.authority = authority;
    }

    public void connectUserDetails(UserDetails userDetails) {
        if (userDetails == null)
            return;
        this.userDetails = userDetails;
    }

    public void addArticleProposal(ArticleProposal articleProposal) {
        if (articleProposal == null)
            return;
        if (articleProposals == null)
            articleProposals = new ArrayList<>();
        articleProposals.add(articleProposal);
        articleProposal.setJournalist(this);
    }

    public void removeArticleProposal(ArticleProposal articleProposal) {
        if (articleProposal == null || articleProposals == null)
            return;
        articleProposals.remove(articleProposal);
        articleProposal.setJournalist(null);
    }

    public void addArticleDraft(ArticleDraft articleDraft) {
        if (articleDraft == null)
            return;
        if (articleDrafts == null)
            articleDrafts = new ArrayList<>();
        articleDrafts.add(articleDraft);
        articleDraft.setJournalist(this);
    }

    public void removeArticleDraft(ArticleDraft articleDraft) {
        if (articleDraft == null || articleDrafts == null)
            return;
        articleDrafts.remove(articleDraft);
        articleDraft.setJournalist(null);
    }

    public void addArticleCorrectCorrector(ArticleCorrect articleCorrect) {
        if (articleCorrect == null)
            return;
        if (articleCorrectsCorrector == null)
            articleCorrectsCorrector = new ArrayList<>();
        articleCorrectsCorrector.add(articleCorrect);
        articleCorrect.setCorrector(this);
    }

    public void removeArticleCorrectCorrector(ArticleCorrect articleCorrect) {
        if (articleCorrect == null || articleCorrectsCorrector == null)
            return;
        articleCorrectsCorrector.remove(articleCorrect);
        articleCorrect.setCorrector(null);
    }


    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        User other = (User) o;
        return Objects.equals(username, other.username);
    }
}
