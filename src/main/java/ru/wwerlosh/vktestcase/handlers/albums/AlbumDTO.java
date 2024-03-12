package ru.wwerlosh.vktestcase.handlers.albums;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PatchValidationGroup;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PostValidationGroup;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlbumDTO {

    private Long id;

    @NotNull(groups = {PostValidationGroup.class})
    private Long userId;

    @NotBlank(groups = {PatchValidationGroup.class, PostValidationGroup.class})
    @NotEmpty(groups = {PatchValidationGroup.class, PostValidationGroup.class})
    private String title;
}
