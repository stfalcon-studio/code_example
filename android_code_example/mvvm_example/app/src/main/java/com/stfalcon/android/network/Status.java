package com.stfalcon.android.network;

import java.util.UnknownFormatConversionException;

/**
 * Created by troy379 on 03.05.16.
 */
public enum Status {

    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204),

    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),

    INTERNAL_SERVER_ERROR(500),
    BAD_GATEWAY(502);

    private int value;

    Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Status fromInt(int status) {
        switch (status) {
            case 200: return OK;
            case 201: return CREATED;
            case 202: return ACCEPTED;
            case 204: return NO_CONTENT;

            case 400: return BAD_REQUEST;
            case 401: return UNAUTHORIZED;
            case 403: return FORBIDDEN;
            case 404: return NOT_FOUND;

            case 500: return INTERNAL_SERVER_ERROR;
            case 502: return BAD_GATEWAY;
        }
        throw new UnknownFormatConversionException(status + " is not supported. Forgot to add this status to "
                + Status.class.getName() + "?");
    }
}

