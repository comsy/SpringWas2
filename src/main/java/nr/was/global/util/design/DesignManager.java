package nr.was.global.util.design;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.global.util.design.filter.DesignAttendance;
import nr.was.global.util.design.filter.DesignVariableInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@Getter
public class DesignManager {

    private final Map<String, Object> designMap;
    private final Map<String, DesignBase> parserMap;
    private final ObjectMapper objectMapper;

    @Value("${design.path}")
    private String path;

    @Value("${design.prefix}")
    private String prefix;

    private final String extensionName = ".json";


    private final DesignVariableInfo designVariableInfo;
    private final DesignAttendance designAttendance;

    // 특별한 파서가 필요한경우.
    public void filterMatch(){
        parserMap.clear();
        parserMap.put("VariableInfo", designVariableInfo);
        parserMap.put("Attendance", designAttendance);
    }

    @PostConstruct
    public void init() {
        log.debug("[DesignManager]init");

        designMap.clear();
        filterMatch();

        File dirFile = new File(path);
        File[] fileList = dirFile.listFiles();
        //Arrays.sort(fileList, LastModifiedFileComparator.LASTMODIFIED_REVERSE);

        assert(fileList != null);

        for(File file: fileList) {
//            log.debug("path :" + file.getPath());
            String fileName = file.getName();
//            log.debug("filename :" + fileName);
            if(!fileName.contains(extensionName))
                continue;

            // 의미있는 이름만 남겨서 key로 사용
            String key = fileName.replace(prefix, "").replace(extensionName, "");
            log.debug("key : " + key);

            try {
                if(parserMap.containsKey(key)){
                    DesignBase designBase = parserMap.get(key);
                    designBase.filter(file);
                    designMap.put(key, designBase);
                }
                else{
                    JsonNode jsonNode = objectMapper.readTree(file);
                    List<Map<String, Object>> commonDesign = new ArrayList<>();
                    for (JsonNode node : jsonNode) {
                        Map<String, Object> nodeMap = objectMapper.convertValue(node, new TypeReference<>() {});
                        commonDesign.add(nodeMap);
                    }
                    designMap.put(key, commonDesign);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getDesign(String key) {
        if(!designMap.containsKey(key))
            return null;

        return designMap.get(key);
    }

    public <T> T getDesign(String key, Class<T> classType) {
        if(!designMap.containsKey(key))
            return null;

        return objectMapper.convertValue(designMap.get(key), classType);
    }
}
