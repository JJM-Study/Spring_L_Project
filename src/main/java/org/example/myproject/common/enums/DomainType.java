package org.example.myproject.common.enums;

import lombok.Getter;

@Getter
public enum DomainType {
    PRODUCT("product"),
    REVIEW("review"),
    Board("board");

    private final String dirName;

    DomainType(String dirName) {
        this.dirName = dirName;
    }

}
