#Purge REST API

##Purging by a query

``` bash
$ nitr post purge -d expression=* -d all=true
```

That returns:

```
{"id":"d8938124-035e-4d40-a99d-38e59b77da2d"}
```

Where "id" is the purge task id.

##Checking purge task status

``` bash
nitr get purge/d8938124-035e-4d40-a99d-38e59b77da2d
```

That returns:

```
{"status":"COMPLETED","message":"purge 460 files matching (search \u0027*\u0027: 20000000 items down)","purged":460,"failed":0,"expected":460}
```

##Unpurge ALL

``` bash
$ nitr post purge/unpurge -d all=true
```

That returns:

```
{"id":"9f823d1a-4038-4a84-88c7-14bfce8e501e"}
```

Where "id" is the unpurge task id. May be checked with the same api as purge.

##Cancel running purge

``` bash
nitr post purge/cancel/d8938124-035e-4d40-a99d-38e59b77da2d
```

Doesn't return anything.

##Cancel ALL running purges (emergency)

``` bash
nitr post purge/cancel-all
```

Doesn't return anything.


