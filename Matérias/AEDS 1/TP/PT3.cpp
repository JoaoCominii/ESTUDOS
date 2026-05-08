#include <iostream>
#include <string>
#define _MAX 1000
using namespace std;

class Data {
private:
    int dia;
    int mes;
    int ano;
public:
    bool setDia(int dia);
    bool setMes(int mes);
    bool setAno(int ano);
    int getDia();
    int getMes();
    int getAno();
    void setData(int dia, int mes, int ano);
    bool dataValida();
    void leData();
    void escreveData();
    bool verificaMes(int mes);
};

bool Data::setDia(int dia) {
    bool sucesso = false;
    if (dia >= 1 && dia <= 31) {
        this->dia = dia;
        sucesso = true;
    }
    return sucesso;
} // fim de setDia

bool Data::setMes(int mes) {
    bool sucesso = false;
    if (mes >= 1 && mes <= 12) {
        this->mes = mes;
        sucesso = true;
    }
    return sucesso;
} // fim de setMes

bool Data::setAno(int ano) {
    bool sucesso = false;
    if (ano >= 1 && ano <= 2100) {
        this->ano = ano;
        sucesso = true;
    }
    return sucesso;
} // fim de setAno

int Data::getDia() {
    return this->dia;
} // fim de getDia

int Data::getMes() {
    return this->mes;
} // fim de getMes

int Data::getAno() {
    return this->ano;
} // fim de getAno

void Data::setData(int dia, int mes, int ano) {
    setDia(dia);
    setMes(mes);
    setAno(ano);
} // fim de setData

bool Data::dataValida() {
    bool valida = true;

    if (this->ano < 0) {
        valida = false;
    }
    else if (this->mes < 1 || this->mes > 12) {
        valida = false;
    }
    else {
        bool bissexto = (this->ano % 4 == 0 && this->ano % 100 != 0) || (this->ano % 400 == 0);

        if (this->mes == 2) {
            // Fevereiro
            if (bissexto) {
                if (this->dia < 1 || this->dia > 29) {
                    valida = false;
                }
            } else {
                if (this->dia < 1 || this->dia > 28) {
                    valida = false;
                }
            }
        } else if (this->mes == 4 || this->mes == 6 || this->mes == 9 || this->mes == 11) {
            // Abril, Junho, Setembro, Novembro têm 30 dias
            if (this->dia < 1 || this->dia > 30) {
                valida = false;
            }
        } else {
            // Janeiro, Março, Maio, Julho, Agosto, Outubro, Dezembro têm 31 dias
            if (this->dia < 1 || this->dia > 31) {
                valida = false;
            }
        }
    }

    return valida;
} // fim de dataValida

void Data::leData() {
    int dia, mes, ano;
    printf("dd/mm/aaaa: ");
    int result = scanf("%i/%i/%i", &dia, &mes, &ano);
    if (result != 3) {
        printf("Formato invalido!\n");
    }
    setData(dia, mes, ano);
} // fim de leData

void Data::escreveData() {
    printf("%02i/%02i/%04i", getDia(), getMes(), getAno());
} // fim de escreveData

bool Data::verificaMes(int mes) {
    if (this->mes == mes) {
        return true;
    } else {
        return false;
    }
} // fim de verificaMes

class Pessoa {
private:
    string nome;
    Data dataNascimento;
    static int quantidade;
public:
    virtual ~Pessoa() {} // Destrutor virtual
    void setNome(string);
    string getNome();
    bool setNascimento(int dia, int mes, int ano);
    Data getNascimento();
    void leNome();
    void escreveNome();
    virtual void lePessoa();
    virtual void escrevePessoa();
};

int Pessoa::quantidade = 0;

void Pessoa::setNome(string nome) {
    this->nome = nome;
} // fim de setNome

string Pessoa::getNome() {
    return this->nome;
} // fim de getNome

bool Pessoa::setNascimento(int dia, int mes, int ano) {
    bool sucesso = false;
    this->dataNascimento.setData(dia, mes, ano);
    sucesso = true;
    return sucesso;
} // fim de setNascimento

Data Pessoa::getNascimento() {
    return this->dataNascimento;
} // fim de getNascimento

void Pessoa::leNome() {
    string nome;
    cout << "\nNome: ";
    cin.ignore();
    getline(cin, nome);
    setNome(nome);
} // fim de leNome

void Pessoa::escreveNome() {
    cout << "\nNome: " << getNome();
} // fim de escreveNome

void Pessoa::lePessoa() {
    leNome();
    cout << "\nData de nascimento: ";
    this->dataNascimento.leData();
} // fim de lePessoa

void Pessoa::escrevePessoa() {
    cout << "\nNome: " << getNome();
    cout << "\nData de Nascimento: ";
    dataNascimento.escreveData();
} // fim de escrevePessoa

class Aluno : public Pessoa {
private:
    int matricula;
public:
    static int quantidade;
    Aluno() {
        quantidade++;
    }
    virtual ~Aluno() {} // Destrutor virtual
    void setMatricula(int matricula);
    int getMatricula();
    void lePessoa() override; // override para garantir que a função lePessoa seja a que está na classe Aluno
    void escrevePessoa() override; // override para garantir que a função escrevePessoa seja a que está na classe Aluno
};

int Aluno::quantidade = 0;

