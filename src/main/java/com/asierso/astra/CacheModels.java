
package com.asierso.astra;

import com.asierso.astra.exceptions.ModelLoadException;
import com.asierso.astra.models.ScrapperManifest;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Asierso
 */
public class CacheModels {

    private final TreeMap<String, ScrapperManifest> modelsCache;
    private static CacheModels cachedModels;

    private CacheModels() {
        modelsCache = new TreeMap<>();
    }

    public static CacheModels getInstance() {
        if (cachedModels == null) {
            cachedModels = new CacheModels();
        }
        return cachedModels;
    }

    public String loadModelManifest(FileManager file) throws ModelLoadException {
        try {
            ScrapperManifest scm = new Gson().fromJson(file.read(), ScrapperManifest.class);
            if (modelsCache.containsKey(scm.getName())) {
                throw new ModelLoadException("Models must have different names");
            }
            modelsCache.put(scm.getName(), scm);
            return scm.getName();
        } catch (FileNotFoundException e) {
            throw new ModelLoadException("Model file not found");
        }
    }

    public List<String> getModelList() {
        ArrayList<String> modelsNames = new ArrayList<>();
        for (Map.Entry<String, ScrapperManifest> model : modelsCache.entrySet()) {
            modelsNames.add(model.getKey());
        }
        return modelsNames;
    }

    public ScrapperManifest getModelManifest(String key) throws ModelLoadException {
        if (modelsCache.containsKey(key)) {
            return modelsCache.get(key);
        }
        throw new ModelLoadException("Model not found");
    }
}
