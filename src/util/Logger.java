

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitária para registro de logs em arquivo.
 */
public class Logger {
    private static final String ARQUIVO_LOG = "log.txt";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Registra uma mensagem com timestamp no arquivo de log.
     * @param mensagem texto a registrar
     */
    public static void registrar(String mensagem) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_LOG, true))) {
            bw.write(timestamp + " - " + mensagem);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao registrar log: " + e.getMessage());
        }
    }
}
