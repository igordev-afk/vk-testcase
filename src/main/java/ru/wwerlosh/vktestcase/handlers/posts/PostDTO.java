package ru.wwerlosh.vktestcase.handlers.posts;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.wwerlosh.vktestcase.handlers.validation.AtLeastOneNotBlankAndNotNull;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PatchValidationGroup;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PostValidationGroup;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PutValidationGroup;

@AllArgsConstructor
@NoArgsConstructor
@Data
@AtLeastOneNotBlankAndNotNull(groups = {PatchValidationGroup.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {

    private Long id;

    @NotNull(groups = {PostValidationGroup.class})
    private Long userId;

    @NotBlank(groups = {PutValidationGroup.class})
    @NotEmpty(groups = {PutValidationGroup.class})
    private String title;

    @NotBlank(groups = {PutValidationGroup.class})
    @NotEmpty(groups = {PutValidationGroup.class})
    private String body;
}
