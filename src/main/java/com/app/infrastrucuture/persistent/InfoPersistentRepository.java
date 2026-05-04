package com.app.infrastrucuture.persistent;

import com.app.infrastrucuture.entity.Info;
import java.util.List;
import org.springframework.context.annotation.Profile;

@Profile("!test")
public interface InfoPersistentRepository {
  String newId();

  List<Info> findAll();

  Info findById(String id);

  Info insert(Info info);

  Info replace(String id, Info info);

  boolean removeById(String id);
}
