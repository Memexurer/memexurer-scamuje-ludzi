package pl.memexurer.kguild5.bukkit.system.data.codec;

import java.util.HashMap;
import java.util.Map;
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

  @Override
  public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
    Converter<T> converter = findAssignableConverter(clazz); //codec for itemstacks wont work without this
    if (converter == null) {
      return null;
    }

    return new ConverterCodec<>(converter, clazz, registry);
  }

  @SuppressWarnings("unchecked")
  private <T> Converter<T> findAssignableConverter(Class<T> tClass) {
    for(Map.Entry<Class<?>, Converter<?>> converterEntry: classConverterMap.entrySet())
      if(converterEntry.getKey().isAssignableFrom(tClass))
        return (Converter<T>) converterEntry.getValue();
      return null;
  }
}
