package com.example.minioimplement.dto;

import lombok.*;

import java.io.InputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileDto {

    private String name;

    private InputStream inputStream;
}
