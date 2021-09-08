package nr.server.game.domain.character;

import nr.server.domain.db.data.character.data.CharacterDao;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 서버스 단위 테스트
 * MockitoExtension 사용하면
 * CharacterRepository - 가짜로 만들 수 있음.
 */

@ExtendWith(MockitoExtension.class)
class CharacterServiceUnitTest {

    @InjectMocks // Mock으로 생성된 빈을 자동으로 주입한다.
    private CharacterService characterService;

    @Mock
    private CharacterDao characterDao;


}