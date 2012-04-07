#Search REST API

##Starting a search

``` bash
$ nitr get search?expression=*
```

That returns:

```
{"channel":"/search/bd22d917-192e-465a-aa56-c8041c737199"}
```

Where "channel" is a Bayeux channel that will return any matching results.

##Download a whole search result set

``` bash
$ nitr get "search/download?expression=*&text=true"
```

That returns:

```
... long text file ...
```
