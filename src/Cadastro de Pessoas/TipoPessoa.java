public enum TipoPessoa {
    CLIENTE,
    FORNECEDOR,
    AMBOS;

    public static TipoPessoa fromString(String s) {
        switch (s.toLowerCase()) {
            case "cliente":
                return CLIENTE;
            case "fornecedor":
                return FORNECEDOR;
            case "ambos":
                return AMBOS;
            default:
                throw new IllegalArgumentException("Tipo inválido: " + s);
        }
    }
}
