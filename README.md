# ClasseConectada - Sistema Educacional

Sistema completo de gestão escolar com Spring Boot + HTML/CSS/JS

## 📋 Descrição

ClasseConectada é um sistema educacional moderno que permite gerenciar:
- 👨‍🎓 Alunos
- 👨‍🏫 Professores
- 👔 Diretores
- 📚 Turmas
- 📖 Matérias/Disciplinas
- 📊 Notas
- 📝 Observações

## 🛠️ Tecnologias Utilizadas

- **Backend:**
  - Spring Boot 4.0.1
  - Spring Data JPA
  - Spring Web
  - Spring Validation
  - MySQL 8.x
  - Lombok
  - Maven

- **Frontend:**
  - HTML5
  - CSS3
  - JavaScript (Vanilla)

## 📦 Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- MySQL 8.0+ (rodando em localhost:3306)
- Usuário MySQL: `root` / Senha: `root` (ou configure no application.properties)

## 🚀 Como Executar

### 1. Clone o repositório
```bash
git clone https://github.com/onmikronDev/classeconectadaSpring.git
cd classeconectadaSpring
```

### 2. Configure o MySQL
Certifique-se de que o MySQL está rodando e que as credenciais estão corretas em `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=root
```

### 3. Execute a aplicação
```bash
./mvnw spring-boot:run
```
Ou no Windows:
```bash
mvnw.cmd spring-boot:run
```

### 4. Acesse a aplicação
- Frontend: http://localhost:8080/html/Login.html
- API Base URL: http://localhost:8080/api

### 5. Login Padrão
- **Email:** admin@email.com
- **Senha:** 123456

## 📚 Estrutura do Projeto

```
src/main/java/com/me/classeconectada/
├── ClasseConectadaApplication.java  # Classe principal
├── config/
│   └── DataLoader.java              # Carrega dados iniciais
├── model/                           # Entidades JPA
│   ├── User.java                    # Classe base de usuário
│   ├── Student.java                 # Aluno (extends User)
│   ├── Teacher.java                 # Professor (extends User)
│   ├── Director.java                # Diretor (extends User)
│   ├── SchoolClass.java             # Turma
│   ├── Subject.java                 # Matéria
│   ├── Grade.java                   # Nota
│   ├── Observation.java             # Observação
│   └── UserType.java                # Enum de tipos de usuário
├── repository/                      # Repositórios JPA
├── service/                         # Serviços (lógica de negócio)
├── controller/                      # Controllers REST
└── dto/                            # Data Transfer Objects

src/main/resources/
├── application.properties           # Configurações
└── static/                         # Frontend (HTML/CSS/JS)
    ├── html/
    ├── css/
    ├── js/
    └── img/
```

## 🌐 Endpoints da API

### Autenticação
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/login` | Login de usuário |

**Exemplo de requisição:**
```json
{
  "email": "admin@email.com",
  "senha": "123456"
}
```

### Usuários
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/users` | Listar todos os usuários ativos |
| GET | `/api/users/{id}` | Buscar usuário por ID |
| GET | `/api/users/tipo/{tipo}` | Filtrar por tipo (PROFESSOR, ALUNO, DIRETOR) |
| POST | `/api/users` | Criar novo usuário |
| PUT | `/api/users/{id}` | Atualizar usuário |
| DELETE | `/api/users/{id}` | Desativar usuário (soft delete) |

### Alunos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/students` | Listar todos os alunos ativos |
| GET | `/api/students/{id}` | Buscar aluno por ID |
| GET | `/api/students/turma/{turmaId}` | Listar alunos de uma turma |
| POST | `/api/students` | Criar novo aluno |
| PUT | `/api/students/{id}` | Atualizar aluno |
| DELETE | `/api/students/{id}` | Desativar aluno |

### Professores
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/teachers` | Listar todos os professores ativos |
| GET | `/api/teachers/{id}` | Buscar professor por ID |
| GET | `/api/teachers/turma/{turmaId}` | Listar professores de uma turma |
| POST | `/api/teachers` | Criar novo professor |
| PUT | `/api/teachers/{id}` | Atualizar professor |
| DELETE | `/api/teachers/{id}` | Desativar professor |

### Diretores
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/directors` | Listar todos os diretores ativos |
| GET | `/api/directors/{id}` | Buscar diretor por ID |
| POST | `/api/directors` | Criar novo diretor |
| PUT | `/api/directors/{id}` | Atualizar diretor |
| DELETE | `/api/directors/{id}` | Desativar diretor |

