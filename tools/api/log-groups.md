#Log Groups REST API

##Create

``` bash
$ nitr post log-groups -d@- << '__END__' 
    {
        name:"test log group",
        metadata: [{key:"d",value:"e"}],
        patterns: [{expression:"c"}],
        templates: [{position:123,property:"f"}],
        filters: [{expressions:[{key:"a",value:"b"}]}]
    }
__END__
```

That returns:

```
{"id":"ff8081813682d0a101368450a92b0018","name":"test log group","metadata":[{"key":"d","value":"e"}],"patterns":[{"expression":"c"}],"templates":[{"position":123,"property":"f"}],"filters":[{"id":"ff8081813682d0a101368450a92c0019","expressions":[{"id":"ff8081813682d0a101368450a92c001a",key:"a",value:"b"}]}],"spaces":[]}
```

##Read

``` bash
$ nitr get log-groups/ff8081813682d0a101368450a92b0018
```

That returns:

```
{"id":"ff8081813682d0a101368450a92b0018","name":"test log group","metadata":[{"key":"d","value":"e"}],"patterns":[{"expression":"c"}],"templates":[{"position":123,"property":"f"}],"filters":[{"id":"ff8081813682d0a101368450a92c0019","expressions":[{"id":"ff8081813682d0a101368450a92c001a",key:"a",value:"b"}]}],"spaces":[]}
```

##Update

``` bash
$ nitr put log-groups/ff8081813682d0a101368450a92b0018 -d@- << '__END__' 
    {
        name:"test log group (modified)",
        metadata: [{key:"d",value:"e"}],
        patterns: [{expression:"c"}],
        templates: [{position:123,property:"f"}],
        filters: [{expressions:[{key:"a",value:"b"}]}]
    }
__END__
```

That returns:

```
{"id":"ff8081813682d0a101368450a92b0018","name":"test log group (modified)","metadata":[{"key":"d","value":"e"}],"patterns":[{"expression":"c"}],"templates":[{"position":123,"property":"f"}],"filters":[{"id":"ff8081813682d0a101368452140d001b","expressions":[{"id":"ff8081813682d0a101368452140d001c",key:"a",value:"b"}]}],"spaces":[]}
```

##Delete

``` bash
$ nitr delete log-groups/ff8081813682d0a101368450a92b0018
```

That returns:

```
{"id":"ff8081813682d0a101368450a92b0018","name":"test log group (modified)","metadata":[{"key":"d","value":"e"}],"patterns":[{"expression":"c"}],"templates":[{"position":123,"property":"f"}],"filters":[{"id":"ff8081813682d0a101368452140d001b","expressions":[{"id":"ff8081813682d0a101368452140d001c",key:"a",value:"b"}]}],"spaces":[]}
```
