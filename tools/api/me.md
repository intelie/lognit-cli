#Me REST API

All the API that accesses the current logged-in user.

##Read

``` bash
$ nitr get me
```

That returns:

```
{"id":"ff8081813682d0a10136847099300024","email":"test@test.com","displayName":"Test da Silva","teams":[]}
```

##Update

``` bash
$ nitr put me-d@- << '__END__' 
    {
        id: 'ff8081813682d0a10136847099300024',
        email:'test@test.com',
        displayName:'Test da Silva (modified)'
    }
__END__
```

That returns:

```
{"id":"ff8081813682d0a10136847099300024","email":"test@test.com","displayName":"Test da Silva (modified)","teams":[]}
```
