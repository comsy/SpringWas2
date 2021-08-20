package nr.was.component.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.data.domain.CachedEntityInterface;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

/**
 * 리퀘스트 안에서만 살아있는 클래스
 * 리퀘스트 내부 공유 object
 */
@Slf4j
@Component
@RequestScope
@RequiredArgsConstructor
public class CacheSyncUtil<T> {

    private final Map<String, SyncData> syncDataMap;
    private final CacheManager<List<T>> cacheManager;

    private String uuid;

    /**
     * 실제 생성 시점은 request 처음이 아니고 처음으로 사용된 시점이다.
     */
    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        log.debug("[CacheSyncUtil] RequestScope 빈 created. uuid = " + uuid);
    }

    @PreDestroy
    public void close() {
        log.debug("[CacheSyncUtil] RequestScope 빈 destroyed. uuid = " + uuid);
    }


    public void addEntityList(String key, List<T> entityList, Boolean dirty){
        Map<String, T> dataMap = new HashMap<>();
        SyncData syncData = new SyncData(key, dataMap);

        entityList.forEach(syncData::setData);

        syncDataMap.put(key, syncData);

        if(dirty){
            syncData.setDirty();
        }

        log.debug("[CacheSyncUtil]addDataList 캐시싱크Map에 저장함 : " + key);
    }

    public List<T> getEntityList(String key){
        SyncData syncData = syncDataMap.getOrDefault(key, null);
        if(syncData == null){

            List<T> cachedEntityList = cacheManager.getCache(key);
            if(cachedEntityList == null){
                return null;
            }
            log.debug("[CacheSyncUtil]getDataList 캐시에서가져옴 : " + key);

            addEntityList(key, cachedEntityList, false);
            syncData = syncDataMap.getOrDefault(key, null);
        }
        log.debug("[CacheSyncUtil]getDataList 캐시싱크Map에서 가져옴 : " + key);

        return syncData.getDataList();
    }

    public void setEntity(String key, T entity){
        SyncData syncData = syncDataMap.getOrDefault(key, null);
        if(syncData == null){
            List<T> cachedEntityList = cacheManager.getCache(key);
            // 캐시에 없을 때는 빈채로 SyncData 생성
            if(cachedEntityList == null){
                cachedEntityList = new ArrayList<>();
                addEntityList(key, cachedEntityList, false);
            }

            syncData = syncDataMap.getOrDefault(key, null);
        }

        syncData.setData(entity);
        syncData.setDirty();
    }

    public void delEntity(String key, T entity){
        SyncData syncData = syncDataMap.getOrDefault(key, null);
        if(syncData == null){
            return;
        }

        syncData.delData(entity);
        syncData.setDirty();
    }

    public void syncAll(){
        syncDataMap.values().forEach(syncData->{
            if(syncData.getSyncState() == SyncState.DIRTY){
                log.debug("[CacheSyncUtil]syncAll : " + syncData.getKey());
                cacheManager.putCache(syncData.getKey(), syncData.getDataList());
            }
        });
        syncDataMap.clear();
    }

    public void rollbackAll(){
        syncDataMap.values().forEach(syncData-> cacheManager.delCache(syncData.getKey()));
        syncDataMap.clear();
    }

    @Getter
    @RequiredArgsConstructor
    private class SyncData {

        private final String key;
        private SyncState syncState = SyncState.IDLE;
        private final Map<String, T> dataMap;

        public List<T> getDataList(){
            return new ArrayList<>(dataMap.values());
        }

        public T getData(String key){
            return dataMap.get(key);
        }

        public void setData(T data){
            CachedEntityInterface cachedEntity = (CachedEntityInterface) data;
            dataMap.put(cachedEntity.getCacheKey(), data);
        }

        public void delData(T data){
            CachedEntityInterface cachedEntity = (CachedEntityInterface) data;
            dataMap.remove(cachedEntity.getCacheKey());
        }

        public void setDirty() {
            if(!dataMap.isEmpty())
                this.syncState = SyncState.DIRTY;
        }
    }

    public enum SyncState {
        IDLE,
        DIRTY
    }
}
