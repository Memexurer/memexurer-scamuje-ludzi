package pl.nordhc.core.bukkit.system.data.flat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class FlatConfigurationUtils {
  private static final Gson gson = new Gson();

  private FlatConfigurationUtils() {
  }

  public static void load(Class<?> clazz, File file) throws IOException {
    try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
      JsonArray array = gson.fromJson(reader, JsonArray.class);

      int i = 0;
      for (Field field : clazz.getFields()) {
        JsonObject object = array.get(i++).getAsJsonObject();
        field.set(null, gson.fromJson(object.get(field.getName()), field.getType()));
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static void save(Class<?> clazz, File file) throws IOException {
    JsonArray jsonElements = new JsonArray();

    for (Field field : clazz.getFields()) {
      try {
        JsonObject object = new JsonObject();
        object.add(field.getName(), gson.toJsonTree(field.get(null)));
        jsonElements.add(object);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    Files.write(file.toPath(), gson.toJson(jsonElements).getBytes(StandardCharsets.UTF_8));
  }
}
