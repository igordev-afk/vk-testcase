package ru.wwerlosh.vktestcase.handlers.users.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class UserDTO {

    private Long id;

    @NotBlank(groups = {PutValidationGroup.class, PostValidationGroup.class})
    @NotEmpty(groups = {PutValidationGroup.class, PostValidationGroup.class})
    private String name;

    @NotBlank(groups = {PutValidationGroup.class, PostValidationGroup.class})
    @NotEmpty(groups = {PutValidationGroup.class, PostValidationGroup.class})
    private String username;

    @NotBlank(groups = {PutValidationGroup.class, PostValidationGroup.class})
    @NotEmpty(groups = {PutValidationGroup.class, PostValidationGroup.class})
    private String email;

    @Valid
    private Address address;

    @NotBlank(groups = {PutValidationGroup.class, PostValidationGroup.class})
    @NotEmpty(groups = {PutValidationGroup.class, PostValidationGroup.class})
    private String phone;

    @NotBlank(groups = {PutValidationGroup.class, PostValidationGroup.class})
    @NotEmpty(groups = {PutValidationGroup.class, PostValidationGroup.class})
    private String website;

    @Valid
    private Company company;

}