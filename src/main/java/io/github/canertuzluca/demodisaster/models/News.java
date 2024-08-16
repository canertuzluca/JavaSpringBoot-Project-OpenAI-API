package io.github.canertuzluca.demodisaster.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class News {
    private long id;
    private String title;
    private String author;
    private String content;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
}
