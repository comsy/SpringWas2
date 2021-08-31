package nr.was.global.util.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.domain.EntityMaster;
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
public class CacheManager<T extends EntityMaster> {

    private final Map<String, CacheSyncData<T>> syncDataMap;
    private final CacheUtil<T> cacheUtil;

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
        CacheSyncData<T> syncData = new CacheSyncData<>(key, dataMap);

        entityList.forEach(syncData::setData);

        syncDataMap.put(key, syncData);

        if(dirty){
            syncData.setDirty();
        }

        log.debug("[CacheSyncUtil]addDataList 캐시싱크Map에 저장함 : " + key);
    }

    public List<T> getEntityList(String key, Class<T> parsingClassType){
        CacheSyncData<T> syncData = syncDataMap.getOrDefault(key, null);
        if(syncData == null){

            List<T> cachedEntityList = cacheUtil.getCache(key, parsingClassType);
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

    public void setEntity(String key, T entity, Class<T> parsingClassType){
        CacheSyncData<T> syncData = syncDataMap.getOrDefault(key, null);
        if(syncData == null){
            List<T> cachedEntityList = cacheUtil.getCache(key, parsingClassType);
            // 캐시에 없을 때는 빈채로 CacheSyncData 생성
            if(cachedEntityList == null){
                cachedEntityList = new ArrayList<>();
            }
            addEntityList(key, cachedEntityList, false);

            syncData = syncDataMap.getOrDefault(key, null);
        }

        syncData.setData(entity);
        syncData.setDirty();
    }

    public void delEntity(String key, T entity){
        CacheSyncData<T> syncData = syncDataMap.getOrDefault(key, null);
        if(syncData == null){
            return;
        }

        syncData.delData(entity);
        syncData.setDirty();
    }

    public void delCache(String key){
        CacheSyncData<T> syncData = syncDataMap.getOrDefault(key, null);
        if(syncData != null){
            syncDataMap.remove(key, syncData);
        }

        cacheUtil.delCache(key);
    }

    public void syncAll(){
        syncDataMap.values().forEach(syncData->{
            if(syncData.getSyncState() == CacheSyncState.DIRTY){
                log.debug("[CacheSyncUtil]syncAll 캐시 저장소에 저장함 : " + syncData.getKey());
                cacheUtil.putCache(syncData.getKey(), syncData.getDataList());
            }
        });
        syncDataMap.clear();
    }

    public void rollbackAll(){
        syncDataMap.values().forEach(syncData-> cacheUtil.delCache(syncData.getKey()));
        syncDataMap.clear();
    }
}
