package com.withins.dto;

import com.withins.entity.Organization;
import com.withins.enums.KoreanRegion;
import lombok.Getter;

@Getter
public class OrgRequest {

    private final String name;
    private final KoreanRegion region;
    
    public OrgRequest(Organization organization) {
        this.name = organization.getName();
        this.region = organization.getRegion();
    }
}
