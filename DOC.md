# Teste Android

![Aiko](imagens/aiko.png)

# **Visão Geral**

O projeto foi criado na arquitetura MVVM + Clean architecture para atender a solicitação no desafio de buscar informações do transporte público em tempo real

Para tal objetivo criei uma chave de API(API DO OLHO VIVO) para acessar os endpoints

Algumas das tecnologias utilizadas no projeto foram:
- Dagger Hilt para injeção de dependência
- Mockito para criação de testes unitários
- Retrofit para chamada de rede
- SDK Maps para criação de mapas dinamicos no android
- Jetpack componets

# **Instalação**

Para instalar o projeto você precisa:

1. clonar o projeto na sua máquina : ``git clone {caminho}`` 

2. Abrir o projeto em : AikoPublicTransport

3. Adicionar chave no diretorio

4. Executar a aplicação em um celular ou emulador

# **Uso da aplicação**

O aplicativo oferece 4 telas que são:

- Primeira splash com o icone da Aiko que contem uma breve animação para Home, 

- Segunda tela contendo histórico de pesquisa em cache no celualar, cada pesquisa, salvo a pesquisa para visualizar as pesquisas recentes

- Terceira tela para buscar na barra de pesquisa e escolher uma linha para direcionamento ao mapa

- Quarta tela de mapas que mostra a posição dos veículos e paradas através de uma linha escolhida pelo usuário que ao clicar em uma das paradas mostrará uma lista de veículos e sua previsão de chegada





