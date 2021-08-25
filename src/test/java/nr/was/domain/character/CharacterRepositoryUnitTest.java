package nr.was.domain.character;

import nr.was.domain.character.character.Character;
import nr.was.domain.character.character.CharacterRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
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
        Assertions.assertThat(save.getGuid()).isEqualTo(guid);
        Assertions.assertThat(save.getCharacterId()).isEqualTo(1L);
        Assertions.assertThat(save.getLevel()).isEqualTo(1);

    }
}