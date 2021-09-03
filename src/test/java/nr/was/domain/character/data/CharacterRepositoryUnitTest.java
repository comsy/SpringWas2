package nr.was.domain.character.data;

import lombok.extern.slf4j.Slf4j;
import nr.was.global.configuration.JpaAuditingConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaAuditingConfiguration.class    // JpaAuditing 위해..
)) // Repository 를 IOC 에 등록해줌
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
        Character save = characterRepository.saveAndFlush(character);   // auditing 위해서 saveAndFlush 사용.

        //then
        assertThat(save.getGuid()).as("guid 테스트").isEqualTo(guid);
        assertThat(save.getCharacterId()).as("characterId 테스트").isEqualTo(1L);
        assertThat(save.getLevel()).as("level 테스트").isEqualTo(1);

        log.debug(character.getCreatedDate().toString());
        log.debug(character.getModifiedDate().toString());
    }

    @Test
    public void idStrategyTest() {
        //given
        Character character = Character.builder()
                .guid(guid)
                .characterId(1L)
                .level(1)
                .exp(0)
                .category(0)
                .build();
        Character character2 = Character.builder()
                .guid(guid)
                .characterId(1L)
                .level(1)
                .exp(0)
                .category(0)
                .build();


        Character savedCharacter1 = characterRepository.save(character);
        Character savedCharacter2 = characterRepository.save(character2);

        assertThat(Math.abs(savedCharacter1.getId() - savedCharacter2.getId())).isEqualTo(1);

    }
}