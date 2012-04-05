#Creating a new log group

``` bash
$ cat << '__END__' | nitr post log-groups -d @-
    {
        name:"test log group",
        metadata: [{key:"d",value:"e"}],
        patterns: [{expression:"c"}],
        templates: [{position:123,property:"f"}],
        filters: [{expressions:[{"key":"a","value":"b"}]}]
    }
__END__

"id":"ff8081813682d0a101368450a92b0018","name":"test log group","metadata":[{"key":"d","value":"e"}],"patterns":[{"expression":"c"}],"templates":[{"position":123,"property":"f"}],"filters":[{"id":"ff8081813682d0a101368450a92c0019","expressions":[{"id":"ff8081813682d0a101368450a92c001a","key":"a","value":"b"}]}],"spaces":[]}
```

#Returning log group info

``` bash
$ nitr get log-groups/ff8081813682d0a101368450a92b0018

{"id":"ff8081813682d0a101368450a92b0018","name":"test log group","metadata":[{"key":"d","value":"e"}],"patterns":[{"expression":"c"}],"templates":[{"position":123,"property":"f"}],"filters":[{"id":"ff8081813682d0a101368450a92c0019","expressions":[{"id":"ff8081813682d0a101368450a92c001a","key":"a","value":"b"}]}],"spaces":[]}
```

#Updating log group

``` bash
$ cat << '__END__' | nitr put log-groups/ff8081813682d0a101368450a92b0018 -d @-
    {
        name:"test log group (modified)",
        metadata: [{key:"d",value:"e"}],
        patterns: [{expression:"c"}],
        templates: [{position:123,property:"f"}],
        filters: [{expressions:[{"key":"a","value":"b"}]}]
    }
__END__

{"id":"ff8081813682d0a101368450a92b0018","name":"test log group (modified)","metadata":[{"key":"d","value":"e"}],"patterns":[{"expression":"c"}],"templates":[{"position":123,"property":"f"}],"filters":[{"id":"ff8081813682d0a101368452140d001b","expressions":[{"id":"ff8081813682d0a101368452140d001c","key":"a","value":"b"}]}],"spaces":[]}
```

#Deleting log group

``` bash
$ nitr delete log-groups/ff8081813682d0a101368450a92b0018

{"id":"ff8081813682d0a101368450a92b0018","name":"test log group (modified)","metadata":[{"key":"d","value":"e"}],"patterns":[{"expression":"c"}],"templates":[{"position":123,"property":"f"}],"filters":[{"id":"ff8081813682d0a101368452140d001b","expressions":[{"id":"ff8081813682d0a101368452140d001c","key":"a","value":"b"}]}],"spaces":[]}
```
