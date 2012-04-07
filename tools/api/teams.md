#Teams REST API

##Creating

``` bash
$ nitr post teams -d@- << '__END__' 
    {
        name:"Teste",
        permission:"MANAGE_LOGS",
        hasPermissionToAllLogs:"false",
        members:[{id:"f478d640307946398d0b81c74bbe0761"}],
        spaces:[{id:"ff808181368eb86401368ecd03f5000d"}]
    }
__END__
```

That returns:

```
{"id":"ff808181368eb86401368ecf34be0010","name":"Teste","permission":"MANAGE_LOGS","hasPermissionToAllLogs":false,"members":[{"id":"f478d640307946398d0b81c74bbe0761","email":"lognit@intelie.net","displayName":"Administrator"}],"spaces":[{"id":"ff808181368eb86401368ecd03f5000d","name":"teste"}]}
```

##Read

``` bash
$ nitr get teams/ff808181368eb86401368ecf34be0010
```

That returns:

```
{"id":"ff808181368eb86401368ecf34be0010","name":"Teste","permission":"MANAGE_LOGS","hasPermissionToAllLogs":false,"members":[{"id":"f478d640307946398d0b81c74bbe0761","email":"lognit@intelie.net","displayName":"Administrator"}],"spaces":[{"id":"ff808181368eb86401368ecd03f5000d","name":"teste"}]}
```

##Update

``` bash
$ nitr put teams/ff808181368eb86401368ecf34be0010 -d@- << '__END__' 
     {
        name:"Teste (modified)",
        permission:"MANAGE_LOGS",
        hasPermissionToAllLogs:"false",
        members:[{id:"f478d640307946398d0b81c74bbe0761"}],
        spaces:[{id:"ff808181368eb86401368ecd03f5000d"}]
    }
__END__
```

That returns:

```
{"id":"ff808181368eb86401368ecf34be0010","name":"Teste (modified)","permission":"MANAGE_LOGS","hasPermissionToAllLogs":false,"members":[{"id":"f478d640307946398d0b81c74bbe0761","email":"lognit@intelie.net","displayName":"Administrator"}],"spaces":[{"id":"ff808181368eb86401368ecd03f5000d","name":"teste"}]}
```

##Delete

``` bash
$ nitr delete teams/ff808181368eb86401368ecf34be0010
```

That returns:

```
{"id":"ff808181368eb86401368ecf34be0010","name":"Teste (modified)","permission":"MANAGE_LOGS","hasPermissionToAllLogs":false,"members":[{"id":"f478d640307946398d0b81c74bbe0761","email":"lognit@intelie.net","displayName":"Administrator"}],"spaces":[{"id":"ff808181368eb86401368ecd03f5000d","name":"teste"}]}
```
