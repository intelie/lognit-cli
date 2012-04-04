# Lognit Protocol

Lognit messages (as of version 0.4) are stored in the following file structure:

```
<hosthash>/<host>/<date>/<hour>/<minute>/<second>/<identifier>
```

The hash is calculated with a prime-based simple math, defined by the python
function:

```python
def hash(host):
    return reduce(lambda x,y: x*31+ord(y), host, 0)%128
```

A message bag location should look something like this:

```
/106/example/20120329/18/08/55/1367f16a4fca8ee5
```

These files are stored in a protobuf format. The proto file is provided here.

You may be able to read the files using the example python script provided.

Test it using samples:

```sh
python read.py samples/compressed

python read.py samples/normal
```
