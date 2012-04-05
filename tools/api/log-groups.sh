cat << '__END__' | nitr post log-groups -d @-
    {
        name:"test log group",
        metadata: [{key:"d",value:"e"}],
        patterns: [{expression:"c"}],
        templates: [{position:123,property:"f"}],
        filters: [{expressions:[{"key":"a","value":"b"}]}]
    }
__END__
