package pl.memexurer.kguild5.bukkit.system.data.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import org.bson.BsonBinary;

public class UserRepository {

  private final MongoCollection<UserDataModel> collection;

  public UserRepository(MongoDatabase database) {
    this.collection = database.getCollection("nordhc_users", UserDataModel.class);
  }

  public Iterator<UserDataModel> fetchUsers() {
    return collection.find()
        .iterator();
  }

  public void update(Collection<UserDataModel> dataModel) {
    collection.bulkWrite(dataModel.stream().map(
        d -> new ReplaceOneModel<>(Filters.eq(new BsonBinary(d.getUuid())), d, new ReplaceOptions().upsert(true)))
        .collect(Collectors.toList()));
  }
}
