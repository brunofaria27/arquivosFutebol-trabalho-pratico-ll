# Trabalho Prático II  - PUC MG - AEDSIII

 ### PROFESSOR:
- Prof. Felipe Soares

### INTEGRANTES:
- Breno Lopes 
- Bruno Faria


## Orientações para a criação do arquivo de índices
- [ ] O arquivo de índices deve conter (no mínimo) o ID (do clube de futebol ou da conta
bancária) e a posição do registro (referente a esse id) no arquivo de dados. Cabe ao
grupo definir se existe a necessidade de algum novo campo, a partir das decisões de
implementação do grupo.
- [ ] Sempre que acontecerem alterações no arquivo de dados, novas alterações devem ser
feitas no arquivo de índices, mantendo sempre a coerência entre esses arquivos.
- [ ] Todas as buscar sequências (por id) feitas no TP1 devem ser substituídas por uma
busca binária feita no arquivo de índices.
- [ ] Para manter a ordenação do arquivo de índices, deve ser implementada uma
ordenação externa do arquivo em ordem crescente pelo id, utilizando o método de
intercalação balanceada com 2 caminhos e com a capacidade de ordenação em
memória primária limitada a 10 registros.

## Orientações sobre a criação da lista invertida
- [ ] Deve-se criar um arquivo contendo a lista invertida para o nome do clube de
futebol.
- [ ] Deve-se criar um arquivo contendo a lista invertida para a cidade do clube de
futebol.
- [ ] O sistema deverá realizar alterações nas listas invertidas sempre que novos registros
forem inseridos, alterados ou deletados no arquivo de dados.
- [ ] O sistema deve ser capaz de receber uma busca por nome e/ou cidade. A partir do
nome e/ou cidade digitados, o sistema deve ser capaz de retornar em quais ids de
registros a pesquisa foi capaz de encontrar os termos informados.
