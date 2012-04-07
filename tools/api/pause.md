#Pause REST API

##Pausing Indexing

``` bash
$ nitr post pause -d all=true
```

That returns:

```
{"count":1}
```

Where "count" is the number of cluster nodes that have been paused.

##Resuming Indexing

``` bash
$ nitr post pause/resume -d all=true
```

That returns:

```
{"count":1}
```

Where "count" is the number of cluster nodes that have been resumed.
