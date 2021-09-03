package nr.was.domain.character.data;

import nr.was.global.util.cache.CacheManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterDaoUnitTest {

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private CacheManager<Character> cacheManager;

    @InjectMocks
    private CharacterDao characterDao;

    private Character character1;
    private Character character2;
    private List<Character> characterList;

    @BeforeEach
    void setUp() {
        character1 = Character.builder()
                .id(1L)
                .guid(1L)
                .characterId(1L)
                .level(1)
                .exp(0)
                .category(0)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        character2 = Character.builder()
                .id(2L)
                .guid(1L)
                .characterId(1L)
                .level(1)
                .exp(0)
                .category(0)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        characterList = new ArrayList<>();
        characterList.add(character1);
        characterList.add(character2);
    }

    @Test
    public void getList_캐시null() throws Exception {
        //given
//        when(cacheManager.getEntityList("character::1", Character.class)).thenReturn(null);
//        when(characterRepository.findByGuid(1L)).thenReturn(characterList);
        given(cacheManager.getEntityList("character::1", Character.class)).willReturn(null);
        given(characterRepository.findByGuid(1L)).willReturn(characterList);

        //when(cacheManager.getEntityList(anyString(), Character.class)).thenReturn(null);

        //when
        List<Character> characterList = characterDao.getList(1L);

        //then
        assertThat(characterList)
                .hasSize(2)
                .containsExactly(character1, character2);
    }

    @Test
    public void getList_캐시ok() throws Exception {
        //given
        when(cacheManager.getEntityList("character::1", Character.class)).thenReturn(characterList);


        //when
        List<Character> characterList = characterDao.getList(1L);

        //then
        assertThat(characterList)
                .hasSize(2)
                .containsExactly(character1, character2);
    }

    @Test
    public void dd() throws Exception {
        //given

        //when

        //then
    }






}