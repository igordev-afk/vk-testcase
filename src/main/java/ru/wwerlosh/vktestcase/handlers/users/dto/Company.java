package ru.wwerlosh.vktestcase.handlers.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PostValidationGroup;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PutValidationGroup;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Company {
    @NotBlank(groups = {PutValidationGroup.class, PostValidationGroup.class})
    @NotEmpty(groups = {PutValidationGroup.class, PostValidationGroup.class})
    private String name;

    @NotBlank(groups = {PutValidationGroup.class, PostValidationGroup.class})
    @NotEmpty(groups = {PutValidationGroup.class, PostValidationGroup.class})
    private String catchPhrase;

    @NotBlank(groups = {PutValidationGroup.class, PostValidationGroup.class})
    @NotEmpty(groups = {PutValidationGroup.class, PostValidationGroup.class})
    private String bs;

}
