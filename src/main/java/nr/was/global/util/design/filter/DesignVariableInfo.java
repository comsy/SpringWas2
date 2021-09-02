package nr.was.global.util.design.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nr.was.global.util.design.DesignBase;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DesignVariableInfo implements DesignBase {

    private final ObjectMapper objectMapper;

    private static Map<String, Object> designMap;

    public void filter(File file) throws IOException {
        if(designMap == null){
            designMap = new HashMap<>();
        }
        designMap.clear();

        // 전체
        JsonNode jsonNode = objectMapper.readTree(file);
        // 첫번째 {}
        JsonNode firstNode = jsonNode.get(0);


        designMap = objectMapper.convertValue(firstNode, new TypeReference<>() {});
    }

    public Object getAll(){
        return designMap;
    }

    public static Map<String, Object> getAllMap(){
        return designMap;
    }

    public static Object get(String key){
        return designMap.getOrDefault(key, null);
    }
}
