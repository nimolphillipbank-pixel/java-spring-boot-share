package com.app.domain.model;

import java.util.Date;

public record Credential(String token, String tokenType, Date exp, boolean isRefresh) {
}