### Turmas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/classes` | Listar todas as turmas ativas |
| GET | `/api/classes/{id}` | Buscar turma por ID |
| GET | `/api/classes/{id}/students` | Listar alunos de uma turma |
| POST | `/api/classes` | Criar nova turma |
| PUT | `/api/classes/{id}` | Atualizar turma |
| DELETE | `/api/classes/{id}` | Desativar turma |

### Matérias
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/subjects` | Listar todas as matérias ativas |
| GET | `/api/subjects/{id}` | Buscar matéria por ID |
| GET | `/api/subjects/teacher/{teacherId}` | Listar matérias de um professor |
| POST | `/api/subjects` | Criar nova matéria |
| PUT | `/api/subjects/{id}` | Atualizar matéria |
| DELETE | `/api/subjects/{id}` | Desativar matéria |

### Notas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/grades` | Listar todas as notas |
| GET | `/api/grades/{id}` | Buscar nota por ID |
| GET | `/api/grades/student/{studentId}` | Listar notas de um aluno |
| GET | `/api/grades/student/{studentId}/subject/{subjectId}` | Notas de um aluno em uma matéria |
| GET | `/api/grades/subject/{subjectId}` | Listar notas de uma matéria |
| POST | `/api/grades` | Aplicar nova nota |
| PUT | `/api/grades/{id}` | Atualizar nota |
| DELETE | `/api/grades/{id}` | Deletar nota |

**Exemplo de requisição para criar nota:**
```json
{
  "student": {
    "id": 1
  },
  "subject": {
    "id": 1
  },
  "value": 8.5,
  "description": "Prova Bimestral",
  "examDate": "2024-12-23"
}
```

### Observações
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/observations` | Listar todas as observações |
| GET | `/api/observations/{id}` | Buscar observação por ID |
| GET | `/api/observations/student/{studentId}` | Observações de um aluno |
| GET | `/api/observations/turma/{turmaId}` | Observações de uma turma |
| POST | `/api/observations` | Criar nova observação |
| PUT | `/api/observations/{id}` | Atualizar observação |
| DELETE | `/api/observations/{id}` | Deletar observação |

## 🎯 Funcionalidades

### Telas do Sistema
- **Login** (Login.html) - Autenticação de usuários
- **Dashboard** (index.html) - Menu principal
- **Turmas** (turma.html) - Gestão de turmas e alunos
- **Cadastro** (cadrastro.html) - Cadastro de usuários
- **Usuários** (usuarios.html) - Gerenciamento de usuários
- **Histórico** (historico.html) - Notas e histórico do aluno
- **Observações** (observacoes.html) - Observações sobre alunos

### Recursos do Backend
- ✅ API REST completa com CRUD
- ✅ Validação de dados com Bean Validation
- ✅ Herança de entidades (User → Student, Teacher, Director)
- ✅ Relacionamentos JPA (OneToMany, ManyToOne)
- ✅ Soft Delete (campo active)
- ✅ CORS habilitado para frontend
- ✅ Dados iniciais automáticos
- ✅ Validação de notas (0-10)

## 🔧 Configurações Avançadas

### Alterar Porta do Servidor
Edite `application.properties`:
```properties
server.port=8081
```

### Alterar Modo de Criação do Schema
```properties
spring.jpa.hibernate.ddl-auto=create  # Recria o schema a cada execução
spring.jpa.hibernate.ddl-auto=update  # Atualiza o schema (padrão)
spring.jpa.hibernate.ddl-auto=none    # Não altera o schema
```

## 🧪 Dados de Teste

A aplicação carrega automaticamente dados de teste na primeira execução:

**Turmas:** Turma A, Turma B, Turma C

**Matérias:** Matemática, Português, Ciências, Geografia, História

**Usuários:**
- Diretor: admin@email.com / 123456
- Professores: joao@email.com, ana@email.com, carlos@email.com
- Alunos: alice@email.com, joao.aluno@email.com, maria@email.com, pedro@email.com, etc.

**Senha padrão para todos:** 123456

⚠️ **NOTA DE SEGURANÇA:** Este sistema utiliza senhas em texto simples para fins educacionais e de demonstração. Em um ambiente de produção, as senhas devem ser criptografadas usando BCrypt ou algoritmo similar.

## 🐛 Resolução de Problemas

### Erro de conexão com MySQL
- Verifique se o MySQL está rodando
- Confirme as credenciais em application.properties
- Certifique-se de que a porta 3306 está acessível

### Porta 8080 já em uso
- Altere a porta em application.properties
- Ou pare o processo que está usando a porta 8080

### Erro ao compilar
```bash
./mvnw clean install
```

## 📄 Licença

Este projeto é de código aberto.

## 👥 Contribuidores

Desenvolvido por onmikronDev

## 📞 Suporte

Para problemas ou dúvidas, abra uma issue no repositório.
