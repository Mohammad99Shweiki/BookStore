package com.projects.bookstore.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Document(indexName = "users")
public class UserDTO {
    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String password;

    private UserRole role;

    private List<String> wishlist;

    List<String> favoriteGenres;

    @Field(type = FieldType.Dense_Vector, store = true, dims = 384)
    @JsonIgnore
    private List<Float> embedding;
}
