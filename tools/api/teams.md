#Teams REST API

##List

``` bash
$ nitr get teams?page=1
```

That returns:

```
{"data":[{"id":"fa474d49ecf941649580d42003101457","name":"Admin","permission":"MANAGE_ACCESS","hasPermissionToAllLogs":true,"members":[{"id":"f478d640307946398d0b81c74bbe0761","email":"lognit@intelie.net","displayName":"Administrator"}],"spaces":[]},{"id":"ff8081813697fd940136980846b10006","name":"Teste","permission":"MANAGE_LOGS","hasPermissionToAllLogs":false,"members":[{"id":"ff8081813697fd9401369804fff50005","email":"test@test.com","displayName":"Test da Silva"}],"spaces":[{"id":"ff8081813697fd9401369801bc720004","name":"teste"}]}],"total":2,"page":1,"pageSize":10}
```

##Create

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
