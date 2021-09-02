package nr.was.global.util.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nr.was.domain.ICachedEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CacheSyncData<T extends ICachedEntity> {
    private final String key;
    private CacheSyncState syncState = CacheSyncState.IDLE;
    private final Map<String, T> dataMap;

    public List<T> getDataList(){
        return new ArrayList<>(dataMap.values());
    }

    public T getData(String key){
        return dataMap.get(key);
    }

    public void setData(T data){
        dataMap.put(data.getCacheKey(), data);
    }

    public void delData(T data){
        dataMap.remove(data.getCacheKey());
    }

    public void setDirty() {
        if(!dataMap.isEmpty())
            this.syncState = CacheSyncState.DIRTY;
    }
}
