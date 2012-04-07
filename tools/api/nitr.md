#nitr - Lognit REST API Command Line Client

nitr is a command designed to ease the task of calling the rest API of lognit. 
It uses the already logged session from nit.

The basic usage is:

```
nitr <verb> <resource> [<arguments>]
```

It's actually a shell script that does:

```
curl -X ${verb} -s -L -H 'Content-Type:application/json' -b JSESSIONID=${sessionid} "http://${server}/rest/${resource}"
```

You can use the same command line options as curl, e.g.:

```
nitr post pause -d all=true
```
