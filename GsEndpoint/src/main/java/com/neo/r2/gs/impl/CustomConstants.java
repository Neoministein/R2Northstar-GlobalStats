package com.neo.r2.gs.impl;

import com.neo.util.common.impl.exception.ExceptionDetails;

public class CustomConstants {

    public static final String URI_PREFIX = "api/v1";

    public static final ExceptionDetails EX_PLAYER_FOUND = new ExceptionDetails(
            "r2ts/result/no-player", "No player found with identifier {0}.", false
    );

    private CustomConstants() {}
}
