package pl.memexurer.kguild5.bukkit.system.data.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class ConverterCodec<T> implements Codec<T> {

  private final Converter<T> converter;
  private final Class<T> tClass;
  private final Codec<Document> documentCodec;

  public ConverterCodec(Converter<T> converter, Class<T> tClass,
      Codec<Document> documentCodec) {
    this.converter = converter;
    this.tClass = tClass;
    this.documentCodec = documentCodec;
  }

  @Override
  public T decode(BsonReader reader, DecoderContext decoderContext) {
    return converter.decode(documentCodec.decode(reader, decoderContext));
  }

  @Override
  public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
    documentCodec.encode(writer, converter.encode(value), encoderContext);
  }

  @Override
  public Class<T> getEncoderClass() {
    return tClass;
  }
}
