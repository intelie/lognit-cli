#Creating a new user

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
{"id":"ff8081813682d0a10136847099300024","email":"test@test.com","displayName":"Test da Silva"}
```

#Returning user info

``` bash
$ nitr get users/ff8081813682d0a10136847099300024
```

That returns:

```
{"id":"ff8081813682d0a10136847099300024","email":"test@test.com","displayName":"Test da Silva"}
```

#Updating user

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
{"id":"ff8081813682d0a10136847099300024","email":"test@test.com","displayName":"Test da Silva (modified)"}
```

#Deleting user

``` bash
$ nitr delete users/ff8081813682d0a10136847099300024
```

That returns:

```
{"id":"ff8081813682d0a10136847099300024","email":"test@test.com","displayName":"Test da Silva (modified)"}
```
