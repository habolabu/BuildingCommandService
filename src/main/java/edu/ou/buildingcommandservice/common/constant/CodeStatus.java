package edu.ou.buildingcommandservice.common.constant;

import edu.ou.coreservice.common.constant.BaseCodeStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CodeStatus {
    public static final String SERVICE_CODE = "21";
    public static final String SUCCESS = String.format(BaseCodeStatus.SUCCESS, SERVICE_CODE);
    public static final String INVALID_INPUT = String.format(BaseCodeStatus.INVALID_INPUT, SERVICE_CODE);
    public static final String NOT_FOUND = String.format(BaseCodeStatus.NOT_FOUND, SERVICE_CODE);
    public static final String CONFLICT = String.format(BaseCodeStatus.CONFLICT, SERVICE_CODE);
    public static final String UNKNOWN = String.format(BaseCodeStatus.UNKNOWN, SERVICE_CODE);
    public static final String SERVER_ERROR = String.format(BaseCodeStatus.SERVER_ERROR, SERVICE_CODE);
    public static final String NOT_EMPTY = String.format(BaseCodeStatus.NOT_EMPTY, SERVICE_CODE);
}