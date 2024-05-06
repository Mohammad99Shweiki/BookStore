package com.projects.bookstore.books;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@ToString
public class BookReadDTO extends BookDTO {
    private Integer available;

    private Integer sold;
}
