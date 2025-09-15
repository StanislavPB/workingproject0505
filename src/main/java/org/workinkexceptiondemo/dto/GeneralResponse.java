package org.workinkexceptiondemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponse <T> {
    private HttpStatus code;
    private T body;
    private String message;


}
