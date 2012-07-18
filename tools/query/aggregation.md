#Básico

Para contar, por exemplo, quantos http 404 estão ocorrendo a cada minuto, é possível escrever a query:

```
http 404 => count() every 1 minute
```

O que transforma uma consulta normal em uma agregação é o operador "=>". Do lado esquerdo fica a consulta, 
no mesmo formato do Lucene, já conhecido. Do lado direito, a query de agregação. No final uma agregação tem 
o seguinte formato:

```
<query> => [<agregações>] [by <agrupamentos>] [every <janela>]
```

ex: 

```
http 404 => count(), avg(response_time#) by host, app every 30 seconds
```
_contagem e média do tempo de resposta de todos os http 404 agrupados por host e app numa janela de 30 segundos_

#Tipagem estática

Os tipos envolvidos nas agregações são fortes e estáticos. Todas as propriedades oriúndas das mensagens são, por padrão, strings. Então, para permitir operações como "avg", é preciso primeiro converter o valor em número. Para isso existe o operador #.

```
response_time#
```
_converte a propriedade response\_time em um número assumindo o formato americano (en-us)_

```
response_time#('pt-br')
```
_converte a propriedade response\_time em um número assumindo o formato brasileiro (pt-br)_

```
response_time#('pt-br', '###,###.##')
```
_converte a propriedade response_time em um número assumindo o formato brasileiro (pt-br) com separadores_

A formatação segue o mesmo padrão da classe DecimalFormat, do Java.

#Sintaxes alternativas

Todas as partes da query de agregação são opcionais. Então,

```
http 404 =>
```
é o mesmo que:
```
http 404 => count() every 1 second
```
Além disso, é possível aplicar funções com uma sintaxe alternativa:

```
avg(response_time#) 
```
é equivalente a
```
response_time#:avg()
```
ou
```
response_time#:avg
```

Isso é bastante útil para compor agregações

```
response_time#:avg:if(response_time# > 1000)
```
_média de todos os response times na janela que sejam maiores que 1000 milisegundos_

#Agregações disponíveis

```
avg(<number>)
avg(response_time#)
```
_média de todos os valores recebidos na janela de tempo definida_

```
count([<object>])
count(http_status) ou count()
```
_contagem de todos os valores não-nulos recebidos para a expressão passada por parâmetro na janela de tempo definida_


```
sum([<number>])
sum(response_time#)
```
_soma de todos os valores numéricos recebidos na janela de tempo definida_


```
min(<comparable>) e max(<comparable>)
min(response_time#)
```
_menor (ou maior) valor recebido na janela de tempo definida_

```
first(<object>) e last(<object>)
min(response_time#)
```
_primeiro (ou último) valor recebido na janela de tempo definida (comparado utilizando o id da mensagem de log, para ser distribuído)_

```
stdev(<number>)
stdev(response_time#)
```
_desvio padrão de todos os valores recebidos na janela de tempo definida_

```
if(<aggregation>, <condition>)
avg(response_time#):if(response_time# > 1000)
```
_modificador que somente agrega um valor se a condição for verdadeira_

```
overlast(<aggregation>, <number literal>)
avg(response_time#):overlast(5) 
```
_modificador que agrega os resultados das últimas janelas_

```
avglast(<numeric aggregation>, <number literal>)
count():avglast(5) 
```
_modificador que efetua a média os resultados das últimas janelas_

#Operadores e funções disponíveis

###Operadores aritiméticos: 
+; - (subtração e negação); \*; / (divisão float); // (divisão inteira); % (mod), \*\* (exponenciação)

###Operadores de comparação: 
==; !=; >; <; >=; <=

###Operadores lógicos:
&, && ou "and"; 
|, || ou "or"; 
^ ou xor; 
! ou not
```
reponse_time# > 1000 and (host == 'aaa' || host == 'bbb')
```

###Operador ternário
```
condition ? if-true, if-false
response_time < 100 ? 'great!', response_time < 1000 ? 'ok.', 'bad
```

###Operador de coalescência de nulos:
```
<expressão> ?? <valor se for expressão for nula>
http_status ?? "000"
```

###Operador de coerção numérica
```
<string>#([<locale>[, <format>]])
response_time#('pt-br', '###,###.00')
```

###Operador de formatação numérica
```
<number>#([<locale>[, <format>]])
@size#('pt-br', '###,###.00')
```

Perceba que tem a mesma sintaxe do operador de coerção. Se o parâmetro for um número, ele formata, se for uma string,
ele faz parse.

###Função de formatação como bytes
```
<number>:bytes([<precision>])
@size:avg:bytes(3) 
```
_tamanho médio da mensagem formatado como bytes com 3 casas decimais. Assim, 123456 é formatado como "120.562 KB"_

#Propriedades

Todas as propriedades das mensagens de log estão disponíveis para serem consumidas pela agregação. 
As propriedades no lognit são multivaloradas, por isso, é possível prover um índice para acessar um 
valor específico de uma propriedade.

```
tag
```
_acessa o primeiro valor da propriedade "tag". O mesmo que tag[0]_

```
text[4]
```
_acessa o quinto valor da propriedade "text"_ 

Usa a mesma semântica de split que as propriedades indexadas nos log groups:

```
127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /apache/pb.gif HTTP/1.0" 200 2326
```

é separado como: 
```
0: 127.0.0.1
1: frank
2: 10/Oct/2000:13:55:36
3: 0700
4: GET
5: apache/pb.gif
6: HTTP/1.0
7: 200
8: 2326
```

Além das propriedades normais, propriedades especiais são providas para serem acessadas. Elas são prefixadas com "@". Por exemplo

```
@size
```
_acessa o tamanho (em bytes) da mensagem, este valor já é numérico e pode ser usado em agregações como ```@size:avg```_

```
@node
```
_acessa o hostname do nó do cluster que processou a mensagem, permite agregações como 
```
* => count(), @size:sum:bytes by @node
```
_retorna o número de mensagens e bytes processador por nó do cluster a cada segundo_
