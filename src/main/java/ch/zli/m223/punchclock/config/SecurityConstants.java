package ch.zli.m223.punchclock.config;

public class SecurityConstants {
    public static final String SECRET = "HZwdxxODgM3gCJ8fguLVGF66FlblGyW32MF8U75oWzdm7r1cZ1kmA1VI1wqX6viXToN3MS6m5bfPh0MA";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String BYPASS_AUTH_PATTERN = "/users/sign-up";
}
