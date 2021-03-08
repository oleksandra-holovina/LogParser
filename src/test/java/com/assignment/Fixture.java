package com.assignment;

public class Fixture {
    public static final String ERROR_LOG = "2020-04-01 10:10:09 ServiceClient - Error while processing row - service returned status code 500";
    public static final String NON_ERROR_LOG = "2020-04-01 10:10:08 ServiceClient - Job started";
    public static final String NON_ERROR_LOG_EARLIER = "2020-04-01 10:05:09 ServiceClient - Job started";
    public static final String LOG_WITHOUT_DATE = "ServiceClient - Error while processing row";
    public static final String LOG_WITHOUT_CLASSNAME = "2020-04-01 10:10:09 - Error while processing row";
    public static final String LOG_WITHOUT_MESSAGE = "2020-04-01 10:10:09 ServiceClient";
    public static final String LOG_INVALID_DATE = "2020-04-01T10:10:09Z ServiceClient - Job started";
}
