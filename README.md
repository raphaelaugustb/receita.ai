<br />
<img src="documents/receitas-logo.png" width="80"/>


### Receita.ai - Administrate and discover new recipes for cooking on!

Receitas.ai gives you the oportunity to try new recipes with just one touch! You can ask IA for doing one for you or search for one from your public repository, with log in you can save your own recipes, edit, share and try it for yourself


#### Setup Project

Clone project

```bash
  git clone https://github.com/raphaelaugustb/receita.ai/
```

Enter on directory

```bash
  cd receita.ai
```

#### Setup back-end

Install depedencies

```bash
  cd backend/receitas.ai && mvn clean install package 
```
Run database on container

```bash
  docker compose up
```
Run api

```bash
  mvn spring-boot:run 
```

#### Documents

![Documents](https://github.com/raphaelaugustb/MoneyTimes/blob/main/documents/documents.md)

