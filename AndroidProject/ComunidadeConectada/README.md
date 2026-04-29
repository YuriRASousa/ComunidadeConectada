# Comunidade Conectada - Fase 3 (Android Nativo)

Este repositório contém a refatoração do projeto "Comunidade Conectada" para **Android Nativo (Kotlin + Jetpack Compose)**, atendendo aos requisitos da Fase 3 do projeto FIAP.

## 🚀 Como testar o projeto na sua máquina

Para rodar este projeto, você precisará do **Android Studio** instalado.

### 1. Clonar o Repositório
Abra o terminal e execute:
```bash
git clone https://github.com/YuriRASousa/ComunidadeConectada.git
```

### 2. Importar no Android Studio
1. Abra o **Android Studio**.
2. Clique em **File > Open** (ou **Open** na tela inicial).
3. Navegue até a pasta onde você clonou o projeto e selecione a pasta raiz `ComunidadeConectada`.
4. Clique em **OK**.

### 3. Sincronizar o Gradle
O Android Studio começará a baixar as dependências automaticamente (Gradle Sync). 
- Certifique-se de estar conectado à internet.
- Aguarde a barra de progresso no rodapé terminar.

### 4. Executar o Aplicativo
1. Conecte um celular Android via USB (com Depuração USB ativa) ou use um **Emulador (AVD)**.
2. No menu superior do Android Studio, selecione o seu dispositivo.
3. Clique no botão de **Play (Run ▶)** ou use o atalho `Shift + F10`.

---

## 📱 Tecnologias Utilizadas
- **Kotlin** & **Jetpack Compose** (Interface Moderna)
- **Room Database** (Banco de dados local)
- **DataStore** (Preferências e Sessão)
- **Retrofit** (Consumo de APIs)
- **Navigation Compose** (Navegação entre telas)
- **Integração com IA** (LLMs e IBM Watson Assistant)

## 📁 Estrutura do Projeto
- `ai/`: Integração com Inteligência Artificial.
- `api/`: Configurações de rede e modelos de dados.
- `data/`: Persistência local (Banco de dados e DAOs).
- `ui/screens/`: Todas as telas do app (Home, Chat, Perfil, Chatbot, etc).
- `navigation/`: Gerenciamento das rotas do aplicativo.

---
**Nota:** Caso queira testar as funções de IA reais, lembre-se de configurar suas chaves de API nos arquivos dentro da pasta `ai/`.
