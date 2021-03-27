package pl.memexurer.kguild5.bukkit.system.data.codec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public final class CodecHelper {

  private final CodecRegistry registry;

  public CodecHelper(CodecRegistry registry) {
    this.registry = registry;
  }

  public <T> T reinterpret(Document document, Class<T> tClass) {
    BsonDocument bson = document.toBsonDocument(null, registry); //bson api broken!?1//?!!?

    return registry.get(tClass)
        .decode(bson.asBsonReader(), DecoderContext.builder().build());
  }

  public <T> Map<String, T> reinterpretMap(Document document, Class<T> mapValueType) {
    Map<String, T> map = new HashMap<>();

    for (Map.Entry<String, Object> mapEntry : document.entrySet()) {
      map.put(mapEntry.getKey(), reinterpret((Document) mapEntry.getValue(), mapValueType));
    }

    return map;
  }

  public <T> List<T> reinterpretList(List<Document> documentList, Class<T> userBackupClass) {
    return documentList
        .stream()
        .map(d -> d == null ? null : reinterpret(d, userBackupClass))
        .collect(Collectors.toList());
  }
}
