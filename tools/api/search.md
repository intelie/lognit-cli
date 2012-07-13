#Search REST API

##Starting a search

``` bash
$ nitr get search?expression=*=>
```

That returns:

```
{"channel":"/search/3937bd6f-4684-485e-95dc-fab470692a06","info":{"filter":{"valid":true},"aggregation":{"window":1000,"selectFields":["count"],"groupFields":[],"valid":true},"valid":true}}
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
