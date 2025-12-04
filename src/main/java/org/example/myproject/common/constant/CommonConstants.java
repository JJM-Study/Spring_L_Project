package org.example.myproject.common.constant;

public class CommonConstants {

    // State를 가지지 않는 순수한 상수 저장소 역할만 수행하니까 일부러 new를 막아놓음.
    private CommonConstants() {

    }

    public static final String ANONYMOUS_ID_KEY = "anonymous_user_id";

    public static final String ANONYMOUS_PREFIX = "ANON-";

}
