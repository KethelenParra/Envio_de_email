package infrastructure.shared;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DefaultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    // @Column(name = "dataCadastro", updatable = false)
    // private LocalDateTime dataCadastro;
    //
    // @Column(name = "dataAlteracao")
    // private LocalDateTime dataAlteracao;
    //
    // @PrePersist
    // protected void onCreate() {
    // this.dataCadastro = LocalDateTime.now();
    // }
    //
    // @PreUpdate
    // protected void onUpdate() {
    // this.dataAlteracao = LocalDateTime.now();
    // }

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
