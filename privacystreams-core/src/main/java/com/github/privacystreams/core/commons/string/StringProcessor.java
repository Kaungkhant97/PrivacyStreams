package com.github.privacystreams.core.commons.string;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.commons.ItemFunction;
import com.github.privacystreams.core.utils.Assertions;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 28/12/2016.
 * Process the string field in an item.
 */
abstract class StringProcessor<Tout> extends ItemFunction<Tout> {

    private final String stringField;

    StringProcessor(String stringField) {
        this.stringField = Assertions.notNull("stringField", stringField);
        this.addParameters(stringField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        String stringValue = input.getValueByField(this.stringField);
        return this.processString(stringValue);
    }

    protected abstract Tout processString(String stringValue);

}