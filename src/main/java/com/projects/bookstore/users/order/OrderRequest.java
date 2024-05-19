package com.projects.bookstore.users.order;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderRequest {
    private String address;

    private String phoneNo;
}
