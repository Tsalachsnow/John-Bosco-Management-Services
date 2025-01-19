package com.johnboscoltd.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.johnboscoltd.enums.AccountTypes;

import java.io.IOException;

public class AccountTypesDeserializer extends JsonDeserializer<AccountTypes> {

    @Override
    public AccountTypes deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String accountTypeString = jsonParser.getText();
        return AccountTypes.fromString(accountTypeString);
    }
}
