package ru.wwerlosh.vktestcase.handlers.users.dto;

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
public class Address {

    @NotBlank(groups = {PutValidationGroup.class, PostValidationGroup.class})
    @NotEmpty(groups = {PutValidationGroup.class, PostValidationGroup.class})
    private String street;

    @NotBlank(groups = {PutValidationGroup.class, PostValidationGroup.class})
    @NotEmpty(groups = {PutValidationGroup.class, PostValidationGroup.class})
    private String suite;

    @NotBlank(groups = {PutValidationGroup.class, PostValidationGroup.class})
    @NotEmpty(groups = {PutValidationGroup.class, PostValidationGroup.class})
    private String city;

    @NotBlank(groups = {PutValidationGroup.class, PostValidationGroup.class})
    @NotEmpty(groups = {PutValidationGroup.class, PostValidationGroup.class})
    private String zipcode;

    @Valid
    private Geo geo;
}
