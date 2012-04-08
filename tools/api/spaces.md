#Spaces REST API

##Create

``` bash
$ nitr post spaces -d@- << '__END__' 
    {
        name:"teste",
        logGroups:[{id:"ff808181355a836601355a8648760005"}]
    }
__END__
```

That returns:

```
{"id":"ff808181368eb86401368eca6a02000c","name":"teste","logGroups":[{"id":"ff808181355a836601355a8648760005","name":"34c09b6a-fe0e-4f22-bac9-a42845088136"}]}
```

##Read

``` bash
$ nitr get spaces/ff808181368eb86401368eca6a02000c
```

That returns:

```
{"id":"ff808181368eb86401368eca6a02000c","name":"teste","logGroups":[{"id":"ff808181355a836601355a8648760005","name":"34c09b6a-fe0e-4f22-bac9-a42845088136"}]}
```

##Update

``` bash
$ nitr put spaces/ff808181368eb86401368eca6a02000c -d@- << '__END__' 
    {
        name:"teste (modified)",
        logGroups:[{id:"ff808181355a836601355a8648760005"}]
    }
__END__
```

That returns:

```
{"id":"ff808181368eb86401368eca6a02000c","name":"teste (modified)","logGroups":[{"id":"ff808181355a836601355a8648760005","name":"34c09b6a-fe0e-4f22-bac9-a42845088136"}]}
```

##Delete

``` bash
$ nitr delete spaces/ff808181368eb86401368eca6a02000c
```

That returns:

```
{"id":"ff808181368eb86401368eca6a02000c","name":"teste (modified)","logGroups":[{"id":"ff808181355a836601355a8648760005","name":"34c09b6a-fe0e-4f22-bac9-a42845088136"}]}
```
