import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
  private static final String ARQUIVO_LOG = "log.txt";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public static void registrar(String acao) {
    String dataHora = LocalDateTime.now().format(FORMATTER);
    String linha = String.format("[%s] %s", dataHora, acao);
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_LOG, true))) {
      bw.write(linha);
      bw.newLine();
    } catch (IOException e) {
      System.out.println("Erro ao registrar log: " + e.getMessage());
    }
  }

}
