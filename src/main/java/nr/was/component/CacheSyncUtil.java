package nr.was.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.data.dto.DtoRoot;
import org.springframework.beans.factory.annotation.Value;
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
    private final CacheUtil<List<T>> cacheUtil;

    @Value("${spring.redis.cache.ttl}")
    private Long ttl;

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


    public void addDataList(String key, List<T> dataList, Boolean dirty){
        Map<String, T> dataMap = new HashMap<>();
        SyncData syncData = new SyncData(key, dataMap);

        dataList.forEach(syncData::addData);

        syncDataMap.put(key, syncData);

        if(dirty){
            syncData.setDirty();
        }

        log.debug("[CacheSyncUtil]addDataList 캐시싱크Map에 저장함 : " + key);
    }

    public List<T> getDataList(String key){
        SyncData syncData = syncDataMap.getOrDefault(key, null);
        if(syncData == null){

            List<T> cachedDtoList = cacheUtil.getValue(key);
            if(cachedDtoList == null){
                return null;
            }
            log.debug("[CacheSyncUtil]getDataList 캐시에서가져옴 : " + key);

            addDataList(key, cachedDtoList, false);
            syncData = syncDataMap.getOrDefault(key, null);
        }
        log.debug("[CacheSyncUtil]getDataList 캐시싱크Map에서 가져옴 : " + key);

        return syncData.getDataList();
    }

    public void setData(String key, T data){
        SyncData syncData = syncDataMap.getOrDefault(key, null);
        if(syncData == null){
            List<T> cachedDtoList = cacheUtil.getValue(key);
            // 캐시에 없을 때는 빈채로 SyncData 생성
            if(cachedDtoList == null){
                cachedDtoList = new ArrayList<>();
                addDataList(key, cachedDtoList, false);
            }

            syncData = syncDataMap.getOrDefault(key, null);
        }

        syncData.setData(data);
        syncData.setDirty();
    }

    public void delDate(String key, T data){
        SyncData syncData = syncDataMap.getOrDefault(key, null);
        if(syncData == null){
            return;
        }

        syncData.delData(data);
        syncData.setDirty();
    }

    public void syncAll(){
        syncDataMap.values().forEach(syncData->{
            if(syncData.getSyncState() == SyncState.DIRTY){
                log.debug("[CacheSyncUtil]syncAll : " + syncData.getKey());
                cacheUtil.putValue(syncData.getKey(), syncData.getDataList(), ttl);
            }
        });
        syncDataMap.clear();
    }

    public void rollbackAll(){
        syncDataMap.values().forEach(syncData-> cacheUtil.delete(syncData.getKey()));
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
            DtoRoot dtoRoot = (DtoRoot) data;
            if(!dataMap.containsKey(dtoRoot.getCacheKey())){
                addData(data);
            }

            // addData, setData 가 다를게 없는데... 흠...
            dataMap.put(dtoRoot.getCacheKey(), data);
        }

        public void addData(T data){
            DtoRoot dtoRoot = (DtoRoot) data;
            dataMap.put(dtoRoot.getCacheKey(), data);
        }

        public void delData(T data){
            DtoRoot dtoRoot = (DtoRoot) data;
            dataMap.remove(dtoRoot.getCacheKey());
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
