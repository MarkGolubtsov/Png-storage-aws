package com.mark.task.storage;

import org.springframework.core.io.Resource;

public interface ResourceStorage {
    String saveResource(Resource resource);

    byte[] readResource(String name);
}
