import java.io.File;

/**
 * class to store data of individual wav
 */
public class WAV {
  private File file;
  public boolean played;

  public WAV(File file) {
    this.file = file;
    played = false;
  }

  public File file() {
    return this.file;
  }
} 