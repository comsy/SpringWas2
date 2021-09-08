package nr.server.common.design;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nr.server.common.design.custom.DesignVariableInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = {
        DesignTestApp.class,
        ObjectMapper.class,
        DesignManager.class,
})
class DesignManagerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DesignManager designManager;

    @Value("${design.path}")
    private String path;

    @Value("${design.prefix}")
    private String prefix;

    private final String extensionName = ".json";

    @Test
    @DisplayName("1. 폴더 내 모든 *.json 파일을 불러온다.")
    void 데이터_로드(){
        // given
        File dirFile = new File(path);
        File[] fileList = dirFile.listFiles();
        assertThat(fileList).isNotNull();
        assertThat(fileList).isNotEmpty();
        int validFileCount = (int) Arrays.stream(fileList).filter(file -> file.getName().contains(extensionName)).count();

        // when

        // then
        assertThat(designManager.getDesignMap()).isNotEmpty();
        assertThat(designManager.getDesignMap()).hasSize(validFileCount);

        designManager.getDesignMap().forEach((key, data)-> {
            String s = null;
            try {
                if(data instanceof DesignBase){
                    Object all = ((DesignBase) data).getAll();
                    s = objectMapper.writeValueAsString(all);
                }
                else{
                    s = objectMapper.writeValueAsString(data);
                }
                log.debug(key + " : " + s);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    @DisplayName("2. 데이터 get")
    void 데이터_get(){
        // given
        File dirFile = new File(path);
        File[] fileList = dirFile.listFiles();
        assertThat(fileList).isNotNull();
        assertThat(fileList).isNotEmpty();

        // when


        // then
        for(File file: fileList) {
            String fileName = file.getName();
            if(!fileName.contains(extensionName))
                continue;;

            // 의미있는 이름만 남겨서 key로 사용
            String key = fileName.replace(prefix, "").replace(extensionName, "");

            Object data = designManager.getDesign(key);
            assertThat(data).isNotNull();
        }
    }

    @Test
    public void DesignVariableInfo테스트() throws Exception {
        //given

        //when
        int startPublicManaPC = (int) DesignVariableInfo.get("StartPublicManaPC");

        //then
        assertThat(startPublicManaPC).isNotNull();
        assertThat(startPublicManaPC).isEqualTo(5);
    }
}