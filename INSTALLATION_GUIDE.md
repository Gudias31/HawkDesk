# Guia de Instalação e Configuração - Hawk Desk (Versão Simplificada)

Este documento fornece instruções detalhadas para configurar, compilar e executar o aplicativo Hawk Desk no Android Studio. Esta versão foi otimizada para uma **interface de usuário (UI) mais simples e clean**, e garante compatibilidade com o **Android 8.1 Oreo (API 27)**.

## Requisitos do Sistema

### Software Necessário

- **Android Studio** (versão Arctic Fox ou superior)
- **Java Development Kit (JDK)** versão 8 ou superior
- **Android SDK** com suporte para **API 27 (Android 8.1 Oreo)** e API 34.

## Passo 1: Instalar o Android Studio

Siga as instruções de instalação do Android Studio no site oficial.

## Passo 2: Configurar o Android SDK

Abra o Android Studio e verifique se as seguintes versões estão instaladas no SDK Manager: **Android 8.1 (API 27)** e Android 14.0 (API 34).

## Passo 3: Importar o Projeto Hawk Desk

Com o Android Studio aberto, clique em **File → Open**, navegue até a pasta raiz do projeto Hawk Desk e clique em **OK**.

## Passo 4: Sincronizar o Gradle

Aguarde a sincronização automática do Gradle. Se necessário, clique em **Sync Now** ou **File → Sync Project with Gradle Files**.

## Passo 5: Configurar um Dispositivo para Execução

Use um dispositivo físico ou emulador com **Android 8.1 (API 27)** ou superior.

## Passo 6: Compilar e Executar o Aplicativo

Clique no botão **Run** (▶️) ou pressione `Shift + F10`. Selecione o dispositivo de destino e aguarde a instalação e execução.

## Passo 7: Primeiro Uso do Aplicativo

1. **Registrar uma Conta:** Na tela de login, digite e-mail e senha e clique em **Registrar**.
2. **Criar um Ticket:** Na tela principal, clique no botão flutuante (+) e preencha os dados.
3. **Visualizar Detalhes:** Toque em um ticket para ver os detalhes e adicionar comentários.

## Confirmação de Compatibilidade e SQLite

- **Compatibilidade Android 8.1:** O projeto está configurado com `minSdk 21` e utiliza apenas recursos compatíveis com a API 27, garantindo o funcionamento no Android 8.1 Oreo.
- **Integração SQLite:** O banco de dados SQLite está totalmente integrado e funcional, gerenciado pelas classes DAO no pacote `com.hawkdesk.app.database`.

## Solução de Problemas Comuns

Consulte a seção de Solução de Problemas no README.md para erros comuns.

---

**Hawk Desk** - Sistema de Help Desk para Android 🦅
