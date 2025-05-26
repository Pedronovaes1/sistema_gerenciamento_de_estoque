# Sistema de Gerenciamento de Estoque

Este projeto é um sistema para controle de estoque de produtos, desenvolvido em Java. Ele oferece tanto uma interface de linha de comando quanto uma interface gráfica (Swing), permitindo o gerenciamento completo do estoque.

## Funcionalidades

- Cadastro, atualização e exclusão de produtos
- Registro de entrada e saída de estoque
- Busca de produtos por nome
- Listagem de todos os produtos
- Listagem de produtos com baixo estoque
- Geração de relatórios em arquivos `.txt`:
    - Produtos em estoque
    - Produtos com baixo estoque
    - Histórico de movimentações

## Estrutura do Projeto

```
sistema_gerenciamento_de_estoque/
    src/
        controller/
            Estoque.java
        model/
            Produto.java
            MovimentacaoEstoque.java
            RelatorioEstoque.java
        repository/
            EstoqueRepositoy.java
        view/
            EstoqueGUI.java
            SistemaEstoque.java
```

- **controller/Estoque.java**: Lógica principal de manipulação do estoque.
- **model/Produto.java**: Representação dos produtos.
- **model/MovimentacaoEstoque.java**: Registro de movimentações de estoque.
- **model/RelatorioEstoque.java**: Geração de relatórios.
- **repository/EstoqueRepositoy.java**: Interface do repositório de estoque.
- **view/EstoqueGUI.java**: Interface gráfica (Swing).
- **view/SistemaEstoque.java**: Interface de linha de comando.

## Como Executar

### Linha de Comando

```sh
java view.SistemaEstoque
```

### Interface Gráfica

```sh
java view.EstoqueGUI
```

## Requisitos

- Java 17 ou superior

## Observações

- Os relatórios são gerados em arquivos `.txt` na raiz do projeto.
- O sistema não utiliza banco de dados; os dados são mantidos em memória enquanto o programa está em execução.

---

## Colaboradores

- Pedro Carvalho ([github.com/Pedronovaes1](https://github.com/Pedronovaes1))
- Allan Prado ([github.com/allan-pradoo](https://github.com/allan-pradoo))