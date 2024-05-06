package com.projects.bookstore.common;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class EmbeddingRequestBody {
    private String content;
}
