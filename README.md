# Projeto denuncias feito em javaFX

## Visão geral
A ideia desse projeto é fazer com que os alunos possam fazer denúncias sobre ocorridos dentro da escola, e se manter informados sobre o acontecido.

### Para dar o start
Para fazer o App startar é preciso criar um arquivo .env e configurar o arquivo init.sql sobrescrevendo o INSERT que está lá.  

Só serão aceitos emails que pertençam ao domínio do Gmail.  

Não esqueça de configurar "aplicativos e serviços de terceiros" do Gmail para poder fazer o envio dos emails.

### O arquivo .env precisa conter as seguintes chaves e valores

#### URL_DB=sua_URL
#### DB_NAME=nome_do_banco
#### DB_USER=seu_usuario
#### DB_PASSWORD=sua_senha

#### EMAIL_USERNAME=seu@email.com
#### EMAIL_PASSWORD=sua_senha