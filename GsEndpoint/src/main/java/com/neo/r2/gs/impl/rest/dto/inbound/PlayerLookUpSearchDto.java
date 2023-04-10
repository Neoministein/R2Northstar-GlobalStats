package com.neo.r2.gs.impl.rest.dto.inbound;

import com.neo.util.framework.rest.api.parser.InboundDto;

@InboundDto
public record PlayerLookUpSearchDto(
        String[] players) {
}
