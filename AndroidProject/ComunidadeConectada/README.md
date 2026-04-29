# Comunidade Conectada - Fase 3 (Android Nativo)

Este repositório contém a refatoração do projeto "Comunidade Conectada" para **Android Nativo (Kotlin + Jetpack Compose)**, atendendo a todos os requisitos da Fase 3 do projeto FIAP 2026.

## 📱 Arquitetura e Tecnologias

O aplicativo foi completamente reescrito para utilizar as tecnologias exigidas:

- **Linguagem:** Kotlin
- **Interface (UI):** Jetpack Compose
- **Navegação:** Navigation Compose
- **Persistência Local:** Room Database e DataStore Preferences
- **Comunicação com API:** Retrofit e OkHttp
- **Integração com IA:** Serviços configurados para LLMs e IBM Watson Assistant

## 🛠️ Como Importar no Android Studio

Siga estes passos exatos para importar e executar o projeto no seu computador:

1. **Baixe e extraia** o arquivo ZIP contendo este código-fonte.
2. Abra o **Android Studio** (versão Hedgehog 2023.1.1 ou superior recomendada).
3. Na tela inicial, clique em **Open** (ou `File > Open` se já tiver um projeto aberto).
4. Navegue até a pasta onde você extraiu o ZIP e selecione a pasta `ComunidadeConectada` (a pasta que contém o arquivo `build.gradle.kts`).
5. Clique em **OK**.
6. Aguarde o Android Studio sincronizar o projeto (Gradle Sync). Isso pode levar alguns minutos na primeira vez, pois ele fará o download de todas as dependências.
7. Após a sincronização (quando a barra de progresso inferior sumir e não houver erros vermelhos), selecione um emulador ou dispositivo físico no menu superior.
8. Clique no botão verde de **Run (▶)** (ou pressione `Shift + F10`).

## 📁 Estrutura de Pacotes

A arquitetura foi organizada para facilitar a manutenção e escalabilidade:

- `com.fiap.comunidadeconectada`
  - `ai/`: Serviços de integração com IA (LLM e Watson Assistant)
  - `api/`: Configuração do Retrofit e modelos de dados (DTOs)
  - `data/`: Room Database, DAOs, Entidades e DataStore
  - `navigation/`: Configuração de rotas e NavGraph
  - `ui/`
    - `components/`: Componentes visuais reutilizáveis (Cards, Botões)
    - `screens/`: Telas completas do aplicativo (Home, Chat, Perfil, etc)
    - `theme/`: Design System (Cores, Tipografia, Tema)

## 🤖 Integração com IA (Requisito Fase 3)

Foram implementadas duas frentes de IA:

1. **LLM (OpenAI/Gemini):** Arquivo `LLMService.kt` preparado para gerar descrições, sugerir categorias e moderar conteúdo.
2. **IBM Watson Assistant:** Arquivo `WatsonAssistantService.kt` configurado para o Chatbot de suporte ao usuário (implementado visualmente em `ChatbotScreen.kt`).

*Nota: Para que as chamadas reais de API funcionem, você precisará inserir suas próprias chaves de API (API Keys) nas respectivas classes de serviço.*

## 💾 Persistência Local (Requisito Fase 3)

- **Room Database:** Configurado com 7 entidades (`User`, `Resource`, `Message`, `Transaction`, etc) para funcionamento offline.
- **DataStore:** Substitui o antigo SharedPreferences para salvar tokens de sessão e preferências do usuário de forma assíncrona.
