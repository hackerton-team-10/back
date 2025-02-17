package com.api.back.domain.designer.exception;

import com.api.back.global.error.exception.BusinessLogicException;
import com.api.back.global.error.exception.ErrorCode;

public class DesignerNotFoundException extends BusinessLogicException {
    public DesignerNotFoundException() {
        super(ErrorCode.DESIGNER_NOT_FOUND);
    }
}
