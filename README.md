# Desafio Votação Fullstack

## Mobile
![Preview Mobile](<preview-mobile.gif>)
## 📋 Descrição do Projeto

Sistema de votação online desenvolvido com foco em arquitetura limpa, componentização e experiência do usuário.

## 🏗️ Arquitetura Backend

### Estrutura de Pastas e Organização
- Implementação seguindo princípios de Clean Code e Arquitetura Limpa
- Separação clara de responsabilidades:
  - `controllers`: Endpoints da API
  - `services`: Lógica de negócio
  - `repositories`: Camada de persistência
  - `models`: Entidades de domínio
  - `exceptions`: Tratamento centralizado de exceções

### Padrões de Projeto
- Clean Code e Clean Architecture
- Tratamento de exceções com `HttpException` personalizada
- Validações com Bean Validation

### Estratégias de Exceção
- Exceções customizadas para diferentes cenários:
  - `HttpException.notFound()`: Recursos não encontrados
  - `HttpException.badRequest()`: Requisições inválidas
  - `HttpException.conflict()`: Conflitos de negócio (ex: voto duplicado)

### Tecnologias
- **Linguagem**: Java 21
- **Framework**: Spring Boot
- **Persistência**: Spring Data JPA
- **Documentação**: Swagger/OpenAPI
- **Validação**: Bean Validation
- **Build**: Gradle

## 🖥️ Arquitetura Frontend

### Conceitos e Escolhas
- Single Page Application (SPA) sem rotas, priorizando simplicidade
- TypeScript para tipagem forte e código mais seguro
- Componentização com Shadcn UI para componentes responsivos e customizáveis

### Bibliotecas e Ferramentas
- **Linguagem**: TypeScript
- **Framework**: React 19
- **Bundler**: Vite
- **UI Components**: Shadcn UI (componentes acessíveis e customizáveis)
- **State Management**: 
  - React Query para gerenciamento de estado de servidor
  - Hooks personalizados para lógicas específicas
- **Estilização**: Tailwind CSS
- **Validação**: Zod
- **Formulários**: React Hook Form

### Estratégias de Desenvolvimento
- Hooks customizados para operações como:
  - [useVotacaoPauta](cci:1://file:///Users/neverson/IdeaProjects/desafio-votacao-fullstack/frontend/src/hooks/useVotacaoPauta.tsx:5:0-28:2): Lógica de votação
  - [useCriarPauta](cci:1://file:///Users/neverson/IdeaProjects/desafio-votacao-fullstack/frontend/src/hooks/useCriarPauta.tsx:4:0-20:2): Criação de novas pautas
  - [useAberturaSessao](cci:1://file:///Users/neverson/IdeaProjects/desafio-votacao-fullstack/frontend/src/hooks/useAberturaSessao.tsx:4:0-30:2): Gerenciamento de sessões
- Tratamento de erros com toasts
- Validações de formulário no frontend

## 🔧 Requisitos

- Java 21
- Node.js 20
- npm ou yarn

## 📦 Instalação e Execução

### Backend (Spring Boot)

1. **Baixar dependências e compilar**
```bash
./gradlew clean build
```

2. **Executar aplicação**
```bash
./gradlew bootRun
```

### FrontEnd

1. **Navegar para o diretório frontend**
```bash
cd ../frontend
```

2. **Instalar dependências**
```bash
npm install
```

3. **Executar aplicação em modo desenvolvimento**
```bash
npm run dev
```

### 🌐 Endpoints

  Backend: http://localhost:8080
  Frontend: http://localhost:3000

📝 Testes
Backend
```bash
./gradlew test
```

# Votação

## Objetivo

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. Imagine que você deve criar uma solução we para gerenciar e participar dessas sessões de votação.
Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de uma API REST / Front:

- Cadastrar uma nova pauta
- Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por
  um tempo determinado na chamada de abertura ou 1 minuto por default)
- Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado
  é identificado por um id único e pode votar apenas uma vez por pauta)
- Contabilizar os votos e dar o resultado da votação na pauta

Para fins de exercício, a segurança das interfaces pode ser abstraída e qualquer chamada para as interfaces pode ser considerada como autorizada. A solução deve ser construída em java com Spring-boot e Angular/React conforme orientação, mas os frameworks e bibliotecas são de livre escolha (desde que não infrinja direitos de uso).

É importante que as pautas e os votos sejam persistidos e que não sejam perdidos com o restart da aplicação.

## Como proceder

Por favor, realize o FORK desse repositório e implemente sua solução no FORK em seu repositório GItHub, ao final, notifique da conclusão para que possamos analisar o código implementado.

Lembre de deixar todas as orientações necessárias para executar o seu código.

### Tarefas bônus

- Tarefa Bônus 1 - Integração com sistemas externos
  - Criar uma Facade/Client Fake que retorna aleátoriamente se um CPF recebido é válido ou não.
  - Caso o CPF seja inválido, a API retornará o HTTP Status 404 (Not found). Você pode usar geradores de CPF para gerar CPFs válidos
  - Caso o CPF seja válido, a API retornará se o usuário pode (ABLE_TO_VOTE) ou não pode (UNABLE_TO_VOTE) executar a operação. Essa operação retorna resultados aleatórios, portanto um mesmo CPF pode funcionar em um teste e não funcionar no outro.

```
// CPF Ok para votar
{
    "status": "ABLE_TO_VOTE
}
// CPF Nao Ok para votar - retornar 404 no client tb
{
    "status": "UNABLE_TO_VOTE
}
```

Exemplos de retorno do serviço

### Tarefa Bônus 2 - Performance

- Imagine que sua aplicação possa ser usada em cenários que existam centenas de
  milhares de votos. Ela deve se comportar de maneira performática nesses
  cenários
- Testes de performance são uma boa maneira de garantir e observar como sua
  aplicação se comporta

### Tarefa Bônus 3 - Versionamento da API

○ Como você versionaria a API da sua aplicação? Que estratégia usar?

## O que será analisado

- Simplicidade no design da solução (evitar over engineering)
- Organização do código
- Arquitetura do projeto
- Boas práticas de programação (manutenibilidade, legibilidade etc)
- Possíveis bugs
- Tratamento de erros e exceções
- Explicação breve do porquê das escolhas tomadas durante o desenvolvimento da solução
- Uso de testes automatizados e ferramentas de qualidade
- Limpeza do código
- Documentação do código e da API
- Logs da aplicação
- Mensagens e organização dos commits
- Testes
- Layout responsivo

## Dicas

- Teste bem sua solução, evite bugs

  Observações importantes
- Não inicie o teste sem sanar todas as dúvidas
- Iremos executar a aplicação para testá-la, cuide com qualquer dependência externa e
  deixe claro caso haja instruções especiais para execução do mesmo
  Classificação da informação: Uso Interno



# desafio-votacao