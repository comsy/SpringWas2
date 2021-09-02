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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DesignAttendance implements DesignBase {

    private final ObjectMapper objectMapper;

    private static List<Map<String, Object>> designList;

    public void filter(File file) throws IOException {
        if(designList == null){
            designList = new ArrayList<>();
        }
        designList.clear();

        // 전체
        JsonNode jsonNode = objectMapper.readTree(file);
        for (JsonNode node : jsonNode) {
            Map<String, Object> nodeMap = objectMapper.convertValue(node, new TypeReference<>() {});
            designList.add(nodeMap);
        }
    }

    public Object getAll(){
        return designList;
    }

    public static List<Map<String, Object>> getAllList(){
        return designList;
    }

    public static Map<String, Object> get(int index){
        return designList.get(index);
    }
}
