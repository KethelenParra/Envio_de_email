package domain.shared;

public abstract class Default {

    protected Long id;

    // private LocalDateTime dataCadastro;
    //
    // private LocalDateTime dataAlteracao;
    //
    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    // public LocalDateTime getDataCadastro() {
    // return this.dataCadastro;
    // }
    //
    // public void setDataCadastro(final LocalDateTime dataCadastro) {
    // this.dataCadastro = dataCadastro;
    // }
    //
    // public LocalDateTime getDataAlteracao() {
    // return this.dataAlteracao;
    // }
    //
    // public void setDataAlteracao(final LocalDateTime dataAlteracao) {
    // this.dataAlteracao = dataAlteracao;
    // }
}
