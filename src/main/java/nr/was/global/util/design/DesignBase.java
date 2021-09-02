package nr.was.global.util.design;

import java.io.File;
import java.io.IOException;

public interface DesignBase {

    void filter(File file) throws IOException;

    Object getAll();
}
