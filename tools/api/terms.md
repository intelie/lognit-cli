#Term completion REST API

##Completing a search

``` bash
$ nitr get "terms?field=&term=test&size=20"
```

That returns:

```
{"terms":["host:test1","host:test2","host:test3","host:test4","host:test5","host:test6","host:test7","host:test8","text:test"]}
```

