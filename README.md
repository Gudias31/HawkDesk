# Hawk Desk - Sistema de Help Desk para Android (Versão Simplificada)

![Android](https://img.shields.io/badge/Android-21%2B-green)
![Java](https://img.shields.io/badge/Java-8-orange)
![SQLite](https://img.shields.io/badge/SQLite-3-blue)

**Hawk Desk** é um aplicativo mobile de help desk desenvolvido nativamente para Android usando Java. Esta versão foi revisada para apresentar uma **interface de usuário (UI) mais simples e clean**, garantindo total compatibilidade com o **Android 8.1 Oreo (API 27)** e versões superiores. O sistema utiliza **SQLite** como backend local para persistência de dados.

## Características Principais

### Funcionalidades
- ✅ **SQLite Integrado:** Backend local completo para persistência de dados.
- ✅ **Compatibilidade:** Totalmente funcional no Android 8.1 (API 27) e superior.
- ✅ **UI Simplificada:** Design clean e funcional, com foco na usabilidade e componentes básicos.
- ✅ **Autenticação:** Login e registro de usuários com segurança (hash SHA-256).
- ✅ **Gerenciamento de Tickets:** Criação, visualização, acompanhamento de status e comentários.
- ✅ **Controle de Acesso:** Perfis de Usuário e Administrador.

### Tecnologias Utilizadas

- **Linguagem**: Java 8
- **Plataforma**: Android (API 21+)
- **Build System**: Gradle
- **Banco de Dados**: SQLite
- **UI Framework**: AppCompat e componentes básicos do Android (foco na simplicidade)
- **Arquitetura**: MVC (Model-View-Controller)

## Estrutura do Projeto

```
HawkDesk/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/hawkdesk/app/
│   │       │   ├── activities/          # Telas do aplicativo
│   │       │   ├── adapters/            # Adapters para listas
│   │       │   ├── database/            # Camada de dados (SQLite)
│   │       │   ├── models/              # Classes de modelo
│   │       │   └── utils/               # Utilitários
│   │       ├── res/                     # Recursos (Layouts XML simplificados)
│   │       └── AndroidManifest.xml
│   └── build.gradle
└── ... (Arquivos de configuração Gradle)
```

## Modelos de Dados

O projeto utiliza os modelos `User`, `Ticket` e `Comment`, todos persistidos no banco de dados SQLite local.

### Confirmação da Integração SQLite
A integração com o SQLite é realizada através das classes:
- `DatabaseHelper`: Gerencia a criação e atualização do banco de dados.
- `UserDAO`: Implementa operações CRUD para a tabela de usuários.
- `TicketDAO`: Implementa operações CRUD para a tabela de tickets, incluindo joins para buscar nomes de usuários.
- `CommentDAO`: Implementa operações CRUD para a tabela de comentários.

## Como Importar no Android Studio

Siga os passos no arquivo **INSTALLATION_GUIDE.md** para importar e executar o projeto.

## Segurança

- ✅ Senhas armazenadas com hash SHA-256.
- ✅ Uso de DAOs para encapsular o acesso ao banco de dados.
- ✅ Controle de acesso baseado em roles.

## Melhorias Futuras

- [ ] Notificações push.
- [ ] Anexar arquivos.
- [ ] Sincronização com servidor remoto.

---

**Hawk Desk** - Gerenciamento de Suporte Técnico Simplificado 🦅
