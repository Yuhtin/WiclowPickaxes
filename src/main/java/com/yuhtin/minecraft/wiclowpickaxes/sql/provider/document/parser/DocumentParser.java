package com.yuhtin.minecraft.wiclowpickaxes.sql.provider.document.parser;

import com.yuhtin.minecraft.wiclowpickaxes.sql.provider.document.Document;

public interface DocumentParser<T> {

    T parse(Document document);

}
