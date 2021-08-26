package nr.was.domain.character.character;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureCache(cacheProvider = CacheType.SIMPLE)
@DataJpaTest // Repository 를 IOC 에 등록해줌.
class CharacterRepositoryUnitTest {

    @Autowired
    private CharacterRepository characterRepository;

    private final Long guid = 1L;

    @Test
    public void 생성_테스트() throws Exception {
        //given
        Character character = Character.builder()
                .guid(guid)
                .characterId(1L)
                .level(1)
                .exp(0)
                .category(0)
                .build();

        //when
        Character save = characterRepository.save(character);

        //then
        assertThat(save.getGuid()).as("guid 테스트").isEqualTo(guid);
        assertThat(save.getCharacterId()).as("characterId 테스트").isEqualTo(1L);
        assertThat(save.getLevel()).as("level 테스트").isEqualTo(1);

    }
}