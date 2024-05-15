package org.ilia.licensingservice.entity.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@AllArgsConstructor
@Data
public class ResponseWrapper {

    private Object data;
    private Object metadata;
    private List<ErrorMessage> errors;
}
