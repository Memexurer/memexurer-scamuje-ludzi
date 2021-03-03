package pl.nordhc.core.bukkit.system.data.codec;

import org.bson.Document;

public interface Converter<T> {

  Document encode(T t);

  T decode(Document document);

  Class<T> getConvertedClass();
}
