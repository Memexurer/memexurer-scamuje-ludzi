package pl.nordhc.core.bukkit.system.data.codec;

import java.util.HashMap;
import java.util.Map;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class ConverterProvider implements CodecProvider {

  private final Map<Class<?>, Converter<?>> classConverterMap = new HashMap<>();

  public ConverterProvider(Converter<?>... converters) {
    for (Converter<?> converter : converters) {
      classConverterMap.put(converter.getConvertedClass(), converter);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
    Codec<Document> documentCodec = registry.get(Document.class);

    Converter<T> converter = (Converter<T>) classConverterMap.get(clazz);
    if (converter == null) {
      return null;
    }

    return new ConverterCodec<>(converter, clazz, documentCodec);
  }
}
