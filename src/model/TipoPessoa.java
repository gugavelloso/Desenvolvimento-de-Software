
/**
 * Enum para tipos de pessoa no sistema.
 */
public enum TipoPessoa {
    CLIENTE, FORNECEDOR, AMBOS;

    public static TipoPessoa fromString(String tipo) {
        return TipoPessoa.valueOf(tipo.toUpperCase());
    }
}
