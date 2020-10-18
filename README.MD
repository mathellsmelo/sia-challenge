# Requerimentos

Para rodar essa aplicação é necessário ter o Docker e Maven instalado na máquina.

## Instalação

Rode os comandos abaixo, em ordem, dentro do repositório para rodar a API.

```bash
mvn clean install
docker-compose up --build
```

## Endpoints da API

```
localhost:8080
```

#### Endpoints públicos

```
[POST] /api/auth/cadastrar (Cadastre um usuário na aplicação)
[POST] /api/auth/login (Faça login com o usuário)
```

#### Endpoints privados
É necessário enviar o Bearer Token gerado no endpoint de Login junto da requisição para ter acesso a esses endpoints.

```
[POST] /api/feriados (Cadastre um feriado para impedir registro de ponto neste dia) {Feriado}
[DELETE] /api/feriados/:id (Delete um feriado) 
[POST] /api/ponto-eletronico/manha (Registrar ponto no periodo da manhã) {Ponto}
[POST] /api/ponto-eletronico/tarde (Registrar ponto no periodo da tarde) {Ponto}
[PUT] /api/ponto-eletronico/:id/manha (Atualizar ponto no periodo da manhã) {Ponto}
[PUT] /api/ponto-eletronico/:id/tarde (Atualizar ponto no periodo da tarde) {Ponto}
[GET] /api/ponto-eletronico/:mes/horas (Trazer informações do ponto do usuário logado) {Alocacao}
[POST] /api/ponto-eletronico/alocacao-horas (Alocar horas em um determinado dia de trabalho) {Alocacao}
``` 
#### Objetos de requisição
```
Ponto:
{
   "data" : "dd/MM/yyy",
   "horaEntrada" : "hh:mm",
   "horaSaida" :  "hh:mm"
}
```
```
Feriado:
{
   "data" : "dd/MM/yyy"
}
```
```
Alocacao:
{
   "projeto" : "String",
   "data" : "dd/MM/yyy",
   "horas" : "hh:mm"
}
```
```
Login:
{
   "username" : "String",
   "password" : "String
}
```