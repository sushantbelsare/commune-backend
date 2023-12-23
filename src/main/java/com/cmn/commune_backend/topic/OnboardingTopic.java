package com.cmn.commune_backend.topic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnboardingTopic {
    private String username;
    private String flow;
    private Date created_at = new Date();
}
