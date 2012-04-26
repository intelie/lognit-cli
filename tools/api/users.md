#Users REST API

##List

``` bash
$ nitr get users?page=1
```

That returns:

```
{"data":[{"id":"f478d640307946398d0b81c74bbe0761","email":"lognit@intelie.net","displayName":"Administrator","teams":[]},{"id":"ff8081813682d0a10136847099300024","email":"test@test.com","displayName":"Test da Silva","teams":[]}],"total":2,"page":1,"pageSize":10}
```

##Create

``` bash
$ nitr post users -d@- << '__END__' 
    {
        email:'test@test.com',
        displayName:'Test da Silva'
    }
__END__
```

That returns:

```
{"id":"ff8081813682d0a10136847099300024","email":"test@test.com","displayName":"Test da Silva","teams":[]}
```

##Read

``` bash
$ nitr get users/ff8081813682d0a10136847099300024
```

That returns:

```
{"id":"ff8081813682d0a10136847099300024","email":"test@test.com","displayName":"Test da Silva","teams":[]}
```

##Update

``` bash
$ nitr put users/ff8081813682d0a10136847099300024 -d@- << '__END__' 
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

##Delete

``` bash
$ nitr delete users/ff8081813682d0a10136847099300024
```

That returns:

```
{"id":"ff8081813682d0a10136847099300024","email":"test@test.com","displayName":"Test da Silva (modified)","teams":[]}
```
