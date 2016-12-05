/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author CPD
 */
@Entity
@Table(name = "paciente")
@NamedQueries({
    @NamedQuery(name = "Paciente.findAll", query = "SELECT p FROM Paciente p")
    , @NamedQuery(name = "Paciente.findByIdpaciente", query = "SELECT p FROM Paciente p WHERE p.idpaciente = :idpaciente")
    , @NamedQuery(name = "Paciente.findByNome", query = "SELECT p FROM Paciente p WHERE p.nome = :nome")
    , @NamedQuery(name = "Paciente.findByCpf", query = "SELECT p FROM Paciente p WHERE p.cpf = :cpf")
    , @NamedQuery(name = "Paciente.findByPai", query = "SELECT p FROM Paciente p WHERE p.pai = :pai")
    , @NamedQuery(name = "Paciente.findByMae", query = "SELECT p FROM Paciente p WHERE p.mae = :mae")
    , @NamedQuery(name = "Paciente.findByCns", query = "SELECT p FROM Paciente p WHERE p.cns = :cns")
    , @NamedQuery(name = "Paciente.findByOperadora", query = "SELECT p FROM Paciente p WHERE p.operadora = :operadora")
    , @NamedQuery(name = "Paciente.findByNcartao", query = "SELECT p FROM Paciente p WHERE p.ncartao = :ncartao")
    , @NamedQuery(name = "Paciente.findByNascimento", query = "SELECT p FROM Paciente p WHERE p.nascimento = :nascimento")
    , @NamedQuery(name = "Paciente.findByNacionalidade", query = "SELECT p FROM Paciente p WHERE p.nacionalidade = :nacionalidade")
    , @NamedQuery(name = "Paciente.findByNaturalidade", query = "SELECT p FROM Paciente p WHERE p.naturalidade = :naturalidade")
    , @NamedQuery(name = "Paciente.findBySexo", query = "SELECT p FROM Paciente p WHERE p.sexo = :sexo")
    , @NamedQuery(name = "Paciente.findByRua", query = "SELECT p FROM Paciente p WHERE p.rua = :rua")
    , @NamedQuery(name = "Paciente.findByCep", query = "SELECT p FROM Paciente p WHERE p.cep = :cep")
    , @NamedQuery(name = "Paciente.findByCidade", query = "SELECT p FROM Paciente p WHERE p.cidade = :cidade")
    , @NamedQuery(name = "Paciente.findByEstado", query = "SELECT p FROM Paciente p WHERE p.estado = :estado")
    , @NamedQuery(name = "Paciente.findByTelefone", query = "SELECT p FROM Paciente p WHERE p.telefone = :telefone")})
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idpaciente")
    private Integer idpaciente;
    @Size(max = 30)
    @Column(name = "nome")
    private String nome;
    @Size(max = 255)
    @Column(name = "cpf")
    private String cpf;
    @Size(max = 30)
    @Column(name = "pai")
    private String pai;
    @Size(max = 30)
    @Column(name = "mae")
    private String mae;
    @Size(max = 50)
    @Column(name = "cns")
    private String cns;
    @Size(max = 30)
    @Column(name = "operadora")
    private String operadora;
    @Size(max = 50)
    @Column(name = "ncartao")
    private String ncartao;
    @Column(name = "nascimento")
    @Temporal(TemporalType.DATE)
    private Date nascimento;
    @Size(max = 20)
    @Column(name = "nacionalidade")
    private String nacionalidade;
    @Size(max = 20)
    @Column(name = "naturalidade")
    private String naturalidade;
    @Size(max = 20)
    @Column(name = "sexo")
    private String sexo;
    @Size(max = 30)
    @Column(name = "rua")
    private String rua;
    @Size(max = 30)
    @Column(name = "cep")
    private String cep;
    @Size(max = 30)
    @Column(name = "cidade")
    private String cidade;
    @Size(max = 30)
    @Column(name = "estado")
    private String estado;
    @Size(max = 30)
    @Column(name = "telefone")
    private String telefone;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idpaciente")
    private Collection<Agenda> agendaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idpaciente")
    private Collection<Consulta> consultaCollection;

    public Paciente() {
    }

    public Paciente(Integer idpaciente) {
        this.idpaciente = idpaciente;
    }

    public Integer getIdpaciente() {
        return idpaciente;
    }

    public void setIdpaciente(Integer idpaciente) {
        this.idpaciente = idpaciente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPai() {
        return pai;
    }

    public void setPai(String pai) {
        this.pai = pai;
    }

    public String getMae() {
        return mae;
    }

    public void setMae(String mae) {
        this.mae = mae;
    }

    public String getCns() {
        return cns;
    }

    public void setCns(String cns) {
        this.cns = cns;
    }

    public String getOperadora() {
        return operadora;
    }

    public void setOperadora(String operadora) {
        this.operadora = operadora;
    }

    public String getNcartao() {
        return ncartao;
    }

    public void setNcartao(String ncartao) {
        this.ncartao = ncartao;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Collection<Agenda> getAgendaCollection() {
        return agendaCollection;
    }

    public void setAgendaCollection(Collection<Agenda> agendaCollection) {
        this.agendaCollection = agendaCollection;
    }

    public Collection<Consulta> getConsultaCollection() {
        return consultaCollection;
    }

    public void setConsultaCollection(Collection<Consulta> consultaCollection) {
        this.consultaCollection = consultaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpaciente != null ? idpaciente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paciente)) {
            return false;
        }
        Paciente other = (Paciente) object;
        if ((this.idpaciente == null && other.idpaciente != null) || (this.idpaciente != null && !this.idpaciente.equals(other.idpaciente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //return "entidades.Paciente[ idpaciente=" + idpaciente + " ]";
        return nome + ", " + idpaciente;
    }
    
}
