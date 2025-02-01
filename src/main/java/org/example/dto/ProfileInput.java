package org.example.dto;

import lombok.Data;

@Data
public class ProfileInput {
    private String firstName;
    private String lastName;
    private String aboutMe;
    private String pictureUrl;
    private BioInput bio;
}