void Aluno::setMatricula(int matricula) {
    this->matricula = matricula;
}

int Aluno::getMatricula() {
    return matricula;
}

void Aluno::lePessoa() {
    Pessoa::lePessoa();
    cout << "\nMatricula: ";
    cin >> matricula;
}

void Aluno::escrevePessoa() {
    Pessoa::escrevePessoa();
    cout << "\nMatricula: " << matricula;
}

class Professor : public Pessoa {
private:
    string titulacao;
public:
    static int quantidade;
    Professor() {
        quantidade++;
    }
    virtual ~Professor() {} // Destrutor virtual
    void setTitulacao(string titulacao);
    string getTitulacao();
    void lePessoa() override; // override para garantir que a função lePessoa seja a que está na classe Professor
    void escrevePessoa() override; // override para garantir que a função escrevePessoa seja a que está na classe Professor
};

int Professor::quantidade = 0;

void Professor::setTitulacao(string titulacao) {
    this->titulacao = titulacao;
}

string Professor::getTitulacao() {
    return titulacao;
}

void Professor::lePessoa() {
    Pessoa::lePessoa();
    cout << "\nTitulacao: ";
    cin.ignore();
    getline(cin, titulacao);
}

void Professor::escrevePessoa() {
    Pessoa::escrevePessoa();
    cout << "\nTitulacao: " << titulacao;
}

// Função para cadastrar um novo aluno
void cadastrarAluno(Aluno* alunos[], int& quantidade) {
    alunos[quantidade] = new Aluno();
    alunos[quantidade]->lePessoa();
    quantidade++;
} // fim de cadastrarAluno

// Função para cadastrar um novo professor
void cadastrarProfessor(Professor* professores[], int& quantidade) {
    professores[quantidade] = new Professor();
    professores[quantidade]->lePessoa();
    quantidade++;
} // fim de cadastrarProfessor

// Função para listar todos os alunos cadastrados
void listarAlunos(Aluno* alunos[], int quantidade) {
    for (int i = 0; i < quantidade; i++) {
        alunos[i]->escrevePessoa();
        cout << endl;
    }
} // fim de listarAlunos

// Função para listar todos os professores cadastrados
void listarProfessores(Professor* professores[], int quantidade) {
    for (int i = 0; i < quantidade; i++) {
        professores[i]->escrevePessoa();
        cout << endl;
    }
} // fim de listarProfessores

// Função para listar os aniversariantes do mês
void listarAniversariantesDoMes(Pessoa* pessoas[], int quantidade, int mes) {
    for (int i = 0; i < quantidade; i++) {
        if (pessoas[i]->getNascimento().getMes() == mes) {
            pessoas[i]->escrevePessoa();
            cout << endl;
        }
    }
} // fim de listarAniversariantesDoMes

// Função para exibir o menu e retornar a opção escolhida pelo usuário
int menu() {
    int opcao;
    do {
        cout << "\nMenu de Opções:";
        cout << "\n0 - Sair";
        cout << "\n1 - Cadastrar um aluno";
        cout << "\n2 - Cadastrar um professor";
        cout << "\n3 - Listar alunos cadastrados";
        cout << "\n4 - Listar professores cadastrados";
        cout << "\n5 - Listar aniversariantes do mês (alunos e professores)";
        cout << "\nEscolha uma opção: ";
        cin >> opcao;
        if (opcao < 0 || opcao > 5) {
            cout << "Opção inválida. Tente novamente.\n";
        }
    } while (opcao < 0 || opcao > 5);
    return opcao;
} // fim de menu

int main() {
    Aluno* alunos[_MAX];
    Professor* professores[_MAX];
    Pessoa* pessoas[_MAX];
    int quantidadeAlunos = 0;
    int quantidadeProfessores = 0;
    int quantidadePessoas = 0;
    int opcao;

    do {
        opcao = menu();
        switch (opcao) {
        case 0:
            cout << "Saindo...\n";
            break;
        case 1:
            cadastrarAluno(alunos, quantidadeAlunos);
            pessoas[quantidadePessoas++] = alunos[quantidadeAlunos - 1];
            break;
        case 2:
            cadastrarProfessor(professores, quantidadeProfessores);
            pessoas[quantidadePessoas++] = professores[quantidadeProfessores - 1];
            break;
        case 3:
            cout << "\nAlunos cadastrados:\n";
            listarAlunos(alunos, quantidadeAlunos);
            break;
        case 4:
            cout << "\nProfessores cadastrados:\n";
            listarProfessores(professores, quantidadeProfessores);
            break;
        case 5: {
            int mes;
            cout << "Digite o mês: ";
            cin >> mes;
            if (mes >= 1 && mes <= 12) {
                cout << "\nAniversariantes do mês " << mes << ":\n";
                listarAniversariantesDoMes(pessoas, quantidadePessoas, mes);
            } else {
                cout << "Mês inválido. Tente novamente.\n";
            }
            break;
        }
        default:
            cout << "Opção inválida. Tente novamente.\n";
            break;
        }
    } while (opcao != 0);

    // Libera a memória alocada para alunos
    for (int i = 0; i < quantidadeAlunos; i++) {
        delete alunos[i];
    }

    // Libera a memória alocada para professores
    for (int i = 0; i < quantidadeProfessores; i++) {
        delete professores[i];
    }

    return 0;
} // fim de main
