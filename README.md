# CRdb
API para classificacao e reviews de disciplinas

> Para acessar a documentacao gerada pelo Swagger, rode a aplicação e digite o endereço: **localhost:8080/v1/swagger-ui.html/** no seu navegador

## Casos de Uso da API

### Caso de uso 1: cadastrar/autenticar usuários
* Method: `POST`
* Endpoint: `localhost:8080/v1/api/usuario`
* Body: os campos `email`, `primeiroNome` e `senha` nao podem ser nulos

### Caso de uso 2: pesquisar disciplinas a partir de uma (sub)string
* Method: `GET`
* Endpoint: `localhost:8080/v1/api/disciplina/buscar/{nome}`
* PathVariable: `nome` é onde vai a (sub)string 

### Caso de uso 3: Adicionar comentários de uma disciplina
* Method: `POST`
* Endpoint: `localhost:8080/v1/api/disciplina/avaliacao/comentario`
* Header: `Authorization`
* Body: campos requisitados são: `email`, `disciplinaId`, `comentario`

### Caso de uso 4: Apagar comentários de uma disciplina
* Method: `DELETE`
* Endpoint: `localhost:8080/v1/api/disciplina/avaliacao`
* Header: `Authorization`
* Body: campos requisitados são: `email`, `disciplinaId`

### Caso de uso 5: Dar/retirar like em uma disciplina
* Method: `POST`
* Endpoint: `localhost:8080/v1/api/disciplina/avaliacao/favoritar`
* Header: `Authorization`
* Body: campos requisitados são: `email`, `disciplinaId`

### Caso de uso 6: Deve ser possível recuperar o perfil de uma disciplina a partir do seu código numérico
* Method: `GET`
* Endpoint: `localhost:8080/v1/api/disciplina/perfil/{email}/{disciplinaId}`
* Header: `Authorization`
* PathVariable: email e o id da disciplina devem ser passados
